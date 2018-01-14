import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@SuppressWarnings("WeakerAccess")
public class CopyFilesVisitor extends SimpleFileVisitor<Path> {
    private Path source;
    private Path target;

    public CopyFilesVisitor(Path source, Path target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetDir = target.resolve(source.relativize(dir));
        try {
            Files.copy(dir, targetDir);
        } catch (FileAlreadyExistsException e) {
            if (!Files.isDirectory(targetDir))
                throw e;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path targetFile = target.resolve(source.relativize(file));

        if (Files.exists(targetFile)) {
            Long targetFileSize = (Long) Files.getAttribute(targetFile, "size");
            if (attrs.size() == targetFileSize) {
                return FileVisitResult.CONTINUE;
            }
        }
        Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);

        return FileVisitResult.CONTINUE;
    }
}
