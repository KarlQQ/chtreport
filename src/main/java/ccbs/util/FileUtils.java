package ccbs.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
  private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private static final String BOM = "\uFEFF";

  public static List<Path> moveFilesMatchingPattern(
      String sourceFolder, String targetFolder, String pattern) throws IOException {
    Pattern filePattern = Pattern.compile(pattern);
    List<Path> movedFiles = new ArrayList<>();

    Path targetPath = Paths.get(targetFolder == null ? "." : targetFolder);
    if (!Files.exists(targetPath)) {
      Files.createDirectories(targetPath);
    }
    Path sourcePath = Paths.get(sourceFolder);
    if (!Files.exists(sourcePath)) {
      Files.createDirectories(sourcePath);
    }

    try (Stream<Path> files = Files.list(sourcePath)) {
      movedFiles = files.filter(Files::isRegularFile)
                       .filter(path -> filePattern.matcher(path.toFile().getName()).matches())
                       .map(path -> {
                         try {
                           return targetFolder != null
                               ? Files.move(path, targetPath.resolve(path.getFileName()),
                                   StandardCopyOption.REPLACE_EXISTING)
                               : path;
                         } catch (IOException e) {
                           log.error("Failed to move file: " + path + " - " + e.getMessage());
                           return null;
                         }
                       })
                       .filter(java.util.Objects::nonNull)
                       .collect(Collectors.toList());
    }

    return movedFiles;
  }

  public static File generateFile(String filePath, List<String> fileContents)
      throws UnsupportedEncodingException, FileNotFoundException, IOException {
    return generateFile(filePath, fileContents, DEFAULT_CHARSET, true);
  }

  public static File generateFile(String filePath, List<String> fileContent, Charset cs,
      Boolean withBOM) throws UnsupportedEncodingException, FileNotFoundException, IOException {
    Path path = Path.of(filePath);
    Path parentDir = path.getParent();
    if (parentDir != null && Files.notExists(parentDir)) {
      Files.createDirectories(parentDir);
    }

    StringBuilder contentBuilder = new StringBuilder();
    if (withBOM && cs.equals(StandardCharsets.UTF_8)) {
      contentBuilder.append(BOM);
    }
    for (String text : fileContent) {
      contentBuilder.append(text);
    }
    Files.writeString(path, contentBuilder.toString(), cs, StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);

    return path.toFile();
  }
}
