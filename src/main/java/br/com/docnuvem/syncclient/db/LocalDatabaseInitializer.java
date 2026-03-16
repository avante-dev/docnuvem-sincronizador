package br.com.docnuvem.syncclient.db;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LocalDatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    public LocalDatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS sync_config (
                chave TEXT PRIMARY KEY,
                valor TEXT
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS sync_dir (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                local_path TEXT NOT NULL UNIQUE,
                remote_dir_id INTEGER NOT NULL,
                parent_remote_dir_id INTEGER,
                nome TEXT NOT NULL,
                remoto_caminho TEXT,
                ultima_sync TEXT,
                remoto_ultima_alteracao TEXT,
                status TEXT
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS sync_file (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                local_path TEXT NOT NULL UNIQUE,
                remote_document_id INTEGER NOT NULL,
                remote_dir_id INTEGER NOT NULL,
                nome TEXT NOT NULL,
                extensao TEXT,
                tamanho INTEGER,
                hash_local TEXT,
                hash_remoto TEXT,
                last_modified_local TEXT,
                last_modified_remoto TEXT,
                status TEXT,
                ultimo_erro TEXT
            )
        """);
    }
}