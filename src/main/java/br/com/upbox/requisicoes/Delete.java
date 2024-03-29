package br.com.upbox.requisicoes;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class Delete implements Requisicao {
    @Override
    public HttpUriRequest prepara(String url, String usuario, String contentType) throws IOException {
        DeleteWithBody httpDelete = new DeleteWithBody(url);
        StringEntity params = new StringEntity(usuario);
        httpDelete.addHeader("content-type", contentType);
        httpDelete.addHeader("accept", contentType);
        httpDelete.setEntity(params);
        return httpDelete;
    }
}
