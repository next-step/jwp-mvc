package core.mvc.utils.type;

public interface TypeParser {

    boolean supports(Class<?> type);

    Object convert(String raw);
}
