package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Favorito;
import com.example.adviertepucp.entity.FavoritoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
}
