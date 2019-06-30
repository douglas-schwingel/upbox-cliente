package br.com.upbox.models;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPFile;


public class ArquivoVazio extends Arquivo {

    private String nome;
    private long tamanho;
    private String type;
    private String owner;
    private FTPFile arquivoOriginal;

    public ArquivoVazio() {
        this.arquivoOriginal = arquivoOriginal;
        this.nome = "";
        this.type = "";
        this.tamanho = 0;
        this.owner = "";
    }

    public String getNome() {
        return nome;
    }

    public long getTamanho() {
        return tamanho;
    }

    public String getType() {
        return type;
    }

    public FTPFile getArquivoOriginal() {
        return arquivoOriginal;
    }

    public String getOwner() {
        return owner;
    }
}
