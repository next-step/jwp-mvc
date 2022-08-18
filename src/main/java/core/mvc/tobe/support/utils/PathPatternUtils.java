package core.mvc.tobe.support.utils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternUtils {
	private static final PathPatternParser parser = new PathPatternParser();

	public static String getValue(String pattern, String uri, String key) {
		PathPattern.PathMatchInfo pathMatchInfo = parser.parse(pattern).matchAndExtract(PathContainer.parsePath(uri));
		Map<String, String> uriVariables = Optional.ofNullable(pathMatchInfo.getUriVariables()).orElse(Collections.emptyMap());

		return uriVariables.get(key);
	}
}
