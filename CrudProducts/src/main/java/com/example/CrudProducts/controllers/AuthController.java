package com.example.CrudProducts.controllers;

import com.example.CrudProducts.security.model.AuthenticationReq;
import com.example.CrudProducts.security.config.JwtUtilService;
import com.example.CrudProducts.security.model.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /* solicitud de autenticacion */
    @PostMapping("/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getUser());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationReq.getUser(), authenticationReq.getPass()));

        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUser());

        final String jwt = jwtUtilService.generateToken(userDetails);

        return ResponseEntity.ok(new TokenInfo(jwt));
    }

}

