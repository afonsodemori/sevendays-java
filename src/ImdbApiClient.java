import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class ImdbApiClient {

    public static final String ENDPOINT_TOP_250_MOVIES = "https://imdb-api.com/en/API/Top250Movies/";
    private static final String API_KEY = System.getenv("IMDB_API_KEY");
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    public String get(String endpoint) throws IOException, InterruptedException {
        String tmpdir = System.getProperty("java.io.tmpdir");
        String filename = endpoint.replaceAll("[:/.]", "_");
        Path filepath = Path.of("%s/dev.afonso.sevendays-java--%s.json".formatted(tmpdir, filename));

        if (!Files.exists(filepath)) {
            URI uri = URI.create(ENDPOINT_TOP_250_MOVIES + API_KEY);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(REQUEST_TIMEOUT)
                    .GET()
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString()); // @see sendAsync

            Files.writeString(filepath, response.body());
        }

        return Files.readString(filepath);
    }
}
