package com.example.library.repository;

import com.example.library.model.Library;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface LibraryRepository extends JpaRepository<Library,Integer>{

}