package com.example.Mess.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "mess")
public class Mess {
    @Column(name = "id")
    @Id
    @NotNull
    private Integer id;
    @Column(name = "food_type")
    private String foodType;
    void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public String getFoodType() {
        return foodType;
    }
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
