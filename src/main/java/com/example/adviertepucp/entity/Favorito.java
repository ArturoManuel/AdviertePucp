package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

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
    private Integer esfavorito;

    @NotNull
    @Column(name = "hacomentado", nullable = false)
    private Integer hacomentado;

    @NotNull
    @Column(name = "hasolucionado", nullable = false)
    private Integer hasolucionado;

    @NotNull
    @Column(name = "pusoenproceso", nullable = false)
    private Integer pusoenproceso;

    @NotNull
    @Column(name = "reaperturacaso", nullable = false)
    private Integer reaperturacaso;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

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

    public Integer getEsfavorito() {
        return esfavorito;
    }

    public void setEsfavorito(Integer esfavorito) {
        this.esfavorito = esfavorito;
    }

    public Integer getHacomentado() {
        return hacomentado;
    }

    public void setHacomentado(Integer hacomentado) {
        this.hacomentado = hacomentado;
    }

    public Integer getHasolucionado() {
        return hasolucionado;
    }

    public void setHasolucionado(Integer hasolucionado) {
        this.hasolucionado = hasolucionado;
    }

    public Integer getPusoenproceso() {
        return pusoenproceso;
    }

    public void setPusoenproceso(Integer pusoenproceso) {
        this.pusoenproceso = pusoenproceso;
    }

    public Integer getReaperturacaso() {
        return reaperturacaso;
    }

    public void setReaperturacaso(Integer reaperturacaso) {
        this.reaperturacaso = reaperturacaso;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

}