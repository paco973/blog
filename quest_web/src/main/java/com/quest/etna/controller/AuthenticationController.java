package com.quest.etna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.etna.config.AuthService;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.config.PasswordEncoderS;
import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
public class AuthenticationController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authentificationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoderS passwordEncoderS;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody User user) throws Exception {

        HashMap<String, String> response = new HashMap<String, String>();
        String username = user.getUsername();
        String email = user.getEmail();

        //400
        if (username == null || user.getPassword() == null) {
            response.put("status", "la requête n'est pas valide");
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
        }

        //409
        else if (userRepository.findByUsername(username).isPresent()) {
            response.put("status", "Le nom d'utilisateur est déjà utilisé, Veillez utiliser un nouveau");
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.CONFLICT);
        }
        //201
        else {

            User user1 = new User(username, passwordEncoderS.passwordEncoder().encode(user.getPassword()), email);
            userRepository.save(user1);
            authService.setupUser(user.getUsername(),
                    user.getPassword());

            String token = authService.getValidToken();
            response.put("username", username);
            response.put("role", user1.getRole().toString());
            response.put("token", token);
            return new ResponseEntity(user1, HttpStatus.CREATED);
        }
    }


    @GetMapping("/me")
    ResponseEntity<?> authenticate(@RequestBody User user) {
        try {

            authService.setupUser(user.getUsername(),
                    user.getPassword());

            String token = authService.getValidToken();


            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("token", token);

            String jsonString = mapper.writeValueAsString(response);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(jsonString);
        } catch (Exception ex) {
            return ResponseEntity

                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ex.getMessage());
        }
    }

}
