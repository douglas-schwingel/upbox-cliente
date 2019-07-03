package br.com.upbox.controller;

import br.com.upbox.models.Arquivo;
import br.com.upbox.models.ArquivoCompartilhado;
import br.com.upbox.models.Usuario;
import br.com.upbox.requisicoes.*;
import br.com.upbox.utils.FtpUtil;
import br.com.upbox.utils.UsuarioControllerUtil;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private static final Marker marker = MarkerFactory.getMarker("usuario-controller");
    private static final String USUARIO = "usuario";

    @GetMapping("/")
    public String index() {
        logger.info(marker, "entrando no {}", "/index");
        return "index";
    }

    @GetMapping("/cadastro")
    public ModelAndView cadastro() {
        logger.info(marker, "entrando no {}", "/cadastro");
        ModelAndView view = new ModelAndView("cadastro");
        Usuario usuario = new Usuario();
        view.addObject(USUARIO, usuario);
        return view;
    }

    @PostMapping("/envia_usuario")
    public ModelAndView enviaUsuario(@ModelAttribute(USUARIO) Usuario usuario) {
        UsuarioControllerUtil.enviaUsuario(usuario);
        Usuario usuarioRetornado = UsuarioControllerUtil.buscaUsuario(usuario.getUsername());
        FtpUtil.getArquivos(usuarioRetornado);
        logger.info(marker, "Usuario {} adicionado com sucesso!", usuarioRetornado.getUsername());
        ModelAndView view = new ModelAndView("redirect:/usuario/" + usuario.getUsername());
        view.addObject(USUARIO, usuario);
        return view;
    }

    @GetMapping(value = "/usuario/{username}")
    public ModelAndView buscaUsuario(@NotNull @PathVariable("username") String username) {
        ModelAndView view = new ModelAndView(USUARIO);
        Usuario usuarioRetornado = UsuarioControllerUtil.buscaUsuario(username);
        List<Arquivo> listaDeArquivos = FtpUtil.getArquivos(usuarioRetornado);
        view.addObject("lista", listaDeArquivos);
        view.addObject(USUARIO, usuarioRetornado);
        return view;
    }

    @PostMapping("/remove")
    public ModelAndView removerUsuario(@ModelAttribute(USUARIO) Usuario usuario) {
        ModelAndView view = new ModelAndView("redirect:/");
        Usuario usuarioRetornado = UsuarioControllerUtil.deletaUsuario(usuario);
        FtpUtil.removeUsuario(usuarioRetornado);
        logger.info(marker, "Usuario {} removido com sucesso!", usuarioRetornado.getUsername());
        view.addObject(USUARIO, usuarioRetornado);
        return view;
    }

    @PostMapping("/search")
    public String search(@RequestParam("username") String username) {
       return "redirect:/usuario/" + username;
    }

    //    TODO arrumar esse metodo desgraçado pra fazer ele funcionar! -- Set ta vazio!
    @PostMapping("/usuario/{username}/compartilhado")
    public ModelAndView listaCompartilhamento(@PathVariable("username") String username) {
        ModelAndView view = new ModelAndView("lista_compartilhados");
        Usuario usuario = UsuarioControllerUtil.buscaUsuario(username);
        String s = UsuarioControllerUtil.preparaJsonString(usuario);
        System.out.println(s);
        Set<BasicDBObject> set = usuario.getArquivosCompartilhados();
        System.out.println(set.isEmpty());
        List<ArquivoCompartilhado> lista = new ArrayList<>();
        set.forEach(b -> lista.add(new ArquivoCompartilhado(b)));
        lista.forEach(a -> System.out.println("Arquivo compartilhado: " + a.getNome()));
        view.addObject("lista", lista);
        view.addObject(USUARIO, usuario);
        return view;
    }

}
