package br.com.docnuvem.syncclient.service;

import br.com.docnuvem.syncclient.dto.SyncDiretoriosPageResponse;
import br.com.docnuvem.syncclient.dto.SyncDocumentosPageResponse;
import br.com.docnuvem.syncclient.dto.SyncTreeDiretorioDTO;
import br.com.docnuvem.syncclient.dto.SyncTreeDocumentoDTO;
import br.com.docnuvem.syncclient.http.DocnuvemSyncApiClient;
import br.com.docnuvem.syncclient.util.LocalPathUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SyncBrowseService {

    private final DocnuvemSyncApiClient apiClient;
    private final LocalMappingService localMappingService;
    private final SyncDownloadService syncDownloadService;

    public SyncBrowseService(DocnuvemSyncApiClient apiClient,
                             LocalMappingService localMappingService,
                             SyncDownloadService syncDownloadService) {
        this.apiClient = apiClient;
        this.localMappingService = localMappingService;
        this.syncDownloadService = syncDownloadService;
    }

    public void sincronizarSubArvore(Integer remoteDirId, Path localParentPath) {
        sincronizarDiretorios(remoteDirId, localParentPath);
        sincronizarDocumentos(remoteDirId, localParentPath);
    }

    private void sincronizarDiretorios(Integer remoteDirId, Path localParentPath) {
        int page = 0;
        int size = 200;

        while (true) {
            SyncDiretoriosPageResponse response = apiClient.listarDiretoriosFilhos(remoteDirId, page, size);

            if (response.getDiretorios() != null) {
                for (SyncTreeDiretorioDTO dir : response.getDiretorios()) {
                    Path localDir = LocalPathUtil.resolveChild(localParentPath, dir.getNome());

                    try {
                        Files.createDirectories(localDir);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao criar diretório local " + localDir + ": " + e.getMessage(), e);
                    }

                    localMappingService.salvarDiretorio(
                            localDir.toString(),
                            dir.getId(),
                            dir.getDiretorioPaiId(),
                            dir.getNome(),
                            dir.getCaminhoNomesPais(),
                            dir.getUltimaAlteracao()
                    );

                    sincronizarSubArvore(dir.getId(), localDir);
                }
            }

            if (!response.isTemMais()) {
                break;
            }

            page++;
        }
    }

    private void sincronizarDocumentos(Integer remoteDirId, Path localParentPath) {
        int page = 0;
        int size = 200;

        while (true) {
            SyncDocumentosPageResponse response = apiClient.listarDocumentosDiretorio(remoteDirId, page, size);

            if (response.getDocumentos() != null) {
                for (SyncTreeDocumentoDTO doc : response.getDocumentos()) {
                    Path localFile = LocalPathUtil.resolveChild(localParentPath, doc.getNome());
                    syncDownloadService.baixarDocumentoSeNecessario(doc.getId(), localFile);
                }
            }

            if (!response.isTemMais()) {
                break;
            }

            page++;
        }
    }
}