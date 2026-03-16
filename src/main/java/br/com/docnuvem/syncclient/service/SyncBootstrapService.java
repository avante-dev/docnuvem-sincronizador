package br.com.docnuvem.syncclient.service;

import br.com.docnuvem.syncclient.config.AppProperties;
import br.com.docnuvem.syncclient.db.repository.SyncConfigRepository;
import br.com.docnuvem.syncclient.dto.SyncRootResponse;
import br.com.docnuvem.syncclient.http.DocnuvemSyncApiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SyncBootstrapService {

    private final AppProperties props;
    private final DocnuvemSyncApiClient apiClient;
    private final SyncConfigRepository syncConfigRepository;
    private final LocalMappingService localMappingService;
    private final SyncBrowseService syncBrowseService;

    public SyncBootstrapService(AppProperties props,
                                DocnuvemSyncApiClient apiClient,
                                SyncConfigRepository syncConfigRepository,
                                LocalMappingService localMappingService,
                                SyncBrowseService syncBrowseService) {
        this.props = props;
        this.apiClient = apiClient;
        this.syncConfigRepository = syncConfigRepository;
        this.localMappingService = localMappingService;
        this.syncBrowseService = syncBrowseService;
    }

    @PostConstruct
    public void iniciar() {
        try {
            Path raizLocalBase = Paths.get(props.getPastaLocalRaiz());
            Files.createDirectories(raizLocalBase);

            SyncRootResponse root = apiClient.carregarRaiz();

            syncConfigRepository.salvar("remoteRootId", String.valueOf(root.getId()));
            syncConfigRepository.salvar("remoteRootNome", root.getNome());
            syncConfigRepository.salvar("ultimoEventoId", String.valueOf(root.getUltimoEventoId()));

            Path pastaRaizSync = raizLocalBase.resolve(root.getNome());
            Files.createDirectories(pastaRaizSync);

            localMappingService.salvarDiretorio(
                    pastaRaizSync.toString(),
                    root.getId(),
                    null,
                    root.getNome(),
                    root.getCaminhoNomesPais(),
                    null
            );

            syncBrowseService.sincronizarSubArvore(root.getId(), pastaRaizSync);

            System.out.println("Sincronização inicial concluída. Pasta raiz local: " + pastaRaizSync);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao iniciar bootstrap de sincronização: " + e.getMessage(), e);
        }
    }
}