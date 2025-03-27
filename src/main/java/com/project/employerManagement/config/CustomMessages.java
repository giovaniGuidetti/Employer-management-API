package com.project.employerManagement.config;

public class CustomMessages {

    //Validation messages
    public static final String NAME_NOT_EMPTY = "Name cannot be empty";
    public static final String EMAIL_NOT_EMPTY = "Email cannot be empty";
    public static final String POSITION_NOT_EMPTY = "Position cannot be empty";

    //Exception messages
    public static String employerAlreadyExists(String email) {
        return "Employer with email " + email + " already exists";
    }
    public static final String EMPLOYER_NOT_FOUND = "Employer not found";

}
