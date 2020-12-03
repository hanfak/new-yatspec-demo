package fileservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSystemFileReader implements FileReader<String, String> {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemFileReader.class);

    @Override
    public String readFile(String path) {
        logger.info("Reading file: \"{}\"", path);
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            logger.error("Something went wrong reading file", e);
            throw new UncheckedIOException(e);
        }
    }
}
