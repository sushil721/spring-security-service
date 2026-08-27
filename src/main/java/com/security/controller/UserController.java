package com.security.controller;

import com.security.entity.UserEntity;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/api/users/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    public PasswordEncoder passwordEncoder;

    @GetMapping("encoded-user")
    public ResponseEntity<UserEntity> createUser(@RequestParam("username") String username,
                                                 @RequestParam("password") String password){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        //Encode password from text to encrypted formate using PasswordEncoder bean in SecurityConfig.java
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setActive(true);
        return new ResponseEntity<>(userService.createUser(userEntity), HttpStatus.OK);
    }
}
