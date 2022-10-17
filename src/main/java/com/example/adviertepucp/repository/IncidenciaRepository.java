package com.example.adviertepucp.repository;

import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.dto.TipoIncidenciadto;
import com.example.adviertepucp.dto.IncidenciaDashboardDto;
import com.example.adviertepucp.dto.IncidenciaPorZona;
import com.example.adviertepucp.entity.Incidencia;
import com.example.adviertepucp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    @Query(value = "select " +
            "t.idtipoincidencia as idt , t.nombre as nombret , t.color as colort from tipoincidencia t",nativeQuery = true)
    List<TipoIncidenciadto> listaTipo();

    //filtro
    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion as descripcion , fecha as fecha , \n" +
            "estado as estado , urgencia as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud, \n" +
            "longitud as longitud ,  z.nombre as zonapucp \n" +
            "from incidencia i \n" +
            "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "WHERE (i.fecha >= concat(?1, ' ', '0:0:0') AND i.fecha <= concat(?2, ' ', '23:59:59')) and (i.estado = ?3) and (t.nombre = ?4);",
            nativeQuery = true)
    List<IncidenciaListadto> buscarlistaFiltroIncidencia(String fechainicio, String fechafin,String estado, String nombre);

    //dashboard
    @Query(value = "select count(idincidencia) from incidencia WHERE fecha > NOW() - INTERVAL 1 MONTH",
            nativeQuery = true)
    Integer incidenciasPorMes();

    @Query(value = "select count(idincidencia) from incidencia WHERE fecha > NOW() - INTERVAL 12 MONTH",
            nativeQuery = true)
    Integer incidenciasPorAnio();

    @Query(value = "select count(idincidencia) from incidencia WHERE estado = 'atendido'",
            nativeQuery = true)
    Integer incidenciasAtendidas();

    //select zp.nombre from incidencia i
    //inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)
    @Query(value = "select zp.nombre as'nombre', count(i.zonapucp) as 'zona' from incidencia i\n" +
            "inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)\n" +
            "group by zp.nombre;",
            nativeQuery = true)
    List<IncidenciaPorZona> ubicacionesPUCP();

    //select count(codigo) from usuario WHERE suspendido = '1';

    @Query(value = "SELECT  concat(round((count(u.suspendido)*100)/t.total, 2), '%') as 'porcentaje' from usuario u\n" +
            "CROSS JOIN (SELECT count(codigo)  as total FROM usuario u) t\n" +
            "where u.suspendido=1",
            nativeQuery = true)
    String usuariosReportados();

    @Query(value = "select count(codigo) from usuario",
            nativeQuery = true)
    List<Usuario> totalUsuarios();


    //lista de todos los estados
    @Query(value = "SELECT count(codigo),\n" +
            "CASE WHEN suspendido = 0 THEN 'activo' WHEN suspendido = 1 THEN 'suspendido' ELSE 'no registrado' END AS estado\n" +
            "FROM adviertedb.usuario\n" +
            "group by estado;",
    nativeQuery = true)
    List<Usuario> estadoUsuarios();

    @Query(value = "SELECT  concat(u.nombre,' ' , u.apellido) as 'nombre', count(i.idincidencia) as 'cantidad' from usuario u\n" +
            "left join favorito f on (f.usuario_codigo= u.codigo)\n" +
            "left join incidencia i on (i.idincidencia= f.incidencia_idincidencia)\n" +
            "group by concat(u.nombre,' ' , u.apellido) \n" +
            "order by count(i.idincidencia) desc LIMIT 10;",
            nativeQuery = true)
    List<IncidenciaDashboardDto> UsariosconMasIncidencias();

    //@Modifying
   // @Transactional
    //@Query(value="update incidencia set estado= ?1 where idincidencia=?2",nativeQuery = true)
    //void atenderIncidencia(String estado, int id);
    //select round((count(f.esfavorito)*100)/( count(i.idincidencia)), 2) as 'porcentajedeFavPor',  count(i.idincidencia)  from favorito f
    //left join incidencia i on (f.incidencia_idincidencia = i.idincidencia);


}