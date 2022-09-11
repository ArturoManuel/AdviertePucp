package com.example.adviertepucp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "incidencia")
public class Incidencia {
    @Id
    @Column(name = "idincidencia", nullable = false)
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 45)
    private String titulo;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @Column(name = "urgencia", nullable = false, length = 8)
    private String urgencia;

    @Column(name = "ubicacion", nullable = false, length = 100)
    private String ubicacion;

    @Column(name = "zonapucp", nullable = false, length = 100)
    private String zonapucp;



}