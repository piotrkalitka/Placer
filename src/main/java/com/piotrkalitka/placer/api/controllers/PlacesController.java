package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.ApiError;
import com.piotrkalitka.placer.api.DataManager;
import com.piotrkalitka.placer.api.ErrorMessages;
import com.piotrkalitka.placer.api.apiModels.getPlace.GetPlaceResponseModel;
import com.piotrkalitka.placer.api.dbModels.Place;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping(value = "/v1/places")
public class PlacesController {

    private DataManager dataManager = new DataManager();

    @RequestMapping(value = "/{placeId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getPlace(@PathVariable("placeId") int placeId) {
        Place place = dataManager.getPlace(placeId);
        if (place == null) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorMessages.GET_PLACE_NOT_FOUND);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        return new ResponseEntity<>(new GetPlaceResponseModel(place), new HttpHeaders(), HttpStatus.OK);
    }
    
}