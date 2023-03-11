import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String json = new ImdbApiClient().get(ImdbApiClient.ENDPOINT_TOP_250_MOVIES);
        List<Movie> movies = ImdbMovieJsonParser.parse(json);

        PrintWriter writer = new PrintWriter("out/movies.html");
        new HTMLGenerator(writer).generate(movies);
        writer.close();
    }
}
