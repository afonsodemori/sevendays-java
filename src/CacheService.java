import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class CacheService {

    private static final String CACHE_DIR = System.getProperty("java.io.tmpdir");
    private static final String FILE_PREFIX = "dev.afonso.sevendays-java";

    public static String get(String key, Callable<?> callable) {
        Path filepath = generateFilepath(key);

        if (!Files.exists(filepath)) {
            String value;
            try {
                value = (String) callable.call();
                Files.writeString(filepath, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {
            return Files.readString(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path generateFilepath(String key) {
        // TODO: This regex is for URLs. Enough for this project, but not the best regex you'll see
        String filename = key.replaceAll("[:/.&?=%]", "_");
        return Path.of("%s/%s--%s.txt".formatted(CACHE_DIR, FILE_PREFIX, filename));
    }
}
