package br.com.docnuvem.syncclient.service;

import br.com.docnuvem.syncclient.db.repository.SyncDirRepository;
import br.com.docnuvem.syncclient.db.repository.SyncFileRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LocalMappingService {

    private final SyncDirRepository syncDirRepository;
    private final SyncFileRepository syncFileRepository;

    public LocalMappingService(SyncDirRepository syncDirRepository,
                               SyncFileRepository syncFileRepository) {
        this.syncDirRepository = syncDirRepository;
        this.syncFileRepository = syncFileRepository;
    }

    public void salvarDiretorio(String localPath,
                                Integer remoteDirId,
                                Integer parentRemoteDirId,
                                String nome,
                                String remotoCaminho,
                                String remotoUltimaAlteracao) {
        syncDirRepository.upsert(
                localPath,
                remoteDirId,
                parentRemoteDirId,
                nome,
                remotoCaminho,
                Instant.now().toString(),
                remotoUltimaAlteracao,
                "SYNCED"
        );
    }

    public void salvarArquivo(String localPath,
                              Integer remoteDocumentId,
                              Integer remoteDirId,
                              String nome,
                              String extensao,
                              Long tamanho,
                              String hashLocal,
                              String hashRemoto,
                              String lastModifiedLocal,
                              String lastModifiedRemoto,
                              String status,
                              String ultimoErro) {
        syncFileRepository.upsert(
                localPath,
                remoteDocumentId,
                remoteDirId,
                nome,
                extensao,
                tamanho,
                hashLocal,
                hashRemoto,
                lastModifiedLocal,
                lastModifiedRemoto,
                status,
                ultimoErro
        );
    }
}