import java.util.List;

public interface JsonParser {
    List<Content> parse(String json);
}
