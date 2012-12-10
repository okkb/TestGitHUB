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
				String choice = s.next();
				if (choice.matches("\\d")) {					
					switch (Integer.parseInt(choice)) {
					case 1:
						System.out.print("n!의 n을 입력해주세요 : ");
						String input = s.next();
						if (input.matches("\\d+")) {
							System.out.println(input + "! ="+ fac.factorial(Long.parseLong(input)));
						} else {
							System.out.println("숫자를 입력해주세요.");
						}
						break;
					case 2:
						roof = false;
						break;
					default:
						System.out.println("1이나 2를 입력해 주세요.");
					}
					
				}else{
					System.out.println("숫자를 입력해 주세요.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
