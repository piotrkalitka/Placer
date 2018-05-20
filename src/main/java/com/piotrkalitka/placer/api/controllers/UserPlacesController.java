package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.ApiError;
import com.piotrkalitka.placer.api.DataManager;
import com.piotrkalitka.placer.api.DataValidator;
import com.piotrkalitka.placer.api.ErrorMessages;
import com.piotrkalitka.placer.api.apiModels.addPlace.AddPlaceRequestModel;
import com.piotrkalitka.placer.api.apiModels.addPlace.AddPlaceResponseModel;
import com.piotrkalitka.placer.api.apiModels.getMyPlaces.GetMyPlacesResponseModel;
import com.piotrkalitka.placer.api.dbModels.Place;
import com.piotrkalitka.placer.api.dbModels.User;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@RequestMapping(value = "/v1/user/places")
public class UserPlacesController {

    private DataManager dataManager = new DataManager();

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getMyPlaces(@RequestHeader("Authorization") String authToken) {

        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        int userId = DataManager.getUserIdFromToken(authToken);
        List<Place> places = dataManager.getUserPlaces(userId);

        GetMyPlacesResponseModel model = new GetMyPlacesResponseModel(places);

        return new ResponseEntity<>(model, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> addPlace(@RequestBody AddPlaceRequestModel requestModel, @RequestHeader("Authorization") String authToken) {
        String name = requestModel.getName();
        String address = requestModel.getAddress();
        String website = requestModel.getWebsite();
        String phoneNumber = requestModel.getPhoneNumber();
        String description = requestModel.getDescription();

        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        if (DataValidator.isEmpty(name, address, website, phoneNumber, description)) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.ADD_PLACE_MISSING_DATA);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        User user = dataManager.getUserByToken(authToken);
        int userId = user.getId();

        int placeId = dataManager.addPlace(userId, name, address, website, phoneNumber, description);
        Place place = new Place(placeId, userId, name, address, website, phoneNumber, description);
        AddPlaceResponseModel model = new AddPlaceResponseModel(place);
        return new ResponseEntity<>(model, new HttpHeaders(), HttpStatus.CREATED);
    }

}