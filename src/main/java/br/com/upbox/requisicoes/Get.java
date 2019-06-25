package br.com.upbox.requisicoes;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class Get implements Requisicao {
    @Override
    public CloseableHttpResponse executa(String url, String usuario, String contentType, CloseableHttpClient httpClient) throws IOException {
        HttpGet httpDelete = new HttpGet(url + "/" + usuario);
        httpDelete.addHeader("content-type", contentType);
        httpDelete.addHeader("accept", contentType);
        return httpClient.execute(httpDelete);
    }
}
