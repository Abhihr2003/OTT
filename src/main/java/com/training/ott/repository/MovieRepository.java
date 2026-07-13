package com.training.ott.repository;

import com.training.ott.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    	List<Movie> findByGenreIgnoreCase(String genre);
}
