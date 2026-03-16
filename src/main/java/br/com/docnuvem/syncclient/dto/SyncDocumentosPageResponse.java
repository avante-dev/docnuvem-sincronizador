package br.com.docnuvem.syncclient.dto;

import lombok.Data;

import java.util.List;

@Data
public class SyncDocumentosPageResponse {

    private Integer diretorioId;
    private List<SyncTreeDocumentoDTO> documentos;
    private long total;
    private int page;
    private int size;
    private boolean temMais;

}
