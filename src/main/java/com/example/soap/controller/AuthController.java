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
            response.put("error", "Invalid username or password");
        } else {
            response.put("token", token);
            response.put("username", username);
        }
        return response;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        String result = authService.register(username, password);
        
        Map<String, String> response = new HashMap<>();
        if (result.contains("successfully")) {
            response.put("message", result);
        } else {
            response.put("error", result); // "User already exists" гэх мэт
        }
        return response;
    }
}
