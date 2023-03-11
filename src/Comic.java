import java.net.URI;

public record Comic(
        String title,
        int year,
        URI image,
        Double rating
) implements Content {
}
