package com.security.config;

import com.security.config.jwt.JwtFilter;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Using this class We can override form base authentication.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

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
            //.csrf(csrf -> csrf.disable())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
                            auth
                               //Step-3: Giving permission /hello can be used by without authentication.
                               .requestMatchers("/v1/api/hello").permitAll()
                               .requestMatchers("/v1/api/users/encoded-user").permitAll()
                               .requestMatchers("/v1/api/users/authenticate").permitAll()

                               //Role based access
                                //authorize only admin can add room
                               .requestMatchers(HttpMethod.POST, "/v1/api/rooms/addRoom")
                                    .hasRole("ADMIN")
                                //All room access only have Admin and Staff
                               .requestMatchers(HttpMethod.GET, "/v1/api/rooms")
                                    .hasAnyRole("ADMIN", "STAFF")
                               //a specific room (booked) can be  accessed by Admin, Staff, and Guest.
                               .requestMatchers(HttpMethod.GET, "/v1/api/rooms/**")
                                    .hasAnyRole("ADMIN", "STAFF", "GUEST")

                            .anyRequest().authenticated())
            // this .httpBasic is for Basic Session based Authorization.
            //.httpBasic(Customizer.withDefaults());
            // this .addFilterAt is for JWT Authorization.
            .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserService();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
