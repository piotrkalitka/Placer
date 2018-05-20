package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.ApiError;
import com.piotrkalitka.placer.api.DataManager;
import com.piotrkalitka.placer.api.ErrorMessages;
import com.piotrkalitka.placer.api.apiModels.ratePlace.RatePlaceRequestModel;
import com.piotrkalitka.placer.api.apiModels.ratings.GetRatingsResponseModel;
import com.piotrkalitka.placer.api.dbModels.Rating;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@RequestMapping(value = "/v1/places/{placeId}/ratings")
public class RatingsController {

    private DataManager dataManager = new DataManager();

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> rate(@RequestBody RatePlaceRequestModel requestModel, @PathVariable("placeId") int placeId, @RequestHeader("Authorization") String authToken) {
        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        int rate = requestModel.getRate();

        if (rate < 1 || rate > 5) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.RATE_PLACE_RATE_RANGE);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        int userId = DataManager.getUserIdFromToken(authToken);

        if (dataManager.isRated(userId, placeId)) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.RATE_PLACE_ALREADY_RATED);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        dataManager.rate(userId, placeId, rate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getRatings(@PathVariable("placeId") int placeId) {
        List<Rating> ratings = dataManager.getRatings(placeId);

        GetRatingsResponseModel responseModel = new GetRatingsResponseModel(ratings);
        return new ResponseEntity<>(responseModel, new HttpHeaders(), HttpStatus.OK);
    }

}