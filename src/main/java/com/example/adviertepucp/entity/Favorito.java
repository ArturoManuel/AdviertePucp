package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "favorito")
public class Favorito {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private FavoritoId id;

    @MapsId("usuarioCodigo")
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_codigo", nullable = false)
    private Usuario usuarioCodigo;

    @MapsId("incidenciaIdincidencia")
    @ManyToOne(optional = false)
    @JoinColumn(name = "incidencia_idincidencia", nullable = false)
    private Incidencia incidenciaIdincidencia;

    @NotNull
    @Column(name = "esfavorito", nullable = false)
    private Byte esfavorito;

    public FavoritoId getId() {
        return id;
    }

    public void setId(FavoritoId id) {
        this.id = id;
    }

    public Usuario getUsuarioCodigo() {
        return usuarioCodigo;
    }

    public void setUsuarioCodigo(Usuario usuarioCodigo) {
        this.usuarioCodigo = usuarioCodigo;
    }

    public Incidencia getIncidenciaIdincidencia() {
        return incidenciaIdincidencia;
    }

    public void setIncidenciaIdincidencia(Incidencia incidenciaIdincidencia) {
        this.incidenciaIdincidencia = incidenciaIdincidencia;
    }

    public Byte getEsfavorito() {
        return esfavorito;
    }

    public void setEsfavorito(Byte esfavorito) {
        this.esfavorito = esfavorito;
    }

}