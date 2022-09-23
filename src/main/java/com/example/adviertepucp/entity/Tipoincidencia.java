package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tipoincidencia")
public class Tipoincidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipoincidencia", nullable = false)
    private Integer id;

    @Size(max = 60)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Size(max = 45)
    @NotNull
    @Column(name = "color", nullable = false, length = 45)
    private String color;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "logo", nullable = false)
    private Fotoalmacenada logo;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Fotoalmacenada getLogo() {
        return logo;
    }

    public void setLogo(Fotoalmacenada logo) {
        this.logo = logo;
    }

}