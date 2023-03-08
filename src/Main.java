import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    // HTTP Client constants
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);
    private static final String API_KEY = System.getenv("IMDB_API_KEY");
    private static final String ENDPOINT_TOP_250_MOVIES = "https://imdb-api.com/en/API/Top250Movies/";

    public static void main(String[] args) throws IOException, InterruptedException {
        String json = getApiCachedResponse(ENDPOINT_TOP_250_MOVIES);

        List<Movie> movies = getMovies(json);

        movies.forEach(movie -> System.out.printf(
                "%s (%s) - Rating: %s%nImage: %s%n%n",
                movie.title(),
                movie.year(),
                movie.rating(),
                movie.image()
        ));
    }

    private static String getApiCachedResponse(String endpoint) throws IOException, InterruptedException {
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
                    .send(request, BodyHandlers.ofString()); // @see sendAsync

            Files.writeString(filepath, response.body());
        }

        return Files.readString(filepath);
    }

    private static List<Movie> getMovies(String json) {
        List<Movie> movies = new ArrayList<>();

        // Not the best way to parse a json, of course. But...
        // @see https://github.com/FasterXML/jackson-docs and https://github.com/google/gson/blob/master/UserGuide.md
        String items = json.substring(json.indexOf("[{") + 2, json.lastIndexOf("}]"));
        String[] lines = items.split("},\\{");

        Map<String, String> movieMap;
        for (String line : lines) {
            movieMap = new HashMap<>();
            String[] propertiesWithValue = line.substring(1, line.length() - 1).split("\",\"");
            for (String propertyWithValue : propertiesWithValue) {
                String[] parts = propertyWithValue.split("\":\"");
                movieMap.put(parts[0], parts[1]);
            }
            movies.add(Movie.fromMap(movieMap));
        }

        return movies;
    }
}
