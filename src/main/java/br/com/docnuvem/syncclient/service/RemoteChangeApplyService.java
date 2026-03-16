package br.com.docnuvem.syncclient.service;

import br.com.docnuvem.syncclient.db.repository.SyncDirRepository;
import br.com.docnuvem.syncclient.db.repository.SyncFileRepository;
import br.com.docnuvem.syncclient.dto.SyncEventoDTO;
import br.com.docnuvem.syncclient.util.FileTreeUtil;
import br.com.docnuvem.syncclient.util.LocalPathUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RemoteChangeApplyService {

    private final SyncDirRepository syncDirRepository;
    private final SyncFileRepository syncFileRepository;
    private final LocalMappingService localMappingService;
    private final SyncDownloadService syncDownloadService;

    public RemoteChangeApplyService(SyncDirRepository syncDirRepository,
                                    SyncFileRepository syncFileRepository,
                                    LocalMappingService localMappingService,
                                    SyncDownloadService syncDownloadService) {
        this.syncDirRepository = syncDirRepository;
        this.syncFileRepository = syncFileRepository;
        this.localMappingService = localMappingService;
        this.syncDownloadService = syncDownloadService;
    }

    public void aplicarEventos(List<SyncEventoDTO> eventos) {
        if (eventos == null || eventos.isEmpty()) {
            return;
        }

        for (SyncEventoDTO ev : eventos) {
            if ("DIRETORIO".equalsIgnoreCase(ev.getTipoEntidade())) {
                aplicarEventoDiretorio(ev);
            } else if ("DOCUMENTO".equalsIgnoreCase(ev.getTipoEntidade())) {
                aplicarEventoDocumento(ev);
            }
        }
    }

    private void aplicarEventoDiretorio(SyncEventoDTO ev) {
        if ("DELETED".equalsIgnoreCase(ev.getTipoEvento())) {
            String localPath = syncDirRepository.findLocalPathByRemoteDirId(ev.getEntidadeId());
            if (localPath != null) {
                try {
                    FileTreeUtil.deleteRecursively(Paths.get(localPath));
                } catch (Exception ignored) {
                }
            }
            return;
        }

        String localParent = ev.getDiretorioPaiId() != null
                ? syncDirRepository.findLocalPathByRemoteDirId(ev.getDiretorioPaiId())
                : null;

        if (localParent == null) {
            return;
        }

        Path localDir = LocalPathUtil.resolveChild(Paths.get(localParent), ev.getNome());

        try {
            Files.createDirectories(localDir);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar diretório local para evento remoto: " + ev.getNome(), e);
        }

        localMappingService.salvarDiretorio(
                localDir.toString(),
                ev.getEntidadeId(),
                ev.getDiretorioPaiId(),
                ev.getNome(),
                ev.getCaminhoNomesPais(),
                ev.getUltimaAlteracao()
        );
    }

    private void aplicarEventoDocumento(SyncEventoDTO ev) {
        if ("DELETED".equalsIgnoreCase(ev.getTipoEvento())) {
            String localPath = syncFileRepository.findLocalPathByRemoteDocumentId(ev.getEntidadeId());
            if (localPath != null) {
                try {
                    Files.deleteIfExists(Paths.get(localPath));
                } catch (Exception ignored) {
                }
            }
            return;
        }

        if (ev.getDiretorioId() == null) {
            return;
        }

        String localParent = syncDirRepository.findLocalPathByRemoteDirId(ev.getDiretorioId());
        if (localParent == null) {
            return;
        }

        Path localFile = LocalPathUtil.resolveChild(Paths.get(localParent), ev.getNome());
        syncDownloadService.baixarDocumentoSeNecessario(ev.getEntidadeId(), localFile);
    }

}