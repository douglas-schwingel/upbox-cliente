package br.com.upbox.models;

import org.apache.commons.io.FilenameUtils;
import org.bson.Document;


public class Arquivo {

    private String nome;
    private String type;
    private String nomeInteiro;
    private String dono;

    public Arquivo(String arquivoOriginal, String username) {
        this.nomeInteiro = arquivoOriginal;
        this.nome = FilenameUtils.getBaseName(nomeInteiro);
        this.type = FilenameUtils.getExtension(nomeInteiro);
        this.dono = username;
    }

    public Arquivo(Document document) {
        this.nomeInteiro = document.getString("arquivo");
        this.nome = FilenameUtils.getBaseName(nomeInteiro);
        this.type = FilenameUtils.getExtension(nomeInteiro);
        String owner = document.getString("owner");
        if (owner == null) {
            this.dono = document.getString("destinatario");
        } else {
            this.dono = document.getString("owner");
        }
    }

    protected Arquivo() {
    }

    public String getNomeInteiro() {
        return nomeInteiro;
    }

    public String getNome() {
        return nome;
    }

    public String getType() {
        return type;
    }

    public String getDono() {
        return dono;
    }
}
