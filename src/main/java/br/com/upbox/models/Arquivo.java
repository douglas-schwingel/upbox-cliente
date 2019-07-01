package br.com.upbox.models;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPFile;


public class Arquivo {

    private String nome;
    private String tamanho;
    private String type;
    private String owner;
    private String nomeInteiro;
    private FTPFile arquivoOriginal;

    public Arquivo(FTPFile arquivoOriginal) {
        this.arquivoOriginal = arquivoOriginal;
        this.nomeInteiro = arquivoOriginal.getName();
        this.nome = FilenameUtils.getBaseName(nomeInteiro);
        this.type = FilenameUtils.getExtension(nomeInteiro);
        this.tamanho = getTamanhoFormatado(arquivoOriginal.getSize());
        this.owner = arquivoOriginal.getUser();
    }

    private String getTamanhoFormatado(long size) {
        if ((size / 1048576) < 1) {
            return (size / 1024) + " kB";
        }
        return size / 1048576 + " mB";
    }

    protected Arquivo() {
    }

    public String getNomeInteiro() {
        return nomeInteiro;
    }

    public String getNome() {
        return nome;
    }

    public String getTamanho() {
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
