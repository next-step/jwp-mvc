package core.mvc.param;

public class IntParser implements Parser<Integer> {

    @Override
    public boolean isParsable(Class<?> clazz) {
        return clazz.equals(Integer.class) ||
                clazz.equals(int.class);
    }

    @Override
    public Integer parse(final String value) {
        return Integer.parseInt(value);
    }
}
