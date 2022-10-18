package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Usuariobd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UsuarioBDRepository extends JpaRepository<Usuariobd, Integer> {


    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE usuariobd SET nombre = ?1, apellido = ?2, dni = ?3," +
                    " correo = ?4 WHERE (codigo = ?5);")
    void actualizarUsuarioBD(String nombre, String apellido, String dni,
                             String email, String id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "insert into usuariobd (codigo, nombre, apellido, dni, correo)" +
                    " values (?1, ?2, ?3, ?4, ?5);")
    void crearUsuarioBD(String codigo, String nombre, String apellido, String dni, String correo);

}
