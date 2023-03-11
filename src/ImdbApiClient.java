import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ImdbApiClient implements ApiClient {

    public static final String ENDPOINT_TOP_250_MOVIES = "https://imdb-api.com/en/API/Top250Movies/";
    private static final String API_KEY = System.getenv("IMDB_API_KEY");

    public String get(String endpoint) {
        return CacheService.get(endpoint, () -> {
            URI uri = URI.create(ENDPOINT_TOP_250_MOVIES + API_KEY);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(REQUEST_TIMEOUT)
                    .GET()
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString()); // @see sendAsync

            return response.body();
        });
    }
}
