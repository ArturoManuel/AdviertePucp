
package com.example.adviertepucp.controller;

import com.example.adviertepucp.entity.Usuario;
import com.example.adviertepucp.repository.UsuarioRepository;
import com.example.adviertepucp.service.MailService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("")
public class LoguinController {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,64}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Autowired
    MailService mailService;

    @Autowired
    UsuarioRepository usuarioRepository;




    @GetMapping({"/autenticacion"})
    public String autenticacion()
    {
        return "loguin/autenticacion";
    }

    @GetMapping({"registro"})
    public String registro()
    {

        return "loguin/registro";
    }
    @PostMapping("enviaDatosRegistro")
    public String enviaRegistro(@RequestParam("id")  String id,
                                @RequestParam("correo")  String correo, RedirectAttributes attr, Model model, HttpServletRequest httpServletRequest) throws MessagingException, UnsupportedEncodingException {

        attr.addFlashAttribute("id", id);
        attr.addFlashAttribute("correo", correo);

        List<Usuario> usuarioEncontrado=usuarioRepository.validarUsuario(id,correo);
        if (usuarioEncontrado.size()==0){
            attr.addFlashAttribute("fail", "Los datos ingresados no coinciden con el registro de usuarios.");
        } else{
            if(usuarioEncontrado.get(0).getSuspendido()!=4){
                attr.addFlashAttribute("already", "Este usuario ya se encuentra registrado en el sistema.");
            }
            else{
                if (usuarioEncontrado.get(0).getCodigoverificacion()!=null){
                    int minutes= mailService.contadorDiezMin();
                    attr.addFlashAttribute("already","Ya se envió un correo de validación. Por favor, verifica la bandeja de entrada o spam en tu correo o inténtalo de nuevo dentro de "+minutes+" minuto(s)");
                }
                else {

                    int endIndex=httpServletRequest.getRequestURL().length()-18;
                    String contextpath=httpServletRequest.getRequestURL().substring(0,endIndex);

                    String codigoVerificacion = RandomString.make(64);
                    usuarioRepository.enviarcodigo(codigoVerificacion, id);
                    mailService.sendVerificationEmail(usuarioEncontrado.get(0), codigoVerificacion,contextpath);

                    attr.addFlashAttribute("success", "Datos confirmados correctamente, se ha enviado un correo de confirmación.");
                }
            }
        }
        return "redirect:/registro";
    }

    @GetMapping({"nuevacontrasena"})
    public String nuevacontrasena(@RequestParam("token") String token,Model model, RedirectAttributes attr)
    {
        if (token.length() ==64){
            Usuario usuario=usuarioRepository.validarToken(token);
            if (usuario != null){
                model.addAttribute("token", token);
                return "/loguin/nuevacontrasena";
            }
        }
            attr.addFlashAttribute("invalidtoken", "Error:token inválido o vencido (El token de verificación vence cada media hora)");
            return "redirect:/registro";
    }

    @GetMapping({"/restablecercontrasena"})
    public String restablececontrasena()
    {
        return "/loguin/restablececontrasena";
    }

    @GetMapping({""})
    public String index() {
        return "/loguin/loguin";
    }

    @PostMapping({"/nuevopwd"})
    public String nuevapwd(@RequestParam("token") String token,
                           @RequestParam("pwd") String pwd,
                           @RequestParam("confirmpwd") String confirmpwd,
                           RedirectAttributes attr,
                           HttpServletRequest request) {
        Usuario usuario =usuarioRepository.validarToken(token);
        attr.addFlashAttribute("pwd", pwd);
        attr.addFlashAttribute("confirmpwd", confirmpwd);
        Matcher matcher =pattern.matcher(pwd);
        String referer = request.getHeader("Referer");
        boolean pwdvalido=matcher.matches();

        if (usuario == null){
            attr.addFlashAttribute("invalidtoken", "Error:token inválido o vencido (El token de verificación vence cada media hora)");
            return "redirect:/registro";
        }
        if (!Objects.equals(pwd, confirmpwd)){
            attr.addFlashAttribute("fail", "Los campos de contraseña no coinciden");
            return "redirect:"+ referer;
        }
        if (!pwdvalido){
            attr.addFlashAttribute("fail", "Comprueba que los campos cumplan las condiciones minimas");
            return "redirect:"+ referer;
        }
        usuarioRepository.establecerContrasena(pwd, usuario.getId());
        usuarioRepository.deleteTokenbyId(usuario.getId());
        attr.addFlashAttribute("registrado", "Cuanta registrada correctamente, ahora puedes ingresar al sistema");

        return "redirect:/";
    }



}
