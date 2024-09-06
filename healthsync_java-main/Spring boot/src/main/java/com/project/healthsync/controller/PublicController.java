package com.project.healthsync.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.healthsync.config.JwtUtil;
import com.project.healthsync.entity.User;
import com.project.healthsync.service.UserDetailsServiceImpl;
import com.project.healthsync.service.UserService;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.saveNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(token,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);
        }
    }
}
