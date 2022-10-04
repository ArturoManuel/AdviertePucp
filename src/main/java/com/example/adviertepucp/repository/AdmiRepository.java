package com.example.adviertepucp.repository;

import com.example.adviertepucp.dto.AdminUsuariosDto;
import com.example.adviertepucp.dto.CategoriaDto;
import com.example.adviertepucp.dto.UsuariosDBDto;
import com.example.adviertepucp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AdmiRepository extends JpaRepository<Usuario, Integer> {
    @Query(value ="SELECT c.idcategoria as id_ca, u.codigo as codigo, concat(u.apellido,' ', u.nombre) as nombres, u.correo as correo,\n" +
            "            u.suspendido as estado,\n" +
            "            c.nombre as rol, f.fotoalmacenada as foto  FROM usuario u \n" +
            "            inner join categoria c on (u.categoria = c.idcategoria)\n" +
            "            inner join fotoalmacenada f on (u.foto=f.idfotoalmacenada)\n" +
            "            order by case when c.nombre = 'administrativo' then 1 else 2 end",
            nativeQuery = true)
    List<AdminUsuariosDto> listaUsuariosAdmin();


    @Query(value ="SELECT codigo, nombre, apellido, dni, correo FROM usuariobd;",
            nativeQuery = true)
    List<UsuariosDBDto> UsuariosDB();

    @Query(value = "SELECT c.nombre as categoria, u.categoria as idCategoria FROM usuario u\n" +
            "inner join categoria c on (c.idcategoria=u.categoria);",
            nativeQuery = true)
    List<CategoriaDto> CategoriaList();
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE `adviertedb`.`usuario` SET `suspendido` = 3 WHERE (`codigo` = ?1);")
    void suspenderUsuario(Integer id_codigo);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE `adviertedb`.`usuario` SET `suspendido` = 0 WHERE (`codigo` = ?1);")
    void activarUsuario(Integer id_codigo);


}
