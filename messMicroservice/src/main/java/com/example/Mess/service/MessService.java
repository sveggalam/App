package com.example.Mess.service;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.Mess.dto.StudentRegistration;
import com.example.Mess.model.Mess;
import com.example.Mess.repository.MessRepository;
import reactor.core.publisher.Mono;
import java.util.*;

@Service
public class MessService {

    private final WebClient webClient;
    private final MessRepository repo;

    public MessService(WebClient webClient, MessRepository repo) {
        this.webClient = webClient;
        this.repo = repo;
    }

    public Mess addStudent(Mess m) {

        StudentRegistration student = webClient.get()
                .uri("/students/{id}", m.getId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(
                                new RuntimeException("Student not found")))
                .bodyToMono(StudentRegistration.class)
                .block();

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        return repo.save(m);
    }

    public List<Mess> getAllStudents() {
        return repo.findAll();
    }
}
