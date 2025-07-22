package com.example.Mess.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Mess.model.Mess;

public interface MessRepository extends JpaRepository<Mess,Integer>{

}