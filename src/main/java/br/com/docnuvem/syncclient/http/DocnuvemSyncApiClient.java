package br.com.docnuvem.syncclient.http;

import br.com.docnuvem.syncclient.config.AppProperties;
import br.com.docnuvem.syncclient.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DocnuvemSyncApiClient {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final AppProperties props;

    public DocnuvemSyncApiClient(OkHttpClient httpClient, AppProperties props) {
        this.httpClient = httpClient;
        this.props = props;
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    public SyncRootResponse carregarRaiz() {
        Request request = new Request.Builder()
                .url(props.getApiBaseUrl() + "/sync/treeRoot?instancia=" + props.getInstancia()
                        + "&diretorioRaizId=" + props.getDiretorioRaizId())
                .addHeader("Authorization", "Bearer " + props.getToken())
                .post(RequestBody.create(new byte[0]))
                .build();

        return executeJson(request, SyncRootResponse.class);
    }

    public SyncDocumentoInfoDTO buscarDocumentoInfo(Integer documentoId) {
        Request request = new Request.Builder()
                .url(props.getApiBaseUrl() + "/sync/documentoInfo?instancia=" + props.getInstancia()
                        + "&documentoId=" + documentoId)
                .addHeader("Authorization", "Bearer " + props.getToken())
                .post(RequestBody.create(new byte[0]))
                .build();

        return executeJson(request, SyncDocumentoInfoDTO.class);
    }

    public SyncDiretoriosPageResponse listarDiretoriosFilhos(Integer diretorioPaiId, int page, int size) {
        Request request = new Request.Builder()
                .url(props.getApiBaseUrl() + "/sync/listarDiretoriosFilhos?instancia=" + props.getInstancia()
                        + "&diretorioPaiId=" + diretorioPaiId
                        + "&page=" + page
                        + "&size=" + size)
                .addHeader("Authorization", "Bearer " + props.getToken())
                .post(RequestBody.create(new byte[0]))
                .build();

        return executeJson(request, SyncDiretoriosPageResponse.class);
    }

    public SyncDocumentosPageResponse listarDocumentosDiretorio(Integer diretorioId, int page, int size) {
        Request request = new Request.Builder()
                .url(props.getApiBaseUrl() + "/sync/listarDocumentosDiretorio?instancia=" + props.getInstancia()
                        + "&diretorioId=" + diretorioId
                        + "&page=" + page
                        + "&size=" + size)
                .addHeader("Authorization", "Bearer " + props.getToken())
                .post(RequestBody.create(new byte[0]))
                .build();

        return executeJson(request, SyncDocumentosPageResponse.class);
    }

    public SyncChangesResponse buscarAlteracoes(Long ultimoEventoId, Integer diretorioRaizId) {
        Request request = new Request.Builder()
                .url(props.getApiBaseUrl() + "/sync/changes?instancia=" + props.getInstancia()
                        + "&ultimoEventoId=" + ultimoEventoId
                        + "&diretorioRaizId=" + diretorioRaizId)
                .addHeader("Authorization", "Bearer " + props.getToken())
                .post(RequestBody.create(new byte[0]))
                .build();

        return executeJson(request, SyncChangesResponse.class);
    }

    private <T> T executeJson(Request request, Class<T> clazz) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String body = response.body() != null ? response.body().string() : "";
                throw new RuntimeException("Erro HTTP " + response.code() + ": " + body);
            }

            if (response.body() == null) {
                throw new RuntimeException("Resposta vazia da API");
            }

            return objectMapper.readValue(response.body().string(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao chamar API DocNuvem: " + e.getMessage(), e);
        }
    }
}