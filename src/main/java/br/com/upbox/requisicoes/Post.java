package br.com.upbox.requisicoes;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class Post implements Requisicao {
    @Override
    public HttpUriRequest prepara(String url, String usuario, String contentType) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity params = new StringEntity(usuario);
        httpPost.addHeader("content-type", contentType);
        httpPost.addHeader("accept", contentType);
        httpPost.setEntity(params);
        return httpPost;
    }
}
