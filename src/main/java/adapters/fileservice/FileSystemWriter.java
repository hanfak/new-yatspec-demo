package adapters.fileservice;

import core.usecases.ports.outgoing.FileWriter;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileSystemWriter implements FileWriter {

    private final Logger logger;

    public FileSystemWriter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Path write(Supplier<Path> path, String fileContents) {
        try {
            final Path filePath = path.get();
            logger.info("Writing File: \"{}\"", filePath);
            return Files.write(filePath, fileContents.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to write file", e);
        }
    }

    @Override
    public void copy(Supplier<Path> path, InputStream inputStream) {
        try {
            final Path filePath = path.get();
            logger.info("Writing File: \"{}\"", filePath);
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to copy file", e);
        }
    }

    @Override
    public void copyFileToFileSystem(Supplier<Path> path, Path targetFile) {
        try {
            final Path sourceFile = path.get();
            logger.info("Coping File: \"{}\":\"{}\"", sourceFile, targetFile);
            Files.copy(sourceFile, targetFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to copy file", e);
        }
    }

    @Override
    public Path append(Supplier<Path> path, String fileContents) {
        try {
            final Path filePath = path.get();
            logger.info("Writing File: \"{}\"", filePath);
            return Files.write(filePath, fileContents.getBytes(), CREATE, APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to append to file: " + path.get().toString(), e);
        }
    }
}
