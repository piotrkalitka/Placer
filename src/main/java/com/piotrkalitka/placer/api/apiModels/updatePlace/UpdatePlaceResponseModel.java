package com.piotrkalitka.placer.api.apiModels.updatePlace;

import com.piotrkalitka.placer.api.dbModels.Place;

public class UpdatePlaceResponseModel {

    private Place place;

    public UpdatePlaceResponseModel(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

}