package br.com.upbox.requisicoes;

import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public interface Requisicao {
    HttpUriRequest prepara(String url, String usuario, String contentType) throws IOException;
}
