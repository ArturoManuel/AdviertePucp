package com.example.adviertepucp.dto;

public interface UsuarioPorCategoria  {

   //select  u.codigo as codigo ,
    // u.nombre  as nombre , u.apellido as apellido ,
    // u.suspendido as estado ,
    // c.nombre as categoria
    // from usuario u inner join categoria c on (u.categoria=c.idcategoria);
    int getCodigo();
    String nombre();
    String apellido();



}