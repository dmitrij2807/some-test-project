package dev.mesh.moneytransfer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class TestFileUtils {

  public static String getResourceAsString(String resource) throws URISyntaxException, IOException {
    return Files.readString(
        Path.of(
            Objects.requireNonNull(TestFileUtils.class
                .getClassLoader()
                .getResource(resource)).toURI()),
        StandardCharsets.UTF_8);
  }
}
