package com.security.config.jwt;

import com.security.enums.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    private static final String AUTHORIZATION = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        String token = null;
        if(authHeader!=null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
        }
        if(token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = jwtService.verifySignatureAndExtractClaims(token);
            //get Role from claim.
            String roleString = claims.get("Role", String.class);

            Role role = Role.valueOf("ROLE_"+roleString);

            //List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority(role.name()));
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>(List.of(new SimpleGrantedAuthority(role.name())));
            //add permissions
            role
                .getPermissions()
                .forEach(permission ->{
                    simpleGrantedAuthorities.add(new SimpleGrantedAuthority(permission.name()));
            });
            if (!jwtService.isTokenExpired(token)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(
                                  claims.getSubject(),
                        null, //any Credentials from request
                                  //new ArrayList<>() // List of roles from authorization call.
                                 simpleGrantedAuthorities // role
                );
                // from here we can set any other request.
                // so we can set from here and saved in the SecurityContextHolder context.
                usernamePasswordAuthenticationToken
                        .setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
