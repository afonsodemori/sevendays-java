import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarvelComicJsonParser implements JsonParser {
    public List<Content> parse(String json) {
        ArrayList<Content> contents = new ArrayList<>();

        Matcher matcher = Pattern.compile("\"results\":\\[(.*)]}}$").matcher(json);
        if (!matcher.find()) {
            // TODO: Give it some attention
            return contents;
        }

        String group = matcher.group(1); // TODO: Learn more about Matcher

        // Each Comic will miss {"id" and will have a comma (,) or curly bracket (}) in the end of the string
        String[] comics = group.split("\\{?\"id\"");

        for (String comic : comics) {
            if (comic.length() == 0) {
                // First one is empty
                continue;
            }

            String fixedComic = "{\"id\"" + comic.substring(0, comic.length() - 1);

            String title = getTitle(fixedComic);
            URI image = URI.create(getImage(fixedComic));

            // TODO: Should not be obligated to have year and rating attributes...
            contents.add(new Comic(title, 0, image, 0.0));
        }

        return contents;
    }

    private String getTitle(String comic) {
        Matcher matcher = Pattern
                .compile("\"title\":\"(.*)\",\"issueNumber\"")
                .matcher(comic);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private String getImage(String comic) {
        String path = "";
        Matcher pathMatcher = Pattern
                .compile("\"thumbnail\":\\{\"path\":\"([^\"]*)\",")
                .matcher(comic);

        if (pathMatcher.find()) {
            path = pathMatcher.group(1);
        }

        String extension = "";
        Matcher extensionMatcher = Pattern
                .compile("\"thumbnail\":\\{.*,\"extension\":\"([^\"]*)\"")
                .matcher(comic);

        if (extensionMatcher.find()) {
            extension = extensionMatcher.group(1);
        }

        return "%s.%s".formatted(path, extension);
    }
}
