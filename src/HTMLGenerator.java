import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HTMLGenerator {
    private final PrintWriter writer;

    public HTMLGenerator(PrintWriter writer) {
        this.writer = writer;
    }

    public void generate(List<Content> contents) {
        // Generating here the HTML is not a good idea, but it's for studying purposes...
        // TODO: Awful
        String title = (contents.get(0) instanceof Movie) ? "Top 250 Movies IMDB" : "Comics Marvel";
        writer.write(getHeaderTemplate(title));

        contents.forEach(content -> {
            writer.write(String.format(
                            getCardTemplate(),
                            content.image(),
                            content.title(),
                            content.title(),
                            content.year(),
                            content.rating()
                    )
            );
        });

        writer.write(getFooterTemplate());
    }

    private String getHeaderTemplate(String title) {
        String template = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>%s</title>
                    <style>
                """.formatted(title);

        try {
            template += Files.readString(Path.of("resources/style.css"));
        } catch (IOException e) {
            System.out.println("CSS file not found.");
        }

        template += """
                    </style>
                </head>
                <body>
                <header>
                    <h1>%s</h1>
                </header>
                <div class="container">
                """.formatted(title);

        return template;
    }

    private String getCardTemplate() {
        return """
                <div class="card">
                    <img src="%s" alt="Image: %s">
                    <div class="info">
                        <h3>%s</h3>
                        <p>Year: %d | Rating: %.1f</p>
                    </div>
                </div>
                """;
    }

    private String getFooterTemplate() {
        return """
                <div class="clear"></div>
                </div>
                <footer>
                    <a href="https://afonso.dev">afonso.dev</a> |
                    <a href="https://github.com/afonsodemori/sevendays-java">GitHub</a> |
                    <a href="https://afonso.dev/contact">Contact</a>
                </footer>
                </body>
                </html>
                """;
    }
}
