package br.com.docnuvem.syncclient.dto;

import lombok.Data;

@Data
public class SyncEventoDTO {

    private Long id;
    private String tipoEntidade;
    private Integer entidadeId;
    private String tipoEvento;
    private Integer diretorioId;
    private Integer diretorioPaiId;
    private String nome;
    private String extensao;
    private Long tamanho;
    private String hash;
    private Boolean lixeira;
    private String caminho;
    private String caminhoIdsPais;
    private String caminhoNomesPais;
    private String ultimaAlteracao;
    private String dataEvento;

}