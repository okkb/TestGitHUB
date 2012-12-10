package Stack;

class Stacks {
	private int top = 0; // top
	private String[] st = null; // Stack으로 사용할 배열

	public Stacks(int i) {
		this.top = 0;
		this.st = new String[i + 1]; // 배열0번째 자리는 초기 top의 위치
	}

	public void Push(String str) throws OverFlowException // Stack의 상단에 문자열 추가
	{
		if (top + 1 >= st.length) {
			throw new OverFlowException();
		} else {
			top++;
			st[top] = str;
			System.out.println("Stack의" + top + "번째 : " + st[top]);
		}
	}

	public String Pop() throws UnderFlowException // 가장 최근 입력된 문자열 출력 후 삭제
	{
		String temp = "";
		if (top == 0) {
			throw new UnderFlowException();
		} else {
			temp = st[top];
			st[top] = null;
			top--;
		}
		return temp;
	}

	public void Check() // 현재 스택의 상태를 출력해주는 메소드
	{
		System.out.println("< 현재 Stack의 상태 >");
		for (int i = top; i > 0; i--) {
			System.out.println(st[i]);
		}
		System.out.println("---------------------");
	}
}