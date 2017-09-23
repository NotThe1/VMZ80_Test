package hardware;

public class HandTest {

	public static void main(String[] args) {
		ArithmeticUnit adder = ArithmeticUnit.getInstance();

		// byte[] arg1 = new byte[] {(byte) 0XFF,(byte) 0X0F};
		// byte[] arg2 = new byte[] {(byte) 0XFF,(byte) 0X0F};
		// byte[] ans = adder.subWordWithCarry(arg1, arg2, true);
		// boolean HC = adder.hasHalfCarry();
		// boolean CY = adder.hasCarry();
		// System.out.printf("HC = %s, CY = %s , %02X - %02X %n",HC,CY,ans[0],ans[1]);

		byte arg1 = (byte) 0X67;
		byte arg2 = (byte) 0X99;
		
		boolean CY = true;
		boolean HC = true;
		byte sum = adder.add(arg1, arg2);
		byte daa = adder.daa(sum, false, CY, HC);

		boolean HC1 = adder.hasHalfCarry();	
		boolean CY1 = adder.hasCarry();
		
		
		System.out.printf(" %02X, %02X, %s, %s %02X,%02X, %s, %s.%n",arg1,arg2,HC,CY,sum,daa,HC1,CY1);

	}// main

}// HandTest
