package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/administrador2")

public class AdminController2 {

    @GetMapping("lista")
    public String lista(){
        return "admin/listaIncidentes";
    }
}
