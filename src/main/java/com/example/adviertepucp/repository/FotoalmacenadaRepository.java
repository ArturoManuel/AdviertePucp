package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Fotoalmacenada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoalmacenadaRepository extends JpaRepository<Fotoalmacenada,Integer> {
    Fotoalmacenada findFirstByOrderByIdDesc();
}
