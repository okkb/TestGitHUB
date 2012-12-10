package Stack;

class OverFlowException extends Exception // 사용자 정의 예외 OverFlowException
{
	public OverFlowException() {
		super("OverFlow 발생 더 이상 Stack에 Push할 수 없습니다.");
	}
}