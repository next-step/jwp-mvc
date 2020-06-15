package core.mvc.param;

public class LongParser implements Parser<Long> {

    @Override
    public boolean isParsable(Class<?> clazz) {
        return clazz.equals(Long.class) ||
                clazz.equals(long.class);
    }

    @Override
    public Long parse(final String value) {
        return Long.parseLong(value);
    }
}
