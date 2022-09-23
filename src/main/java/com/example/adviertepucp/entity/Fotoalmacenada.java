package com.example.adviertepucp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "fotoalmacenada")
public class Fotoalmacenada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idfotoalmacenada", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "fotoalmacenada", nullable = false)
    private byte[] fotoalmacenada;

    @Size(max = 255)
    @NotNull
    @Column(name = "tipofoto", nullable = false)
    private String tipofoto;

    @OneToMany(mappedBy = "logo")
    private Set<Tipoincidencia> tipoincidencias = new LinkedHashSet<>();

    @OneToMany(mappedBy = "foto")
    private Set<Icono> iconos = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "incidenciatienefoto",
            joinColumns = @JoinColumn(name = "idfotoalmacenada"),
            inverseJoinColumns = @JoinColumn(name = "idincidencia"))
    private Set<Incidencia> incidencias = new LinkedHashSet<>();

    @OneToMany(mappedBy = "foto")
    private Set<Usuario> usuarios = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getFotoalmacenada() {
        return fotoalmacenada;
    }

    public void setFotoalmacenada(byte[] fotoalmacenada) {
        this.fotoalmacenada = fotoalmacenada;
    }

    public String getTipofoto() {
        return tipofoto;
    }

    public void setTipofoto(String tipofoto) {
        this.tipofoto = tipofoto;
    }

    public Set<Tipoincidencia> getTipoincidencias() {
        return tipoincidencias;
    }

    public void setTipoincidencias(Set<Tipoincidencia> tipoincidencias) {
        this.tipoincidencias = tipoincidencias;
    }

    public Set<Icono> getIconos() {
        return iconos;
    }

    public void setIconos(Set<Icono> iconos) {
        this.iconos = iconos;
    }

    public Set<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(Set<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}