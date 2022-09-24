

package com.example.adviertepucp.controller;

import com.example.adviertepucp.entity.Fotoalmacenada;
import com.example.adviertepucp.entity.Incidencia;
import com.example.adviertepucp.entity.Incidenciatienefoto;
import com.example.adviertepucp.entity.Tipoincidencia;
import com.example.adviertepucp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Controller
@RequestMapping({"/usuario"})
public class UsuarioController {
    @Autowired
    IncidenciaRepository incidenciaRepository;

    @Autowired
    FotoalmacenadaRepository fotoalmacenadaRepository;

    @Autowired
    ZonapucpRepository zonapucpRepository;

    @Autowired
    TipoincidenciaRepository tipoincidenciaRepository;

    @Autowired
    IncidenciatienefotoRepository incidenciatienefotoRepository;

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
    @GetMapping({"/lista"})
    public String listaIncidencias() {
        return "usuario/lista";
    }
    @GetMapping({"/perfil"})
    public String perfil() {
        return "usuario/perfil";
    }
    @GetMapping({"/nuevoIncidente"})
    public String nuevo(Model model) {
        model.addAttribute("listaZonas",zonapucpRepository.findAll());
        model.addAttribute("listaTiposIncidencia",tipoincidenciaRepository.findAll());
        return "usuario/nuevoIncidente";
    }
    @PostMapping("/guardarincidente")
    public String guardarIncidente(@RequestParam("archivos") MultipartFile[] files,
            Incidencia incidencia,
                                   Model model){

        ArrayList<Fotoalmacenada> listaFotoAlmacenada = new ArrayList<>();

        for (MultipartFile foto : files ) {
        if (foto.isEmpty()){
            model.addAttribute("msg", "Debe subir un archivo");
            return "usuario/nuevoIncidente";
        }
        String fileName = foto.getOriginalFilename();

        if (fileName.contains("..")){
            model.addAttribute("msg", "No se permiten '..' en el archivo");
            return "usuario/nuevoIncidente";
        }
        try{
            Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
            fotoalmacenada.setFotoalmacenada(foto.getBytes());
            fotoalmacenada.setTipofoto(foto.getContentType());
            fotoalmacenadaRepository.save(fotoalmacenada);
            listaFotoAlmacenada.add(fotoalmacenada);



        }catch (IOException e){
            e.printStackTrace();
            model.addAttribute("msg", "Ocurrió un error al subir los archivo");
            return "usuario/nuevoIncidente";
        }
        }

        try{
            Double latitud = 1.5;
            Double longitud = 2.6;
            Instant datetime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
            incidencia.setLatitud(latitud);
            incidencia.setLongitud(longitud);
            incidencia.setFecha(datetime);
            incidenciaRepository.save(incidencia);

            for (Fotoalmacenada fotoDB: listaFotoAlmacenada) {
                Incidenciatienefoto incidenciatienefoto = new Incidenciatienefoto();
                incidenciatienefoto.setIdfotoalmacenada(fotoDB);
                incidenciatienefoto.setIdincidencia(incidencia);
                incidenciatienefotoRepository.save(incidenciatienefoto);
            }

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg", "Ocurrió un error al crear la incidencia");
            return "usuario/nuevoIncidente";
        }


        return "usuario/lista";
    }
}
