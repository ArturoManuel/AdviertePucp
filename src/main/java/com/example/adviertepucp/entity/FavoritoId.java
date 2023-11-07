package com.example.adviertepucp.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoritoId implements Serializable {
    private static final long serialVersionUID = -295002704995266924L;
    @Size(max = 8)
    @NotNull
    @Column(name = "usuario_codigo", nullable = false, length = 8)
    private String usuarioCodigo;

    @NotNull
    @Column(name = "incidencia_idincidencia", nullable = false)
    private Integer incidenciaIdincidencia;

    public String getUsuarioCodigo() {
        return usuarioCodigo;
    }

    public void setUsuarioCodigo(String usuarioCodigo) {
        this.usuarioCodigo = usuarioCodigo;
    }

    public Integer getIncidenciaIdincidencia() {
        return incidenciaIdincidencia;
    }

    public void setIncidenciaIdincidencia(Integer incidenciaIdincidencia) {
        this.incidenciaIdincidencia = incidenciaIdincidencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FavoritoId entity = (FavoritoId) o;
        return Objects.equals(this.incidenciaIdincidencia, entity.incidenciaIdincidencia) &&
                Objects.equals(this.usuarioCodigo, entity.usuarioCodigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(incidenciaIdincidencia, usuarioCodigo);
    }

}