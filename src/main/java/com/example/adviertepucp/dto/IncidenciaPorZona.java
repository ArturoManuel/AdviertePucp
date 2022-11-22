package com.example.adviertepucp.dto;

public interface IncidenciaPorZona {

    //select zp.nombre from incidencia
    // "inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)

    String getNombre();
}
