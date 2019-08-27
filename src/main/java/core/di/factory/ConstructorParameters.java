package core.di.factory;

import static java.util.stream.Collectors.toCollection;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ConstructorParameters {

    private final Constructor<?> cosntructor;
    private final List<ParameterTypeName> parameterTypeNames;

    public ConstructorParameters(Constructor<?> cosntructor, List<ParameterTypeName> parameterTypeNames) {
        this.cosntructor = cosntructor;
        this.parameterTypeNames = parameterTypeNames;
    }

    public Constructor<?> getConstructor() {
        return this.cosntructor;
    }

	public List<ParameterTypeName> getParameterTypeNames() {
		return this.parameterTypeNames;
	}
	
	public List<String> getParameterNames() {
		return this.parameterTypeNames.stream()
				.map(ParameterTypeName::getName)
				.collect(Collectors.toList());
	}
	
	public boolean isMatchedParamNames(List<String> names) {
        return names
                .stream()
                .collect(toCollection(() -> new TreeSet<String>(String.CASE_INSENSITIVE_ORDER)))
                .containsAll(getParameterNames());
    }
    
}
