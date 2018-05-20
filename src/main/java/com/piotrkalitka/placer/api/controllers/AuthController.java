package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.ApiError;
import com.piotrkalitka.placer.api.DataManager;
import com.piotrkalitka.placer.api.DataValidator;
import com.piotrkalitka.placer.api.ErrorMessages;
import com.piotrkalitka.placer.api.PasswordEncoder;
import com.piotrkalitka.placer.api.apiModels.login.LoginRequestModel;
import com.piotrkalitka.placer.api.apiModels.login.LoginResponseModel;
import com.piotrkalitka.placer.api.apiModels.register.RegisterRequestModel;
import com.piotrkalitka.placer.api.dbModels.User;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping(value = "/v1/auth")
public class AuthController {

    private DataManager dataManager = new DataManager();

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> register(@RequestBody RegisterRequestModel requestModel) {
        String email = requestModel.getEmail();
        String name = requestModel.getName();
        String surname = requestModel.getSurname();
        String password = requestModel.getPassword();
        String passwordConfirmation = requestModel.getPasswordConfirmation();

        if (DataValidator.isEmpty(email, name, surname, password, passwordConfirmation)) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.REGISTER_MISSING_DATA);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }
        if (!DataValidator.isEmailValid(email)) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.REGISTER_EMAIL_INVALID);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }
        if (!password.equals(passwordConfirmation)) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.REGISTER_PASSWORDS_NOT_MATCHES);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }
        if (dataManager.isEmailRegistered(email)) {
            ApiError error = new ApiError(HttpStatus.CONFLICT, ErrorMessages.REGISTER_EMAIL_TAKEN);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }

        dataManager.addUser(email, name, surname, PasswordEncoder.encodePasswordSHA256(password));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> login(@RequestBody LoginRequestModel requestModel) {
        String email = requestModel.getEmail();
        String password = requestModel.getPassword();

        if (!dataManager.isEmailRegistered(email)) {
            ApiError error = new ApiError(HttpStatus.NOT_FOUND, ErrorMessages.LOGIN_EMAIL_NOT_FOUND);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }

        User user = dataManager.getUser(email);

        if (!PasswordEncoder.encodePasswordSHA256(password).equals(user.getPassword())) {
            ApiError error = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.LOGIN_WRONG_PASSWORD);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }

        String accessToken = DataManager.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = DataManager.generateRefreshToken(user.getId(), user.getEmail());
        return new ResponseEntity<>(new LoginResponseModel(user.getEmail(), accessToken, refreshToken), new HttpHeaders(), HttpStatus.OK);
    }

}