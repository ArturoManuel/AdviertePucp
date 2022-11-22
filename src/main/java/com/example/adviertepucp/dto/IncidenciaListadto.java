package com.example.adviertepucp.dto;

public interface IncidenciaListadto {

   // select titulo as "titulo" ,
    // descripcion as "descripcion" ,
    // fecha as "fecha" ,
    // estado as "estado" ,
    // urgencia as"urgencia",
    // t.nombre as "tincidencia" ,
    // latitud as "latitud", longitud as "longitud" ,
    // z.nombre as "zonapucp"
    // from incidencia i inner join zonapucp z on (z.idzonapucp=i.zonapucp) inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia);
    int getIdI();
    String getTitulo();
    String getDescripcion();
    String getFecha();
    String getEstado();
    String getUrgencia();
    String getTincidencia();
    String getColor();
    double getLatitud();
    double getLongitud();
    String getZonapucp();
    String getCreador();
    Integer getUsuario_codigo();
    String getEsfavorito();




}