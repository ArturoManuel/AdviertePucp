

package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.dto.TipoIncidenciadto;
import com.example.adviertepucp.dto.UsuarioEstaCreandoDto;
import com.example.adviertepucp.entity.*;
import com.example.adviertepucp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    @Autowired
    FavoritoRepository favoritoRepository;





    @GetMapping("")
    String listaUsuario(Model model, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());

        return "usuario/lista";
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

        return "usuario/MasInfoUsuario";
    }

    @GetMapping("/mapa")
    String mapa(HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        return "usuario/mapa";
    }
    //filtro
    @PostMapping("/filtro")
    public String busquedaIncidencia(@RequestParam("fechainicio") String fechainicio,
                                     @RequestParam("fechafin") String fechafin,
                                     @RequestParam("estado") String estado,
                                     @RequestParam("nombre") String nombre,
                                     @RequestParam("titulo") String titulo,
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
        List<IncidenciaListadto> listaFiltroTitulo = incidenciaRepository.buscarlistaPorTitulo(titulo);
        model.addAttribute("listaIncidentes", listaFiltroTitulo);

        return "usuario/lista";
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

        return "usuario/lista";
    }

    @GetMapping({"/lista"})
    public String listaIncidencias(Model model, HttpSession session) {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());
        return "usuario/lista";
    }
    @GetMapping({"/perfil"})
    public String perfil(HttpSession session)
    {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        return "usuario/perfil";
    }
    @GetMapping({"/nuevoIncidente"})
    public String nuevo(@ModelAttribute("incidencia") Incidencia incidencia,
            Model model, Authentication auth, HttpSession session) {

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        UsuarioEstaCreandoDto usuarioEstaCreandoDto = usuarioRepository.obtenerCreandoUsuario(auth.getName());

        Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());


        if (usuarioEstaCreandoDto!=null){
            Optional<Incidencia> incidenciaOpt = incidenciaRepository.findById(usuarioEstaCreandoDto.getIdincidencia());

            incidencia = incidenciaOpt.get();

            model.addAttribute("incidenciaPrevia",incidencia);
            model.addAttribute("listaZonas",zonapucpRepository.findAll());
            model.addAttribute("listaTiposIncidencia",tipoincidenciaRepository.findAll());
            return "usuario/nuevoIncidente";


        }

        else {
            Incidencia nuevaIncidencia = new Incidencia();
            Double latitud = 1.5;
            Double longitud = 2.6;
            nuevaIncidencia.setPublicado(0);
            nuevaIncidencia.setLatitud(latitud);
            nuevaIncidencia.setLongitud(longitud);
            incidenciaRepository.save(nuevaIncidencia);
            //TODO: RELLENAR LA TABLA DE FAVORITO PERO EN EL SAVE
            Instant datetime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
            Favorito favorito = new Favorito();
            favorito.setEsfavorito(0);
            favorito.setHacomentado(0);
            favorito.setHasolucionado(0);
            favorito.setPusoenproceso(0);
            favorito.setReaperturacaso(0);
            favorito.setFecha(datetime);
            favorito.setUsuarioCodigo(usuarioLogueadoOpt.get());
            favorito.setIncidenciaIdincidencia(nuevaIncidencia);
            favoritoRepository.save(favorito);
            System.out.println(nuevaIncidencia.getId());
            model.addAttribute("nuevaIncidenciaId",nuevaIncidencia.getId());
            model.addAttribute("listaZonas",zonapucpRepository.findAll());
            model.addAttribute("listaTiposIncidencia",tipoincidenciaRepository.findAll());
            return "usuario/nuevoIncidente";


        }


    }
    @PostMapping("/guardarincidente")
    public String guardarIncidente(@RequestParam("archivos") MultipartFile[] files,
                                   @ModelAttribute("incidencia") @Valid Incidencia incidencia,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession session,
                                   Authentication auth){




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
                Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());
                Usuario usuarioLogueado=usuarioLogueadoOpt.get();

                Double latitud = 1.5;
                Double longitud = 2.6;
                String estado = "registrado";
                Instant datetime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
                Integer idasdasd= incidencia.getId();
                incidencia.setEstado(estado);

                incidencia.setFecha(datetime);
                incidencia.setPublicado(1);
                incidenciaRepository.save(incidencia);


                Favorito favorito = new Favorito();
                favorito.setId(usuarioRepository.obtenerInteraccionId(Integer.parseInt(usuarioLogueado.getId()),incidencia.getId()));
                favorito.setUsuarioCodigo(usuarioLogueado);
                favorito.setEsfavorito(0);
                favorito.setHacomentado(0);
                favorito.setHasolucionado(0);
                favorito.setPusoenproceso(0);
                favorito.setReaperturacaso(0);
                favorito.setFecha(datetime);
                favorito.setIncidenciaIdincidencia(incidencia);
                favoritoRepository.save(favorito);


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
