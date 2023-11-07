package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "iconos")
public class Icono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idicono", nullable = false)
    private Integer id;

    @Size(max = 45)
    @Column(name = "nombre", length = 45)
    private String nombre;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "foto", nullable = false)
    private Fotoalmacenada foto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Fotoalmacenada getFoto() {
        return foto;
    }

    public void setFoto(Fotoalmacenada foto) {
        this.foto = foto;
    }

}