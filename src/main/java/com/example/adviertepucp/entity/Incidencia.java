package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "incidencia")
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idincidencia", nullable = false)
    private Integer id;

    @Column(name = "fecha")
    private Instant fecha;

    @Size(max = 10)
    @Column(name = "estado", length = 10)
    private String estado;

    @Size(max = 8)
    @Column(name = "urgencia", length = 8)
    private String urgencia;

    @ManyToOne
    @JoinColumn(name = "tipoincidencia")
    private Tipoincidencia tipoincidencia;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @ManyToOne
    @JoinColumn(name = "zonapucp")
    private Zonapucp zonapucp;

    @NotNull
    @Column(name = "publicado", nullable = false)
    private Integer publicado;


    @ManyToMany
    @JoinTable(name = "incidenciatienefoto",
            joinColumns = @JoinColumn(name = "idincidencia"),
            inverseJoinColumns = @JoinColumn(name = "idfotoalmacenada"))
    private Set<Fotoalmacenada> fotoalmacenadas = new LinkedHashSet<>();

    @Size(max = 110)
    @Column(name = "titulo", length = 110)
    private String titulo;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPublicado() {
        return publicado;
    }

    public void setPublicado(Integer publicado) {
        this.publicado = publicado;
    }

    public Set<Fotoalmacenada> getFotoalmacenadas() {
        return fotoalmacenadas;
    }

    public void setFotoalmacenadas(Set<Fotoalmacenada> fotoalmacenadas) {
        this.fotoalmacenadas = fotoalmacenadas;
    }

}