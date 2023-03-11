import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MarvelApiClient implements ApiClient {

    public static final String ENDPOINT_COMICS = "https://gateway.marvel.com/v1/public/comics?ts=%s&apikey=%s&hash=%s";
    private static final String API_KEY = System.getenv("MARVEL_PUBLIC_KEY");
    private static final String PRIVATE_KEY = System.getenv("MARVEL_PRIVATE_KEY");

    public String get(String endpoint) {
        return CacheService.get(endpoint, () -> {
            String ts = String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            String hash = generateHash(ts, PRIVATE_KEY, API_KEY);
            URI uri = URI.create(ENDPOINT_COMICS.formatted(ts, API_KEY, hash));
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

    private String generateHash(String ts, String privateKey, String apiKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = (ts + privateKey + apiKey).getBytes();
            BigInteger bigInteger = new BigInteger(1, md.digest(bytes));

            return bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
