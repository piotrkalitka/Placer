package com.piotrkalitka.placer.api.apiModels.addPlace;


import com.piotrkalitka.placer.api.dbModels.Place;

public class AddPlaceResponseModel {

    private Place place;

    public AddPlaceResponseModel(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

}
