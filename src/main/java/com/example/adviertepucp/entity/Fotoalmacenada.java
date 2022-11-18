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
    private String fotoalmacenada;

    @Size(max = 255)
    @NotNull
    @Column(name = "tipofoto", nullable = false)
    private String tipofoto;

    @ManyToMany
    @JoinTable(name = "incidenciatienefoto",
            joinColumns = @JoinColumn(name = "idfotoalmacenada"),
            inverseJoinColumns = @JoinColumn(name = "idincidencia"))
    private Set<Incidencia> incidencias = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFotoalmacenada() {
        return fotoalmacenada;
    }

    public void setFotoalmacenada(String fotoalmacenada) {
        this.fotoalmacenada = fotoalmacenada;
    }

    public String getTipofoto() {
        return tipofoto;
    }

    public void setTipofoto(String tipofoto) {
        this.tipofoto = tipofoto;
    }

    public Set<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(Set<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }
}