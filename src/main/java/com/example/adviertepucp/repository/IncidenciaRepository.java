package com.example.adviertepucp.repository;

import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.dto.TipoIncidenciadto;
import com.example.adviertepucp.dto.IncidenciaDashboardDto;
import com.example.adviertepucp.dto.UsuarioCantidadIncidencia;
import com.example.adviertepucp.dto.UsarioMasIncidencia;
import com.example.adviertepucp.dto.IncidenciaPorZona;
import com.example.adviertepucp.dto.ZonaPUCP;
import com.example.adviertepucp.entity.Favorito;
import com.example.adviertepucp.entity.Incidencia;
import org.springframework.data.domain.Pageable;
import com.example.adviertepucp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    @Query(value = "select t.idtipoincidencia as idt , t.nombre as nombret , t.color as colort , ft.fotoalmacenada as fotot from tipoincidencia t inner join fotoalmacenada ft on (t.logo=ft.idfotoalmacenada);",nativeQuery = true)
    List<TipoIncidenciadto> listaTipo();

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "insert into favorito values (?1,?2,1,0,0,0,0,?3)")
    void darlike(int idusuario, int idincidencia, Instant fecha);
    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "UPDATE incidencia SET estado = 'resuelto' WHERE (idincidencia = ?1)")
    void resolverIncidencia(int idincidencia);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
    value = "UPDATE incidencia SET estado = 'en proceso' WHERE (idincidencia = ?1)")
    void atenderIncidencia(int idincidencia);
    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "UPDATE incidencia SET reabierto=reabierto+1 WHERE (idincidencia = ?1)")
    void sumarReabierto(int idincidencia);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "UPDATE incidencia SET estado = 'en proceso' WHERE (idincidencia = ?1)")
    void reabrirIncidencia(int idincidencia);
    //agregar comentario
    @Modifying
    @Transactional
    @Query(value="INSERT INTO comentario (idincidencia, nombre, usuario_codigo, fecha) \n" +
            "VALUES ( ?1,?2, ?3, now());",
            nativeQuery = true)
    void agregarComentario(int idincidencia, String nombre, int idCodigo);

    //filtro
    /*
    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion as descripcion , fecha as fecha , \n" +
            "estado as estado , urgencia as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud, \n" +
            "longitud as longitud ,  z.nombre as zonapucp \n" +
            "from incidencia i \n" +
            "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "WHERE (i.fecha >= concat(?1, ' ', '0:0:0') AND i.fecha <= concat(?2, ' ', '23:59:59')) and (i.estado = ?3) and (t.nombre = ?4)",
            nativeQuery = true)
    List<IncidenciaListadto> buscarlistaFiltroIncidencia(String fechainicio, String fechafin,String estado, String nombre);
    */
    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion as descripcion , left(fecha,10) as fecha , \n" +
            "estado as estado , urgencia as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud, \n" +
            "longitud as longitud ,  z.nombre as zonapucp \n" +
            "from incidencia i \n" +
            "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "WHERE (i.fecha >= concat(LEFT(?1, 10), ' ', '0:0:0') AND i.fecha <= concat(right(?1, 10), ' ', '23:59:59')) and (i.estado like( concat('%',?2,'%')))and (t.nombre like( concat('%',?3,'%')))",
            nativeQuery = true)
    List<IncidenciaListadto> buscarlistaFiltro(String datetimes,String estado, String nombre, Pageable pageable);

    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion as descripcion , left(fecha,10) as fecha , \n" +
            "estado as estado , urgencia as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud, \n" +
            "longitud as longitud ,  z.nombre as zonapucp \n" +
            "from incidencia i \n" +
            "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "WHERE (i.fecha >= concat(LEFT(?1, 10), ' ', '0:0:0') AND i.fecha <= concat(right(?1, 10), ' ', '23:59:59')) and (i.estado like( concat('%',?2,'%')))and (t.nombre like( concat('%',?3,'%')))",
            nativeQuery = true)
    List<IncidenciaListadto> buscarlistaFiltroSinPaginado(String datetimes,String estado, String nombre);

    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion as descripcion , fecha as fecha , \n" +
            "estado as estado , urgencia as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud,\n" +
            "longitud as longitud ,  z.nombre as zonapucp \n" +
            "from incidencia i \n" +
            "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "WHERE titulo like( concat('%',?1,'%'))",
            nativeQuery = true)
    List<IncidenciaListadto> buscarlistaPorTitulo(String titulo, Pageable pageable);

    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion as descripcion , fecha as fecha , \n" +
            "estado as estado , urgencia as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud,\n" +
            "longitud as longitud ,  z.nombre as zonapucp \n" +
            "from incidencia i \n" +
            "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "WHERE titulo like( concat('%',?1,'%'))",
            nativeQuery = true)
    List<IncidenciaListadto> buscarlistaPorTituloSinPaginado(String titulo);



    //dashboard
    @Query(value = "select count(idincidencia) from incidencia WHERE fecha > NOW() - INTERVAL 1 MONTH",
            nativeQuery = true)
    Integer incidenciasPorMes();

    @Query(value = "select idinteraccion from favorito where usuario_codigo=?1 and incidencia_idincidencia=?2 and esfavorito=1;",
            nativeQuery = true)
    Integer obtenerFavorito(int codigo, int id);

    @Query(value = "select count(idincidencia) from incidencia WHERE fecha > NOW() - INTERVAL 12 MONTH",
            nativeQuery = true)
    Integer incidenciasPorAnio();

    @Query(value = "select count(idincidencia) from incidencia WHERE estado = 'atendido'",
            nativeQuery = true)
    Integer incidenciasAtendidas();

    //select zp.nombre from incidencia i
    //inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)
    @Query(value = "select ANY_VALUE(zp.nombre) as'nombre', count(ANY_VALUE(i.zonapucp)) as 'zona' from incidencia i\n" +
            "inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)\n" +
            "group by zp.nombre\n" +
            "order by count(ANY_VALUE(i.zonapucp)) desc LIMIT 5",
            nativeQuery = true)
    List<IncidenciaPorZona> ubicacionesPUCP();

    @Query(value = "select ANY_VALUE(zp.nombre) as'nombre' from incidencia i\n" +
            "inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)\n" +
            "group by zp.nombre\n" +
            "order by count(ANY_VALUE(i.zonapucp)) desc LIMIT 5",
            nativeQuery = true)
    List<IncidenciaPorZona> ubicacionesNombrePUCP();

    @Query(value = "select count(ANY_VALUE(i.zonapucp)) as 'zona' from incidencia i\n" +
            "inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)\n" +
            "group by zp.nombre\n" +
            "order by count(ANY_VALUE(i.zonapucp)) desc LIMIT 5",
            nativeQuery = true)
    List<ZonaPUCP> ubicacionesZonaPUCP();


    //select count(codigo) from usuario WHERE suspendido = '1';

    @Query(value = "SELECT  concat(round((count(ANY_VALUE(u.suspendido))*100)/ANY_VALUE(t.total), 2), '%') as 'porcentaje' from usuario u\n" +
            "CROSS JOIN (SELECT count(ANY_VALUE(codigo))  as total FROM usuario u) t\n" +
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
            "group by estado",
    nativeQuery = true)
    List<Usuario> estadoUsuarios();

    @Query(value = "SELECT  concat(ANY_VALUE(u.nombre),' ' , ANY_VALUE(u.apellido)) as 'nombre', ANY_VALUE(u.codigo) as 'codigo', count(ANY_VALUE(i.idincidencia)) as 'cantidad' from usuario u\n" +
            "left join favorito f on (f.usuario_codigo= u.codigo)\n" +
            "left join incidencia i on (i.idincidencia= f.incidencia_idincidencia)\n" +
            "group by concat(u.nombre,' ' , u.apellido) \n" +
            "order by count(i.idincidencia) desc LIMIT 10",
            nativeQuery = true)
    List<IncidenciaDashboardDto> UsariosconMasIncidencias();

    @Query(value = "SELECT  concat(ANY_VALUE(u.nombre),' ' , ANY_VALUE(u.apellido)) as 'nombre' from usuario u\n" +
            "left join favorito f on (f.usuario_codigo= u.codigo)\n" +
            "left join incidencia i on (i.idincidencia= f.incidencia_idincidencia)\n" +
            "group by concat(u.nombre,' ' , u.apellido) \n" +
            "order by count(i.idincidencia) desc LIMIT 10",
            nativeQuery = true)
    List<UsarioMasIncidencia> NombreUsariosconMasIncidencias();


    @Query(value = "SELECT  count(ANY_VALUE(i.idincidencia)) as 'cantidad' from usuario u\n" +
            "left join favorito f on (f.usuario_codigo= u.codigo)\n" +
            "left join incidencia i on (i.idincidencia= f.incidencia_idincidencia)\n" +
            "group by concat(u.nombre,' ' , u.apellido) \n" +
            "order by count(i.idincidencia) desc LIMIT 10",
            nativeQuery = true)
    List<UsuarioCantidadIncidencia> CantidadUsariosconMasIncidencias();


    //SELECT  concat(u.nombre,' ' , u.apellido) as 'nombre', u.codigo as 'codigo', count(i.idincidencia) as 'cantidad' from usuario u
    //left join favorito f on (f.usuario_codigo= u.codigo)
    //left join incidencia i on (i.idincidencia= f.incidencia_idincidencia)
    //where (u.categoria=3 or u.categoria=4 or u.categoria=5 or u.categoria=6) and
    //f.esfavorito=0
    //group by concat(u.nombre,' ' , u.apellido)
    //order by count(i.idincidencia) desc LIMIT 10;


    //@Modifying
   // @Transactional
    //@Query(value="update incidencia set estado= ?1 where idincidencia=?2",nativeQuery = true)
    //void atenderIncidencia(String estado, int id);
    //select round((count(f.esfavorito)*100)/( count(i.idincidencia)), 2) as 'porcentajedeFavPor',  count(i.idincidencia)  from favorito f
    //left join incidencia i on (f.incidencia_idincidencia = i.idincidencia);




    @Query(nativeQuery = true,
            value = "select count(*) \n" +
                    "from incidencia i \n" +
                    "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
                    "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
                    "WHERE titulo like( concat('%',?1,'%'))")
    public long countIncidenciasFiltro2(String titulo);

    @Query(nativeQuery = true,
            value = "select count(*) \n" +
                    "from incidencia i \n" +
                    "inner join zonapucp z on (z.idzonapucp=i.zonapucp) \n" +
                    "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
                    "WHERE (i.fecha >= concat(LEFT(?1, 10), ' ', '0:0:0') AND i.fecha <= concat(right(?1, 10), ' ', '23:59:59')) and (i.estado like( concat('%',?2,'%')))and (t.nombre like( concat('%',?3,'%')))")
    public long countIncidenciasFiltro(String datetimes,String estado, String nombre);

}