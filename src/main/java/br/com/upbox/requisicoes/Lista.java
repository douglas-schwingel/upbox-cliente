package br.com.upbox.requisicoes;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class Lista implements Requisicao{
    @Override
    public HttpUriRequest prepara(String url, String usuario, String contentType) throws IOException {
        HttpPost lista = new HttpPost(url + "/compartilhados");
        StringEntity params = new StringEntity(usuario);
        lista.addHeader("content-type", contentType);
        lista.addHeader("accept", contentType);
        lista.setEntity(params);
        return lista;
    }
}
