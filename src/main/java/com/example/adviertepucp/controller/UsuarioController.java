

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import javax.lang.model.element.ModuleElement;
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

    @Autowired
    IconoRepository iconoRepository;


    private final int personasPaginas =6;


    @GetMapping({"/","","/lista"})
    String listaUsuario(Model model, @RequestParam("pag") Optional<String> pag, HttpSession session, Authentication auth){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String ruta =  "/usuario?";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/usuario";
        }

        pagina = pagina<1? 1 : pagina;
        int paginas = (int) Math.ceil((float)usuarioRepository.countIncidencias()/personasPaginas)-1;
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

        return "usuario/lista";
    }

    //filtro
    @PostMapping("/filtro")
    public String busquedaIncidencia( @RequestParam("pag") Optional<String> pag,@RequestParam("datetimes") String datetimes,
                                     @RequestParam("estado") String estado,
                                     @RequestParam("nombre") String nombre,
                                     Model model, HttpSession session,
                                     RedirectAttributes attr) {

        Usuario usuario = (Usuario) session.getAttribute("usuariolog");
        String ruta =  "/usuario/getfiltro?datetimes="+datetimes+"&estado="+estado+"&nombre="+nombre +"&";
        if (usuario.getSuspendido() == 3) {
            return "redirect:/suspendido";
        }


        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/usuario";
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


        model.addAttribute("listaTipoIncidencias", tipoincidenciaRepository.findAll());

        List<IncidenciaListadto> listaFiltroIncidencia = incidenciaRepository.buscarlistaFiltro(datetimes,estado,nombre,lista);
        model.addAttribute("listaIncidentes", listaFiltroIncidencia);

        List<IncidenciaListadto> listaFiltroIncidenciaSinPaginado = incidenciaRepository.buscarlistaFiltroSinPaginado(datetimes,estado,nombre);
        model.addAttribute("listaIncidentesSinPaginado", listaFiltroIncidenciaSinPaginado);

        List<List<String>> listaFotos = new ArrayList<>();
        List<IncidenciaListadto> listaIncidencias=  listaFiltroIncidencia;
        for (IncidenciaListadto incidenciaListadto : listaIncidencias){
            listaFotos.add(usuarioRepository.listaFotoIncidencia(incidenciaListadto.getIdI()));
        }
        model.addAttribute("listaFotos",listaFotos);

        model.addAttribute("pag", pagina);
        model.addAttribute("paginas", paginas);
        model.addAttribute("ruta", ruta);

        if (datetimes !=null && estado !=null && nombre !=null) {
            model.addAttribute("datetimes", datetimes);
            model.addAttribute("estado", estado);
            model.addAttribute("nombre", nombre);
        }


        model.addAttribute("msg", "Filtro aplicado exitosamente");
        return "usuario/lista";
    }

    @PostMapping("/filtro2")
    public String busquedaIncidencia(@RequestParam("pag") Optional<String> pag,@RequestParam("titulo") String titulo,
                                     Model model, HttpSession session,
                                     RedirectAttributes attr) {

        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String ruta =  "/usuario/getfiltro2?titulo=" +titulo +"&";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/usuario";
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

        model.addAttribute("pag", pagina);
        model.addAttribute("paginas", paginas);
        model.addAttribute("ruta", ruta);

        model.addAttribute("tit", titulo);
        model.addAttribute("resultados", incidenciaRepository.countIncidenciasFiltro2(titulo));

        model.addAttribute("msg", "Filtro aplicado exitosamente");

        return "usuario/lista";
    }
    @GetMapping("/getfiltro")
    String listaUsuariogetfilro(Model model, @RequestParam("pag") Optional<String> pag,@RequestParam("datetimes") Optional<String> optionalDate,@RequestParam("estado") Optional<String> optionalEstado,@RequestParam("nombre") Optional<String> optionalNombre, HttpSession session, Authentication auth){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String datetimes = optionalDate.isPresent()? optionalDate.get().trim() : "";
        String estado = optionalEstado.isPresent()? optionalEstado.get().trim() : "";
        String nombre = optionalNombre.isPresent()? optionalNombre.get().trim() : "";
        String ruta =  "/usuario/getfiltro?datetimes="+datetimes+"&estado="+estado+"&nombre="+nombre +"&";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/usuario";
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

        return "usuario/lista";
    }
    @GetMapping("/getfiltro2")
    String listaUsuariogetfilro2(Model model, @RequestParam("pag") Optional<String> pag,@RequestParam("titulo") Optional<String> optionalTitulo, HttpSession session, Authentication auth){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        String titulo = optionalTitulo.isPresent()? optionalTitulo.get().trim() : "";
        String ruta =  "/usuario/getfiltro2?titulo=" +titulo +"&";
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        int pagina=0;
        try{
            pagina = pag.isEmpty() ? 1 : Integer.parseInt(pag.get());
        } catch (Exception e){
            return "redirect:/usuario";
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
                                    @RequestParam(value = "zonapucp",required = false)   String zonapucp,
                                    @RequestParam(value = "longitud",required = false)  String longitud,
                                    @RequestParam(value = "validaciondetitulo",required = false)  String validaciondetitulo,
                                    @RequestParam(value = "validaciondebody",required = false)  String validaciondebody,
                                    @RequestParam("archivos") MultipartFile[] files,
                                   @ModelAttribute("incidencia") @Valid Incidencia incidencia,
                                   BindingResult bindingResult,
                                   Model model,
                                   HttpSession session,
                                   Authentication auth,
                                    RedirectAttributes attr){


            //*****Validación unicamente para titulo y descripcion (No pueden estar vacíos)*****

            if (Objects.equals(incidencia.getTitulo(), "undefined") || Objects.equals(incidencia.getDescripcion(), "undefined") || (incidencia.getDescripcion()==null) || (incidencia.getTitulo()==null)) {
                attr.addFlashAttribute("err", "No se puede dejar el titulo o la descripción vacíos");
                return "redirect:/usuario/nuevoIncidente";
            }
            else if(validaciondetitulo.length()>60 || validaciondebody.length()>400){
                attr.addFlashAttribute("err", "No se puede exceder el limite de caracteres");
                return "redirect:/usuario/nuevoIncidente";
            }

        //System.out.println(incidencia.getTitulo());
        //System.out.println(incidencia.getDescripcion());



            //**********

            if (zonapucp==null || zonapucpRepository.validarZonaPucp(zonapucp)==null){
                attr.addFlashAttribute("errZona","Debe seleccionar una zona válida");
                return "redirect:/usuario/nuevoIncidente";
            }
            else{
                Zonapucp zonavalida = zonapucpRepository.validarZonaPucp(zonapucp);
                incidencia.setZonapucp(zonavalida);

            }





            ArrayList<Fotoalmacenada> listaFotoAlmacenada = new ArrayList<>();

            for (MultipartFile foto : files ) {
                if (foto.isEmpty()){
                    model.addAttribute("err", "Debe subir un archivo");
                    return "usuario/nuevoIncidente";
                }
                switch (foto.getContentType()) {

                    case "image/jpeg":
                    case "image/png":
                    case "application/octet-stream":
                        break;
                    default:
                        model.addAttribute("err", "Solo se deben de enviar imágenes");
                        model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
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
                    model.addAttribute("err", "Debe subir un archivo");
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
                return "redirect:/usuario/lista";
            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("err", "Ocurrió un error al crear la incidencia");
                return "usuario/nuevoIncidente";
            }
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
    @GetMapping({"/misFavoritos"})
    public String misFavoritos(Model model, HttpSession session) {
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");
        if (usuario.getSuspendido()==3){
            return "redirect:/suspendido";
        }

        model.addAttribute("listaIncidentes",usuarioRepository.misFavoritos(usuario.getId()));
        return "usuario/misFavoritos";
    }
    @GetMapping("/darlike")
    public String darLike(Model model, @RequestParam("id") int id,
                          Authentication auth){
        Optional<Incidencia> optionalIncidencia = incidenciaRepository.findById(id);

        if (optionalIncidencia.isPresent()) {
            Instant datetime = Instant.now().truncatedTo(ChronoUnit.MILLIS);
            Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());
            Usuario usuarioLogueado=usuarioLogueadoOpt.get();
            Favorito favorito = new Favorito();
            favorito.setUsuarioCodigo(usuarioLogueado);
            favorito.setIncidenciaIdincidencia(incidenciaRepository.getById(id));
            favorito.setFecha(datetime);
            favorito.setEsfavorito(1);
            favorito.setReaperturacaso(0);
            favorito.setPusoenproceso(0);
            favorito.setHasolucionado(0);
            favorito.setHacomentado(0);
            favoritoRepository.save(favorito);

        }
        return "redirect:/usuario/";
    }

    @GetMapping("/quitarlike")
    public String quitarLike(Model model, @RequestParam("id") int id,
                          Authentication auth){
        Optional<Incidencia> optionalIncidencia = incidenciaRepository.findById(id);

        if (optionalIncidencia.isPresent()) {
            Optional<Usuario> usuarioLogueadoOpt = usuarioRepository.findById(auth.getName());
            Usuario usuarioLogueado=usuarioLogueadoOpt.get();
            Integer idfavorito = incidenciaRepository.obtenerFavorito(Integer.parseInt(usuarioLogueado.getId()),id);
            favoritoRepository.deleteById(idfavorito);

        }
        return "redirect:/usuario/";
    }


    @PostMapping("/perfilEditar")
    public String editarPerfil(@RequestParam("archivo") MultipartFile logo , Model model , @RequestParam("codigo") String codigo, HttpSession session){
        if (logo.isEmpty()) {
            System.out.println("No recibi la imagen");
            model.addAttribute("err", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "usuario/perfil";
        }
        switch (logo.getContentType()) {

            case "image/jpeg":
            case "image/png":
            case "application/octet-stream":
                break;
            default:
                model.addAttribute("err", "Solo se deben de enviar imágenes");
                model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
                return "usuario/perfil";
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
            return "usuario/perfil";
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
        return "usuario/perfil";
    }

    @PostMapping("/iconoEditar")
    public String editarIcono(@RequestParam("archivo") MultipartFile logo , Model model , @RequestParam("codigo") String codigo){

        if (logo.isEmpty()) {
            System.out.println("No recibi la imagen");
            model.addAttribute("msg", "Debe subir un archivo");
            model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
            return "usuario/perfil";
        }
        switch (logo.getContentType()) {

            case "image/jpeg":
            case "image/png":
            case "application/octet-stream":
                break;
            default:
                model.addAttribute("err", "Solo se deben de enviar imágenes");
                model.addAttribute("listaTipos",incidenciaRepository.listaTipo());
                return "usuario/perfil";
        }

        Icono icono = new Icono();
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
            return "usuario/perfil";
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
        return "usuario/perfil";
    }


    @PostMapping("/actualizarcelular")
    public String actualizarcelular(@RequestParam(value = "celular",required = false)  String celular,

                                 Model model, HttpSession session,
                                 RedirectAttributes attr) {

        if (celular==null || celular.length()!=9 || !celular.matches("[0-9]*")) {
            attr.addFlashAttribute("err","El celular debe tener 9 digitos");
            return "redirect:/usuario/perfil";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuariolog");

        usuarioRepository.actualizarcelular(celular,usuario.getId());
        attr.addFlashAttribute("msg","Se actualizo tu celular correctamente");
        session.setAttribute("celular",celular);

        return "redirect:/usuario/perfil";


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
    String mapa(HttpSession session, Model model,HttpServletRequest request){
        Usuario usuario= (Usuario) session.getAttribute("usuariolog");


        //System.out.println(usuario.getIcono().getFoto().getFotoalmacenada());
        if (usuario.getSuspendido()==3){

            return "redirect:/suspendido";
        }

        model.addAttribute("usuariomapa",usuarioRepository.usuarioExiste(usuario.getId()));

        model.addAttribute("listaIncidentes",usuarioRepository.incidenciaMapa());

        return "usuario/mapa";
    }


}
