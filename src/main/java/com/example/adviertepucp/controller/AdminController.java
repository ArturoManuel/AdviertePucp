package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.UsuariosDBDto;
import com.example.adviertepucp.entity.Fotoalmacenada;
import com.example.adviertepucp.entity.Tipoincidencia;
import com.example.adviertepucp.entity.Usuario;
import com.example.adviertepucp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @GetMapping("")
    public String listaUsuarios(Model model){
        model.addAttribute("listaUsuarios", admiRepository.listaUsuariosAdmin());
        model.addAttribute("listaUsuarios1", admiRepository.findAll());
        model.addAttribute("usuariosDB", admiRepository.UsuariosDB());
        model.addAttribute("listacategorias", admiRepository.CategoriaList());
        return "admin/listaUsuarios";
    }



    @PostMapping("/guardarUser")
    public String editarUsuario(Model model,
                                @ModelAttribute("usuario") @Valid Usuario usuario,
                                BindingResult bindingResult,
                                RedirectAttributes attr) {
        System.out.println("ERROREEEEES" + " " + bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaUsers", usuarioRepository.findAll());
            model.addAttribute("listacategorias", categoriaRepository.findAll());
            System.out.println("LLEGOOOOOO JEJEJJEEJE");
            return "admin/editar_User";
        } else {
            if (usuario.getId().isEmpty()) {
                attr.addFlashAttribute("msg", "Usuario registrado exitosamente");
            } else {
                attr.addFlashAttribute("msg", "Usuario actualizado exitosamente");
            }
            usuarioRepository.save(usuario);
            return "redirect:/administrador/";
        }
    }



    @GetMapping("/editUser")
    public String listaUserBD(@ModelAttribute("usuario") Usuario usuario,
                              Model model, @RequestParam("id") int id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(String.valueOf(id));
        System.out.println("LLEGOOOO" + " " +optUsuario);
        if (optUsuario.isPresent()){
            usuario =optUsuario.get();
            model.addAttribute("usuario", usuario);
            model.addAttribute("listaUsers", usuarioRepository.findAll());
            model.addAttribute("listacategorias", categoriaRepository.findAll());
            return "admin/editar_User";
        }else {
            return "redirect:/administrador/";
        }
    }




    @GetMapping("/suspenderUser")
    public String suspenderUser(@RequestParam("id") int id) {
        System.out.println("ESTO ES ID: " + id);


            admiRepository.suspenderUsuario(id);
            System.out.println("Se ha suspendido correctamente");

            return "redirect:/administrador/";

    }
    @GetMapping("/activarUser")
    public String activarUser(@RequestParam("id") int id) {
        System.out.println("ESTO ES ID: " + id);

            admiRepository.activarUsuario(id);
            System.out.println("Se ha reactivado correctamente");

            return "redirect:/administrador/";

    }


    @GetMapping("fotoUser/{id2}")
    public ResponseEntity<byte[]>mostrarImagenUser(@PathVariable("id2") int id2){
        Optional<Fotoalmacenada> opt = fotoalmacenadaRepository.findById(id2);
        if (opt.isPresent()){
            Fotoalmacenada f = opt.get();
            byte[] fotoComoByte = f.getFotoalmacenada();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType("image/png"));
            return new ResponseEntity<>(
                    fotoComoByte,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }




    @GetMapping("/incidencias")
    String listaIncidencias( Model model){

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



    @PostMapping("/guardaCrear")
    public String Crear(@RequestParam("nombre") String nombre ,
                           @RequestParam("archivo") MultipartFile logo ,
                           @RequestParam("color") String color ,RedirectAttributes attr, Model model) {
        if (logo.isEmpty()) {
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }
        String nombrelogo=logo.getContentType();
        System.out.println(nombrelogo);
        if (nombrelogo.contains("..")) {
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }

        if(nombre.isEmpty()){
            model.addAttribute("msg", "El nombre no debe ser vacio");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }

        Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
        try {
            fotoalmacenada.setFotoalmacenada(logo.getBytes());
            fotoalmacenada.setTipofoto(logo.getContentType());
            fotoalmacenadaRepository.save(fotoalmacenada);
            System.out.println("he guardado mi imagen");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }

        Tipoincidencia tipoincidencia = new Tipoincidencia();
        try {
            tipoincidencia.setNombre(nombre);
            tipoincidencia.setColor(color);
            tipoincidencia.setLogo(fotoalmacenada);
            tipoincidenciaRepository.save(tipoincidencia);
            return "redirect:/administrador/incidencias";
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("No se puede crear ");
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }



    }


    @PostMapping("/guardarEditar")
    public String editar(  @RequestParam("id") int id,
                                    @RequestParam("nombre") String nombre ,
                                  @RequestParam("archivo") MultipartFile logo ,
                                  @RequestParam("color") String color , RedirectAttributes redirectAttributes, Model model) {
        if (logo.isEmpty()) {
            System.out.println("No recibi la imagen");
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }
        String nombrelogo=logo.getOriginalFilename();
        if (nombrelogo.contains("..")) {
            model.addAttribute("msg", "El tipo de dato no es el correcto");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }
        if(nombre.isEmpty()){
            model.addAttribute("msg", "El nombre no debe ser vacio");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }
        Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
        try {
            fotoalmacenada.setFotoalmacenada(logo.getBytes());
            fotoalmacenada.setTipofoto(logo.getContentType());
            fotoalmacenadaRepository.save(fotoalmacenada);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }
        Tipoincidencia tipoincidencia;
        if(tipoincidenciaRepository.obtenerTipo(id)!=null){
            tipoincidencia = tipoincidenciaRepository.obtenerTipo(id);
        }else{
            tipoincidencia = new Tipoincidencia();
        }
        try {
            tipoincidencia.setNombre(nombre);
            tipoincidencia.setColor(color);
            tipoincidencia.setLogo(fotoalmacenada);
            tipoincidenciaRepository.save(tipoincidencia);
            return "redirect:/administrador/incidencias";

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("msg", "Algun campo esta vacio para editar");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/listaIncidentes";
        }

        }


    @GetMapping("/delete")
    public String borrarTransportista(@RequestParam("id") int id,
                                      RedirectAttributes attr) {

        int cantidadTipo = tipoincidenciaRepository.incidenciaTipo(id);

        if (cantidadTipo==0) {
            tipoincidenciaRepository.deleteById(id);
            attr.addFlashAttribute("msg", "Tipo de incidencia Borrado");
        }else {
            attr.addFlashAttribute("msg","Existe una incidencia con dicho tipo");
        }
        return "redirect:/administrador/incidencias";

    }












}
