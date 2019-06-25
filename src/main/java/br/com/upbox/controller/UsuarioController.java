package br.com.upbox.controller;

import br.com.upbox.models.Usuario;
import br.com.upbox.requisicoes.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UsuarioController {
    private static final Logger logger = Logger.getLogger(UsuarioController.class.getName());
    public static final String URL_API = "http://localhost:9000/usuario";
    public static final String CONTENT_TYPE = "application/json";
    private CloseableHttpClient httpClient;

    @GetMapping("/")
    public String index() {
        logger.log(Level.INFO, "entrando no /index");
        return "index";
    }

    @GetMapping("/cadastro")
    public ModelAndView cadastro() {
        logger.log(Level.INFO, "entrando no /cadastro");
//        List<Integer> dias = new ArrayList<>();
//        for (int i = 1; i <= 31; i++) {
//            dias.add(i);
//        }
//        List<String> mesesLista = Arrays.asList(
//                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
//                "Outubro", "Novembro", "Dezembro");
//        Map<Integer, String> meses = new HashMap<>();
//        for (int i = 1; i <= 12; i++) {
//            meses.put(i, mesesLista.get(i - 1));
//        }
        ModelAndView view = new ModelAndView("cadastro");
        Usuario usuario = new Usuario();
        view.addObject("usuario", usuario);
        return view;
    }

    @PostMapping("/envia_usuario")
    public String envia_usuario(@ModelAttribute("usuario") Usuario usuario) {
        String usuarioComoString = preparaJsonString(usuario);
        conectaComAAPI(usuarioComoString);

        return "/usuario/usuario";
    }

    @GetMapping("/usuario")
    public String usuario() {
//        TODO receber o usuario e conectar com a API
        logger.log(Level.INFO, "entrando em /usuario");
        return "/usuario/usuario";
    }

    private String preparaJsonString(@ModelAttribute("usuario") Usuario usuario) {
        Document document = new Document();
        document.put("nome", usuario.getNome());
        document.put("email", usuario.getEmail());
        document.put("username", usuario.getUsername());
        document.put("senha", usuario.getSenha());
        document.put("uuid", UUID.randomUUID().toString());
        return document.toJson();
    }

    private void conectaComAAPI(String usuarioComoString) {
        try {
            logger.log(Level.INFO, "Usuario como String: {0}", usuarioComoString);
            httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = new Post().executa(URL_API, usuarioComoString, CONTENT_TYPE, httpClient);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            Usuario usuario1 = new ObjectMapper().readValue(content, Usuario.class);
            logger.log(Level.INFO,usuario1.getNome() + " | " + usuario1.getUsername());
            httpClient.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Erro: {0}", e);
        }
    }

}
