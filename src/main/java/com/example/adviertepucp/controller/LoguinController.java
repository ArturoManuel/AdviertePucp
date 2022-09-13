
package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoguinController {
    @GetMapping({"/autenticacion"})
    public String autenticacion()
    {
        return "/loguin/autenticacion";
    }

    @GetMapping({"/registro"})
    public String registro()
    {
        return "/loguin/registro";
    }

    @GetMapping({"/nuevacontrasena"})
    public String nuevacontrasena()
    {
        return "/loguin/nuevacontrasena";
    }

    @GetMapping({"/restablecercontrasena"})
    public String restablececontrasena()
    {
        return "/loguin/restablececontrasena";
    }






    @GetMapping({""})
    public String index() {
        return "/loguin/loguin";
    }



}
