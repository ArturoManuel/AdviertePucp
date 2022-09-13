

package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
