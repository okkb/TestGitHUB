/*
 		2012-12-04   김보현
 */
package Stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class StackTest {
	public static void main(String[] args) throws IOException // main
	{
		boolean roof = true;
		Scanner s = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Stack 테스트 시작 합니다.");
		System.out.print("사용하실 Stack의 크기를 입력해 주세요 : ");
		Stacks st = new Stacks(s.nextInt());

		while (roof) {
			try {
				System.out.println();
				System.out.println("#################################");
				System.out.println("1 선택은 Push수행");
				System.out.println("2 선택은 Pop수행");
				System.out.println("3 선택은 현재 Stack상태 보기");
				System.out.println("4 선택은 Stack 테스트 종료");
				System.out.print("선택 : ");

				int choice = s.nextInt();
				switch (choice) {
				case 1:
					System.out.print("Push할 문자열 입력 : ");
					String input = br.readLine();
					st.Push(input);
					break;
				case 2:
					System.out.println("Pop된 문자열 : " + (String) st.Pop());
					break;
				case 3:
					st.Check();
					break;
				case 4:
					roof = false;
					break;
				default:
					System.out.println("잘못된 번호를 입력하셨습니다.");
				}
			} catch (OverFlowException e) {
				System.out.println(e.getMessage());
			} catch (UnderFlowException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
