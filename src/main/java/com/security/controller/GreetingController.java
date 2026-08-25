package com.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/api/")
public class GreetingController {

    @GetMapping(path = "hi")
    public ResponseEntity<String> greetHi(){
        return new ResponseEntity<>("Hi World.", HttpStatus.OK);
    }

    @GetMapping(path = "hey")
    public ResponseEntity<String> greetHey(){
        return new ResponseEntity<>("Hey World.", HttpStatus.OK);
    }

    @GetMapping(path = "hello")
    public ResponseEntity<String> greetHello(){
        return ResponseEntity.ok().body("Hello World.");
    }
}
