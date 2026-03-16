package br.com.docnuvem.syncclient.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SyncTreeDocumentoDTO {

    private Integer id;
    private String nome;
    private String extensao;
    private String contentType;
    private Long tamanho;
    private Integer diretorioId;
    private Boolean lixeira;
    private String hash;
    private Timestamp dataEnvio;
    private Timestamp ultimaAlteracao;
    private Integer documentoVersaoId;

}
