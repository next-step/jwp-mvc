package core.mvc.utils.type;

public class CharacterParser implements TypeParser {
    @Override
    public boolean supports(Class<?> type) {
        return char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type);
    }

    @Override
    public Object convert(String raw) {
        return raw.charAt(0);
    }
}
