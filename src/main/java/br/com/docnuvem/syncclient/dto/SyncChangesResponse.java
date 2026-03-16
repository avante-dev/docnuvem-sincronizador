package br.com.docnuvem.syncclient.dto;

import lombok.Data;

import java.util.List;

@Data
public class SyncChangesResponse {

    private List<SyncEventoDTO> eventos;
    private Long ultimoEventoId;

}