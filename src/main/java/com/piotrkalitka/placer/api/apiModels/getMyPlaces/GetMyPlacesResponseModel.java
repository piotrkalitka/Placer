package com.piotrkalitka.placer.api.apiModels.getMyPlaces;

import com.piotrkalitka.placer.api.dbModels.Place;
import java.util.List;

public class GetMyPlacesResponseModel {

    private List<Place> places;

    public GetMyPlacesResponseModel(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }
}