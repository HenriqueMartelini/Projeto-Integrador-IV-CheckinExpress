package com.checkinExpress.checkin_express.controller;

import com.checkinExpress.checkin_express.model.JwtRequest;
import com.checkinExpress.checkin_express.model.JwtResponse;
import com.checkinExpress.checkin_express.security.JwtUtil;
import com.checkinExpress.checkin_express.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
        );

        final var userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new JwtResponse(jwt);
    }
}