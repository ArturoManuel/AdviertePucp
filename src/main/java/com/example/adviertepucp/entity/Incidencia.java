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

    @Size(max = 45, message = "El título no puede tener más de 40 caracteres")
    @Column(name = "titulo", length = 45)
    private String titulo;

    @Size(max = 300,message = "La descripción no puede tener más de 300 caracteres")
    @Column(name = "descripcion", length = 300)
    private String descripcion;

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