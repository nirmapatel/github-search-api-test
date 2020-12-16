package com.patel.nirma.practice.api;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Unit test for simple App.
 */
public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    public static String baseURI;
    public static String basePath;

    protected static RequestSpecification spec;

    @BeforeClass
    public static void initSpec(ITestContext context) {

        logger.info("Set the base URI and base path");
        baseURI = context.getCurrentXmlTest().getParameter("baseURI");
        basePath = context.getCurrentXmlTest().getParameter("basePath");

//        baseURI = "https://api.github.com";
//        basePath = "/search/repositories";
        
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseURI)
                .setBasePath(basePath)
                .build();
    }

}

