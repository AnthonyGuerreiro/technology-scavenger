package com.tscavenger.conf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.tscavenger.log.LogManager;
import com.tscavenger.log.Logger;

public class ResourceLineReader {

    private static Logger logger = LogManager.getInstance(ResourceLineReader.class);

    public List<String> getLines(String resource) throws IOException, URISyntaxException {

        URL url = this.getClass().getResource(resource);
        if (url == null) {
            logger.warn("File does not exist: " + resource);
            return new ArrayList<>();
        }
        return Files.readAllLines(Paths.get(url.toURI()), Charset.defaultCharset());
    }
}
