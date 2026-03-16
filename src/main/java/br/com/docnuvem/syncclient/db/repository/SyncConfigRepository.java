package br.com.docnuvem.syncclient.db.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SyncConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    public SyncConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void salvar(String chave, String valor) {
        jdbcTemplate.update("""
            INSERT INTO sync_config (chave, valor)
            VALUES (?, ?)
            ON CONFLICT(chave) DO UPDATE SET valor = excluded.valor
        """, chave, valor);
    }

    public String buscar(String chave) {
        return jdbcTemplate.query("""
            SELECT valor FROM sync_config WHERE chave = ?
        """, rs -> rs.next() ? rs.getString("valor") : null, chave);
    }
}