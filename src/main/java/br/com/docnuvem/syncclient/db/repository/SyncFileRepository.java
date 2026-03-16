package br.com.docnuvem.syncclient.db.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SyncFileRepository {

    private final JdbcTemplate jdbcTemplate;

    public SyncFileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void upsert(String localPath,
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
        jdbcTemplate.update("""
            INSERT INTO sync_file (
                local_path, remote_document_id, remote_dir_id, nome, extensao, tamanho,
                hash_local, hash_remoto, last_modified_local, last_modified_remoto, status, ultimo_erro
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(local_path) DO UPDATE SET
                remote_document_id = excluded.remote_document_id,
                remote_dir_id = excluded.remote_dir_id,
                nome = excluded.nome,
                extensao = excluded.extensao,
                tamanho = excluded.tamanho,
                hash_local = excluded.hash_local,
                hash_remoto = excluded.hash_remoto,
                last_modified_local = excluded.last_modified_local,
                last_modified_remoto = excluded.last_modified_remoto,
                status = excluded.status,
                ultimo_erro = excluded.ultimo_erro
        """, localPath, remoteDocumentId, remoteDirId, nome, extensao, tamanho,
                hashLocal, hashRemoto, lastModifiedLocal, lastModifiedRemoto, status, ultimoErro);
    }

    public Map<String, Object> findByRemoteDocumentId(Integer remoteDocumentId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
            SELECT * FROM sync_file WHERE remote_document_id = ?
        """, remoteDocumentId);

        return rows.isEmpty() ? null : rows.get(0);
    }

    public Map<String, Object> findByLocalPath(String localPath) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
            SELECT * FROM sync_file WHERE local_path = ?
        """, localPath);

        return rows.isEmpty() ? null : rows.get(0);
    }

    public String findLocalPathByRemoteDocumentId(Integer remoteDocumentId) {
        List<String> rows = jdbcTemplate.query("""
        SELECT local_path FROM sync_file WHERE remote_document_id = ?
    """, (rs, rowNum) -> rs.getString("local_path"), remoteDocumentId);

        return rows.isEmpty() ? null : rows.get(0);
    }

}