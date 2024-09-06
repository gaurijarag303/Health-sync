package com.project.healthsync.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.healthsync.entity.User;
import com.project.healthsync.repository.UserRepository;
import com.project.healthsync.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable String username) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDb = userService.findByUserName(userName);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveUser(userInDb);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByusername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
