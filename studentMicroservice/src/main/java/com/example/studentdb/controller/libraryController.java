package com.example.studentdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentdb.model.Library;
import com.example.studentdb.service.LibraryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/library")
@CrossOrigin("*")
public class libraryController {
    @Autowired
    private LibraryService libraryService; // dependency injection

    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@Valid @RequestBody Library entry  ) {
        //TODO: process POST request
        String result = libraryService.register(entry);
        
        return result.contains("already") ? ResponseEntity.badRequest().body(result)
                                        : ResponseEntity.ok(result);
    }

}   
