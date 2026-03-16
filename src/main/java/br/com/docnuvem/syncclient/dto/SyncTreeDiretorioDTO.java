package br.com.docnuvem.syncclient.dto;

import lombok.Data;

@Data
public class SyncTreeDiretorioDTO {
    private Integer id;
    private String nome;
    private Integer diretorioPaiId;
    private String caminhoNomesPais;
    private String caminhoIdsPais;
    private Boolean lixeira;
    private String ultimaAlteracao;
}