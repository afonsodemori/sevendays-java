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

    private static final int REQUEST_TIMEOUT = 10;
    private static final String API_KEY = System.getenv("IMDB_API_KEY");
    public static final String ENDPOINT_TOP_250_MOVIES = "https://imdb-api.com/en/API/Top250Movies/";

    public static void main(String[] args) throws IOException, InterruptedException {
        String json = getApiCachedResponse(ENDPOINT_TOP_250_MOVIES);

        List<String> titles = getPropertyList(json, "title");
        List<String> images = getPropertyList(json, "image");
        List<String> years = getPropertyList(json, "year");
        List<String> ratings = getPropertyList(json, "imDbRating");
        Map<String, String> map = getPropertyMap(json);

        for (int i = 0; i < titles.size(); i++) {
            System.out.printf(
                    "%s (%s) - Rating: %s%nImage: %s%n%n",
                    titles.get(i),
                    years.get(i),
                    ratings.get(i),
                    images.get(i)
            );
        }
    }

    private static String getApiCachedResponse(String endpoint) throws IOException, InterruptedException {
        String tmpdir = System.getProperty("java.io.tmpdir");
        String filename = endpoint.replaceAll("[:/.]", "_");
        Path filepath = Path.of("%s/dev.afonso.sevendays-java--%s.json".formatted(tmpdir, filename));

        if (!Files.exists(filepath)) {
            String finalEndpoint = ENDPOINT_TOP_250_MOVIES + API_KEY;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(finalEndpoint))
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT))
                    .GET()
                    .build();

            HttpResponse<String> response = client
                    .send(request, BodyHandlers.ofString()); // @see sendAsync

            Files.writeString(filepath, response.body());
        }

        return Files.readString(filepath);
    }

    private static List<String> getPropertyList(String json, String propertyName) {
        // Not the best way to parse a json, of course. But...
        // @see https://github.com/FasterXML/jackson-docs and https://github.com/google/gson/blob/master/UserGuide.md
        List<String> list = new ArrayList<>();

        String items = json.substring(json.indexOf("[{") + 2, json.lastIndexOf("}]"));
        String[] lines = items.split("},\\{");

        for (String line : lines) {
            String[] propertiesWithValue = line.split("\",\"");
            for (String propertyWithValue : propertiesWithValue) {
                propertyWithValue = "\"" + propertyWithValue + "\"";
                String[] parts = propertyWithValue.substring(1, propertyWithValue.length() - 1).split("\":\"");

                if (parts[0].equals(propertyName)) {
                    list.add(parts[1]);
                }
            }
        }

        return list;
    }

    private static Map<String, String> getPropertyMap(String json) {
        // Not the best way to parse a json, of course. But...
        // @see https://github.com/FasterXML/jackson-docs and https://github.com/google/gson/blob/master/UserGuide.md
        Map<String, String> map = new HashMap<>();

        String items = json.substring(json.indexOf("[{") + 2, json.lastIndexOf("}]"));
        String[] lines = items.split("},\\{");

        for (String line : lines) {
            String[] propertiesWithValue = line.split("\",\"");
            for (String propertyWithValue : propertiesWithValue) {
                propertyWithValue = "\"" + propertyWithValue + "\"";
                String[] parts = propertyWithValue.substring(1, propertyWithValue.length() - 1).split("\":\"");

                map.put(parts[0], parts[1]);
            }
        }

        return map;
    }
}
