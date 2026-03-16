package br.com.docnuvem.syncclient.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalPathUtil {

    private LocalPathUtil() {
    }

    public static String sanitizeFileName(String nome) {
        if (nome == null || nome.isBlank()) {
            return "_sem_nome_";
        }

        String sanitized = nome
                .replace("\\", "_")
                .replace("/", "_")
                .replace(":", "_")
                .replace("*", "_")
                .replace("?", "_")
                .replace("\"", "_")
                .replace("<", "_")
                .replace(">", "_")
                .replace("|", "_");

        sanitized = sanitized.trim();

        if (sanitized.isEmpty()) {
            return "_sem_nome_";
        }

        return sanitized;
    }

    public static Path resolveChild(Path parent, String nome) {
        return parent.resolve(sanitizeFileName(nome));
    }

    public static Path fromString(String path) {
        return Paths.get(path);
    }
}