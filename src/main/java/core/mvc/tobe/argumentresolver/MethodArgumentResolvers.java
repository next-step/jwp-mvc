package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.argumentresolver.custom.IntegerArgumentResolver;
import core.mvc.tobe.argumentresolver.custom.StringArgumentResolver;

import java.util.ArrayList;
import java.util.List;

public class MethodArgumentResolvers {
    private static List<MethodArgumentResolver> resolvers = new ArrayList<>();

    static {
        resolvers.add(new IntegerArgumentResolver());
        resolvers.add(new StringArgumentResolver());
    }
}
