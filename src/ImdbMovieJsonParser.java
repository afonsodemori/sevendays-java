import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImdbMovieJsonParser {
    public static List<Movie> parse(String json) {
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
