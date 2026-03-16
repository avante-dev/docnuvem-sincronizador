package br.com.docnuvem.syncclient.service;

import br.com.docnuvem.syncclient.dto.SyncDocumentoInfoDTO;
import br.com.docnuvem.syncclient.http.DocnuvemSyncApiClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class SyncDownloadService {

    private final DocnuvemSyncApiClient apiClient;
    private final OkHttpClient httpClient;
    private final LocalMappingService localMappingService;

    public SyncDownloadService(DocnuvemSyncApiClient apiClient,
                               OkHttpClient httpClient,
                               LocalMappingService localMappingService) {
        this.apiClient = apiClient;
        this.httpClient = httpClient;
        this.localMappingService = localMappingService;
    }

    public void baixarDocumentoSeNecessario(Integer documentoId, Path destinoArquivo) {
        try {
            SyncDocumentoInfoDTO info = apiClient.buscarDocumentoInfo(documentoId);

            boolean precisaBaixar = true;

            if (Files.exists(destinoArquivo)) {
                long tamanhoLocal = Files.size(destinoArquivo);
                if (info.getTamanho() != null && tamanhoLocal == info.getTamanho()) {
                    precisaBaixar = false;
                }
            }

            if (!precisaBaixar) {
                localMappingService.salvarArquivo(
                        destinoArquivo.toString(),
                        info.getId(),
                        info.getDiretorioId(),
                        info.getNome(),
                        info.getExtensao(),
                        info.getTamanho(),
                        null,
                        info.getHash(),
                        Files.exists(destinoArquivo) ? Files.getLastModifiedTime(destinoArquivo).toString() : null,
                        info.getUltimaAlteracao(),
                        "SYNCED",
                        null
                );
                return;
            }

            Files.createDirectories(destinoArquivo.getParent());

            Request request = new Request.Builder()
                    .url(info.getDownloadUrl())
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Falha ao baixar documento. HTTP " + response.code());
                }

                if (response.body() == null) {
                    throw new RuntimeException("Resposta sem corpo no download");
                }

                try (InputStream in = response.body().byteStream()) {
                    Files.copy(in, destinoArquivo, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            localMappingService.salvarArquivo(
                    destinoArquivo.toString(),
                    info.getId(),
                    info.getDiretorioId(),
                    info.getNome(),
                    info.getExtensao(),
                    info.getTamanho(),
                    null,
                    info.getHash(),
                    Files.getLastModifiedTime(destinoArquivo).toString(),
                    info.getUltimaAlteracao(),
                    "SYNCED",
                    null
            );

        } catch (Exception e) {
            localMappingService.salvarArquivo(
                    destinoArquivo.toString(),
                    documentoId,
                    null,
                    destinoArquivo.getFileName().toString(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "ERROR",
                    e.getMessage()
            );
            throw new RuntimeException("Erro ao baixar documento " + documentoId + ": " + e.getMessage(), e);
        }
    }
}