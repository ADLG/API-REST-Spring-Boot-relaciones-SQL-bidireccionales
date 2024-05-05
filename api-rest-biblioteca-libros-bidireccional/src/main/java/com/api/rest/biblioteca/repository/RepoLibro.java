package com.api.rest.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.rest.biblioteca.entity.Libro;

public interface RepoLibro extends JpaRepository<Libro, Integer>{

}
