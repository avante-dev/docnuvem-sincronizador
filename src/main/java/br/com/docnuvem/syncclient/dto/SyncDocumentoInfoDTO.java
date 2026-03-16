package br.com.docnuvem.syncclient.dto;

import lombok.Data;

@Data
public class SyncDocumentoInfoDTO {
    private Integer id;
    private String nome;
    private String extensao;
    private String contentType;
    private Long tamanho;
    private Integer diretorioId;
    private String hash;
    private Integer documentoVersaoId;
    private String dataEnvio;
    private String ultimaAlteracao;
    private String downloadUrl;
}