package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.UsuariosDBDto;
import com.example.adviertepucp.entity.*;
import com.example.adviertepucp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/administrador")
public class AdminController extends Usuario {


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

    @Autowired
    UsuarioBDRepository usuarioBDRepository;



    @GetMapping("")
    public String listaUsuarios(Model model){
        model.addAttribute("listaUsuarios", admiRepository.listaUsuariosAdmin());
        model.addAttribute("listaUsuarios1", admiRepository.findAll());
        model.addAttribute("usuariosDB", admiRepository.UsuariosDB());
        model.addAttribute("listacategorias", admiRepository.CategoriaList());
        return "admin/listaUsuarios";
    }

    //Perfil
    @GetMapping({"/perfil"})
    public String perfil(HttpSession session)
    {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        return "admin/perfil";
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


    @GetMapping("/crearUser")
    public String crearUser(@ModelAttribute("usuario") Usuario usuario,
                            Model model){
        model.addAttribute("listacategorias", categoriaRepository.findAll());
        return "admin/newUser";
    }

    @PostMapping("/guardarCrearUser")
    public String guardarCrearUsuario(Model model, @RequestParam("id")
                                String codigo,
                                @RequestParam("nombre") @Size(max = 45) @NotNull(message = "Este campo no puede estar nulo")
                                String nombre,
                                @RequestParam("apellido")@Size(max = 45) @NotNull(message = "Este campo no puede estar nulo")
                                String apellido,
                                @RequestParam("dni") @Size(max = 8 , message = "El DNI tiene un máximo de 8 dígitos")
                                String dni,
                                @RequestParam("celular") @Size(max = 9 , message = "El celular tiene un máximo de 9 dígitos")
                                String celular,
                                @RequestParam("correo") @Size(max = 80) @NotNull(message = "Este campo no puede estar nulo")
                                String correo,
                                @RequestParam("categoria") @NotNull(message = "Este campo no puede estar nulo")
                                int categoria,
                                @ModelAttribute("usuario") Usuario usuario,
                                RedirectAttributes attr) {
        System.out.println("LLGOOOOOO A REGISTRAAAR WAA");
        System.out.println("CODIGOOOOO" + "   " + codigo);


        System.out.println("LISTA DE USUARIOS    " + usuarioRepository.findAll());
        Usuario usuario1 = new Usuario();
        System.out.println("CODIGO QUE LLEGA   " + codigo);
        int flag = 0;

        if (codigo.length() != 8) {
            model.addAttribute("msg", "El codigo debe ser de 8 dígitos");
            flag ++;

        }
        if (nombre.isEmpty() || nombre.length() > 45){
            model.addAttribute("msg1", "El nombre no debe ser nulo");
            flag ++;

        }
        if (apellido.isEmpty() || apellido.length() > 45){
            model.addAttribute("msg2", "El apellido no debe ser nulo");
            flag ++;
        }
        if (dni.length() != 8) {
            model.addAttribute("msg3", "El DNI debe ser de 8 dígitos");
            flag ++;

        }
        if (celular.length() != 9) {
            model.addAttribute("msg4", "El celular debe ser de 9 dígitos");
            flag ++;

        }
        if (correo.length() > 80) {
            model.addAttribute("msg5", "Correo debe respetar el formato @pucp.edu.pe o @pucp.pe");
            flag ++;
        }

        if (flag !=0){
            model.addAttribute("listacategorias", categoriaRepository.findAll());
            return "admin/newUser";
        }
        try {
            attr.addFlashAttribute("msg", "Usuario registrado exitosamente");
            System.out.println("CATEGORIAAA LLEGAA" + "  "+ categoria);
            int suspendido = 0;
            int habilitado = 0;
            admiRepository.crearUsuario(codigo,nombre,apellido,dni,correo,categoria, suspendido, habilitado);
            usuarioBDRepository.crearUsuarioBD(nombre,apellido,dni,correo,codigo);
            System.out.println("DETECTANDO ERROROOOOOOOOR");
            System.out.println("asdklasjdlaskjdlaksjda");
            return "redirect:/administrador/";


        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("NO SE PUEDEEE ACTUALIZAAAAAR");
            model.addAttribute("msg", "No se pudo realizar la acción");
            model.addAttribute("listaUsuarios", admiRepository.listaUsuariosAdmin());
            model.addAttribute("listaUsuarios1", admiRepository.findAll());
            model.addAttribute("usuariosDB", admiRepository.UsuariosDB());
            model.addAttribute("listacategorias", admiRepository.CategoriaList());
            return "admin/listaUsuarios";
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

    @GetMapping({"nuevacontrasena"})
    public String nuevacontrasena(@RequestParam("token") String token,Model model, RedirectAttributes attr)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            if (token.length() ==64){
                Usuario usuario=usuarioRepository.validarToken(token);
                if (usuario != null){
                    model.addAttribute("token", token);
                    return "loguin/nuevacontrasena";
                }
            }
            attr.addFlashAttribute("invalidtoken", "Error: token inválido o vencido.");
            return "redirect:/registro";
        }
        return "redirect:/redirectByRole";
    }

    @PostMapping("/guardarUser")
    public String editarUsuario(Model model, @RequestParam("id")
                                String codigo,
                                @RequestParam("nombre") @Size(max = 45) @NotNull(message = "Este campo no puede estar nulo")
                                String nombre,
                                @RequestParam("apellido")@Size(max = 45) @NotNull(message = "Este campo no puede estar nulo")
                                String apellido,
                                @RequestParam("dni") @Size(max = 8 , message = "El DNI tiene un máximo de 8 dígitos")
                                String dni,
                                @RequestParam("celular") @Size(max = 9 , message = "El celular tiene un máximo de 9 dígitos")
                                String celular,
                                @RequestParam("correo") @Size(max = 80) @NotNull(message = "Este campo no puede estar nulo")
                                String correo,
                                @RequestParam("categoria") @NotNull(message = "Este campo no puede estar nulo")
                                int categoria,
                                @ModelAttribute("usuario") Usuario usuario,
                                RedirectAttributes attr) {
            System.out.println("LLGOOOOOO A REGISTRAAAR WAA");
        System.out.println("CODIGOOOOO" + "   " + codigo);


        System.out.println("LISTA DE USUARIOS    " + usuarioRepository.findAll());
        Usuario usuario1 = new Usuario();
        System.out.println("CODIGO QUE LLEGA   " + codigo);
        int flag = 0;


        if (nombre.isEmpty() || nombre.length() > 45){
            model.addAttribute("msg1", "El nombre no debe ser nulo");
            flag ++;

        }
        if (apellido.isEmpty() || apellido.length() > 45){
            model.addAttribute("msg2", "El apellido no debe ser nulo");
            flag ++;
        }
        if (dni.length() != 8) {
            model.addAttribute("msg3", "El DNI debe ser de 8 dígitos");
            flag ++;

        }
        if (celular.length() != 9) {
            model.addAttribute("msg4", "El celular debe ser de 9 dígitos");
            flag ++;

        }
        if (correo.length() > 80) {
            model.addAttribute("msg5", "Correo debe respetar el formato @pucp.edu.pe o @pucp.pe");
            flag ++;
        }

        if (flag !=0){

            model.addAttribute("listaUsers", usuarioRepository.findAll());
            model.addAttribute("listacategorias", categoriaRepository.findAll());
            return "admin/editar_User";
        }
        try {

            attr.addFlashAttribute("msg", "Usuario actualizado exitosamente");
            System.out.println("CATEGORIAAA LLEGAA" + "  "+ categoria);
            admiRepository.actualizarUsuario(nombre,apellido,dni,celular,correo,categoria,codigo);
            usuarioBDRepository.actualizarUsuarioBD(nombre,apellido,dni,correo,codigo);
            System.out.println("DETECTANDO ERROROOOOOOOOR");
            System.out.println("asdklasjdlaskjdlaksjda");
            return "redirect:/administrador/";


        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("NO SE PUEDEEE ACTUALIZAAAAAR");
            model.addAttribute("msg", "No se pu do realizar la acción");
            model.addAttribute("listaUsuarios", admiRepository.listaUsuariosAdmin());
            model.addAttribute("listaUsuarios1", admiRepository.findAll());
            model.addAttribute("usuariosDB", admiRepository.UsuariosDB());
            model.addAttribute("listacategorias", admiRepository.CategoriaList());
            return "admin/listaUsuarios";
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
