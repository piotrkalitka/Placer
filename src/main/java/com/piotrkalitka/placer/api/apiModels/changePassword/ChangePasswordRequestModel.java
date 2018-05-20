package com.piotrkalitka.placer.api.apiModels.changePassword;

public class ChangePasswordRequestModel {

    private String email;
    private String oldPassword;
    private String password;
    private String passwordConfirmation;

    public String getEmail() {
        return email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
}