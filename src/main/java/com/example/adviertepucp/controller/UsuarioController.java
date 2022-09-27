

package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.dto.TipoIncidenciadto;
import com.example.adviertepucp.entity.*;
import com.example.adviertepucp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping({"/usuario"})
public class UsuarioController {
    @Autowired
    IncidenciaRepository incidenciaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    FotoalmacenadaRepository fotoalmacenadaRepository;

    @Autowired
    ZonapucpRepository zonapucpRepository;

    @Autowired
    TipoincidenciaRepository tipoincidenciaRepository;

    @Autowired
    IncidenciatienefotoRepository incidenciatienefotoRepository;



    @GetMapping("")
    String listaUsuario(Model model){


        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());
        return "usuario/lista";
    }

    @GetMapping("/info")
    String masInformacion(@RequestParam("id") int id,
                          Model model){
        IncidenciaListadto incidencia = null;
        List<IncidenciaListadto> listaIncidencias = usuarioRepository.listaIncidencia();
       for( IncidenciaListadto lista : listaIncidencias){
           if(id == lista.getIdI()){
               incidencia=lista;
               break;
           }
        }
        model.addAttribute("incidencia",incidencia);

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
    public String listaIncidencias(Model model) {
        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());
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
            String estado = "registrado";
            Instant datetime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
            incidencia.setEstado(estado);
            incidencia.setLatitud(latitud);
            incidencia.setLongitud(longitud);
            incidencia.setFecha(datetime);
            incidenciaRepository.save(incidencia);

            for (Fotoalmacenada fotoDB: listaFotoAlmacenada) {
                incidenciatienefotoRepository.insertarFotoEIncidencia(fotoDB.getId(),incidencia.getId());
            }

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg", "Ocurrió un error al crear la incidencia");
            return "usuario/nuevoIncidente";
        }


        return "redirect:/usuario/lista";
    }



    @GetMapping("/images/{id}/{id2}")
    public ResponseEntity<byte[]> mostrarImagen(@RequestParam("id")IncidenciatienefotoId id, @RequestParam("id2") int id2) {
        System.out.println("Esoy entrando");
        List<Incidenciatienefoto> opt = incidenciatienefotoRepository.findAll();
        for( Incidenciatienefoto incidenciatienefoto : opt){
            if (incidenciatienefoto.getId()== id && incidenciatienefoto.getIdfotoalmacenada().getId()==id2 ) {
                byte[] imagenComoBytes = incidenciatienefoto.getIdfotoalmacenada().getFotoalmacenada();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(
                        MediaType.parseMediaType("image/jpeg"));

                return new ResponseEntity<>(
                        imagenComoBytes,
                        httpHeaders,
                        HttpStatus.OK);
            } else {
                return null;
            }
        }
        return  null;
    }


}
