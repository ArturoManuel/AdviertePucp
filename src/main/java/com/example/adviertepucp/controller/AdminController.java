package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdminController {
    public AdminController(){

    }
    @GetMapping("lista")
    public String lista(){
        return "admin/listaUsuarios";
    }
}
