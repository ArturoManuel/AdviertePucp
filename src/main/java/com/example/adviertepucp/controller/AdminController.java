package com.example.adviertepucp.controller;

import com.example.adviertepucp.entity.Tipoincidencia;
import com.example.adviertepucp.repository.AdmiRepository;
import com.example.adviertepucp.repository.IncidenciaRepository;
import com.example.adviertepucp.repository.TipoincidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/administrador")
public class AdminController {
    @Autowired
    AdmiRepository admiRepository;
    @Autowired
    IncidenciaRepository incidenciaRepository;

    @Autowired
    TipoincidenciaRepository tipoincidenciaRepository;
    @GetMapping("")
    public String listaUsuarios(Model model){
        model.addAttribute("listaUsuarios", admiRepository.listaUsuariosAdmin());
        return "admin/listaUsuarios";
    }

    @GetMapping("/incidencias")
    String listaIncidencias(Model model){
        model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
        return "admin/listaIncidentes";
    }
    @GetMapping("/prueba")

    String pruebas(){
        return "admin/prueba";
    }

    //Para obtener imagenes de la base de datos
    //Para ver al el llamado vayan al archivo listaIncidentes.html
  /*@GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id) {
        Optional<Tipoincidencia> opt = tipoincidenciaRepository.findById(id);
        if (opt.isPresent()) {
            Tipoincidencia p = opt.get();

            byte[] imagenComoBytes = p.getLogo();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType("image/png"));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    } */



}
