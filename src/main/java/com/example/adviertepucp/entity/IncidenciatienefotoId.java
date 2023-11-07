package com.example.adviertepucp.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class IncidenciatienefotoId implements Serializable {
    private static final long serialVersionUID = 2226191318551889480L;
    @NotNull
    @Column(name = "idfotoalmacenada", nullable = false)
    private Integer idfotoalmacenada;

    @NotNull
    @Column(name = "idincidencia", nullable = false)
    private Integer idincidencia;

    public Integer getIdfotoalmacenada() {
        return idfotoalmacenada;
    }

    public void setIdfotoalmacenada(Integer idfotoalmacenada) {
        this.idfotoalmacenada = idfotoalmacenada;
    }

    public Integer getIdincidencia() {
        return idincidencia;
    }

    public void setIdincidencia(Integer idincidencia) {
        this.idincidencia = idincidencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IncidenciatienefotoId entity = (IncidenciatienefotoId) o;
        return Objects.equals(this.idfotoalmacenada, entity.idfotoalmacenada) &&
                Objects.equals(this.idincidencia, entity.idincidencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idfotoalmacenada, idincidencia);
    }

}