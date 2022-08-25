package next.support.resolver;

public class ParameterSpec {

    private int order;
    private Class<?> clazz;
    private Object value;

    public ParameterSpec(int order, Class<?> clazz, Object value) {
        this.order = order;
        this.clazz = clazz;
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getValue() {
        return value;
    }
}
