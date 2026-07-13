package com.training.ott.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String genre;

    @Enumerated(EnumType.STRING)
    private Plan requiredPlan;

    public Movie() {
    }

    public Movie(String title, String genre, Plan requiredPlan) {
        this.title = title;
        this.genre = genre;
        this.requiredPlan = requiredPlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Plan getRequiredPlan() {
        return requiredPlan;
    }

    public void setRequiredPlan(Plan requiredPlan) {
        this.requiredPlan = requiredPlan;
    }
}
