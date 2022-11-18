

package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.IncidenciaComentarioDto;
import java.util.Random;

import com.example.adviertepucp.dto.IncidenciaListadto;
import com.example.adviertepucp.dto.TipoIncidenciadto;
import com.example.adviertepucp.dto.UsuarioEstaCreandoDto;
import com.example.adviertepucp.entity.*;
import com.example.adviertepucp.repository.*;
import com.example.adviertepucp.service.BlobService;
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
    ComentarioRepository comentarioRepository;

    @Autowired
    IncidenciatienefotoRepository incidenciatienefotoRepository;

    @Autowired
    FavoritoRepository favoritoRepository;
    @Autowired
    BlobService blobService;





    @GetMapping("/")
    String listaUsuario(Model model, HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidencia());
        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  usuarioRepository.listaIncidencia();
        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);
        System.out.println(listaFotos);

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
        model.addAttribute("incidenciaId",incidencia.getIdI());
        List<IncidenciaComentarioDto> listaComentarios = comentarioRepository.listaComentario(incidencia.getIdI());
        model.addAttribute("listaComentarios", listaComentarios);
        model.addAttribute("listaFotosInfo",usuarioRepository.listaFotoIncidencia(incidencia.getIdI()));
        System.out.println(usuarioRepository.listaFotoIncidencia(incidencia.getIdI()).get(0));

        return "usuario/MasInfoUsuario";
    }
    //comentario
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
        String direccion= "redirect:/usuario/info?id=" + idincidencia ;
        return direccion;
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
    public String busquedaIncidencia(@RequestParam("datetimes") String datetimes,
                                     @RequestParam("estado") String estado,
                                     @RequestParam("nombre") String nombre,
                                     Model model, HttpSession session,
                                     RedirectAttributes attr) {

        Usuario usuario = (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido() == 3) {
            return "redirect:/suspendido";
        }
        model.addAttribute("listaTipoIncidencias", tipoincidenciaRepository.findAll());
        //List<IncidenciaListadto> listaFiltroIncidencia = incidenciaRepository.buscarlistaFiltroIncidencia(fechainicio, fechafin, estado, nombre);
        List<IncidenciaListadto> listaFiltroIncidencia = incidenciaRepository.buscarlistaFiltro(datetimes,estado,nombre);
        model.addAttribute("listaIncidentes", listaFiltroIncidencia);
        model.addAttribute("msg", "Filtro aplicado exitosamente");
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


        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  usuarioRepository.listaIncidencia();
        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);

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
    public String guardarIncidente( @RequestParam(value = "latitud",required = false)   String latitud,
                                    @RequestParam(value = "longitud",required = false)  String longitud,
                                    @RequestParam("archivos") MultipartFile[] files,
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
                try {
                    Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
                    blobService.subirArchivo(foto);
                    fotoalmacenada.setFotoalmacenada(blobService.obtenerUrl(foto.getOriginalFilename()));
                    fotoalmacenada.setTipofoto(foto.getContentType());
                    fotoalmacenadaRepository.save(fotoalmacenada);
                    listaFotoAlmacenada.add(fotoalmacenada);
                    System.out.println("Se subio la foto");
                    System.out.println("Esta es la foto" +fotoalmacenada);
                } catch (Exception e){
                    e.printStackTrace();
                    model.addAttribute("msg", "Debe subir un archivo");
                    model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
                    return "usuario/nuevoIncidente";
                }
            }

            try{
                Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());
                Usuario usuarioLogueado=usuarioLogueadoOpt.get();


                String estado = "registrado";
                Instant datetime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
                Integer idasdasd= incidencia.getId();
                incidencia.setEstado(estado);
                incidencia.setLatitud(Double.valueOf(latitud));
                incidencia.setLongitud(Double.valueOf(longitud));

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







    @GetMapping({"/misIncidencias"})
    public String misIncidencias(Model model, HttpSession session) {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        model.addAttribute("listaIncidentes",usuarioRepository.misIncidencias(usuario.getId()));
        return "usuario/misIncidencias";
    }


}
