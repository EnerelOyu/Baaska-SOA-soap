package com.example.soap.service;

import com.example.model.User;
import com.example.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private Map<String, String> tokens = new HashMap<>();

    @Transactional
    public String register(String username, String password) {
        System.out.println("DEBUG: Registering user: " + username);
        if (userRepository.findByUsername(username).isPresent()) {
            System.out.println("DEBUG: User already exists: " + username);
            return "User already exists";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
        System.out.println("DEBUG: User saved successfully: " + username);
        return "User registered successfully";
    }

    public String login(String username, String password) {
        System.out.println("DEBUG: Login attempt for: " + username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("DEBUG: User found in DB. Comparing passwords...");
            if (user.getPassword().equals(password)) {
                String token = UUID.randomUUID().toString();
                tokens.put(token, username);
                System.out.println("DEBUG: Login successful. Token generated.");
                return token;
            } else {
                System.out.println("DEBUG: Password mismatch for user: " + username);
            }
        } else {
            System.out.println("DEBUG: User not found in DB: " + username);
        }
        return "Invalid credentials";
    }

    public boolean validateToken(String token) {
        return tokens.containsKey(token);
    }
}
