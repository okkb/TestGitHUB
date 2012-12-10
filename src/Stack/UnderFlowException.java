package Stack;

class UnderFlowException extends Exception // 사용자 정의 예외 UnderFlowException
{
	public UnderFlowException() {
		super("UnderFlow 발생 더 이상 Stack에 Pop할 수 없습니다.");
	}
}