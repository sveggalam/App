package com.example.library.controller;

import com.example.library.model.Library;
import com.example.library.service.LibraryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/library")
@CrossOrigin("*")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@Valid @RequestBody Library entry  ) {
        //TODO: process POST request
        String result = libraryService.register(entry);
        
        return result.contains("already") ? ResponseEntity.badRequest().body(result)
                                        : ResponseEntity.ok(result);
    }
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        if (id == null) 
            return ResponseEntity.badRequest().build(); // 400 Bad Request

        List<String> books = libraryService.getListofBooks(id);

        if (books == null || books.isEmpty()) 
            return ResponseEntity.notFound().build(); // 404 Not Found
        return ResponseEntity.ok(books);
    }
    @GetMapping("/{id:\\d+}/{bookName}")
    public ResponseEntity<?> checkBook(@PathVariable Integer id, @PathVariable String bookName) {
        if (id == null || bookName == null) 
            return ResponseEntity.badRequest().build(); // 400 Bad Request
    
        boolean result = libraryService.checkForBookAccess(id, bookName);
        if (!result) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Student not found or has no access to book");
        }
        return ResponseEntity.ok("Has Access");
    }
    @GetMapping("/all")
    public ResponseEntity<List<Library>> getAllStudents() {
        List<Library> students = libraryService.getAllStudents();
        if (students.isEmpty()) 
            return ResponseEntity.noContent().build(); // 204 No Content   
        return ResponseEntity.ok(students); // 200 OK
    }
    @GetMapping("/{CourseName}/books")  
    public ResponseEntity<List<String>> getBooksByCourse(@PathVariable String CourseName) {
        List<String> books = libraryService.getListofBooksByCourse(CourseName);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        return ResponseEntity.ok(books); // 200 OK
    }
}