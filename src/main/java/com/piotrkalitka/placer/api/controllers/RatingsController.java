package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.DataManager;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping(value = "/v1/places/{placeId}/ratings")
public class RatingsController {

    private DataManager dataManager = new DataManager();

}