//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.adviertepucp.repository;

import com.example.adviertepucp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {


    @Query(value = "select * from usuario where codigo=?1 and correo=?2 ",nativeQuery = true)
    List<Usuario> validarUsuario(String codigo, String correo);

    @Modifying
    @Transactional
    @Query(value="update usuario set codigoverificacion=?1 where codigo=?2",nativeQuery = true)
    void enviarcodigo(String codigoverificacion, String codigo);

    /*Verifica token para el registro del usuario==Establecer contraseña*/

    @Query(value = "select * from usuario where codigoverificacion=?1",nativeQuery = true)
    Usuario validarToken(String token);

    @Modifying
    @Transactional
    @Query(value="update usuario set codigoverificacion=null ",nativeQuery = true)
    void deleteToken();


    @Modifying
    @Transactional
    @Query(value="update usuario set codigoverificacion=null where codigo=?1 ",nativeQuery = true)
    void deleteTokenbyId(String id);

    @Modifying
    @Transactional
    @Query(value="update usuario set pwd=SHA2(?1,256), suspendido=0 where codigo=?2",nativeQuery = true)
    void establecerContrasena(String pwd, String id);

    @Modifying
    @Transactional
    @Query(value="update usuario set pwd=SHA2(?1,256) where codigo=?2",nativeQuery = true)
    void reestablecerContrasena(String pwd, String id);


    @Query(value = "select * from usuario where codigo=?1",nativeQuery = true)
    Usuario usuarioExiste(String id);

    @Query(value = "select * from usuario where pwd=SHA2(?1,256)",nativeQuery = true)
    Usuario contrasenaescorrecta(String id);

}
