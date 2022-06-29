package com.api.rest.biblioteca.repositorios;

import com.api.rest.biblioteca.entidades.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliotecaRepository extends JpaRepository<Biblioteca,Integer> {
}
