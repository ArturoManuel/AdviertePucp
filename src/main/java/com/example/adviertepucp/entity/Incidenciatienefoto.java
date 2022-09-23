package com.example.adviertepucp.entity;

import javax.persistence.*;

@Entity
@Table(name = "incidenciatienefoto")
public class Incidenciatienefoto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private IncidenciatienefotoId id;

    @MapsId("idfotoalmacenada")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idfotoalmacenada", nullable = false)
    private Fotoalmacenada idfotoalmacenada;

    @MapsId("idincidencia")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idincidencia", nullable = false)
    private Incidencia idincidencia;

    public IncidenciatienefotoId getId() {
        return id;
    }

    public void setId(IncidenciatienefotoId id) {
        this.id = id;
    }

    public Fotoalmacenada getIdfotoalmacenada() {
        return idfotoalmacenada;
    }

    public void setIdfotoalmacenada(Fotoalmacenada idfotoalmacenada) {
        this.idfotoalmacenada = idfotoalmacenada;
    }

    public Incidencia getIdincidencia() {
        return idincidencia;
    }

    public void setIdincidencia(Incidencia idincidencia) {
        this.idincidencia = idincidencia;
    }

}