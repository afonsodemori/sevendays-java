import java.io.PrintWriter;
import java.util.List;

public class HTMLGenerator {
    private final PrintWriter writer;

    public HTMLGenerator(PrintWriter writer) {
        this.writer = writer;
    }

    public void generate(List<Movie> movies) {
        // Generating here the HTML is not a good idea, but it's for studying purposes...
        writer.write(getHeaderTemplate());

        movies.forEach(movie -> {
            writer.write(String.format(
                            getCardTemplate(),
                            movie.image(),
                            movie.title(),
                            movie.title(),
                            movie.year(),
                            movie.rating()
                    )
            );
        });

        writer.write(getFooterTemplate());
    }

    private String getHeaderTemplate() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>Top 250 Movies - IMDB</title>
                    <style>
                    * {
                        box-sizing: border-box;
                    }
                    html {
                        margin: 0;
                        padding: 0;
                    }
                    body {
                        margin: auto;
                        padding: 0 5em;
                        max-width: 1600px;
                        font: normal 1em Arial;
                        color: #fff;
                        background: #0d1218;
                    }
                    header {
                        padding: 3em 1em;
                        border-bottom: 1px solid #000;
                        text-align: center;
                    }
                    .container {
                        margin: 3em 0;
                    }
                    .card {
                        float: left;
                        position: relative;
                        margin: .5em;
                        width: 200px;
                        height: 272px;
                        border: 2px solid transparent;
                        overflow: hidden;
                        transition: 200ms;
                    }
                    .card:hover {
                        transform: scale(1.5);
                        z-index: 1;
                        cursor: default;
                        border-radius: 3px;
                        box-shadow: 0 0 50px 15px rgba(1, 1, 1, .8);
                        border-radius: 3px;
                        border-color: #0d8cbc;
                    }
                    .card:hover .info {
                        opacity: 1;
                    }
                    .card:hover img {
                        background: #000;
                    }
                    img {
                        width: 100%;
                        padding: 2px;
                        transition: 200ms;
                    }
                    .info {
                        position: absolute;
                        padding: .5em;
                        padding-top: 4em;
                        background: rgba(1, 1, 1, .8);
                        background: linear-gradient(180deg, rgba(1, 1, 1, 0) 0%, rgba(1, 1, 1, .9) 50%);
                        opacity: 0;
                        right: 0;
                        bottom: 0;
                        left: 0;
                        text-shadow: 1px 1px 1px #000;
                    }
                    .info p {
                        margin: 0 .2em;
                        font-size: .8em;
                    }
                    .clear {
                        clear: both;
                    }
                    footer {
                        border-top: 1px solid #000;
                        padding: 2em;
                        text-align: center;
                        color: #000;
                    }
                    footer a {
                        padding: .5em;
                        color: #0d8cbc;
                    }
                    footer a:hover {
                        background: #0d8cbc;
                        text-decoration: none;
                        color: #000;
                    }
                    </style>
                </head>
                <body>
                <header>
                    <h1>Top 250 Movies IMDB</h1>
                </header>
                <div class="container">
                """;
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
