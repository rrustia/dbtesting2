package com.taskmanager.controller;

import com.taskmanager.dto.LoginRequest;
import com.taskmanager.dto.RegisterRequest;
import com.taskmanager.model.User;
import com.taskmanager.security.JwtTokenProvider;
import com.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Exposes the authentication endpoints used for registration and login.
 * Registration stays public, while other protected routes rely on the issued JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * Builds the controller with the services needed for authentication.
     * Input: the user service, token provider, and password encoder.
     * Output: a configured authentication controller.
     */
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new account and issues a token right away.
     * Input: a validated registration payload with username, email, and password.
     * Output: an HTTP response containing a success message, token, user id, and username.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);

        // The new account receives a token immediately so a second login call is not required.
        String token = jwtTokenProvider.generateToken(user.getUsername());

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "token", token,
                "userId", user.getId(),
                "username", user.getUsername()
        ));
    }

    /**
     * Checks the supplied credentials and returns a JWT when they match.
     * Input: a validated login payload with username and password.
     * Output: an HTTP response containing either an error body or token details for the user.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());

        // Password comparison happens against the stored encoded value.
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "username", user.getUsername()
        ));
    }
}
