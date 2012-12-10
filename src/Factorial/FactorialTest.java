// 김보현
package Factorial;

import java.util.Scanner;

public class FactorialTest {
	public static void main(String[] args) {
		boolean roof = true;
		Scanner s = new Scanner(System.in);
		Factorials fac = new Factorials();
		System.out.println("Factorial 테스트 시작 합니다.");
		while (roof) {
			try {
				System.out.println("1 선택은 Factorial 구하기");
				System.out.println("2 선택은 테스트 종료");
				System.out.print("선택 : ");
				int choice = s.nextInt();
				switch (choice) {
				case 1:
					System.out.print("n!의 n을 입력해주세요 : ");
					int input = s.nextInt();
					if (input > 0)
						System.out.println(input+"! ="+fac.factorial(input));
					else
						System.out.println("0이상의 숫자를 입력해주세요");
						break;
				case 2:
					roof = false;
					break;
				default:
					System.out.println("잘못된 번호를 입력하셨습니다.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
