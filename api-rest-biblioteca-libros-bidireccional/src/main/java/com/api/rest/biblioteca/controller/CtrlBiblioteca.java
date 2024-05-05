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
import com.api.rest.biblioteca.repository.RepoBiblioteca;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/biblioteca")
public class CtrlBiblioteca
{
	@Autowired
	private RepoBiblioteca repoBiblioteca;

	@GetMapping
	public ResponseEntity<Page<Biblioteca>> listarBibliotecas(Pageable pageable)
	{
		return ResponseEntity.ok(repoBiblioteca.findAll(pageable));
	}

	@PostMapping
	public ResponseEntity<Biblioteca> guardarBiblioteca(@Valid @RequestBody Biblioteca biblioteca)
	{
		Biblioteca bibliotecaGuardada = repoBiblioteca.save(biblioteca);
		URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(bibliotecaGuardada.getId()).toUri();
		return ResponseEntity.created(ubicacion).body(bibliotecaGuardada);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Biblioteca> actualizarBiblioteca(@PathVariable Integer id,@Valid @RequestBody Biblioteca biblioteca)
	{
		Optional<Biblioteca> bibliotecaOptional = repoBiblioteca.findById(id);

		if (!bibliotecaOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}

		biblioteca.setId(bibliotecaOptional.get().getId());
		repoBiblioteca.save(biblioteca);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Biblioteca> eliminarBiblioteca(@PathVariable Integer id)
	{
		Optional<Biblioteca> bibliotecaOptional = repoBiblioteca.findById(id);

		if (!bibliotecaOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}

		repoBiblioteca.delete(bibliotecaOptional.get());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Biblioteca> obtenerBibliotecaPorId(@PathVariable Integer id)
	{
		Optional<Biblioteca> bibliotecaOptional = repoBiblioteca.findById(id);

		if (!bibliotecaOptional.isPresent())
		{
			return ResponseEntity.unprocessableEntity().build();
		}
		
		return ResponseEntity.ok(bibliotecaOptional.get());
	}

}
