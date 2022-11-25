package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Usuario;
import com.example.adviertepucp.entity.Zonapucp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonapucpRepository extends JpaRepository<Zonapucp,Integer>{

    @Query(value = "select * from zonapucp where nombre=?1 limit 1 ",nativeQuery = true)
    Zonapucp validarZonaPucp(String zonapucp);


}
