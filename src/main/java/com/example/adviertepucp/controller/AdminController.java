package com.example.adviertepucp.controller;

import com.example.adviertepucp.entity.Fotoalmacenada;
import com.example.adviertepucp.entity.Tipoincidencia;
import com.example.adviertepucp.entity.Usuario;
import com.example.adviertepucp.repository.AdmiRepository;
import com.example.adviertepucp.repository.FotoalmacenadaRepository;
import com.example.adviertepucp.repository.IncidenciaRepository;
import com.example.adviertepucp.repository.TipoincidenciaRepository;
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

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    FotoalmacenadaRepository fotoalmacenadaRepository;

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
  @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id) {
        Optional<Tipoincidencia> opt = tipoincidenciaRepository.findById(id);
        if (opt.isPresent()) {
            Tipoincidencia p = opt.get();

            byte[] imagenComoBytes = p.getLogo().getFotoalmacenada();

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
    }

    @PostMapping("/crearUsuario")
    public String crearUsuario(Usuario usuario,
                               RedirectAttributes attr){
        admiRepository.save(usuario);
        return "redirect:/admin/listaUsuarios";

    }

    @PostMapping("/guardaCrear")
    public String Crear(@RequestParam("nombre") String nombre ,
                           @RequestParam("archivo") MultipartFile logo ,
                           @RequestParam("color") String color ,RedirectAttributes attr) {
        if (logo.isEmpty()) {
            attr.addAttribute("msg", "Debe subir un archivo");
            return "redirect:/administrador/incidencias";
        }
        String nombrelogo=logo.getOriginalFilename();
        if (nombrelogo.contains("..")) {
            attr.addAttribute("msg", "No se permiten '..' en el archivo");
            return "redirect:/administrador/incidencias";
        }
        Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
        try {
            fotoalmacenada.setFotoalmacenada(logo.getBytes());
            fotoalmacenada.setTipofoto(logo.getContentType());
            fotoalmacenadaRepository.save(fotoalmacenada);

        } catch (IOException e) {
            e.printStackTrace();
            attr.addAttribute("msg", "ocurrió un error al subir el archivo");
            return "redirect:/administrador/incidencias";
        }

        Tipoincidencia tipoincidencia = new Tipoincidencia();

        tipoincidencia.setNombre(nombre);
        tipoincidencia.setColor(color);
        tipoincidencia.setLogo(fotoalmacenada);
        tipoincidenciaRepository.save(tipoincidencia);
        return "redirect:/administrador/incidencias";


    }


    @PostMapping("/guardarEditar")
    public String editar(  @RequestParam("id") int id,
                                    @RequestParam("nombre") String nombre ,
                                  @RequestParam("archivo") MultipartFile logo ,
                                  @RequestParam("color") String color , Model model ) {
        if (logo!=null) {
            System.out.println("No recibi la imagen");
            model.addAttribute("msg", "Debe subir un archivo");

            return "redirect:/administrador/incidencias";
        }
        String nombrelogo=logo.getOriginalFilename();
        if (nombrelogo.contains("..")) {
            model.addAttribute("msg", "No se permiten '..' en el archivo");
            return "redirect:/administrador/incidencias";
        }
        Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
        try {
            fotoalmacenada.setFotoalmacenada(logo.getBytes());
            fotoalmacenada.setTipofoto(logo.getContentType());
            fotoalmacenadaRepository.save(fotoalmacenada);
            System.out.println("HE GUARDADO UNA IMAGEN");

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("msg", "ocurrió un error al subir el archivo");
            return "redirect:/administrador/incidencias";
        }
        Tipoincidencia tipoincidencia;
        if(tipoincidenciaRepository.obtenerTipo(id)!=null){
            tipoincidencia = tipoincidenciaRepository.obtenerTipo(id);
        }else{
            tipoincidencia = new Tipoincidencia();
        }
        tipoincidencia.setNombre(nombre);
        tipoincidencia.setColor(color);
        tipoincidencia.setLogo(fotoalmacenada);
        tipoincidenciaRepository.save(tipoincidencia);
        return "redirect:/administrador/incidencias";


        }










}
