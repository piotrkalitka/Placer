package com.piotrkalitka.placer.api.apiModels.addPlace;

public class AddPlaceRequestModel {

    private String name;
    private String address;
    private String website;
    private String phoneNumber;
    private String description;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDescription() {
        return description;
    }
}
