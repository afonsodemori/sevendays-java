import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final HashMap<Integer, String> API_OPTIONS = new HashMap<>();

    static {
        // TODO: Improve menu
        API_OPTIONS.put(1, "imdb");
        API_OPTIONS.put(2, "marvel");
    }

    public static void main(String[] args) throws IOException {

        String api = askUser("Which api would you like to generate a list from?", API_OPTIONS);

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

        File file = new File("%s.html".formatted(api));
        PrintWriter writer = new PrintWriter(file);
        new HTMLGenerator(writer).generate(contents);
        writer.close();

        System.out.printf("File generated at: %s%n", file.getAbsolutePath());
    }

    private static String askUser(String question, HashMap<Integer, String> options) {

        String chosenApi = null;
        do {
            try {
                System.out.printf("[?] %s%n", question);
                options.forEach((key, value) -> {
                    System.out.printf("%d: %s%n", key, value);
                });

                int option = Integer.parseInt(new Scanner(System.in).nextLine());
                chosenApi = options.get(option);
            } catch (Exception ignored) {
            }

            if (chosenApi == null) {
                System.out.println("[!] Invalid option.\n");
            }
        } while (chosenApi == null);

        return chosenApi;
    }
}
