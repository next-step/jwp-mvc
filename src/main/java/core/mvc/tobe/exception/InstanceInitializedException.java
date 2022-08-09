package core.mvc.tobe.exception;

public class InstanceInitializedException extends RuntimeException {
	private static final String EXCEPTION_MESSAGE = "인스턴스 생성을 할 수 없습니다.";
	public InstanceInitializedException() {
		super(EXCEPTION_MESSAGE);
	}
}
