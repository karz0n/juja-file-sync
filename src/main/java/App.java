import java.io.*;
import java.nio.file.*;

import org.apache.commons.lang3.Validate;

public class App {
    public static void main(String[] args) {
        try {
            Validate.isTrue(args.length == 2);
            Validate.notEmpty(args[0]);
            Validate.notEmpty(args[1]);

            Path source = Paths.get(args[0]);
            Path target = Paths.get(args[1]);
            sync(source, target);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static private void sync(Path source, Path target) throws IOException {
        Validate.isTrue(Files.exists(source));
        if (!Files.exists(target)) {
            Files.createDirectory(target);
        }
        Files.walkFileTree(source, new CopyFilesVisitor(source, target));
        Files.walkFileTree(target, new CleanVisitor(target, source));
    }
}