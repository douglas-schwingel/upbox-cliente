package br.com.upbox.models;

public class ArquivoVazio extends Arquivo {

    private String nome;
    private long tamanho;
    private String type;
    private String owner;

    public ArquivoVazio() {
        this.nome = "";
        this.type = "";
        this.tamanho = 0;
        this.owner = "";
    }

    @Override
    public String getNome() {
        return nome;
    }

    public long getTamanhoFormatado() {
        return tamanho;
    }

    @Override
    public String getType() {
        return type;
    }


    @Override
    public String getOwner() {
        return owner;
    }
}
