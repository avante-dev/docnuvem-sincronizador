package br.com.docnuvem.syncclient.dto;

import lombok.Data;

import java.util.List;

@Data
public class SyncDiretoriosPageResponse {

    private Integer diretorioPaiId;
    private List<SyncTreeDiretorioDTO> diretorios;
    private long total;
    private int page;
    private int size;
    private boolean temMais;

}
