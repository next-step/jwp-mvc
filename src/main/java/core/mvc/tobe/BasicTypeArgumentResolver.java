package core.mvc.tobe;

public class BasicTypeArgumentResolver {

    private Class parameterType;

    public BasicTypeArgumentResolver(final Class parameterType) {
        this.parameterType = parameterType;
    }

    public Object getParameterValue(String parameterValue) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameterValue);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameterValue);
        }
        return parameterValue;
    }
}
