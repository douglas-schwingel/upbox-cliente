package br.com.upbox.controller;

import br.com.upbox.ftp.FtpConnectionFactory;
import br.com.upbox.ftp.FtpUtil;
import br.com.upbox.models.Arquivo;
import br.com.upbox.models.Usuario;
import br.com.upbox.requisicoes.Delete;
import br.com.upbox.requisicoes.Get;
import br.com.upbox.requisicoes.Post;
import br.com.upbox.requisicoes.Requisicao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private static final Marker marker = MarkerFactory.getMarker("usuario-controller");
    private static final String URL_API = "http://localhost:9000/usuario";
    private static final String CONTENT_TYPE = "application/json";

    private FtpConnectionFactory fsf;

    @GetMapping("/")
    public String index() {
        logger.info(marker, "entrando no /index");
        return "index";
    }

    @GetMapping("/cadastro")
    public ModelAndView cadastro() {
        logger.info(marker, "entrando no /cadastro");
        ModelAndView view = new ModelAndView("cadastro");
        Usuario usuario = new Usuario();
        view.addObject("usuario", usuario);
        return view;
    }

    @PostMapping("/envia_usuario")
    public ModelAndView enviaUsuario(@ModelAttribute("usuario") Usuario usuario) {
        String usuarioComoString = preparaJsonString(usuario);
        conectaComAAPI(usuarioComoString, new Post());
        Usuario usuarioRetornado = conectaComAAPI(usuario.getUsername(), new Get());
        FtpUtil.getArquivos(usuarioRetornado);
        logger.info(marker, "Usuario {} adicionado com sucesso!", usuarioRetornado.getUsername());
        ModelAndView view = new ModelAndView("redirect:/usuario/" + usuario.getUsername());
        view.addObject("usuario", usuario);
        return view;
    }

    @GetMapping(value = "/usuario/{username}")
    public ModelAndView buscaUsuario(@NotNull @PathVariable("username") String username) {
        ModelAndView view = new ModelAndView("usuario");
        Usuario usuarioRetornado = conectaComAAPI(username, new Get());
        List<Arquivo> listaDeArquivos = FtpUtil.getArquivos(usuarioRetornado);
        view.addObject("lista", listaDeArquivos);
        view.addObject("usuario", usuarioRetornado);
        return view;
    }

    @PostMapping("/remove")
    public ModelAndView removerUsuario(@ModelAttribute("usuario") Usuario usuario) {
        ModelAndView view = new ModelAndView("redirect:/");
        String usuarioComoString = preparaJsonString(usuario);
        Usuario usuarioRetornado = conectaComAAPI(usuarioComoString, new Delete());
        FtpUtil.removeUsuario(usuarioRetornado);
        logger.info(marker, "Usuario {} removido com sucesso!", usuarioRetornado.getUsername());
        view.addObject("usuario", usuarioRetornado);
        return view;
    }

    @PostMapping("/envia_arquivo")
    public String enviaArquivos(@RequestParam("arquivo") MultipartFile arquivo,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) {
        try {
            boolean sucesso = FtpUtil.storeFiles(arquivo, username, password);
            if (sucesso) {
                logger.info(marker, "Arquivo inserido");
            }
        } catch (IOException e) {
            logger.error(marker, "Erro ao enviar o arquivo: {}", arquivo.getOriginalFilename());
            e.printStackTrace();
        }
        return "redirect:/usuario/" + username;
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
            logger.info(marker, "Usuario como String: {}", usuarioComoString);
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = requisicao.executa(URL_API, usuarioComoString, CONTENT_TYPE, httpClient);
            HttpEntity entity = response.getEntity();
            Usuario usuarioRecebido = getUsuario(entity.getContent());
            httpClient.close();
            return usuarioRecebido;
        } catch (IOException e) {
            logger.error(marker, "Erro: {}", e);
            return null;
        }
    }

    private Usuario getUsuario(InputStream content) throws IOException {
        return new ObjectMapper().readValue(content, Usuario.class);
    }



}
