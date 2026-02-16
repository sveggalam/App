package com.example.studentdb.controller;

import com.example.studentdb.model.Student;
import com.example.studentdb.service.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
// Base URL → http://localhost:8081/students
@CrossOrigin("*")
public class StudentController {

    private final StudentService studentService;

    // Constructor Injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // INSERT STUDENT
    // POST http://localhost:8081/students/insert
    @PostMapping("/insert")
    public ResponseEntity<Student> insertStudent(@Valid @RequestBody Student s) {
        Student saved = studentService.addStudent(s);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET ALL STUDENTS
    // GET http://localhost:8081/students/allStudents
    @GetMapping("/allStudents")
    public ResponseEntity<List<Student>> getAll() {
        List<Student> students = studentService.getAllStudents();
        return students.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(students);
    }

    // GET STUDENTS BY MARKS RANGE
    // GET http://localhost:8081/students/marks-range?min=60&max=90
    @GetMapping("/marks-range")
    public ResponseEntity<List<Student>> getByMarksRange(@RequestParam int min,
                                                         @RequestParam int max) {
        return ResponseEntity.ok(studentService.getByMarksRange(min, max));
    }

    // GET STUDENT BY ID
    // GET http://localhost:8081/students/1
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Student> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // SEARCH BY FIRST NAME
    // GET http://localhost:8081/students/firstname?name=Alice
    @GetMapping("/firstname")
    public ResponseEntity<List<Student>> searchByFirstName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.getStudentsByFirstName(name));
    }

    // SEARCH BY LAST NAME
    // GET http://localhost:8081/students/lastname?name=Smith
    @GetMapping("/lastname")
    public ResponseEntity<List<Student>> searchByLastName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.getStudentsByLastName(name));
    }

    // GET TOP SCORING STUDENT(S)
    // GET http://localhost:8081/students/topper
    @GetMapping("/topper")
    public ResponseEntity<List<Student>> getTopScoringStudent() {
        return ResponseEntity.ok(studentService.getTopper());
    }
}
