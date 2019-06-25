package br.com.upbox.controller;

import br.com.upbox.models.Usuario;
import br.com.upbox.requisicoes.Delete;
import br.com.upbox.requisicoes.Get;
import br.com.upbox.requisicoes.Post;
import br.com.upbox.requisicoes.Requisicao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UsuarioController {
    private static final Logger logger = Logger.getLogger(UsuarioController.class.getName());
    private static final String URL_API = "http://localhost:9000/usuario";
    private static final String CONTENT_TYPE = "application/json";

    @GetMapping("/")
    public String index() {
        logger.log(Level.INFO, "entrando no /index");
        return "index";
    }

    @GetMapping("/cadastro")
    public ModelAndView cadastro() {
        logger.log(Level.INFO, "entrando no /cadastro");
        ModelAndView view = new ModelAndView("cadastro");
        Usuario usuario = new Usuario();
        view.addObject("usuario", usuario);
        return view;
    }

    @PostMapping("/envia_usuario")
    public ModelAndView envia_usuario(@ModelAttribute("usuario") Usuario usuario) {
        String usuarioComoString = preparaJsonString(usuario);
        Usuario usuarioRetornado = conectaComAAPI(usuarioComoString, new Post());
        logger.log(Level.INFO, "Usuario {0} adicionado com sucesso!", usuarioRetornado.getUsername());
        ModelAndView view = new ModelAndView("redirect:/usuario/" + usuario.getUsername());
        view.addObject("usuario", usuario);
        return view;
    }

    @GetMapping(value = "/usuario/{username}")
    public ModelAndView buscaUsuario(@NotNull @PathVariable("username") String username) {
        ModelAndView view = new ModelAndView("usuario");
        Usuario usuarioRetornado = conectaComAAPI(username, new Get());
        view.addObject("usuario", usuarioRetornado);
        return view;
    }

    @PostMapping("/remove")
    public ModelAndView removerUsuario(@ModelAttribute("usuario") Usuario usuario) {
        ModelAndView view = new ModelAndView("redirect:/");
        String usuarioComoString = preparaJsonString(usuario);
        logger.log(Level.INFO, "Usuario enviado: {0}", usuarioComoString);
        Usuario usuarioRetornado = conectaComAAPI(usuarioComoString, new Delete());
        logger.log(Level.INFO, "Usuario {0} removido com sucesso!", usuarioRetornado.getUsername());
        view.addObject("usuario", usuarioRetornado);
        return view;
    }

//    ****** Métodos auxiliares

    private String preparaJsonString(@ModelAttribute("usuario") Usuario usuario) {
        Document document = new Document();
        document.put("nome", usuario.getNome());
        document.put("email", usuario.getEmail());
        document.put("username", usuario.getUsername());
        document.put("senha", usuario.getSenha());
        document.put("uuid", UUID.randomUUID().toString());
        return document.toJson();
    }

    private Usuario conectaComAAPI(String usuarioComoString, Requisicao requisicao) {
        try {
            logger.log(Level.INFO, "Usuario como String: {0}", usuarioComoString);
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = requisicao.executa(URL_API, usuarioComoString, CONTENT_TYPE, httpClient);
            HttpEntity entity = response.getEntity();
            Usuario usuarioRecebido = getUsuario(entity.getContent());
            httpClient.close();
            return usuarioRecebido;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Erro: {0}", e);
            return null;
        }
    }

    private Usuario getUsuario(InputStream content) throws IOException {
        return new ObjectMapper().readValue(content, Usuario.class);
    }

}
