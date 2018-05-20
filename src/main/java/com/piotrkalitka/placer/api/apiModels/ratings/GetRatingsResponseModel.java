package com.piotrkalitka.placer.api.apiModels.ratings;

import com.piotrkalitka.placer.api.dbModels.Rating;

import java.util.List;

public class GetRatingsResponseModel {

    private List<Rating> ratings;

    public GetRatingsResponseModel(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Rating> getRatings() {
        return ratings;
    }
}