package com.patel.nirma.practice.api;

public class MaxResultTestData {

    private String query;
    private int maxResults;
    private int expectedResults;

    public MaxResultTestData(String query, int maxResults, int expectedResults) {
        this.query = query;
        this.maxResults = maxResults;
        this.expectedResults = expectedResults;
    }

    public String getQuery() {
        return query;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getExpectedResults() {
        return expectedResults;
    }

    @Override
    public String toString() {
        return "MaxResultTestData{" +
                "query='" + query + '\'' +
                ", maxResults=" + maxResults +
                ", expectedResults=" + expectedResults +
                '}';
    }
}
