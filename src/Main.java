import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class Main {

    private static final int REQUEST_TIMEOUT = 10;
    private static final String API_KEY = System.getenv("IMDB_API_KEY");

    public static void main(String[] args) {
        String endpoint = "https://imdb-api.com/en/API/Top250Movies/" + API_KEY;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, BodyHandlers.ofString()); // @see sendAsync
            System.out.println(response.body());
        } catch (HttpTimeoutException e) {
            System.out.println("IMDB API is taking too long to respond. Try again later.");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
