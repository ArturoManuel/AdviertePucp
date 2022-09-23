package com.example.adviertepucp.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "tipoincidencia", schema = "adviertedb", catalog = "")
public class Tipoincidencia {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idtipoincidencia")
    private int idtipoincidencia;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "color")
    private String color;
    @Basic
    @Column(name = "logo")
    private byte[] logo;

    public int getIdtipoincidencia() {
        return idtipoincidencia;
    }

    public void setIdtipoincidencia(int idtipoincidencia) {
        this.idtipoincidencia = idtipoincidencia;
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

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tipoincidencia that = (Tipoincidencia) o;
        return idtipoincidencia == that.idtipoincidencia && Objects.equals(nombre, that.nombre) && Objects.equals(color, that.color) && Arrays.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idtipoincidencia, nombre, color);
        result = 31 * result + Arrays.hashCode(logo);
        return result;
    }
}
