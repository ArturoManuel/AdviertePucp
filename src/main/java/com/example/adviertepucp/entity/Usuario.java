package com.example.adviertepucp.entity;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "codigo", nullable = false, length = 8)
    private String id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "dni", length = 8)
    private String dni;

    @Column(name = "celular", length = 9)
    private String celular;

    @Column(name = "correo", nullable = false, length = 80)
    private String correo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "suspendido", nullable = false)
    private Integer suspendido;



}