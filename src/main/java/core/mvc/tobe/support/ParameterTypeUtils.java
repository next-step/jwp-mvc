package core.mvc.tobe.support;

public class ParameterTypeUtils {
	public static Object cast(Class<?> parameterType, Object value) {
		if (parameterType.equals(String.class)) {
			return value.toString();
		}
		if (parameterType.equals(Integer.class)) {
			return Integer.valueOf(value.toString());
		}
		if (parameterType.equals(int.class)) {
			return Integer.parseInt(value.toString());
		}
		if (parameterType.equals(long.class)) {
			return Long.parseLong(value.toString());
		}
		throw new RuntimeException("Parameter Type Cast Fail");
	}
}
