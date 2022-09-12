//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.adviertepucp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "categoria"
)
public class Categoria {
    @Id
    @Column(
            name = "idcategoria",
            nullable = false
    )
    private Integer id;
    @Column(
            name = "nombre",
            nullable = false,
            length = 45
    )
    private String nombre;

    public Categoria() {
    }
}
