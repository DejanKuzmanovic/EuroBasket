package org.unibl.eurobasket.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.eurobasket.service.AuthenticationService;
import org.unibl.eurobasket.service.GameService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/game")
public class GameController {

    private GameService gameService;
    private AuthenticationService authenticationService;

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getActiveGames() {
        return ResponseEntity.ok(gameService.getAllActive().toString());
    }

    @GetMapping(value = "/finished", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFinishedGames() {
        return ResponseEntity.ok(gameService.getAllFinished().toString());
    }

    @GetMapping(value = "/score/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getScore(@PathVariable int id) {
        JSONObject response = gameService.getScore(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Utakmica koji ima id " + id + " ne postoji!");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createGame(@RequestParam String host, @RequestParam String guest, @RequestParam String startTime, @RequestParam int hostPoints, @RequestParam int guestPoints, @RequestParam boolean isActive) {
        if (authenticationService.getCurrentUser().isEmpty()) {
            return ResponseEntity.badRequest().body("Niste prijavljeni!");
        }

        try {
            Date start = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(startTime);
            JSONObject response = gameService.createGame(host, guest, start, hostPoints, guestPoints, isActive, authenticationService.getCurrentUser().get());
            return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Jedan ili oba tima ne postoje");
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Datum nije odgovarajućeg formata!");
        }
    }

    @PutMapping(value = "/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeScore(@PathVariable int id, @RequestParam int hostPoints, @RequestParam int guestPoints) {
        JSONObject response = gameService.changeScore(id, hostPoints, guestPoints);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Utakmica ne postoji!");
    }

    @PutMapping(value = "/activate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateGame(@PathVariable int id) {
        JSONObject response = gameService.activateGame(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Utakmica ne postoji ili je već aktivna!");
    }

    @PutMapping(value = "/finish/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> finishGame(@PathVariable int id) {
        JSONObject response = gameService.finishGame(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Utakmica ne postoji ili je već neaktivna!");
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
