package com.example.Mess.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Mess.model.Mess;
import com.example.Mess.service.MessService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/mess")
@CrossOrigin("*")
public class MessController {

    private final MessService messService;

    public MessController(MessService messService) {
        this.messService = messService;
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertStudent(@Valid @RequestBody Mess m) {
        try {
            Mess saved = messService.addStudent(m);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not found in Student Database");
        }
    }

    @GetMapping("/allStudents")
    public ResponseEntity<List<Mess>> getAll() {

        List<Mess> students = messService.getAllStudents();

        return students.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students);
    }
}
