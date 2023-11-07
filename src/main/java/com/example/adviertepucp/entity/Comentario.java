package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "comentario")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomentario", nullable = false)
    private Integer id;

    @Size(max = 300)
    @Column(name = "nombre", length = 300)
    private String nombre;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idincidencia", nullable = false)
    private Incidencia idincidencia;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_codigo", nullable = false)
    private Usuario usuarioCodigo;

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

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Incidencia getIdincidencia() {
        return idincidencia;
    }

    public void setIdincidencia(Incidencia idincidencia) {
        this.idincidencia = idincidencia;
    }

    public Usuario getUsuarioCodigo() {
        return usuarioCodigo;
    }

    public void setUsuarioCodigo(Usuario usuarioCodigo) {
        this.usuarioCodigo = usuarioCodigo;
    }

}