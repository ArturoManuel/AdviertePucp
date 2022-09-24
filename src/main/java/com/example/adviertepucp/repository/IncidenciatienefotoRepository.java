package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Incidenciatienefoto;
import com.example.adviertepucp.entity.IncidenciatienefotoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface IncidenciatienefotoRepository extends JpaRepository<Incidenciatienefoto, IncidenciatienefotoId>{

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
    value = "insert into incidenciatienefoto values (?1,?2)")
    void insertarFotoEIncidencia(Integer idfoto,Integer idincidencia);
}
