package com.example.Mess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Mess.model.Mess;
import com.example.Mess.repository.MessRepository;
import com.example.Mess.service.MessService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/mess")
@CrossOrigin("*")
public class MessController {

    @Autowired
    private MessService mess; // dependency injection

    @Autowired
    private MessRepository repo;
    //  separate registration for mess
    @PostMapping("/insert")
    public ResponseEntity<?> insertStudent(@Valid @RequestBody Mess s) {
        return mess.addStudent(s);
    }
    //  ResponseEntity is a wrapper of HTTP body
    //  Get all students rest api
    @GetMapping("/allStudents")
    public ResponseEntity<?>getAll() {
        // return repo.findAll();
        return repo.findAll().isEmpty() 
            ? ResponseEntity.noContent().build() // 204 No Content
            : ResponseEntity.ok(repo.findAll()); // 200 OK
        // return ResponseEntity.ok(studentService.getAllStudents());
    }
}
