package com.example.studentdb.service;

import com.example.studentdb.model.Student;
import com.example.studentdb.repository.StudentRepository;
import com.example.studentdb.dto.LibraryRegistrationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class StudentService {

    private final WebClient webClient;
    private final StudentRepository repo;

    public StudentService(WebClient webClient, StudentRepository repo) {
        this.webClient = webClient;
        this.repo = repo;
    }

    public Student addStudent(Student s) {
        if (s.getId() != null && repo.existsById(s.getId())) {
            throw new RuntimeException("Student already exists");
        }

        Student saved = repo.save(s);
        notifyLibrary(saved);
        return saved;
    }

    public Student getStudentById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public List<Student> getByMarksRange(int min, int max) {
        return repo.findByMarksBetween(min, max);
    }

    public List<Student> getStudentsByFirstName(String name) {
        return repo.findByFirstNameContainingIgnoreCase(name);
    }

    public List<Student> getStudentsByLastName(String name) {
        return repo.findByLastNameContainingIgnoreCase(name);
    }

    public List<Student> getTopper() {
        Optional<Integer> max = repo.findMaxMarks();
        return max.map(repo::findByMarks)
                  .orElse(Collections.emptyList());
    }

    private void notifyLibrary(Student student) {
    LibraryRegistrationRequest req = new LibraryRegistrationRequest();
    req.setId(student.getId());
    req.setCourse(student.getCourse());

    webClient.post()
            .uri("/library/register")
            .bodyValue(req)
            .retrieve()
            .bodyToMono(String.class)
            .block();   // Wait for response
}
}