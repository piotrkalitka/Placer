package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.ApiError;
import com.piotrkalitka.placer.api.DataManager;
import com.piotrkalitka.placer.api.ErrorMessages;
import com.piotrkalitka.placer.api.apiModels.getUserFavourites.GetUserFavouritesResponseModel;
import com.piotrkalitka.placer.api.dbModels.Place;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class FavouritesController {

    private DataManager dataManager = new DataManager();

    @RequestMapping(value = "/{placeId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> addUserToFavourites(@PathVariable("placeId") int placeId, @RequestHeader("Authorization") String authToken) {
        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        int userId = DataManager.getUserIdFromToken(authToken);

        if (dataManager.isFavourite(userId, placeId)) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.ADD_FAVOURITE_ALREADY_ADDED);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        dataManager.addFavourite(userId, placeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getUserFavourites(@RequestHeader("Authorization") String authToken) {
        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        int userId = DataManager.getUserIdFromToken(authToken);
        List<Place> places = dataManager.getUserFavourites(userId);
        GetUserFavouritesResponseModel model = new GetUserFavouritesResponseModel(places);
        return new ResponseEntity<>(model, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Object> removeFromFavourites(@PathVariable("id") int placeId, @RequestHeader("Authorization") String authToken) {
        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        int userId = DataManager.getUserIdFromToken(authToken);
        if (!dataManager.isFavourite(userId, placeId)) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.REMOVE_FAVOURITE_PLACE_NOT_FAVOURITE);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        dataManager.removeFromFavourite(userId, placeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}