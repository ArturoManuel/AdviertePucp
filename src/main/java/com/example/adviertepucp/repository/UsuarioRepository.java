//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.adviertepucp.repository;

import com.example.adviertepucp.dto.*;
import com.example.adviertepucp.entity.Favorito;
import com.example.adviertepucp.entity.Fotoalmacenada;
import com.example.adviertepucp.entity.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {


    @Query(value = "select * from usuario where codigo=?1 and correo=?2 ",nativeQuery = true)
    List<Usuario> validarUsuario(String codigo, String correo);

    public Usuario findByEmail(String correo);

    @Modifying
    @Transactional
    @Query(value = "update usuario set secret=?1,habilitado=1 where codigo=?2",nativeQuery = true)
    void asignarSecret(String secret,String codigo);


    @Modifying
    @Transactional
    @Query(value = "update usuario set celular=?1 where codigo=?2",nativeQuery = true)
    void actualizarcelular(String celular, String codigo);



    @Modifying
    @Transactional
    @Query(value="update usuario set codigoverificacion=?1,contadortoken=?2 where codigo=?3",nativeQuery = true)
    void enviarcodigo(String codigoverificacion,int contadortoken ,String codigo);

    @Modifying
    @Transactional
    @Query(value="update usuario set otp=NULL,pwd=?1 where codigo=?2",nativeQuery = true)
    void restablecerotp(String pwd,String codigo);


    /*Verifica token para el registro del usuario==Establecer contraseña*/

    @Query(value = "select * from usuario where codigoverificacion=?1",nativeQuery = true)
    Usuario validarToken(String token);

    @Modifying
    @Transactional
    @Query(value="update usuario set codigoverificacion=null,contadortoken=0 ",nativeQuery = true)
    void deleteToken();


    @Modifying
    @Transactional
    @Query(value="update usuario set codigoverificacion=null where codigo=?1 ",nativeQuery = true)
    void deleteTokenbyId(String id);

    @Modifying
    @Transactional
    @Query(value="update usuario set contadortoken=0 where codigo=?1 ",nativeQuery = true)
    void registroResetearContador(String id);


    @Modifying
    @Transactional
    @Query(value="update usuario set pwd=?1, habilitado=1 where codigo=?2",nativeQuery = true)
    void establecerContrasena(String pwd, String id);

    @Modifying
    @Transactional
    @Query(value="update usuario set pwd=?1 where codigo=?2",nativeQuery = true)
    void reestablecerContrasena(String pwd, String id);


    @Query(value = "select * from usuario where codigo=?1",nativeQuery = true)
    Usuario usuarioExiste(String id);

    @Query(value = "select * from usuario where pwd=?1",nativeQuery = true)
    Usuario contrasenaescorrecta(String id);

    @Query(value = "select * from usuario where correo=?1",nativeQuery = true)
    Usuario oauth2User(String correo);


    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion\n" +
            "            as descripcion , concat(substring(i.fecha,1,10),'  (' ,substring(i.fecha,12,5),')') as fecha , estado as estado , urgencia\n" +
            "            as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud,\n" +
            "            longitud as longitud ,  z.nombre as zonapucp, subq1.creador\n" +
            "            from incidencia i inner join\n" +
            "            zonapucp z on (z.idzonapucp=i.zonapucp)\n" +
            "            inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "            left join (select incidencia_idincidencia,usuario_codigo as 'creador' from favorito where esfavorito=0) subq1\n" +
            "            on (subq1.incidencia_idincidencia=i.idincidencia)\n" +
            "            where publicado=1 and estado!=\"resuelto\" order by i.fecha desc"
            , nativeQuery = true)
    List<IncidenciaListadto> listaIncidencia();


    @Query (value = "select idincidencia as idI , titulo as titulo , descripcion\n" +
            "                        as descripcion , concat(substring(i.fecha,1,10),'  (' ,substring(i.fecha,12,5),')') as fecha , estado as estado , urgencia\n" +
            "                        as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud,\n" +
            "                       longitud as longitud ,  z.nombre as zonapucp, subq1.creador, f.fotoalmacenada as icono\n" +
            "                       from incidencia i inner join\n" +
            "                        zonapucp z on (z.idzonapucp=i.zonapucp)\n" +
            "                        inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "                        left join (select incidencia_idincidencia,usuario_codigo as 'creador' from favorito where esfavorito=0) subq1\n" +
            "                        on (subq1.incidencia_idincidencia=i.idincidencia)\n" +
            "                        inner join fotoalmacenada f on (f.idfotoalmacenada=t.logo)\n" +
            "                        where publicado=1 and estado!=\"resuelto\" order by i.fecha desc;"
            , nativeQuery = true)
    List<IncidenciaMapaDto> incidenciaMapa();








    @Query (value = "select i.idincidencia as idI , titulo as titulo , descripcion\n" +
            "as descripcion , concat(substring(i.fecha,1,10),'  (' ,substring(i.fecha,12,5),')') as fecha , estado as estado , urgencia\n" +
            "as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud,\n" +
            "longitud as longitud ,  z.nombre as zonapucp, subq1.creador, subq2.usuario_codigo,\n" +
            "subq2.esfavorito\n" +
            "from incidencia i inner join\n" +
            "zonapucp z on (z.idzonapucp=i.zonapucp)\n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "left join (select incidencia_idincidencia,usuario_codigo as 'creador'\n" +
            "from favorito where esfavorito=0) subq1\n" +
            "on (subq1.incidencia_idincidencia=i.idincidencia)\n" +
            "left join (select i.idincidencia, f.usuario_codigo, f.esfavorito\n" +
            "from incidencia i left join favorito f on (i.idincidencia=f.incidencia_idincidencia)\n" +
            "where f.usuario_codigo=?) subq2 on (i.idincidencia=subq2.idincidencia)\n" +
            "where publicado=1 and estado!=\"resuelto\" order by i.fecha desc"
    , nativeQuery = true)

    List<IncidenciaListadto> listaIncidenciaUsuarios(Integer codigo,  Pageable pageable);

    @Query (value = "select i.idincidencia as idI , titulo as titulo , descripcion\n" +
            "as descripcion , concat(substring(i.fecha,1,10),'  (' ,substring(i.fecha,12,5),')') as fecha , estado as estado , urgencia\n" +
            "as urgencia, t.nombre as tincidencia ,t.color as color,latitud as latitud,\n" +
            "longitud as longitud ,  z.nombre as zonapucp, subq1.creador, subq2.usuario_codigo,\n" +
            "subq2.esfavorito\n" +
            "from incidencia i inner join\n" +
            "zonapucp z on (z.idzonapucp=i.zonapucp)\n" +
            "inner join tipoincidencia t on (t.idtipoincidencia=i.tipoincidencia)\n" +
            "left join (select incidencia_idincidencia,usuario_codigo as 'creador'\n" +
            "from favorito where esfavorito=0) subq1\n" +
            "on (subq1.incidencia_idincidencia=i.idincidencia)\n" +
            "left join (select i.idincidencia, f.usuario_codigo, f.esfavorito\n" +
            "from incidencia i left join favorito f on (i.idincidencia=f.incidencia_idincidencia)\n" +
            "where f.usuario_codigo=?) subq2 on (i.idincidencia=subq2.idincidencia)\n" +
            "where publicado=1 and estado!=\"resuelto\" order by i.fecha desc"
            , nativeQuery = true)

    List<IncidenciaListadto> listaIncidenciaUsuariosSinPaginado(Integer codigo);




    //Para extraer las fotos de cada incidencia
    @Query(value="select f.fotoalmacenada from  \n" +
            "incidencia i inner join incidenciatienefoto it on (i.idincidencia=it.idincidencia) \n" +
            "       inner join fotoalmacenada f on (it.idfotoalmacenada=f.idfotoalmacenada) where i.idincidencia=?;",nativeQuery = true)
    List<String> listaFotoIncidencia(Integer id);
    @Query(value = "select idfotoalmacenada from incidenciatienefoto where idincidencia=?;",nativeQuery = true)
    List<Integer> listaDeFotosId(Integer id);

    @Query(value = "select f.fotoalmacenada from  \n" +
            "            usuario u inner join fotoalmacenada f on (u.foto=f.idfotoalmacenada) where u.codigo=?;",nativeQuery = true)
    String fotoAlmacenadaUser(String codigo);
    @Query(nativeQuery = true,
    value = "select f.idinteraccion,f.usuario_codigo,i.idincidencia\n" +
            "from favorito f left join incidencia i on (f.incidencia_idincidencia=i.idincidencia)\n" +
            "where f.usuario_codigo=?1 and f.esfavorito=0 and i.publicado=0;")
    UsuarioEstaCreandoDto obtenerCreandoUsuario(String codigo);

    @Query(nativeQuery = true,
            value = "select idinteraccion from favorito where usuario_codigo=?1\n" +
                    "    and incidencia_idincidencia=?2 and esfavorito=0;")
    Integer obtenerInteraccionId(Integer codigo, Integer idIncidencia);


    @Query (value = "SELECT u.codigo as 'codigo', i.idincidencia as 'inci', i.titulo as 'nombre', i.urgencia as 'urgencia', \n" +
            "z.nombre as 'zona', substring(i.fecha,1,10) as 'fecha', substring(i.fecha,12,5) as 'hora', i.estado as 'estado'\n" +
            "FROM usuario u\n" +
            "INNER JOIN favorito f\n" +
            "ON u.codigo = f.usuario_codigo\n" +
            "INNER JOIN incidencia i\n" +
            "on f.incidencia_idincidencia=i.idincidencia\n" +
            "INNER JOIN zonapucp z\n" +
            "on i.zonapucp = z.idzonapucp\n" +
            "WHERE u.codigo=?1 and f.esfavorito=0\n" +
            "order by i.fecha desc", nativeQuery = true)
    List<MisIncidenciasDto> misIncidencias(String codigo);

    @Query (value = "SELECT u.codigo as 'codigo', i.idincidencia as 'inci', i.titulo as 'nombre', i.urgencia as 'urgencia', \n" +
            "z.nombre as 'zona', substring(i.fecha,1,10) as 'fecha', substring(i.fecha,12,5) as 'hora', i.estado as 'estado'\n" +
            "FROM usuario u\n" +
            "INNER JOIN favorito f\n" +
            "ON u.codigo = f.usuario_codigo\n" +
            "INNER JOIN incidencia i\n" +
            "on f.incidencia_idincidencia=i.idincidencia\n" +
            "INNER JOIN zonapucp z\n" +
            "on i.zonapucp = z.idzonapucp\n" +
            "WHERE u.codigo=?1 and f.esfavorito=1\n" +
            "order by i.fecha desc", nativeQuery = true)
    List<MisFavoritosDto> misFavoritos(String codigo);

    @Query(nativeQuery = true,
            value = "SELECT count(*) FROM incidencia where zonapucp is not null;")
    public long countIncidencias();

}
