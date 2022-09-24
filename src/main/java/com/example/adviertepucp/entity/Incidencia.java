package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "incidencia")
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idincidencia", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "titulo", nullable = false, length = 45)
    private String titulo;

    @Size(max = 300)
    @NotNull
    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Size(max = 10)
    @NotNull
    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @Size(max = 8)
    @NotNull
    @Column(name = "urgencia", nullable = false)
    private String urgencia;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "tipoincidencia", nullable = false)
    private Tipoincidencia tipoincidencia;

    @NotNull
    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @NotNull
    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "zonapucp", nullable = false)
    private Zonapucp zonapucp;

    @ManyToMany
    @JoinTable(name = "incidenciatienefoto",
            joinColumns = @JoinColumn(name = "idincidencia"),
            inverseJoinColumns = @JoinColumn(name = "idfotoalmacenada"))
    private Set<Fotoalmacenada> fotoalmacenadas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    public Tipoincidencia getTipoincidencia() {
        return tipoincidencia;
    }

    public void setTipoincidencia(Tipoincidencia tipoincidencia) {
        this.tipoincidencia = tipoincidencia;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Zonapucp getZonapucp() {
        return zonapucp;
    }

    public void setZonapucp(Zonapucp zonapucp) {
        this.zonapucp = zonapucp;
    }

    public Set<Fotoalmacenada> getFotoalmacenadas() {
        return fotoalmacenadas;
    }

    public void setFotoalmacenadas(Set<Fotoalmacenada> fotoalmacenadas) {
        this.fotoalmacenadas = fotoalmacenadas;
    }

}