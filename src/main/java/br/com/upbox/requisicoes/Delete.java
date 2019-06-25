package br.com.upbox.requisicoes;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class Delete implements Requisicao {
    @Override
    public CloseableHttpResponse executa(String url, String usuario, String contentType, CloseableHttpClient httpClient) throws IOException {
        DeleteWithBody httpDelete = new DeleteWithBody(url);
        StringEntity params = new StringEntity(usuario);
        httpDelete.addHeader("content-type", contentType);
        httpDelete.addHeader("accept", contentType);
        httpDelete.setEntity(params);
        return httpClient.execute(httpDelete);
    }
}
