package core.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import core.mvc.asis.LegacyHandlerMapping;
import core.mvc.tobe.AnnotationHandlerMapping;

public class HandlerMappings {

	private static final String NOT_FOUND_HANDLER = "지원하지 않는 handler 입니다.";

	private List<HandlerMapping> mappings = new ArrayList<>();

	public HandlerMappings(Object... basePackage) {
		LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
		AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);

		mappings.add(legacyHandlerMapping);
		mappings.add(annotationHandlerMapping);
	}

	public Object getHandler(HttpServletRequest request) {
		return mappings.stream()
					   .filter(mapping -> mapping.supports(request))
					   .findFirst()
					   .map(mapping -> mapping.getHandler(request))
					   .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_HANDLER));
	}
}
