package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.entity.Usuario;
import com.example.adviertepucp.repository.IncidenciaRepository;
import com.example.adviertepucp.repository.TipoincidenciaRepository;
import com.example.adviertepucp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.adviertepucp.entity.Incidencia;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
    /*
    @GetMapping("")
    String listarSeguridad(){
        return "seguridad/listaMapa";
    }*/
    @GetMapping("")
    String listarSeguridad(Model model, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());
        return "seguridad/listaMapa";
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
        List<IncidenciaListadto> listaIncidencias = usuarioRepository.listaIncidencia();
        for( IncidenciaListadto lista : listaIncidencias){
            if(id == lista.getIdI()){
                incidencia=lista;
                break;
            }
        }
        model.addAttribute("incidencia",incidencia);

        return "seguridad/MasInfoSeguridad";
    }
    @GetMapping("/mapa")
    String mapa(HttpSession session){
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
    }

}
