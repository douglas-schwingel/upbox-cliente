package br.com.upbox.models;

import org.apache.commons.io.FilenameUtils;
import org.bson.Document;

public class ArquivoCompartilhado {

    private String nome;
    private String nomeInteiro;
    private String formato;
    private String dono;

    public ArquivoCompartilhado(Document basic) {
        this.nomeInteiro = basic.getString("arquivo");
        this.nome = FilenameUtils.getBaseName(nomeInteiro);
        this.formato = FilenameUtils.getExtension(nomeInteiro);
        this.dono = basic.getString("owner");
    }

    public String getNome() {
        return nome;
    }

    public String getNomeInteiro() {
        return nomeInteiro;
    }


    public String getFormato() {
        return formato;
    }

    public String getDono() {
        return dono;
    }
}
