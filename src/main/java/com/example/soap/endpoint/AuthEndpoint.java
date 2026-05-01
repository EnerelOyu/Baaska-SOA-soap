package com.example.soap.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.soap.service.AuthService;

import com.example.soap.model.LoginUserRequest;
import com.example.soap.model.LoginUserResponse;

import com.example.soap.model.RegisterUserRequest;
import com.example.soap.model.RegisterUserResponse;

import com.example.soap.model.ValidateTokenRequest;
import com.example.soap.model.ValidateTokenResponse;

@Endpoint
public class AuthEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/auth";

    @Autowired
    private AuthService authService;


    // LOGIN USER
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginUserRequest")
    @ResponsePayload
    public LoginUserResponse login(@RequestPayload LoginUserRequest request){

        String token = authService.login(
                request.getUsername(),
                request.getPassword());

        LoginUserResponse response = new LoginUserResponse();
        response.setToken(token);

        return response;
    }


    // REGISTER USER
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegisterUserRequest")
    @ResponsePayload
    public RegisterUserResponse register(@RequestPayload RegisterUserRequest request){

        authService.register(
                request.getUsername(),
                request.getPassword());

        RegisterUserResponse response = new RegisterUserResponse();
        response.setMessage("User registered successfully");

        return response;
    }


    // VALIDATE TOKEN
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ValidateTokenRequest")
    @ResponsePayload
    public ValidateTokenResponse validate(@RequestPayload ValidateTokenRequest request){

        boolean valid = authService.validateToken(request.getToken());

        ValidateTokenResponse response = new ValidateTokenResponse();
        response.setValid(valid);

        return response;
    }

}