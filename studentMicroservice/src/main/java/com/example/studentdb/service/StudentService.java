package com.example.studentdb.service;

import com.example.studentdb.model.Student;
import com.example.studentdb.repository.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.studentdb.dto.LibraryRegistrationRequest;
import java.io.File;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.studentdb.Trie;
@Service

public class StudentService {
    private static final String FILE_PATH = "students.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer,Student> studentMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private WebClient webClient;
    @Autowired
    private StudentRepository repo;

    public StudentService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void notifyLibrary(Student student) {
        LibraryRegistrationRequest req = new LibraryRegistrationRequest();
        req.setId(student.getId());
        req.setCourse(student.getCourse());

        webClient.post()
            .uri("/library/register")
            .bodyValue(req)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(response -> {
                System.out.println("Library service response: " + response);
            });
    }

    // private List<Student> readFromFile() {
    //     try {
    //         File file = new File(FILE_PATH);
    //         if (!file.exists()) return new ArrayList<>();
    //         return objectMapper.readValue(file, new TypeReference<List<Student>>() {});
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return new ArrayList<>();
    //     }
    // }
    // private void writeToFile(List<Student> students,Student student) {
    //     try {
    //         objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), students);
    //         notifyLibrary(student);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public ResponseEntity<?> addStudent(Student s) {
        // Check required fields manually
        if (s.getId() != null && repo.existsById(s.getId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Student with ID " + s.getId() + " already exists.");
        }
        Student saved = repo.save(s);
        notifyLibrary(saved);
        return ResponseEntity.ok(saved);
    }

    // public Map<String, Object> getAllStudents() {
    //     List<Student> students = readFromFile();
    //     Map<String, Object> result = new HashMap<>();
    //     result.put("count", students.size());
    //     result.put("students", students);
    //     logger.info("Requested for all Students");
    //     return result;
    // }

    public ResponseEntity<?> getStudentById(Integer id) {
                Optional<Student> studentOpt = repo.findById(id);
        if (studentOpt.isPresent()) {
            return ResponseEntity.ok(studentOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Student with ID " + id + " not found.");
        }
    }
    public List<Student> getByMarksRange(int minMarks, int maxMarks) {
        return repo.findByMarksBetween(minMarks, maxMarks);
    }
    public List<Student> getStudentsByFirstName(String firstName) {
        return repo.findByFirstNameContainingIgnoreCase(firstName);  //  findByFirstNameIgnoreCase
    }
    public List<Student> getStudentsByLastName(String firstName) {
        return repo.findByLastNameContainingIgnoreCase(firstName);  //  findByFirstNameIgnoreCase
    }
    public List<Student> searchTrie(String name, Trie trie) {
        List<Integer> ids = trie.search(name);
        List<Student> students = new ArrayList<>();
        for(Integer id : ids) {
            students.add(studentMap.get(id));
        }
        return students;
    }  
    public List<Student> getTopper() {
        Integer topMarks = repo.findMaxMarks();
        if (topMarks == null) {
            return new ArrayList<>();
        }
        return repo.findByMarks(topMarks);
    }
}
