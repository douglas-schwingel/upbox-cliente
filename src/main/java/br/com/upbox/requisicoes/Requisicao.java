package br.com.upbox.requisicoes;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public interface Requisicao {
    CloseableHttpResponse executa(String url, String usuario, String contentType, CloseableHttpClient httpClient) throws IOException;
}
