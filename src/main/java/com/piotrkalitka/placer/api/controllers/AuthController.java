package com.piotrkalitka.placer.api.controllers;

import com.piotrkalitka.placer.api.ApiError;
import com.piotrkalitka.placer.api.DataManager;
import com.piotrkalitka.placer.api.DataValidator;
import com.piotrkalitka.placer.api.ErrorMessages;
import com.piotrkalitka.placer.api.PasswordEncoder;
import com.piotrkalitka.placer.api.apiModels.changePassword.ChangePasswordRequestModel;
import com.piotrkalitka.placer.api.apiModels.login.LoginRequestModel;
import com.piotrkalitka.placer.api.apiModels.login.LoginResponseModel;
import com.piotrkalitka.placer.api.apiModels.refresh.RefreshRequestModel;
import com.piotrkalitka.placer.api.apiModels.refresh.RefreshResponseModel;
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

    @RequestMapping(value = "/changePassword", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordRequestModel requestModel) {

        String email = requestModel.getEmail();
        String oldPassword = requestModel.getOldPassword();
        String password = requestModel.getPassword();
        String passwordConfirmation = requestModel.getPasswordConfirmation();

        if (DataValidator.isEmpty(email, oldPassword, password, passwordConfirmation)) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.CHANGE_PASSWORD_MISSING_DATA);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        User user = dataManager.getUser(email);

        if (user == null) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorMessages.CHANGE_PASSWORD_USER_NOT_FOUND);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        if (!PasswordEncoder.encodePasswordSHA256(oldPassword).equals(user.getPassword())) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.CHANGE_PASSWORD_WRONG_PASSWORD);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        dataManager.changePassword(user.getId(), PasswordEncoder.encodePasswordSHA256(password));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> refresh(@RequestBody RefreshRequestModel requestModel) {
        String refreshToken = requestModel.getRefreshToken();
        if (DataValidator.isEmpty(refreshToken)) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorMessages.REFRESH_MISSING_TOKEN);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        if (!DataManager.isTokenValid(refreshToken)) {
            ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorMessages.REFRESH_TOKEN_INVALID);
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        User user = dataManager.getUserByToken(refreshToken);
        String accessToken = DataManager.generateAccessToken(user.getId(), user.getEmail());
        refreshToken = DataManager.generateRefreshToken(user.getId(), user.getEmail());

        RefreshResponseModel responseModel = new RefreshResponseModel(user.getEmail(), accessToken, refreshToken);
        return new ResponseEntity<>(responseModel, new HttpHeaders(), HttpStatus.OK);
    }

}