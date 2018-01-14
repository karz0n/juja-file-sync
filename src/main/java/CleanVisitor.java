import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@SuppressWarnings("WeakerAccess")
public class CleanVisitor extends SimpleFileVisitor<Path> {
    private Path source;
    private Path target;

    public CleanVisitor(Path source, Path target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetDir = target.resolve(source.relativize(dir));

        if (!Files.exists(targetDir)) {
            Files.walkFileTree(dir, new RemoveRecursivelyVisitor());
            return FileVisitResult.SKIP_SUBTREE;
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path targetFile = target.resolve(source.relativize(file));

        if (!Files.exists(targetFile)) {
            Files.delete(file);
        }

        return FileVisitResult.CONTINUE;
    }
}
