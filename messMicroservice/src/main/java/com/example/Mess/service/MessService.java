package com.example.Mess.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Mess.dto.StudentRegistration;
import com.example.Mess.model.Mess;
import com.example.Mess.repository.MessRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.*;
@Service 
public class MessService {
    @Autowired
    private WebClient webClient;

    @Autowired
    private MessRepository repo;
    public ResponseEntity<?> addStudent(Mess m) {
        try {
            StudentRegistration student = webClient.get()
                .uri("/students/{id}", m.getId())  // use getStudentId(), not getId()
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, 
                        response -> Mono.error(new RuntimeException("Student not found")))
                .bodyToMono(StudentRegistration.class)
                .block();
            System.out.println("test: " + student);
            repo.save(m);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student added to mess successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found in Student Database");
        }
    }
    public List<Mess> getAllStudents() {
        return repo.findAll();
    }

}
