package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.IncidenciaComentarioDto;
import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.entity.Favorito;
import com.example.adviertepucp.entity.Fotoalmacenada;
import com.example.adviertepucp.entity.Comentario;
import com.example.adviertepucp.entity.Usuario;
import com.example.adviertepucp.repository.IncidenciaRepository;
import com.example.adviertepucp.repository.ComentarioRepository;
import com.example.adviertepucp.repository.TipoincidenciaRepository;
import com.example.adviertepucp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.example.adviertepucp.entity.Incidencia;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping({"/seguridad"})
public class SeguridadController {
    @Autowired
    IncidenciaRepository  incidenciaRepository;
    @Autowired
    TipoincidenciaRepository  tipoincidenciaRepository;
    @Autowired
    UsuarioRepository  usuarioRepository;
    @Autowired
    ComentarioRepository comentarioRepository;

    /*
    @GetMapping("")
    String listarSeguridad(){
        return "seguridad/listaMapa";
    }*/
    @GetMapping({"/",""})
    String listarSeguridad(Model model, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());
        return "seguridad/listaMapa";
    }
    //Perfil
    @GetMapping({"/perfil"})
    public String perfil(HttpSession session)
    {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        return "seguridad/perfil";
    }

    //filtro
    @PostMapping("/filtro")
    public String busquedaIncidencia(@RequestParam("fechainicio") String fechainicio,
                                      @RequestParam("fechafin") String fechafin,
                                      @RequestParam("estado") String estado,
                                      @RequestParam("nombre") String nombre,
                                      Model model, HttpSession session,
                                     RedirectAttributes attr) {

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        List<IncidenciaListadto> listaFiltroIncidencia = incidenciaRepository.buscarlistaFiltroIncidencia(fechainicio,fechafin,estado,nombre);
        model.addAttribute("listaIncidentes", listaFiltroIncidencia);
        model.addAttribute("msg", "Filtro aplicado exitosamente");

        return "seguridad/listaMapa";
    }
    @PostMapping("/filtro2")
    public String busquedaIncidencia(@RequestParam("titulo") String titulo,
                                     Model model, HttpSession session,
                                     RedirectAttributes attr) {

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        List<IncidenciaListadto> listaFiltroTitulo = incidenciaRepository.buscarlistaPorTitulo(titulo);
        model.addAttribute("listaIncidentes", listaFiltroTitulo);
        model.addAttribute("msg", "Filtro aplicado exitosamente");

        return "seguridad/listaMapa";
    }
    @PostMapping("/atenderincidencia")
    public String atenderComentario(@RequestParam("idincidencia")  int idincidencia,
                                    @RequestParam("codigopucp")  int codigopucp,
                                    @RequestParam("comentario")  String comentario,
                                    Model model, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        incidenciaRepository.atenderIncidencia(idincidencia);
        incidenciaRepository.agregarComentario(idincidencia, comentario, codigopucp);

        String direccion= "redirect:/seguridad/info?id=" + idincidencia ;
        return direccion;
    }
    @PostMapping("/resolverincidencia")
    public String resolverComentario(@RequestParam("idincidencia")  int idincidencia,
                                    @RequestParam("codigopucp")  int codigopucp,
                                    @RequestParam("comentario")  String comentario,
                                    Model model, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        incidenciaRepository.resolverIncidencia(idincidencia);
        incidenciaRepository.agregarComentario(idincidencia, comentario, codigopucp);

        String direccion= "redirect:/seguridad";
        return direccion;
    }

    @PostMapping("/agregarcomentario")
    public String masInformacion(@RequestParam("idincidencia")  int idincidencia,
                                     @RequestParam("codigopucp")  int codigopucp,
                                     @RequestParam("comentario")  String comentario,
                                     Model model, HttpSession session,
                                     RedirectAttributes attr) {

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        incidenciaRepository.agregarComentario(idincidencia, comentario, codigopucp);

        //model.addAttribute("comentario", incidenciaRepository.agregarComentario(incidencia, comen, codigopucp));
        String direccion= "redirect:/seguridad/info?id=" + idincidencia ;
       return direccion;
    }
/*
    @GetMapping("/info")
    String masInformacion(Model model,
                          @RequestParam("id") int id){
        Optional<Incidencia> opt = incidenciaRepository.findById(id);
        return "seguridad/MasInfoSeguridad";
    }*/
    @GetMapping({"/lista"})
    public String listaIncidencias(Model model, HttpSession session) {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());
        return "seguridad/listaMapa";
    }
    @GetMapping("/info")
    String masInformacion(@RequestParam("id") int id,
                          Model model, HttpSession session){

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        IncidenciaListadto incidencia = null;
        IncidenciaComentarioDto comentario = null;
        List<IncidenciaListadto> listaIncidencias = usuarioRepository.listaIncidencia();
        for( IncidenciaListadto lista : listaIncidencias){
            if(id == lista.getIdI()){
                incidencia=lista;

                break;
            }
        }

        /*
        List<IncidenciaComentarioDto> listaIncidenciasComentario = comentarioRepository.listaComentario();
        for( IncidenciaComentarioDto lista1 : listaIncidenciasComentario){
            if(id == lista1.getIdincidencia()){
                comentario=lista1;
                break;
            }
        }*/
        model.addAttribute("incidencia",incidencia);
        model.addAttribute("incidenciaId",incidencia.getIdI());
        List<IncidenciaComentarioDto> listaComentarios = comentarioRepository.listaComentario(incidencia.getIdI());
        model.addAttribute("listaComentarios", listaComentarios);
        //model.addAttribute("listaComentarios",comentarioRepository.listaComentario(idincidencia));

        return "seguridad/MasInfoSeguridad";
    }
    @GetMapping("/mapa")
    String mapa(Model model,HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        return "seguridad/mapa";
    }
/*
    @GetMapping("/estadisticas")
    String dashboard(Model model, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

       return "seguridad/dashboard";
    }*/



    @GetMapping("/estadisticas")
    public String dashboard(Model model, HttpSession session) {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        model.addAttribute("num_incidenciasPorMes", incidenciaRepository.incidenciasPorMes());
        model.addAttribute("num_incidenciasPorAnio", incidenciaRepository.incidenciasPorAnio());
        model.addAttribute("num_incidenciasAtendidas", incidenciaRepository.incidenciasAtendidas());

        model.addAttribute("lista_UsariosconMasIncidencias", incidenciaRepository.UsariosconMasIncidencias());

        model.addAttribute("lista_ubicacionesPUCP", incidenciaRepository.ubicacionesPUCP());
        model.addAttribute("num_usuariosReportados", incidenciaRepository.usuariosReportados());
        /*
        model.addAttribute("num_totalUsuario", incidenciaRepository.totalUsuarios());
        model.addAttribute("lista_estadousuario", incidenciaRepository.estadoUsuarios());

        */
        return "seguridad/dashboard";
    }
    /*
    @PostMapping("/MasInfoSeguridad")
    public String atenderIncidencia(Incidencia incidencia, HttpSession session) {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        Optional<Incidencia> opt = incidenciaRepository.findById(incidencia.getId());

        if (opt.isPresent()) {
            //incidenciaRepository.atenderIncidencia(incidencia.getEstado(), incidencia.getId());
        }
        return "redirect:/seguridad/lista";
    }*/
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id) {

        byte[] imagenComoBytes = usuarioRepository.listaFotoIncidencia(id).get(0);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(
                MediaType.parseMediaType("image/png"));

        return new ResponseEntity<>(
                imagenComoBytes,
                httpHeaders,
                HttpStatus.OK);
    }

}
