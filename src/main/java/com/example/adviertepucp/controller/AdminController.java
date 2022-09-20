package com.example.adviertepucp.controller;

import com.example.adviertepucp.repository.AdmiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdminController {
    @Autowired
    AdmiRepository admiRepository;
    @GetMapping("")
    public String listaUsuarios(Model model){
        model.addAttribute("listaUsuarios", admiRepository.listaUsuariosAdmin());
        return "admin/listaUsuarios";
    }

    @GetMapping("/incidencias")
    String listaIncidencias(){
        return "admin/listaIncidentes";
    }
}
