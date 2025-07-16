package com.example.studentdb.controller;

import com.example.studentdb.model.Student;
// import com.example.studentdb.service.LibraryService;
import com.example.studentdb.service.StudentService;
import jakarta.validation.Valid;
// import com.example.studentdb.Trie;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
    // private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    // private Trie trie;
   @PostMapping("/insert")
    public ResponseEntity<String> insertStudent(@Valid @RequestBody Student student) {
        String result = studentService.insertStudent(student);
        // trie.insert(student.getFirstName()+" "+student.getLastName(),student);
        return result.contains("already") ? ResponseEntity.badRequest().body(result)
                                        : ResponseEntity.ok(result);
    }
    //  ResponseEntity is a wrapper of HTTP body
    //  Get all students rest api
    @GetMapping("/allStudents")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    // range of marks

    @GetMapping("/marks/{minMarks}/{maxMarks}")
    public ResponseEntity<Map<String, Object>> getByMarksRange(
            @PathVariable int minMarks,
            @PathVariable int maxMarks) {

        Map<String, Object> response = studentService.getStudentsInMarksRange(minMarks, maxMarks);
        return ResponseEntity.ok(response);
    }
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Object result = studentService.getStudentById(id);
        return result instanceof Student ? ResponseEntity.ok(result)
                                         : ResponseEntity.status(404).body(result);
    }
    @GetMapping("/firstname/{name}")
    public ResponseEntity<List<Student>> searchByFirstName(@PathVariable String name) {
        List<Student> result = studentService.searchByFirstName(name);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/topper")
    public ResponseEntity<Object> getTopScoringStudent() {
        return ResponseEntity.ok(studentService.getTopper());
    }
    @GetMapping("/stats")
    public ResponseEntity<Object> getStandardDeviation() {
        return ResponseEntity.ok(studentService.getStandardDeviation());
    }
    // @GetMapping("/")
    // public ResponseEntity<List<Student>> searchTrie(@RequestParam String name) {
    //     List<Student> result = studentService.searchTrie(name,trie);
    //     return ResponseEntity.ok(result);
    // }
}