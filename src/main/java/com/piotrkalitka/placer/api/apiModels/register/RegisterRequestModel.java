package com.piotrkalitka.placer.api.apiModels.register;

public class RegisterRequestModel {

    private String email;
    private String name;
    private String surname;
    private String password;
    private String passwordConfirmation;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
}
