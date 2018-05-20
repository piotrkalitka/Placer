package com.piotrkalitka.placer.api.apiModels.getPlace;

import com.piotrkalitka.placer.api.dbModels.Place;

public class GetPlaceResponseModel {

    private Place place;

    public Place getPlace() {
        return place;
    }

    public GetPlaceResponseModel(Place place) {
        this.place = place;
    }
}