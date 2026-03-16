package br.com.docnuvem.syncclient.service;

import br.com.docnuvem.syncclient.config.AppProperties;
import br.com.docnuvem.syncclient.db.repository.SyncConfigRepository;
import br.com.docnuvem.syncclient.dto.SyncChangesResponse;
import br.com.docnuvem.syncclient.http.DocnuvemSyncApiClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SyncChangePollingService {

    private final AppProperties props;
    private final SyncConfigRepository syncConfigRepository;
    private final DocnuvemSyncApiClient apiClient;
    private final RemoteChangeApplyService remoteChangeApplyService;

    public SyncChangePollingService(AppProperties props,
                                    SyncConfigRepository syncConfigRepository,
                                    DocnuvemSyncApiClient apiClient,
                                    RemoteChangeApplyService remoteChangeApplyService) {
        this.props = props;
        this.syncConfigRepository = syncConfigRepository;
        this.apiClient = apiClient;
        this.remoteChangeApplyService = remoteChangeApplyService;
    }

    @Scheduled(fixedDelayString = "#{${docnuvem.polling-segundos:30} * 1000}")
    public void pollarAlteracoes() {
        try {
            String ultimoEventoIdStr = syncConfigRepository.buscar("ultimoEventoId");
            long ultimoEventoId = ultimoEventoIdStr != null ? Long.parseLong(ultimoEventoIdStr) : 0L;

            SyncChangesResponse response = apiClient.buscarAlteracoes(ultimoEventoId, props.getDiretorioRaizId());

            if (response.getEventos() != null && !response.getEventos().isEmpty()) {
                remoteChangeApplyService.aplicarEventos(response.getEventos());
            }

            if (response.getUltimoEventoId() != null && response.getUltimoEventoId() > ultimoEventoId) {
                syncConfigRepository.salvar("ultimoEventoId", String.valueOf(response.getUltimoEventoId()));
            }
        } catch (Exception e) {
            System.err.println("Erro no polling de alterações: " + e.getMessage());
            e.printStackTrace();
        }
    }
}