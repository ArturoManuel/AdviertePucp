package com.example.adviertepucp.controller;

import com.example.adviertepucp.dto.*;
import com.example.adviertepucp.entity.*;
import com.example.adviertepucp.repository.*;
import com.example.adviertepucp.service.BlobService;
import com.example.adviertepucp.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    @Autowired
    BlobService blobService;

    @Autowired
    FotoalmacenadaRepository fotoalmacenadaRepository;
    @Autowired
    IconoRepository iconoRepository;

    @Autowired
    MailService mailService;

    //REPORTAR USER


    @PostMapping("/reportarUser")
    public String reportarUser(@RequestParam("id") String id, @RequestParam("id1") String id1,
                               @RequestParam("comentarioReporte")  String comentario,  Model model,
                               RedirectAttributes attr) throws MessagingException, UnsupportedEncodingException {

        System.out.println("CODIGO LLEGO" + id);
        Usuario usuario1 = usuarioRepository.getById(String.valueOf(id));


        if (usuario1.getSuspendido() == 0){
            mailService.correoReporte(usuario1.getCorreo(), usuario1.getNombre());

            usuarioRepository.reporteUsuario1(Integer.valueOf(id));
            usuarioRepository.reporteStatus(Integer.valueOf(id1));
            incidenciaRepository.resolverIncidencia(Integer.parseInt(id1));
            incidenciaRepository.agregarComentario(Integer.parseInt(id1), comentario, Integer.parseInt(id));
            attr.addFlashAttribute("msg", "Incidencia Reportada");
            return "redirect:/seguridad" ;
        }
        if (usuario1.getSuspendido() == 1){
            mailService.correoReporte(usuario1.getCorreo(), usuario1.getNombre());
            usuarioRepository.reporteUsuario2(Integer.valueOf(id));
            usuarioRepository.reporteStatus(Integer.valueOf(id1));
            incidenciaRepository.resolverIncidencia(Integer.parseInt(id1));
            incidenciaRepository.agregarComentario(Integer.parseInt(id1), comentario, Integer.parseInt(id));
            attr.addFlashAttribute("msg", "Incidencia Reportada");

            return "redirect:/seguridad";
        }
        if (usuario1.getSuspendido() == 2){
            mailService.correoReporte(usuario1.getCorreo(), usuario1.getNombre());
            usuarioRepository.reporteUsuario3(Integer.valueOf(id));
            usuarioRepository.reporteStatus(Integer.valueOf(id1));
            incidenciaRepository.resolverIncidencia(Integer.parseInt(id1));
            incidenciaRepository.agregarComentario(Integer.parseInt(id1), comentario, Integer.parseInt(id));
            attr.addFlashAttribute("msg", "Incidencia Reportada");
            return "redirect:/seguridad" ;
        }
        else {
            usuarioRepository.reporteStatus(Integer.valueOf(id1));
            incidenciaRepository.resolverIncidencia(Integer.parseInt(id1));
            incidenciaRepository.agregarComentario(Integer.parseInt(id1), comentario, Integer.parseInt(id));
            attr.addFlashAttribute("msg", "Incidencia Reportada");
        }
        return "redirect:/seguridad" ;
    }

    //"redirect:/info"   |||| "seguridad/MasInfoSeguridad"

    private final int personasPaginas =6;
    /*
    @GetMapping("")
    String listarSeguridad(){
        return "seguridad/listaMapa";
    }*/
    @GetMapping({"/","","/listaMapa"})
    String listarSeguridad(Model model,@RequestParam("pag") Optional<String> pag, HttpSession session,Authentication auth){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String ruta =  "/seguridad?";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/seguridad";
        }

        pagina = pagina<1? 1 : pagina;
        int paginas = (int) Math.ceil((float)usuarioRepository.countIncidencias()/personasPaginas);
        pagina = pagina>paginas? paginas : pagina;
        Pageable lista ;
        if (pagina == 0) {
            lista = PageRequest.of(0, personasPaginas);
        } else {
            lista = PageRequest.of(pagina - 1, personasPaginas);

        }

        Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());
        Usuario usuarioLogueado=usuarioLogueadoOpt.get();


        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        model.addAttribute("listaUsers", usuarioRepository.findAll());
        model.addAttribute("listaIncidentes",usuarioRepository.listaIncidenciaUsuarios(Integer.parseInt(usuarioLogueado.getId()),lista));



        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  usuarioRepository.listaIncidenciaUsuarios(Integer.parseInt(usuarioLogueado.getId()),lista);

        List<IncidenciaListadto> listaFiltroIncidenciaSinPaginado = usuarioRepository.listaIncidenciaUsuariosSinPaginado(Integer.parseInt(usuarioLogueado.getId()));
        model.addAttribute("listaIncidentesSinPaginado", listaFiltroIncidenciaSinPaginado);

        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);

        model.addAttribute("pag", pagina);
        model.addAttribute("paginas", paginas);
        model.addAttribute("ruta", ruta);

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
    @PostMapping("filtro")
    public String busquedaIncidencia( @RequestParam("pag") Optional<String> pag,@RequestParam("datetimes") String datetimes,
                                      @RequestParam("estado") String estado,
                                      @RequestParam("nombre") String nombre,
                                      Model model, HttpSession session,
                                      RedirectAttributes attr) {

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String ruta =  "/seguridad/getfiltro?datetimes="+datetimes+"&estado="+estado+"&nombre="+nombre +"&";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/seguridad";
        }

        pagina = pagina<1? 1 : pagina;
        int paginas = (int) Math.ceil((float)incidenciaRepository.countIncidenciasFiltro(datetimes,estado,nombre)/personasPaginas);
        pagina = pagina>paginas? paginas : pagina;
        Pageable lista ;
        if (pagina == 0) {
            lista = PageRequest.of(0, personasPaginas);
        } else {
            lista = PageRequest.of(pagina - 1, personasPaginas);

        }



        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        //List<IncidenciaListadto> listaFiltroIncidencia = incidenciaRepository.buscarlistaFiltroIncidencia(fechainicio,fechafin,estado,nombre);
        List<IncidenciaListadto> listaFiltroIncidencia = incidenciaRepository.buscarlistaFiltro(datetimes,estado,nombre, lista);
        model.addAttribute("listaIncidentes", listaFiltroIncidencia);

        List<IncidenciaListadto> listaFiltroIncidenciaSinPaginado = incidenciaRepository.buscarlistaFiltroSinPaginado(datetimes,estado,nombre);
        model.addAttribute("listaIncidentesSinPaginado", listaFiltroIncidenciaSinPaginado);

        model.addAttribute("msg", "Filtro aplicado");
        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  listaFiltroIncidencia;
        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);

        model.addAttribute("pag", pagina);
        model.addAttribute("paginas", paginas);
        model.addAttribute("ruta", ruta);

        model.addAttribute("msg", "Filtro aplicado exitosamente");

        if (datetimes !=null && estado !=null && nombre !=null) {
            model.addAttribute("datetimes", datetimes);
            model.addAttribute("estado", estado);
            model.addAttribute("nombre", nombre);
        }


        return "seguridad/listaMapa";
    }
    @PostMapping({"filtro2","/filtro2"})
    public String busquedaIncidencia( @RequestParam("pag") Optional<String> pag,@RequestParam("titulo") String titulo,
                                      Model model, HttpSession session,
                                      RedirectAttributes attr) {

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String ruta =  "/seguridad/getfiltro2?titulo=" +titulo +"&";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/seguridad";
        }

        pagina = pagina<1? 1 : pagina;
        int paginas = (int) Math.ceil((float)incidenciaRepository.countIncidenciasFiltro2(titulo)/personasPaginas);
        pagina = pagina>paginas? paginas : pagina;
        Pageable lista ;
        if (pagina == 0) {
            lista = PageRequest.of(0, personasPaginas);
        } else {
            lista = PageRequest.of(pagina - 1, personasPaginas);

        }



        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        List<IncidenciaListadto> listaFiltroTitulo = incidenciaRepository.buscarlistaPorTitulo(titulo, lista);
        model.addAttribute("listaIncidentes", listaFiltroTitulo);

        List<IncidenciaListadto> listaFiltroTituloSinPaginado = incidenciaRepository.buscarlistaPorTituloSinPaginado(titulo);
        model.addAttribute("listaIncidentesSinPaginado", listaFiltroTituloSinPaginado);

        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  listaFiltroTitulo;
        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);
        model.addAttribute("msg", "Filtro aplicado");

        model.addAttribute("pag", pagina);
        model.addAttribute("paginas", paginas);
        model.addAttribute("ruta", ruta);

        model.addAttribute("tit", titulo);
        model.addAttribute("resultados", incidenciaRepository.countIncidenciasFiltro2(titulo));


        return "seguridad/listaMapa";
    }
    @GetMapping("/getfiltro")
    String listaUsuariogetfilro(Model model, @RequestParam("pag") Optional<String> pag,@RequestParam("datetimes") Optional<String> optionalDate,@RequestParam("estado") Optional<String> optionalEstado,@RequestParam("nombre") Optional<String> optionalNombre, HttpSession session, Authentication auth){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String datetimes = optionalDate.isPresent()? optionalDate.get().trim() : "";
        String estado = optionalEstado.isPresent()? optionalEstado.get().trim() : "";
        String nombre = optionalNombre.isPresent()? optionalNombre.get().trim() : "";
        String ruta =  "/seguridad/getfiltro?datetimes="+datetimes+"&estado="+estado+"&nombre="+nombre +"&";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/seguridad";
        }

        pagina = pagina<1? 1 : pagina;
        int paginas = (int) Math.ceil((float)incidenciaRepository.countIncidenciasFiltro(datetimes,estado,nombre)/personasPaginas);
        pagina = pagina>paginas? paginas : pagina;
        Pageable lista ;
        if (pagina == 0) {
            lista = PageRequest.of(0, personasPaginas);
        } else {
            lista = PageRequest.of(pagina - 1, personasPaginas);

        }


        Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());
        Usuario usuarioLogueado=usuarioLogueadoOpt.get();

        model.addAttribute("usercodigo", Integer.parseInt(usuarioLogueado.getId()));
        model.addAttribute("listaTipoIncidencias", tipoincidenciaRepository.findAll());

        List<IncidenciaListadto> listaFiltroIncidencia = incidenciaRepository.buscarlistaFiltro(datetimes,estado,nombre,lista);
        model.addAttribute("listaIncidentes", listaFiltroIncidencia);
        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  listaFiltroIncidencia;
        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);


        model.addAttribute("pag", pagina);
        model.addAttribute("paginas", paginas);
        model.addAttribute("ruta", ruta);

        return "seguridad/listaMapa";
    }
    @GetMapping("/getfiltro2")
    String listaUsuariogetfilro2(Model model, @RequestParam("pag") Optional<String> pag,@RequestParam("titulo") Optional<String> optionalTitulo, HttpSession session, Authentication auth){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String titulo = optionalTitulo.isPresent()? optionalTitulo.get().trim() : "";
        String ruta =  "/seguridad/getfiltro2?titulo=" +titulo +"&";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/seguridad";
        }

        pagina = pagina<1? 1 : pagina;
        int paginas = (int) Math.ceil((float)incidenciaRepository.countIncidenciasFiltro2(titulo)/personasPaginas);
        pagina = pagina>paginas? paginas : pagina;
        Pageable lista ;
        if (pagina == 0) {
            lista = PageRequest.of(0, personasPaginas);
        } else {
            lista = PageRequest.of(pagina - 1, personasPaginas);

        }


        Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());
        Usuario usuarioLogueado=usuarioLogueadoOpt.get();

        model.addAttribute("usercodigo", Integer.parseInt(usuarioLogueado.getId()));
        model.addAttribute("listaTipoIncidencias",tipoincidenciaRepository.findAll());
        List<IncidenciaListadto> listaFiltroTitulo = incidenciaRepository.buscarlistaPorTitulo(titulo, lista);
        model.addAttribute("listaIncidentes", listaFiltroTitulo);
        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  listaFiltroTitulo;
        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);

        model.addAttribute("pag", pagina);
        model.addAttribute("paginas", paginas);
        model.addAttribute("ruta", ruta);

        model.addAttribute("tit", titulo);
        model.addAttribute("resultados", incidenciaRepository.countIncidenciasFiltro2(titulo));

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

    @PostMapping("agregarcomentario")
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
    @GetMapping({"lista"})
    public String listaIncidencias(Model model, HttpSession session) {
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
        return "seguridad/listaMapa";
    }
    @GetMapping("info")
    String masInformacion(@RequestParam("id") int id,
                          Model model, HttpSession session){

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        IncidenciaListadto incidencia = null;
        IncidenciaComentarioDto comentario = null;
        List<IncidenciaListadto> listaIncidencias = usuarioRepository.listaIncidenciaConResueltos();
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
        model.addAttribute("usuario", incidencia.getUsuario_codigo());
        List<IncidenciaComentarioDto> listaComentarios = comentarioRepository.listaComentario(incidencia.getIdI());
        model.addAttribute("listaComentarios", listaComentarios);
        //model.addAttribute("listaComentarios",comentarioRepository.listaComentario(idincidencia));


        model.addAttribute("listaFotosInfo",usuarioRepository.listaFotoIncidencia(incidencia.getIdI()));
        System.out.println(usuarioRepository.listaFotoIncidencia(incidencia.getIdI()).get(0));


        return "seguridad/MasInfoSeguridad";
    }
    @GetMapping("mapa")
    String mapa(Model model,HttpSession session){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }
        model.addAttribute("usuariomapa",usuarioRepository.usuarioExiste(usuario.getId()));
        model.addAttribute("listaIncidentes",usuarioRepository.incidenciaMapa());
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



    @GetMapping("estadisticas")
    public String dashboard(Model model, HttpSession session) {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }


        model.addAttribute("num_incidenciasPorMes", incidenciaRepository.incidenciasPorMes());
        model.addAttribute("num_incidenciasPorAnio", incidenciaRepository.incidenciasPorAnio());
        model.addAttribute("num_incidenciasAtendidas", incidenciaRepository.incidenciasAtendidas());

        model.addAttribute("lista_UsariosconMasIncidencias", incidenciaRepository.UsariosconMasIncidencias());

        List<UsarioMasIncidencia> lista_NombreUsariosconMasIncidencias = incidenciaRepository.NombreUsariosconMasIncidencias();
        model.addAttribute("lista_NombreUsariosconMasIncidencias", lista_NombreUsariosconMasIncidencias);

        List<UsuarioCantidadIncidencia> lista_CantidadUsariosconMasIncidencias = incidenciaRepository.CantidadUsariosconMasIncidencias();
        model.addAttribute("lista_CantidadUsariosconMasIncidencias", lista_CantidadUsariosconMasIncidencias);

        model.addAttribute("lista_ubicacionesPUCP", incidenciaRepository.ubicacionesPUCP());
        //model.addAttribute("lista_ubicacionesNombrePUCP", incidenciaRepository.ubicacionesNombrePUCP());
        //model.addAttribute("lista_ubicacionesZonaPUCP", incidenciaRepository.ubicacionesZonaPUCP());

        List<IncidenciaPorZona> lista_ubicacionesNombrePUCP = incidenciaRepository.ubicacionesNombrePUCP();
        model.addAttribute("lista_ubicacionesNombrePUCP", lista_ubicacionesNombrePUCP);

        List<ZonaPUCP> lista_ubicacionesZonaPUCP = incidenciaRepository.ubicacionesZonaPUCP();
        model.addAttribute("lista_ubicacionesZonaPUCP", lista_ubicacionesZonaPUCP);

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


    @PostMapping("/perfilEditar")
    public String editarPerfil(@RequestParam("archivo") MultipartFile logo , Model model , @RequestParam("codigo") String codigo, HttpSession session){
        if (logo.isEmpty()) {
            System.out.println("No recibi la imagen");
            model.addAttribute("err", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "seguridad/perfil";
        }
        switch (logo.getContentType()) {

            case "image/jpeg":
            case "image/png":
            case "application/octet-stream":
                break;
            default:
                model.addAttribute("err", "Solo se deben de enviar imágenes");
                model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
                return "seguridad/perfil";
        }



        Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
        try {
            blobService.subirArchivo(logo);
            fotoalmacenada.setFotoalmacenada(blobService.obtenerUrl(logo.getOriginalFilename()));
            fotoalmacenada.setTipofoto(logo.getContentType());
            fotoalmacenadaRepository.save(fotoalmacenada);
            System.out.println("Se subio la foto");
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("err", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "admin/perfil";
        }
        System.out.println(codigo);
        Usuario usuario= usuarioRepository.usuarioExiste(codigo);
        if( usuario!=null){
            usuario.setFoto(fotoalmacenada);
            session.getAttribute("usuariolog");

            System.out.println(session.getAttribute("usuariolog"));
            session.setAttribute("foto",fotoalmacenada.getFotoalmacenada());
            usuarioRepository.save(usuario);
        }else{
            model.addAttribute("err","Ocurrio un error en el guardado");
        }
        return "seguridad/perfil";
    }

    public static MultipartFile reziseImage(final MultipartFile image) throws IOException {
        BufferedImage originalImage = ImageIO.read(image.getInputStream());
        BufferedImage resizedImage = new BufferedImage(32, 32, 2);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 32, 32, (ImageObserver)null);
        g.dispose();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", os);
        return new MultipartFile() {
            public String getName() {
                return image.getName();
            }

            public String getOriginalFilename() {
                return image.getOriginalFilename();
            }

            public String getContentType() {
                return "image/png";
            }

            public boolean isEmpty() {
                return os.toByteArray().length == 0;
            }

            public long getSize() {
                return (long)os.toByteArray().length;
            }

            public byte[] getBytes() throws IOException {
                return os.toByteArray();
            }

            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(os.toByteArray());
            }

            public void transferTo(File dest) throws IOException, IllegalStateException {
                (new FileOutputStream(dest)).write(os.toByteArray());
            }
        };
    }

    @PostMapping("/iconoEditar")
    public String editarIcono(@RequestParam("archivo") MultipartFile logo , Model model , @RequestParam("codigo") String codigo){

        if (logo.isEmpty()) {
            System.out.println("No recibi la imagen");
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "seguridad/perfil";
        }
        switch (logo.getContentType()) {

            case "image/jpeg":
            case "image/png":
            case "application/octet-stream":
                break;
            default:
                model.addAttribute("err", "Solo se deben de enviar imágenes");
                model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
                return "seguridad/perfil";
        }

        Icono icono = new Icono();
        Fotoalmacenada fotoalmacenada = new Fotoalmacenada();
        try {
            logo = reziseImage(logo);
            blobService.subirArchivo(logo);
            fotoalmacenada.setFotoalmacenada(blobService.obtenerUrl(logo.getOriginalFilename()));
            fotoalmacenada.setTipofoto(logo.getContentType());
            fotoalmacenadaRepository.save(fotoalmacenada);
            System.out.println("Se subio la foto");
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("err", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "seguridad/perfil";
        }
        System.out.println(codigo);
        Usuario usuario= usuarioRepository.usuarioExiste(codigo);
        if( usuario!=null){
            icono.setNombre(usuario.getId());
            icono.setFoto(fotoalmacenada);
            usuario.setIcono(icono);

            iconoRepository.save(icono);
            usuarioRepository.save(usuario);
            model.addAttribute("msg","Se agrego tu icono correctamente");
        }else{
            model.addAttribute("err","Ocurrio un error en el guardado");
        }
        return "seguridad/perfil";
    }

}

