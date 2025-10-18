package com.errol.db2spring.sql;

import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class SqlFileReader {

    /**
     * Retrieve SQL files if the path is a folder.
     */
    public static List<Path> getSqlFiles(String folderPath) throws IOException {
        List<Path> sqlFiles = new ArrayList<>();
        Path folder = Paths.get(folderPath);

        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new IllegalArgumentException("Folder does not exist or is not a directory: " + folderPath);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.sql")) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    sqlFiles.add(path);
                }
            }
        }

        return sqlFiles;
    }

    /**
     * Read a single SQL file.
     */
    public static String readSqlFile(Path sqlFile) throws IOException {
        return Files.readString(sqlFile);
    }

    /**
     * Read SQL content from either a folder or a single SQL file.
     */
    public static List<String> readAllSql(String path) throws IOException {
        List<String> sqlContents = new ArrayList<>();
        Path target = Paths.get(path);

        if (!Files.exists(target)) {
            throw new FileNotFoundException("Path not found: " + path);
        }

        if (Files.isDirectory(target)) {
            // Handle folder case
            for (Path file : getSqlFiles(path)) {
                sqlContents.add(readSqlFile(file));
            }
        } else if (Files.isRegularFile(target) && path.toLowerCase().endsWith(".sql")) {
            // Handle single file case
            sqlContents.add(readSqlFile(target));
        } else {
            throw new IllegalArgumentException("Provided path is not a valid SQL file or directory: " + path);
        }

        return sqlContents;
    }
}

