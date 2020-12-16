package com.patel.nirma.practice.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.StringJoiner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;

public class SearchRepoTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SearchRepoTest.class);

    @DataProvider(name = "languageTestData")
    public Object[][] createLanguageTestData() {
        return new Object[][]{
                {"Java"}, {"Python"}
        };
    }

    @DataProvider(name = "createdDateTestData")
    public Object[][] createCreatedDateTestData() {
        return new Object[][]{
                {"2020-11-15"}
        };
    }

    @DataProvider(name = "gitHubUserTestData")
    public Object[][] createGitHubUserTestData() {
        return new Object[][]{
                {new GitHubUserTestData("nirmapatel", 1)},
                {new GitHubUserTestData("shadowsocks", 44)}
        };
    }

    @DataProvider(name = "multiLanguageTestData")
    public Object[][] createMultiLanguageTestData() {
        return new Object[][]{
                {Lists.newArrayList("Java", "HTML")}
        };
    }

    @DataProvider(name = "maxResultTestData")
    public Object[][] createMaxResultTestData() {
        return new Object[][]{
                {new MaxResultTestData("language:Python", 10, 10)},
                {new MaxResultTestData("user:nirmapatel", 10, 1)}
        };
    }


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseURI;
    }


    @Test(dataProvider = "languageTestData")
    public void testFetchRepoByLanguage(final String searchLanguage) {
        final String query = "language:" + searchLanguage;

        logger.info("fetch all repository for language {}", searchLanguage);

        //Note: Rest Endpoint returns details of max 30 repositories in response.
        given()
                .spec(spec)
                .param("q", query)
                .when()
                .get()
                .then()
                //.log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("items", hasSize(greaterThan(1)))
                .body("items.language", everyItem(is(searchLanguage)));
    }


    @Test(dataProvider = "createdDateTestData")
    public void testFetchRepoByCreatedDate(final String searchDate) {
        final String query = "created:" + searchDate;

        logger.info("fetch all repository created on {}", searchDate);

        //Note: Rest Endpoint returns details of max 30 repositories in response.
        given()
                .spec(spec)
                .param("q", query)
                .when()
                .get()
                .then()
                //.log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("items", hasSize(greaterThan(1)))
                .body("items.created_at", everyItem(containsString(searchDate)))
                .log();
    }


    //TODO: Need to correct it.
    @Test(dataProvider = "multiLanguageTestData")
    public void testFetchRepoByMultipleLanguages(List<String> searchLanguages) {

        StringJoiner joiner = new StringJoiner("+");
        searchLanguages
                .forEach(lang -> joiner.add("language:" + lang + ""));
        final String query = "java+" + joiner.toString();

        logger.info("fetch all repository for query {}", query);

        // Workaround
        List<String> expectedLanguages = Lists.newArrayList();
        expectedLanguages.addAll(searchLanguages);
        expectedLanguages.add(null);

        //Note: Rest Endpoint returns details of max 30 repositories in response.
        given()
                .spec(spec)
                .param("q", query)
                .when()
                .get()
                .then()
                //.log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("items", hasSize(greaterThan(1)))
                .body("items.language", everyItem(isIn(expectedLanguages)));
    }


    @Test(dataProvider = "gitHubUserTestData")
    public void testFetchRepoByUser(final GitHubUserTestData gitHubUserTestData) {
        final String query = "user:" + gitHubUserTestData.getUser();

        logger.info("fetch all repository of the user {}", gitHubUserTestData.getUser());

        given()
                .spec(spec)
                .param("q", query)
                .when()
                .get()
                .then()
                //.log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("total_count", equalTo(gitHubUserTestData.getNoOfRepo()));
    }


    @Test(dataProvider = "languageTestData")
    public void testFetchMostStarredRepoWithDescendingOrder(final String searchLanguage) {
        final String query = "language:" + searchLanguage;

        logger.info("fetch all repository with query {}.", query);

        List<Long> starCountList = given()
                .spec(spec)
                .param("q", query)
                .param("sort", "stargazers_count")
                .param("order", "desc")
                .when()
                .get()
                .then()
                //.log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("items", hasSize(greaterThan(1)))
                .extract()
                .jsonPath()
                .getList("items.stargazers_count");

        Assert.assertTrue(Ordering.natural().reverse().isOrdered(starCountList));
    }


    @Test(dataProvider = "maxResultTestData")
    public void testFetchRepoWithMaxResult(final MaxResultTestData maxResultTestData) {
        final String query = maxResultTestData.getQuery();
        final int maxResults = maxResultTestData.getMaxResults();

        logger.info("fetch all repository with query {} and max results {}.", query, maxResults);

        given()
                .spec(spec)
                .param("q", query) //per_page
                .param("per_page", maxResults)
                .when()
                .get()
                .then()
                //.log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("items", hasSize(maxResultTestData.getExpectedResults()));
    }

}
