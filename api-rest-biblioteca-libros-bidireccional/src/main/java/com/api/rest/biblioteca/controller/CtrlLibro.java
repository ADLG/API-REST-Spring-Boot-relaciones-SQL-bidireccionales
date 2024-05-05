package com.api.rest.biblioteca.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.rest.biblioteca.entity.Biblioteca;
import com.api.rest.biblioteca.entity.Libro;
import com.api.rest.biblioteca.repository.RepoBiblioteca;
import com.api.rest.biblioteca.repository.RepoLibro;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/libros")
public class CtrlLibro
{
	@Autowired
	private RepoLibro repoLibro;

	@Autowired
	private RepoBiblioteca repoBiblioteca;

	@GetMapping
	public ResponseEntity<Page<Libro>> listarLibros(Pageable pageable)
	{
		return ResponseEntity.ok(repoLibro.findAll(pageable));
	}

	@PostMapping
	public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libro)
	{
		Optional<Biblioteca> bibliotecaOptional = repoBiblioteca.findById(libro.getBiblioteca().getId());
		if (!bibliotecaOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}

		libro.setBiblioteca(bibliotecaOptional.get());
		Libro libroGuardado = repoLibro.save(libro);

		URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(libroGuardado.getId()).toUri();

		return ResponseEntity.created(ubicacion).body(libroGuardado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Libro> actualizarLibro(@PathVariable Integer id, @Valid @RequestBody Libro libro)
	{
		Optional<Biblioteca> bibliotecaOptional = repoBiblioteca.findById(libro.getBiblioteca().getId());
		if (!bibliotecaOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}

		Optional<Libro> libroOptional = repoLibro.findById(id);
		if (!libroOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}

		libro.setBiblioteca(bibliotecaOptional.get());
		libro.setId(libroOptional.get().getId());
		repoLibro.save(libro);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Libro> eliminarLibro(@PathVariable Integer id)
	{
		Optional<Libro> libroOptional = repoLibro.findById(id);
		if (!libroOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}

		repoLibro.delete(libroOptional.get());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Integer id)
	{
		Optional<Libro> libroOptional = repoLibro.findById(id);
		if (!libroOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}

		return ResponseEntity.ok(libroOptional.get());
	}
}


