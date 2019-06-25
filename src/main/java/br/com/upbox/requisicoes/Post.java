package br.com.upbox.requisicoes;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class Post implements Requisicao {
    @Override
    public CloseableHttpResponse executa(String url, String usuarioComoString, String contentType, CloseableHttpClient httpClient) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity params = new StringEntity(usuarioComoString);
        httpPost.addHeader("content-type", contentType);
        httpPost.addHeader("accept", contentType);
        httpPost.setEntity(params);
        return httpClient.execute(httpPost);
    }
}
