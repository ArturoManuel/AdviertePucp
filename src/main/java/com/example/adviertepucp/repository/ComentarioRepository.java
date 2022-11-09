package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Usuario;
import com.example.adviertepucp.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.dto.IncidenciaComentarioDto;
import com.example.adviertepucp.entity.Favorito;
import com.example.adviertepucp.entity.Fotoalmacenada;
import com.example.adviertepucp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.transaction.Transactional;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, String> {

    @Query (value = "SELECT c.idcomentario as 'idcomentario', c.idincidencia as 'idincidencia', c.nombre as 'nombre',\n" +
            "c.usuario_codigo as'usuario_codigo', concat(substring(c.fecha,1,10),'  (' ,substring(c.fecha,12,5),')') as 'fecha', concat(u.nombre,' ' , u.apellido) as 'nombreusuario',\n" +
            "ca.nombre as 'rol'\n" +
            "FROM comentario c \n" +
            "left join usuario u on (c.usuario_codigo= u.codigo)\n" +
            "left join categoria ca on (ca.idcategoria= u.categoria)\n" +
            "where idincidencia=?1",
            nativeQuery = true)
    List<IncidenciaComentarioDto> listaComentario( Integer idincidencia);
}
