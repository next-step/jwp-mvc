package core.mvc.tobe.extractor;

public class IntegerRequestParameterExtractor extends PrimitiveExtractor {

    @Override
    Class<?> getType() {
        return Integer.TYPE;
    }
}
