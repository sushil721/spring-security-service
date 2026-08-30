package com.security.controller;

import com.security.config.jwt.JwtService;
import com.security.entity.UserEntity;
import com.security.model.AuthRequest;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(path = "/v1/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    public PasswordEncoder passwordEncoder;

    // This bean is created in SecurityConfig class.
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/encoded-user")
    public ResponseEntity<UserEntity> createUser(@RequestParam("username") String username,
                                                 @RequestParam("password") String password,
                                                 @RequestParam("role") String role){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        //Encode password from text to encrypted formate using PasswordEncoder bean in SecurityConfig.java
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setActive(true);
        userEntity.setRole(role);

        return new ResponseEntity<>(userService.createUser(userEntity), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                        authRequest.getPassword()));
        if (authenticate.isAuthenticated()){
            // get role from Spring Security.
            String role = Objects.requireNonNull(authenticate
                            .getAuthorities()
                            .iterator()
                            .next()
                            .getAuthority())
                    .replace("ROLE_", "");

            return ResponseEntity.ok(jwtService.generateToken(authRequest.getUsername(), role));
        }/* else {
            throw new RuntimeException("Authentication Failed");
        }*/
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

    }
}
