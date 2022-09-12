

package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/usuarios"})
public class UsuarioController {
    public UsuarioController() {
    }

    @GetMapping({"/suspendido"})
    public String suspendido() {
        return "suspendido";
    }
}
