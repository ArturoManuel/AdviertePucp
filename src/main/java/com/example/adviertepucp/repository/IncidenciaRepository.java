package com.example.adviertepucp.repository;

import com.example.adviertepucp.dto.TipoIncidenciadto;
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



    //dashboard
    @Query(value = "select count(idincidencia) from incidencia WHERE fecha > NOW() - INTERVAL 1 MONTH",
            nativeQuery = true)
    List<Incidencia> incidenciasPorMes();

    @Query(value = "select count(idincidencia) from incidencia WHERE fecha > NOW() - INTERVAL 12 MONTH",
            nativeQuery = true)
    List<Incidencia> incidenciasPorAnio();

    @Query(value = "select count(idincidencia) from incidencia WHERE estado = 'atendido'",
            nativeQuery = true)
    List<Incidencia> incidenciasAtendidas();

    //select zp.nombre from incidencia i
    //inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)
    @Query(value = "select zp.nombre, count(i.zonapucp) from incidencia i\n" +
            "inner join zonapucp zp on (zp.idzonapucp = i.zonapucp)\n" +
            "group by zp.nombre;",
            nativeQuery = true)
    List<IncidenciaPorZona> ubicacionesPUCP();

    //select count(codigo) from usuario WHERE suspendido = '1';

    @Query(value = "SELECT  round((count(u.suspendido)*100)/t.total, 2) as 'porcentaje' from usuario u\n" +
            "CROSS JOIN (SELECT count(codigo)  as total FROM usuario u) t\n" +
            "where u.suspendido=1",
            nativeQuery = true)
    List<Usuario> usuariosReportados();

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

    @Modifying
    @Transactional
    @Query(value="update incidencia set estado= ?1 where idincidencia=?2",nativeQuery = true)
    void atenderIncidencia(String estado, int id);

}