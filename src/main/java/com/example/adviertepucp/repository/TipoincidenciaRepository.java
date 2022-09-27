package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Tipoincidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TipoincidenciaRepository
        extends JpaRepository<Tipoincidencia, Integer> {

    @Query(value = "select*from tipoincidencia where idtipoincidencia= ?1", nativeQuery = true)
    Tipoincidencia obtenerTipo(int id);


    @Query(value = "select count(tipoincidencia) from incidencia where  tipoincidencia=?1", nativeQuery=true)
    int incidenciaTipo(int id );
}