package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/seguridad"})
public class SeguridadController {
    @GetMapping("")
    String listarSeguridad(){
        return "seguridad/listaMapa";
    }

    @GetMapping("/info")
    String masInformacion(){
        return "seguridad/MasInfoSeguridad";
    }

}
