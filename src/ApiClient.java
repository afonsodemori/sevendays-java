import java.time.Duration;

public interface ApiClient {
    Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    String get(String endpoint);
}
