import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        // TODO: Ask user input
        String api = "imdb";
//        String api = "marvel";

        // TODO: Think of a better way to do this... Enums?
        ApiClient apiClient;
        JsonParser jsonParser;
        String endpoint;

        if (api.equals("imdb")) {
            apiClient = new ImdbApiClient();
            endpoint = ImdbApiClient.ENDPOINT_TOP_250_MOVIES;
            jsonParser = new ImdbMovieJsonParser();
        } else if (api.equals("marvel")) {
            apiClient = new MarvelApiClient();
            endpoint = MarvelApiClient.ENDPOINT_COMICS;
            jsonParser = new MarvelComicJsonParser();
        } else {
            throw new RuntimeException("Fail");
        }

        String json = apiClient.get(endpoint);
        List<Content> contents = jsonParser.parse(json);

        PrintWriter writer = new PrintWriter("out/contents.html");
        new HTMLGenerator(writer).generate(contents);
        writer.close();
    }
}
