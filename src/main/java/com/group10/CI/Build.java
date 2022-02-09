package com.group10.CI;

/**
 * Build class taking care of the build information.
 */
public class Build {
    
    private String prId;
    private String email;
    private String buildStatus;
    private String testStatus;
    private String repo;    //The repo name. Ex. ludwigjo/SE-Group10-CI

    /**
     * Constructor for Build class.
     * @param prId the prId of the build. Also called SHA in the gitHub API.
     * @param email the email of the user who initiated the build.
     * @param buildStatus the status of the build.
     * @param testStatus the status of the tests.
     */
    public Build(String prId, String email, String buildStatus, String testStatus, String repo) {
        this.prId = prId;
        this.email = email;
        this.buildStatus = buildStatus;
        this.testStatus = testStatus;
        this.repo = repo;
    }
    //Default constructor
    public Build() {}

    /**
     * Get method for prId.
     * @return prId the id for a pull request. Also called SHA in the gitHub API.
     */
    public String getPrId() {
        return prId;
    }

    /**
     * Get method for email.
     * @return email the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get method for buildStatus.
     * @return buildStatus the status of the build.
     */
    public String getBuildStatus() {
        return buildStatus;
    }

    /**
     * Get method for testStatus.
     * @return testStatus the status of the tests.
     */
    public String getTestStatus() {
        return testStatus;
    }

    /**
     * Get method for repo.
     * @return repo the repo name. Ex. ludwigjo/SE-Group10-CI
     */
    public String getRepo() {
        return repo;
    }

    /**
     * Set method for prId.
     * @param prId the id for a pull request. Also called SHA in the gitHub API.
     */
    public void setPrId(String prId) {
        this.prId = prId;
    }

    /**
     * Set method for email.
     * @param email the email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set method for buildStatus.
     * @param buildStatus the status of the build.
     */
    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    /**
     * Set method for testStatus.
     * @param testStatus the status of the tests.
     */
    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    /**
     * Set method for repo.
     * @param repo the repo name. Ex. ludwigjo/SE-Group10-CI
     */
    public void setRepo(String repo) {
        this.repo = repo;
    }
}
