package org.unibl.eurobasket.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.eurobasket.service.AuthenticationService;
import org.unibl.eurobasket.service.NewsService;

@RestController
@RequestMapping(value = "/news")
public class NewsController {

    private NewsService newsService;
    private AuthenticationService authenticationService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews().toString());
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNews(@RequestParam String title, @RequestParam String content) {
        if (authenticationService.getCurrentUser().isEmpty()) {
            return ResponseEntity.badRequest().body("Niste prijavljeni!");
        } else if (title.length() < 3 || content.length() < 3) {
            return ResponseEntity.badRequest().body("Dužina naslova i sadržaja mora biti veća od 3!");
        } else {
            return ResponseEntity.ok(newsService.createNews(title, content, authenticationService.getCurrentUser().get()).toString());
        }
    }

    @PostMapping(value = "/comment/create/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addComment(@PathVariable int id, @RequestParam String text) {
        if (authenticationService.getCurrentUser().isEmpty()) {
            return ResponseEntity.badRequest().body("Niste prijavljeni!");
        }
        JSONObject response = newsService.addComment(id, text, authenticationService.getCurrentUser().get());
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Vijest koja ima id " + id + " ne postoji!");
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteNews(@PathVariable int id) {
        JSONObject response = newsService.deleteNewsById(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Vijest koja ima id " + id + " ne postoji!");
    }

    @DeleteMapping(value = "/comment/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id) {
        JSONObject response = newsService.deleteCommentById(id);
        return response != null ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().body("Komentar koja ima id " + id + " ne postoji!");
    }

    @Autowired
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
