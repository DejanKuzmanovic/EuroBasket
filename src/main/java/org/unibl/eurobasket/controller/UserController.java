package org.unibl.eurobasket.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.eurobasket.dto.MatchingPasswordDTO;
import org.unibl.eurobasket.dto.UserDTO;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.service.AuthenticationService;
import org.unibl.eurobasket.service.UserService;

import javax.validation.Valid;
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
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserById(@PathVariable int id) {
        JSONObject response = userService.getUserById(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCurrentUser() {
        Optional<User> currentUser = authenticationService.getCurrentUser();
        JSONObject response = new JSONObject();
        if (currentUser.isPresent()) {
            response = userService.getCurrentUser(currentUser.get());
        }
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        JSONObject response = userService.createUser(userDTO);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@Valid @RequestBody MatchingPasswordDTO passwordDTO) {
        JSONObject response = userService.changePassword(authenticationService.getCurrentUser(), passwordDTO.getPassword());
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (authenticationService.isAdmin()) {
            JSONObject response = userService.deleteUserById(id);
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
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


