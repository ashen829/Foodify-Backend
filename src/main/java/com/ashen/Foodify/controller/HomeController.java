package com.ashen.Foodify.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @PostMapping
    public ResponseEntity<String>HomeController(){
        return new ResponseEntity<>("Welcome to food delivery project", HttpStatus.OK);
    }
}
