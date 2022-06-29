package com.api.rest.biblioteca.controladores;

import com.api.rest.biblioteca.entidades.Biblioteca;
import com.api.rest.biblioteca.entidades.Libro;
import com.api.rest.biblioteca.repositorios.BibliotecaRepository;
import com.api.rest.biblioteca.repositorios.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibrosRepository librosRepository;
    @Autowired
    private BibliotecaRepository bibliotecaRepository;


    @GetMapping
    public ResponseEntity<Page<Libro>> listarLibros(Pageable pageable){
        return ResponseEntity.ok(librosRepository.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libro){

        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId());
        if(!bibliotecaOptional.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        libro.setBiblioteca(bibliotecaOptional.get());
        Libro libroGuardado = librosRepository.save(libro);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(libroGuardado.getId()).toUri();

        return ResponseEntity.created(ubicacion).body(libroGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> ActualizarLibro(@Valid @RequestBody Libro libro,@PathVariable Integer id){

        Optional<Biblioteca> bibliotecaOptional = bibliotecaRepository.findById(libro.getBiblioteca().getId());
        if(!bibliotecaOptional.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Libro> libroOptional = librosRepository.findById(id);
        if(!libroOptional.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        libro.setBiblioteca(bibliotecaOptional.get());
        libro.setId(libroOptional.get().getId());
        librosRepository.save(libro);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Libro> eliminarLibro(@PathVariable Integer id){
        Optional<Libro> libroOptional = librosRepository.findById(id);
        if(!libroOptional.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        librosRepository.delete(libroOptional.get());
        return ResponseEntity.noContent().build();

    }
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable Integer id){
        Optional<Libro> libroOptional = librosRepository.findById(id);
        if(!libroOptional.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok(libroOptional.get());
    }

}
