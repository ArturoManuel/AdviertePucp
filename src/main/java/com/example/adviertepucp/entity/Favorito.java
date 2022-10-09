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
    private Byte esfavorito;

    @NotNull
    @Column(name = "hacomentado", nullable = false)
    private Byte hacomentado;

    @NotNull
    @Column(name = "hasolucionado", nullable = false)
    private Byte hasolucionado;

    @NotNull
    @Column(name = "pusoenproceso", nullable = false)
    private Byte pusoenproceso;

    @NotNull
    @Column(name = "reaperturacaso", nullable = false)
    private Byte reaperturacaso;

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

    public Byte getEsfavorito() {
        return esfavorito;
    }

    public void setEsfavorito(Byte esfavorito) {
        this.esfavorito = esfavorito;
    }

    public Byte getHacomentado() {
        return hacomentado;
    }

    public void setHacomentado(Byte hacomentado) {
        this.hacomentado = hacomentado;
    }

    public Byte getHasolucionado() {
        return hasolucionado;
    }

    public void setHasolucionado(Byte hasolucionado) {
        this.hasolucionado = hasolucionado;
    }

    public Byte getPusoenproceso() {
        return pusoenproceso;
    }

    public void setPusoenproceso(Byte pusoenproceso) {
        this.pusoenproceso = pusoenproceso;
    }

    public Byte getReaperturacaso() {
        return reaperturacaso;
    }

    public void setReaperturacaso(Byte reaperturacaso) {
        this.reaperturacaso = reaperturacaso;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

}