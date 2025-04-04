package com.example.glimmerheaven.utils.validations;

import java.util.regex.Pattern;

public class EmailValidation {
    private String email;

    public EmailValidation(String email) {
        this.email = email;
    }

    public boolean validate(){
        boolean status = false;

        if(email != null){
            String regPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
                    + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

            status = Pattern.compile(regPattern)
                    .matcher(email)
                    .matches();
        }

        return status;
    }
}
