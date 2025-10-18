package com.errol.db2spring.writer;

import com.errol.db2spring.exception.Db2springException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Db2springFileWriter {

    public void writeFile(String src, String packageName, String filename, String content) {
        Path fullPath = buildFullPath(src, packageName, filename);
        writeFile(fullPath, content);
    }

    public void writeFile(String fullPath, String content) {
        writeFile(Paths.get(fullPath), content);
    }

    public void writeFile(Path fullPath, String content) {
        try {
            Files.createDirectories(fullPath.getParent());
            Files.writeString(fullPath,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new Db2springException("Failed to write file: " + fullPath, e);
        }
    }

    private Path buildFullPath(String src, String packageName, String filename) {
        String normalizedPackage = packageName == null || packageName.isBlank()
                ? ""
                : packageName.replace('.', '/');
        return Paths.get(src, normalizedPackage, filename + ".java").normalize();
    }
}
