package br.com.docnuvem.syncclient.db.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SyncDirRepository {

    private final JdbcTemplate jdbcTemplate;

    public SyncDirRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void upsert(String localPath,
                       Integer remoteDirId,
                       Integer parentRemoteDirId,
                       String nome,
                       String remotoCaminho,
                       String ultimaSync,
                       String remotoUltimaAlteracao,
                       String status) {
        jdbcTemplate.update("""
            INSERT INTO sync_dir (
                local_path, remote_dir_id, parent_remote_dir_id, nome, remoto_caminho,
                ultima_sync, remoto_ultima_alteracao, status
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(local_path) DO UPDATE SET
                remote_dir_id = excluded.remote_dir_id,
                parent_remote_dir_id = excluded.parent_remote_dir_id,
                nome = excluded.nome,
                remoto_caminho = excluded.remoto_caminho,
                ultima_sync = excluded.ultima_sync,
                remoto_ultima_alteracao = excluded.remoto_ultima_alteracao,
                status = excluded.status
        """, localPath, remoteDirId, parentRemoteDirId, nome, remotoCaminho, ultimaSync, remotoUltimaAlteracao, status);
    }

    public Map<String, Object> findByRemoteDirId(Integer remoteDirId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
            SELECT * FROM sync_dir WHERE remote_dir_id = ?
        """, remoteDirId);

        return rows.isEmpty() ? null : rows.get(0);
    }

    public Map<String, Object> findByLocalPath(String localPath) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
            SELECT * FROM sync_dir WHERE local_path = ?
        """, localPath);

        return rows.isEmpty() ? null : rows.get(0);
    }

    public String findLocalPathByRemoteDirId(Integer remoteDirId) {
        List<String> rows = jdbcTemplate.query("""
        SELECT local_path FROM sync_dir WHERE remote_dir_id = ?
    """, (rs, rowNum) -> rs.getString("local_path"), remoteDirId);

        return rows.isEmpty() ? null : rows.get(0);
    }

}