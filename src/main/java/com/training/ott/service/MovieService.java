package com.training.ott.service;

import com.training.ott.model.Movie;
import com.training.ott.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    public Movie saveMovie(Movie movie) {
        return repository.save(movie);
    }

    public List<Movie> getByGenre(String genre) {
        return repository.findByGenreIgnoreCase(genre);
    }
}
