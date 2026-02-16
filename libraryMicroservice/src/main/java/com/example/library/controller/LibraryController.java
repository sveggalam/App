package com.example.library.controller;

import com.example.library.model.Library;
import com.example.library.service.LibraryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
// Base URL → http://localhost:8082/library
@CrossOrigin("*")
public class LibraryController {

    private final LibraryService libraryService;

    // Constructor injection
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // REGISTER STUDENT TO LIBRARY
    // POST http://localhost:8082/library/register
    @PostMapping("/register")
    public ResponseEntity<Library> registerStudent(
            @Valid @RequestBody Library entry) {

        Library saved = libraryService.registerStudent(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET ALL REGISTERED STUDENTS
    // GET http://localhost:8082/library/allStudents
    @GetMapping("/allStudents")
    public ResponseEntity<List<Library>> getAllStudents() {

        List<Library> students = libraryService.getAllStudents();

        return students.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students);
    }
}
