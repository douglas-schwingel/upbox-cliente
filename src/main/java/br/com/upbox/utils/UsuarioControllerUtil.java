package br.com.upbox.utils;

import br.com.upbox.models.Arquivo;
import br.com.upbox.models.Usuario;
import br.com.upbox.requisicoes.Delete;
import br.com.upbox.requisicoes.Get;
import br.com.upbox.requisicoes.Post;
import br.com.upbox.requisicoes.Requisicao;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioControllerUtil {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioControllerUtil.class);
    private static final Marker marker = MarkerFactory.getMarker("usuario-utils");
    private static final String URL_API = "http://localhost:9000/usuario";
    private static final String CONTENT_TYPE = "application/json";

    private UsuarioControllerUtil() {

    }

    public static Usuario buscaUsuario(String username) {
        InputStream content = conectaComAAPI(username, new Get());
        try {
            return getUsuario(content);
        } catch (IOException e) {
            logger.error(marker, "erro ao buscar usuario {}", username);
        }
        return null;
    }

    public static void enviaUsuario(Usuario usuario) {
        String usuarioJson = preparaJsonString(usuario);
        conectaComAAPI(usuarioJson, new Post());
    }

    public static Usuario deletaUsuario(Usuario usuario) {
        String usuarioJson = preparaJsonString(usuario);
        InputStream content = conectaComAAPI(usuarioJson, new Delete());
        try {
            return getUsuario(content);
        } catch (IOException e) {
            logger.error(marker, "Erro {} ao buscar usuario {}", e.getMessage(), usuario.getUsername());
        }
        return null;
    }

    private static InputStream conectaComAAPI(String usuarioComoString, Requisicao requisicao) {
        try {
            logger.info(marker, "Usuario como String: {}", usuarioComoString);
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpUriRequest request = requisicao.prepara(URL_API, usuarioComoString, CONTENT_TYPE);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            return entity.getContent();
        } catch (IOException e) {
            logger.error(marker, "Erro: {}", e.getMessage());
            return null;
        }
    }

    private static Usuario getUsuario(InputStream content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        return objectMapper.readValue(content, Usuario.class);
    }

    private static String preparaJsonString(@ModelAttribute("usuario") Usuario usuario) {
        Document document = new Document();
        document.put("nome", usuario.getNome());
        document.put("email", usuario.getEmail());
        document.put("username", usuario.getUsername());
        document.put("senha", usuario.getSenha());
        document.put("uuid", UUID.randomUUID().toString());
        document.put("compartilhadosComigo", usuario.getCompartilhadosComigo());
        document.put("compartilheiCom", usuario.getCompartilheiCom());
        return document.toJson();
    }

    public static List<Arquivo> populaListaArquivos(Usuario usuario, String compartilhamento) {
        List<Arquivo> listaArquivo = new ArrayList<>();
        List<Document> lista;
        if (compartilhamento.equals("compartilhado")) {
            lista = usuario.getCompartilhadosComigo();
        } else {
            lista = usuario.getCompartilheiCom();
        }
        lista.forEach(b -> listaArquivo.add(new Arquivo(b)));
        listaArquivo.forEach(a -> System.out.println("Arquivo compartilhado: " + a.getNome()));
        return listaArquivo;
    }
}
