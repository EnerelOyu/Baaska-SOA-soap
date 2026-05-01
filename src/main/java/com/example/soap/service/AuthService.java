package com.example.soap.service;

import com.example.model.User;
import com.example.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // Tokens are kept in memory for simplicity, or could be moved to Redis/DB
    private Map<String, String> tokens = new HashMap<>();

    public String register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "User already exists";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In real apps, use BCrypt!
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            String token = UUID.randomUUID().toString();
            tokens.put(token, username);
            return token;
        }
        return "Invalid credentials";
    }

    public boolean validateToken(String token) {
        return tokens.containsKey(token);
    }
}
