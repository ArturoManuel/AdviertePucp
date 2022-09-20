package com.example.adviertepucp.repository;

import com.example.adviertepucp.dto.TipoIncidenciadto;
import com.example.adviertepucp.entity.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    @Query(value = "select " +
            "t.idtipoincidencia as idt , t.nombre as nombret , t.color as colort from tipoincidencia t",nativeQuery = true)
    List<TipoIncidenciadto> listaTipo();


}