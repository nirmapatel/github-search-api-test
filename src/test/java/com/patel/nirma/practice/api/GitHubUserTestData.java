package com.patel.nirma.practice.api;

public class GitHubUserTestData {

    private String user;
    private int noOfRepo;

    public GitHubUserTestData(String user, int noOfRepo) {
        this.user = user;
        this.noOfRepo = noOfRepo;
    }

    public String getUser() {
        return user;
    }

    public int getNoOfRepo() {
        return noOfRepo;
    }

    @Override
    public String toString() {
        return "GitHubUserTestData{" +
                "user='" + user + '\'' +
                ", noOfRepo=" + noOfRepo +
                '}';
    }
}
