package core.mvc;

public class MethodParameter {
    private String name;
    private Class<?> type;

    public MethodParameter(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}
