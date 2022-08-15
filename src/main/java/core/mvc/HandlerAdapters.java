package core.mvc;

import java.util.ArrayList;
import java.util.List;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;

public class HandlerAdapters {
	private static final String NOT_FOUND_ADAPTER = "지원하지 않는 Adapter 입니다.";

	private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

	public HandlerAdapters() {
		handlerAdapters.add(new ControllerHandlerAdapter());
		handlerAdapters.add(new HandlerExecutionHandlerAdapter());
	}

	public HandlerAdapter getHandlerAdapter(Object handler) {
		return handlerAdapters.stream()
							  .filter(handlerAdapter -> handlerAdapter.supports(handler))
							  .findFirst()
							  .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ADAPTER));
	}
}
