package com.example.adviertepucp.controller;

import com.example.adviertepucp.repository.IncidenciaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/seguridad"})
public class SeguridadController {
    IncidenciaRepository  incidenciaRepository;
    @GetMapping("")
    String listarSeguridad(){
        return "seguridad/listaMapa";
    }

    @GetMapping("/info")
    String masInformacion(){
        return "seguridad/MasInfoSeguridad";
    }

    @GetMapping("/mapa")
    String mapa(){
        return "seguridad/mapa";
    }


     @GetMapping("/estadisticas")
    String dashboard(){
       return "seguridad/dashboard";
    }


    /*
    @GetMapping("/estadisticas")
    public String dashboard(Model model) {

        model.addAttribute("num_incidenciasPorMes", incidenciaRepository.incidenciasPorMes());
        model.addAttribute("num_incidenciasPorAnio", incidenciaRepository.incidenciasPorAnio());
        model.addAttribute("num_incidenciasAtendidas", incidenciaRepository.incidenciasAtendidas());
        model.addAttribute("num_ubicacionesPUCP", incidenciaRepository.ubicacionesPUCP());
        model.addAttribute("num_usuariosReportados", incidenciaRepository.usuariosReportados());


        //
        model.addAttribute("num_totalUsuario", incidenciaRepository.totalUsuarios());
        model.addAttribute("lista_estadousuario", incidenciaRepository.estadoUsuarios());
        return "seguridad/dashboard";
    }

*/

}
