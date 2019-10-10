package core.mvc.tobe.extractor;

public class LongRequestParameterExtractor extends PrimitiveExtractor {

    @Override
    Class<?> getType() {
        return Long.TYPE;
    }
}
