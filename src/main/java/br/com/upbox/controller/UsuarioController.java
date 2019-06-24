package br.com.upbox.controller;

import br.com.upbox.models.Usuario;
import org.json.JSONStringer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UsuarioController {
    private static final Logger logger = Logger.getLogger(UsuarioController.class.getName());

    @GetMapping("/")
    public String index() {
        logger.log(Level.INFO,"entrando no /index");
        return "index";
    }

    @GetMapping("/cadastro")
    public ModelAndView cadastro() {
        logger.log(Level.INFO, "entrando no /cadastro");
        List<Integer> dias = new ArrayList<>();
        for(int i = 1; i <= 31; i++) {
            dias.add(i);
        }
        List<String> mesesLista = Arrays.asList(
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
                "Outubro", "Novembro", "Dezembro");
        Map<Integer, String> meses = new HashMap<>();
        for(int i = 1; i <= 12; i++){
            meses.put(i, mesesLista.get(i-1));
        }
        ModelAndView view = new ModelAndView("cadastro");
        Usuario usuario = new Usuario();
        view.addObject("dias", dias);
        view.addObject("meses", meses);
        view.addObject("usuario", usuario);
        return view;
    }

    @PostMapping("/envia_usuario")
    public String envia_usuario(@ModelAttribute("usuario") Usuario usuario) {
        LocalDate data = LocalDate.of(usuario.getAno(), usuario.getMes(), usuario.getMes());
        usuario.setDataNascimento(data);
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object()
                .key("nome").value(usuario.getNome())
                .key("email").value(usuario.getEmail())
                .key("dataNascimento").value(usuario.getDataNascimento())
                .key("username").value(usuario.getUsername())
                .key("senha").value(usuario.getSenha())
                .endObject();
        String usuarioComoString = jsonStringer.toString();
        System.out.println(usuarioComoString);
        return "/usuario/usuario";
    }

    @GetMapping("/usuario")
    public String usuario() {
//        TODO receber o usuario e conectar com a API
        logger.log(Level.INFO, "entrando em /usuario");
        return "/usuario/usuario";
    }
}
