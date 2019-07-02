package br.com.upbox.requisicoes;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class Share implements Requisicao{
    @Override
    public CloseableHttpResponse executa(String url, String usuario, String contentType, CloseableHttpClient httpClient) throws IOException {
        HttpPost httpPost = new HttpPost(url + "/compartilha");
        StringEntity params = new StringEntity(usuario);
        httpPost.addHeader("content-type", contentType);
        httpPost.addHeader("accept", contentType);
        httpPost.setEntity(params);
        return httpClient.execute(httpPost);
    }
}
