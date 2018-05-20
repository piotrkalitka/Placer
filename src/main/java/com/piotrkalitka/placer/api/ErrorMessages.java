package com.piotrkalitka.placer.api;

public final class ErrorMessages {
    public static final String INVALID_TOKEN = "Invalid token or token expired";

    public static final String REGISTER_MISSING_DATA = "Missing data";
    public static final String REGISTER_EMAIL_INVALID = "Invalid email";
    public static final String REGISTER_PASSWORDS_NOT_MATCHES = "Passwords are not equals";
    public static final String REGISTER_EMAIL_TAKEN = "Email address already taken";

    public static final String LOGIN_EMAIL_NOT_FOUND = "Account with this email does not exist";
    public static final String LOGIN_WRONG_PASSWORD = "Wrong password";

    public static final String CHANGE_PASSWORD_MISSING_DATA = "Missing data";
    public static final String CHANGE_PASSWORD_USER_NOT_FOUND = "User with given email does not exists";
    public static final String CHANGE_PASSWORD_WRONG_PASSWORD = "Invalid password";

    public static final String REFRESH_MISSING_TOKEN = "Missing refresh token";
    public static final String REFRESH_TOKEN_INVALID = "Provided token is invalid";

    public static final String ADD_PLACE_MISSING_DATA = "Missing data";

    public static final String REMOVE_PLACE_FORBIDDEN = "Forbidden";
    public static final String REMOVE_PLACE_NOT_FOUND = "Place with given id not found";

    public static final String UPDATE_PLACE_FORBIDDEN = "Forbidden";
    public static final String UPDATE_PLACE_NOT_FOUND = "Place with given id not found";
}
