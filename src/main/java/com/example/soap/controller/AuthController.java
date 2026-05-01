package com.example.soap.controller;

import com.example.soap.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        String token = authService.login(username, password);
        
        Map<String, String> response = new HashMap<>();
        if ("Invalid credentials".equals(token)) {
            response.put("error", "Login failed");
        } else {
            response.put("token", token);
            response.put("username", username); // Return username so frontend knows who logged in
        }
        return response;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        authService.register(username, password);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return response;
    }
}
