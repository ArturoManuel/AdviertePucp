

package com.example.adviertepucp.controller;

import com.example.adviertepucp.entity.Incidencia;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping({"/usuario"})
public class UsuarioController {
    @GetMapping("")
    String listaUsuario(){
        return "usuario/lista";
    }

    @GetMapping("/info")
    String masInformacion(){
        return "usuario/MasInfoUsuario";
    }

    @GetMapping("/mapa")
    String mapa(){
        return "usuario/mapa";
    }

    @GetMapping({"/suspendido"})
    public String suspendido() {
        return "loguin/suspendido";
    }
    @GetMapping({"/lista"})
    public String listaIncidencias() {
        return "usuario/lista";
    }
    @GetMapping({"/perfil"})
    public String perfil() {
        return "usuario/perfil";
    }
    @GetMapping({"/nuevoIncidente"})
    public String nuevo() {
        return "usuario/nuevoIncidente";
    }
    @PostMapping("/guardarincidente")
    public String guardarIncidente(@RequestParam("archivo") MultipartFile file,
                                   Incidencia incidencia, Model model){

        return "";
    }
}
