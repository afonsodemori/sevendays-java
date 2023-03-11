import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MarvelComicJsonParser implements JsonParser {
    public List<Content> parse(String json) {
        // TODO: This response is much more complex... work on it...
        ArrayList<Content> contents = new ArrayList<>();

        contents.add(new Comic("Fake Info", 0, URI.create("https://i.annihil.us/u/prod/marvel/i/mg/c/80/5e3d7536c8ada.jpg"), 0.0));

        return contents;
    }
}
