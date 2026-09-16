package com.security.oAuth.client.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello World !!";
    }

    @GetMapping("/hi")
    public String hi(){
        return "Hi World !!";
    }

    @GetMapping("/hey")
    public String hey(){
        return "Hey World !!";
    }

    @GetMapping("/me")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User user){

        Map<String, Object> response = new HashMap<>();

        response.put("name", user.getAttribute("name"));
        response.put("email", user.getAttribute("email"));
        response.put("picture", user.getAttribute("picture"));

        return response;
    }

    @GetMapping("/my")
    public Map<String, Object> myUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("name", user.getAttribute("name"));
            response.put("email", user.getAttribute("email"));
            response.put("picture", user.getAttribute("picture"));
        }
        return response;
    }
}
