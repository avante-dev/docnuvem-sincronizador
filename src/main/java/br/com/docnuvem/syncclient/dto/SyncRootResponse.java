package br.com.docnuvem.syncclient.dto;

import lombok.Data;

@Data
public class SyncRootResponse {
    private Integer id;
    private String nome;
    private String caminhoNomesPais;
    private String caminhoIdsPais;
    private Long ultimoEventoId;
}