package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdminController {
    @GetMapping("")
    public String listaUsuarios(){
        return "admin/listaUsuarios";
    }

    @GetMapping("/incidencias")
    String listaIncidencias(){
        return "admin/listaIncidentes";
    }
}
