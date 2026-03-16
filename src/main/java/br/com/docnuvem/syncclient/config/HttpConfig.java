package br.com.docnuvem.syncclient.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpConfig {

    @Bean
    public OkHttpClient okHttpClient(AppProperties props) {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(props.getDownloadTimeoutSegundos(), TimeUnit.SECONDS)
                .writeTimeout(props.getUploadTimeoutSegundos(), TimeUnit.SECONDS)
                .build();
    }
}