package br.com.upbox.models;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPFile;


public class Arquivo {

    private String nome;
    private long tamanho;
    private String type;
    private String owner;
    private FTPFile arquivoOriginal;

    public Arquivo(FTPFile arquivoOriginal) {
        this.arquivoOriginal = arquivoOriginal;
        String nomeInteiro = arquivoOriginal.getName();
        this.nome = FilenameUtils.getBaseName(nomeInteiro);
        this.type = FilenameUtils.getExtension(nomeInteiro);
        this.tamanho = arquivoOriginal. getSize() / 1048576;
        this.owner = arquivoOriginal.getUser();
    }

    protected Arquivo() {
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
