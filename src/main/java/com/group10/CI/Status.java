package com.group10.CI;

/**
 * The different statuses that a job (compilation, test) can have. A job can only have one status at a time.
 *
 * PENDING is for job not yet started
 * SUCCESS is for a job that is successfully completed, without failure (e.g. all test pass)
 * FAILURE is for a job that is successfully completed, but with failures (e.g. all test are run, but not all pass)
 * ERROR is for a job that was unable to be completed (used for exceptions)
 * */
public enum Status {
    PENDING,
    SUCCESS,
    FAILURE,
    ERROR
}
