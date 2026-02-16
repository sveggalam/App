package com.example.studentdb.service;

import com.example.studentdb.model.Student;
import com.example.studentdb.repository.StudentRepository;
import com.example.studentdb.dto.LibraryRegistrationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Service layer to handle business logic and interactions with the repository and external services
// No ResponseEntity here, as this is not a controller. Exceptions will be thrown and handled by the controller layer.
@Service
public class StudentService {

    private final WebClient webClient;
    private final StudentRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(WebClient webClient, StudentRepository repo) {
        this.webClient = webClient;
        this.repo = repo;
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
                .subscribe(response ->
                        logger.info("Library service response: {}", response));
    }

    @SuppressWarnings("null")
    public Student addStudent(Student s) {
        if (s.getId() != null && repo.existsById(s.getId())) {
            throw new RuntimeException(
                    "Student with ID " + s.getId() + " already exists.");
        }

        Student saved = repo.save(s);
        notifyLibrary(saved);
        return saved;
    }

    @SuppressWarnings("null")
    public Student getStudentById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student with ID " + id + " not found."));
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public List<Student> getByMarksRange(int minMarks, int maxMarks) {
        return repo.findByMarksBetween(minMarks, maxMarks);
    }

    public List<Student> getStudentsByFirstName(String firstName) {
        return repo.findByFirstNameContainingIgnoreCase(firstName);
    }

    public List<Student> getStudentsByLastName(String lastName) {
        return repo.findByLastNameContainingIgnoreCase(lastName);
    }

    public List<Student> getTopper() {
    Optional<Integer> topMarks = repo.findMaxMarks();
        if (topMarks.isEmpty()) {
            return Collections.emptyList();
        }
        return repo.findByMarks(topMarks.get());
    }

}
