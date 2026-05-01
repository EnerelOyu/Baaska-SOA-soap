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
        String cleanUsername = username.trim();
        String cleanPassword = password.trim();
        
        System.out.println("DEBUG: Registering user: [" + cleanUsername + "]");
        if (userRepository.findByUsername(cleanUsername).isPresent()) {
            return "User already exists";
        }
        User user = new User();
        user.setUsername(cleanUsername);
        user.setPassword(cleanPassword);
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String username, String password) {
        String cleanUsername = username.trim();
        String cleanPassword = password.trim();
        
        System.out.println("DEBUG: Login attempt for: [" + cleanUsername + "]");
        Optional<User> userOpt = userRepository.findByUsername(cleanUsername);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Хэрэв баазад хадгалагдсан нууц үг зайнаас болоод зөрүүтэй байвал trim хийнэ
            if (user.getPassword().trim().equals(cleanPassword)) {
                String token = UUID.randomUUID().toString();
                tokens.put(token, cleanUsername);
                System.out.println("DEBUG: Login successful for " + cleanUsername);
                return token;
            } else {
                System.out.println("DEBUG: Password mismatch. DB password length: " + user.getPassword().length() + ", Input length: " + cleanPassword.length());
            }
        }
        return "Invalid credentials";
    }

    public boolean validateToken(String token) {
        return tokens.containsKey(token);
    }
}
