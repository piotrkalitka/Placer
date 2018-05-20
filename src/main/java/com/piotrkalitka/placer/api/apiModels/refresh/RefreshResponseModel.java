package com.piotrkalitka.placer.api.apiModels.refresh;

public class RefreshResponseModel {

    private String email;
    private String accessToken;
    private String refreshToken;

    public RefreshResponseModel(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
