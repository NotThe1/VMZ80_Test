package hardware;

public class HandTest {

	public static void main(String[] args) {
		Adder adder = Adder.getInstance();

		// byte[] arg1 = new byte[] {(byte) 0XFF,(byte) 0X0F};
		// byte[] arg2 = new byte[] {(byte) 0XFF,(byte) 0X0F};
		// byte[] ans = adder.subWordWithCarry(arg1, arg2, true);
		// boolean HC = adder.hasHalfCarry();
		// boolean CY = adder.hasCarry();
		// System.out.printf("HC = %s, CY = %s , %02X - %02X %n",HC,CY,ans[0],ans[1]);

		byte arg1 = (byte) 0X00;
		byte arg2 = (byte) 0X00;
		
		byte ans = adder.xor(arg1, arg2);

		boolean Z = adder.isZero();
		boolean CY = adder.hasCarry();
		boolean P = adder.hasParity();
		
//		System.out.printf("Z = %s, CY = %s, arg1 = %02X,arg2 = %02X%n", Z,CY,arg1,arg2);
		
		System.out.printf("P = %s, result = %02X, arg1 = %02X,arg2 = %02X%n", P,ans,arg1,arg2);

	}// main

}// HandTest
