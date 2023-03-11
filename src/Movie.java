import java.net.URI;
import java.util.Map;

public record Movie(
        String title,
        int year,
        URI image,
        Double rating
) implements Content {
    public static Movie fromMap(Map<String, String> movieMap) {
        return new Movie(
                movieMap.get("title"),
                Integer.parseInt(movieMap.get("year")),
                URI.create(movieMap.get("image")),
                Double.parseDouble(movieMap.get("imDbRating"))
        );
    }
}
