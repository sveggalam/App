package com.example.studentdb.controller;

import com.example.studentdb.model.Student;
import com.example.studentdb.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin("*")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertStudent(@Valid @RequestBody Student s) {
        try {
            Student saved = studentService.addStudent(s);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
    @GetMapping("/allStudents")
    public ResponseEntity<List<Student>> getAll() {
        List<Student> students = studentService.getAllStudents();
        return students.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/marks-range")
    public ResponseEntity<List<Student>> getByMarksRange(@RequestParam int min,
                                                         @RequestParam int max) {
        return ResponseEntity.ok(studentService.getByMarksRange(min, max));
    }

    @GetMapping("/firstname")
    public ResponseEntity<List<Student>> searchByFirstName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.getStudentsByFirstName(name));
    }

    @GetMapping("/lastname")
    public ResponseEntity<List<Student>> searchByLastName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.getStudentsByLastName(name));
    }

    @GetMapping("/topper")
    public ResponseEntity<List<Student>> getTopper() {
        return ResponseEntity.ok(studentService.getTopper());
    }
}