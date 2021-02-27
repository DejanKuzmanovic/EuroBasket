package org.unibl.eurobasket.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.service.AuthenticationService;
import org.unibl.eurobasket.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;
    private AuthenticationService authenticationService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllUsers() {
        if (authenticationService.isAdmin()) {
            return ResponseEntity.ok(userService.getAllUsers().toString());
        }
        return ResponseEntity.badRequest().body("Morate biti administrator!");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserById(@PathVariable int id) {
        JSONObject response = userService.getUserById(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Korisnik koji ima id " + id + " ne postoji!");
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCurrentUser() {
        Optional<User> currentUser = authenticationService.getCurrentUser();
        JSONObject response = new JSONObject();
        if (currentUser.isPresent()) {
            response = userService.getCurrentUser(currentUser.get());
        }
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Niste prijavljeni!");
    }

    @PostMapping(value = "/premium", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buyPremium() {
        Optional<User> currentUser = authenticationService.getCurrentUser();
        if (currentUser.isPresent()) {
            JSONObject response = userService.makePremium(currentUser.get());
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Korisnik je već premium!");
        }
        return ResponseEntity.badRequest().body("Niste prijavljeni!");
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        if (username.length() > 3 && password.length() > 3) {
            JSONObject response = userService.createUser(username, password);
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Korisničko ime već postoji!");
        } else {
            return ResponseEntity.badRequest().body("Korisničko ime i šifra moraju biti duži od 3 karaktera!");
        }
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUserWithRole(@RequestParam String username, @RequestParam String password, @RequestParam String roleName) {
        if (username.length() > 3 && password.length() > 3) {
            JSONObject response = userService.createUserWithRole(username, password, roleName);
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Rola ne postoji ili korisnik sa datim korisničkim imenom već postoji");
        } else {
            return ResponseEntity.badRequest().body("Korisničko ime i šifra moraju biti duži od 3 karaktera!");
        }
    }

    @PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestParam String password, @RequestParam String matchingPassword) {
        if (password.length() > 3 && password.equals(matchingPassword)) {
            JSONObject response = userService.changePassword(authenticationService.getCurrentUser(), password);
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Niste prijavljeni!");
        } else {
            return ResponseEntity.badRequest().body("Dužina šifre mora biti veća od 3 i šifre se moraju poklapati!");
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (authenticationService.isAdmin()) {
            JSONObject response = userService.deleteUserById(id);
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Korisnik koji ima id " + id + " ne postoji!");
        }
        return ResponseEntity.badRequest().body("Samo administrator može brisati korisnike!");
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

}


