package com.security.service;

import com.security.entity.UserEntity;
import com.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private UserEntity getUserFromUsername(String username){
        return  userRepository.findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // this method name is provided by spring security.
    // here we put out DB user data in UserDetails class.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = getUserFromUsername(username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().name()));

        userEntity
                .getRole()
                .getPermissions()
                .forEach(permission -> {
                    authorities.add(new SimpleGrantedAuthority(permission.name()));
                 });

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                //loading role in Spring security from DB.
                .authorities(authorities)
                .build();
    }

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
