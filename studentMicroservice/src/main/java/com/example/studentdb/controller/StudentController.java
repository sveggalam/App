package com.example.studentdb.controller;

import com.example.studentdb.model.Student;
import com.example.studentdb.repository.StudentRepository;
// import com.example.studentdb.service.LibraryService;
import com.example.studentdb.service.StudentService;
import jakarta.validation.Valid;
// import com.example.studentdb.Trie;
import java.util.List;
// import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import com.example.studentdb.Trie;
@RestController
@RequestMapping("/students")
//  use /api/v1/ standard
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentService studentService; // dependency injection

    @Autowired
    private StudentRepository repo;
    // private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    // private Trie trie;
   @PostMapping("/insert")
    public ResponseEntity<?> insertStudent(@Valid @RequestBody Student s) {
        return studentService.addStudent(s);
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
    // range of marks
    // students/marks-range?min=60&max=90
    @GetMapping("/marks-range")
    public ResponseEntity<?> getByMarksRange(@RequestParam int min,@RequestParam int max) {
        List<Student> students = studentService.getByMarksRange(min, max);
        return ResponseEntity.ok(students);
    }
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return studentService.getStudentById(id);
    }
    // GET http://localhost:8080/students/by-firstname?name=Alice

    @GetMapping("/firstname")
    public ResponseEntity<List<Student>> searchByFirstName(@RequestParam String name) {
        List<Student> result = studentService.getStudentsByFirstName(name);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/lastname")
    public ResponseEntity<List<Student>> searchByLastName(@RequestParam String name) {
        List<Student> result = studentService.getStudentsByLastName(name);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/topper")
    public ResponseEntity<List<Student>> getTopScoringStudent() {
        return ResponseEntity.ok(studentService.getTopper());
    }
    // @GetMapping("/stats")
    // public ResponseEntity<Object> getStandardDeviation() {
    //     return ResponseEntity.ok(studentService.getStandardDeviation());
    // }

}