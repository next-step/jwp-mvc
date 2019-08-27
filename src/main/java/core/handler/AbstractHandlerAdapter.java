package core.handler;

public abstract class AbstractHandlerAdapter<T> implements HandlerAdapter{

	private final Class<T> parameterizedType;
	
	public AbstractHandlerAdapter(Class<T> parameterizedType) {
		this.parameterizedType = parameterizedType;
	}
	
	@Override
	public boolean supports(Object handler) {
		if(handler == null) {
			return false;
		}
		
		return parameterizedType.isAssignableFrom(handler.getClass());
	}

}
