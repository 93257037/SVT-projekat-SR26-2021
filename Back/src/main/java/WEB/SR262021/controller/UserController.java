package com.ftn.SVT.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.SVT.model.User;
import com.ftn.SVT.model.dto.JwtAuthenticationRequest;
import com.ftn.SVT.model.dto.UserDTO;
import com.ftn.SVT.model.dto.UserTokenState;
import com.ftn.SVT.security.TokenUtils;
import com.ftn.SVT.service.UserService;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    TokenUtils tokenUtils;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser){

        User createdUser = userService.createUser(newUser);

        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        UserDetails user = (UserDetails) authentication.getPrincipal();
        User userLogin = userService.findByUsername(user.getUsername());
        userLogin.setLastLogin(LocalDateTime.now());
        userService.save(userLogin);
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }
    
    @PutMapping("/changePassword/{username}")
    public ResponseEntity<Object> updateResource(@PathVariable String username, @RequestBody String newPass) {
        // Retrieve the existing resource from the database based on the provided ID
        User userOrg = userService.findByUsername(username);
        if (userOrg == null) {
            // Return a 404 Not Found response if the resource doesn't exist
            return ResponseEntity.notFound().build();
        }
        
        // Perform the necessary updates on the existing resource
        userOrg.setPassword(passwordEncoder.encode(newPass));

        
        // Save the updated resource back to the database
        userService.save(userOrg);
        
        // Return the updated resource in the response body with a 200 OK status
        return ResponseEntity.ok(null);
    }
    

    @GetMapping("/all")
    public List<User> loadAll() {
        return this.userService.findAll();
    }
