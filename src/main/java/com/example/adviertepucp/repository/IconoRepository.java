package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Icono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconoRepository extends JpaRepository<Icono, Integer> {
}