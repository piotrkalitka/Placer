package com.piotrkalitka.placer.api.apiModels.getUserFavourites;

import com.piotrkalitka.placer.api.dbModels.Place;

import java.util.List;

public class GetUserFavouritesResponseModel {

    private List<Place> places;

    public GetUserFavouritesResponseModel(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }
}
