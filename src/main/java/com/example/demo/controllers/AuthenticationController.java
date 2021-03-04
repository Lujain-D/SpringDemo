package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.CustomUserDetailsService;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//@RestController
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService useretailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        System.out.println("hello");
        return "Hello World!";
    }


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

        } catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = useretailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user){
        return ResponseEntity.ok("hello");
    }

}
