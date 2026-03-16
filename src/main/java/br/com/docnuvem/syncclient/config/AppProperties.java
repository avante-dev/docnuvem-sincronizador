package br.com.docnuvem.syncclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "docnuvem")
public class AppProperties {

    private String apiBaseUrl;
    private String instancia;
    private String token;
    private Integer diretorioRaizId;
    private String pastaLocalRaiz;
    private Integer pollingSegundos;
    private Integer downloadTimeoutSegundos;
    private Integer uploadTimeoutSegundos;

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getInstancia() {
        return instancia;
    }

    public void setInstancia(String instancia) {
        this.instancia = instancia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDiretorioRaizId() {
        return diretorioRaizId;
    }

    public void setDiretorioRaizId(Integer diretorioRaizId) {
        this.diretorioRaizId = diretorioRaizId;
    }

    public String getPastaLocalRaiz() {
        return pastaLocalRaiz;
    }

    public void setPastaLocalRaiz(String pastaLocalRaiz) {
        this.pastaLocalRaiz = pastaLocalRaiz;
    }

    public Integer getPollingSegundos() {
        return pollingSegundos;
    }

    public void setPollingSegundos(Integer pollingSegundos) {
        this.pollingSegundos = pollingSegundos;
    }

    public Integer getDownloadTimeoutSegundos() {
        return downloadTimeoutSegundos;
    }

    public void setDownloadTimeoutSegundos(Integer downloadTimeoutSegundos) {
        this.downloadTimeoutSegundos = downloadTimeoutSegundos;
    }

    public Integer getUploadTimeoutSegundos() {
        return uploadTimeoutSegundos;
    }

    public void setUploadTimeoutSegundos(Integer uploadTimeoutSegundos) {
        this.uploadTimeoutSegundos = uploadTimeoutSegundos;
    }
}