package br.com.upbox.requisicoes;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public class Get implements Requisicao {
    @Override
    public HttpUriRequest prepara(String url, String usuario, String contentType) throws IOException {
        HttpGet httpGet = new HttpGet(url + "/" + usuario);
        httpGet.addHeader("content-type", contentType);
        httpGet.addHeader("accept", contentType);
        return httpGet;
    }
}
