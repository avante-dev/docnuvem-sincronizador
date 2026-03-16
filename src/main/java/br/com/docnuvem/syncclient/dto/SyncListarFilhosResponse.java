package br.com.docnuvem.syncclient.dto;

import lombok.Data;

import java.util.List;

@Data
public class SyncListarFilhosResponse {
    private Integer diretorioPaiId;
    private List<SyncTreeDiretorioDTO> diretorios;
    private List<SyncTreeDocumentoDTO> documentos;
    private long totalDiretorios;
    private long totalDocumentos;
    private int page;
    private int size;
    private boolean temMaisDiretorios;
    private boolean temMaisDocumentos;
}