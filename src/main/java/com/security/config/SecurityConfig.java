package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//Using this class We can override form base authentication.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Step-1: Using this we don't need to authenticate. it is bypassing authentication.
    /*@Bean
    public SecurityFilterChain basicAuthentication(HttpSecurity http){
        http
           .httpBasic(Customizer.withDefaults());
        return http.build();
    }*/

    //Step-2: This time we need basic authentication on browser pop-up.
    @Bean
    public SecurityFilterChain basicAuthentication(HttpSecurity http){
        http
            .authorizeHttpRequests(auth ->
                            auth
                               //Step-3: Giving permission /hello can be used by without authentication.
                               .requestMatchers("/v1/api/hello").permitAll()
                               .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
