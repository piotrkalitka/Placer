package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.ApiError;
import com.piotrkalitka.placer.api.DataManager;
import com.piotrkalitka.placer.api.ErrorMessages;
import com.piotrkalitka.placer.api.apiModels.addImage.AddImageResponseBody;
import com.piotrkalitka.placer.api.apiModels.getImage.GetImageResponseBody;
import com.piotrkalitka.placer.api.apiModels.getImages.GetImagesResponseBody;
import com.piotrkalitka.placer.api.dbModels.Image;
import com.piotrkalitka.placer.api.dbModels.Place;
import com.piotrkalitka.placer.api.dbModels.User;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RestController
@RequestMapping(value = "/v1/user/places/{placeId}/images")
public class ImagesController {

    private DataManager dataManager = new DataManager();

    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getImageForPlace(@PathVariable("placeId") int placeId, @PathVariable("imageId") int imageId){
        GetImageResponseBody model = new GetImageResponseBody(dataManager.getImage(placeId, imageId));
        return new ResponseEntity<>(model, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getImagesForPlace(@PathVariable("placeId") int placeId) {
        List<Image> images = dataManager.getImages(placeId);
        GetImagesResponseBody model = new GetImagesResponseBody(images);
        return new ResponseEntity<>(model, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> addImage(@RequestHeader("Authorization") String authToken, @PathVariable("placeId") int placeId, @RequestParam("image") MultipartFile image) {
        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        if (image.isEmpty()) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.ADD_IMAGE_IMAGE_EMPTY);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        User user = dataManager.getUserByToken(authToken);
        Place place = dataManager.getPlace(placeId);

        if (user.getId() != place.getUserId()) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.ADD_IMAGE_FORBIDDEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        String filePath = dataManager.saveImage(placeId, image);

        if (filePath == null) {
            ApiError apiError = new ApiError(HttpStatus.BAD_GATEWAY, ErrorMessages.ADD_IMAGE_ERROR);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        int imageId = dataManager.addImage(placeId, filePath);
        AddImageResponseBody model = new AddImageResponseBody(imageId, filePath);

        return new ResponseEntity<>(model, new HttpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{imageId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Object> removeImage(@RequestHeader("Authorization") String authToken, @PathVariable("placeId") int placeId, @PathVariable("imageId") int imageId) {
        if (!DataManager.isTokenValid(authToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.INVALID_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        User user = dataManager.getUserByToken(authToken);
        Place place = dataManager.getPlace(placeId);

        if (place.getUserId() != user.getId()) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.REMOVE_IMAGE_FORBIDDEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        if (!dataManager.doesImageExist(imageId)) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.REMOVE_IMAGE_IMAGE_NOT_EXIST);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        dataManager.removeImage(imageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}