package com.errol.db2spring.writer;

import com.errol.db2spring.exception.Db2springException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Handles writing generated source files to the specified directory.
 */
public class Db2springFileWriter {

    /**
     * Writes a generated Java file to the target directory.
     *
     * @param src         the source output directory (e.g. "src/main/java" or "output")
     * @param packageName the package name (e.g. "com.errol.app.model")
     * @param filename    the class name without extension (e.g. "User")
     * @param content     the generated file content
     */
    public void writeFile(String src, String packageName, String filename, String content) {
        Path fullPath = buildFullPath(src, packageName, filename);

        try {
            // Ensure directory structure exists
            Files.createDirectories(fullPath.getParent());

            // Write file (overwrite if exists)
            Files.writeString(
                    fullPath,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new Db2springException("Failed to write file: " + fullPath, e);
        }
    }

    /**
     * Constructs the full path for the generated file.
     */
    private Path buildFullPath(String src, String packageName, String filename) {
        String normalizedPackage = packageName == null || packageName.isBlank()
                ? ""
                : packageName.replace('.', '/');
        return Paths.get(src, normalizedPackage, filename + ".java").normalize();
    }
}
