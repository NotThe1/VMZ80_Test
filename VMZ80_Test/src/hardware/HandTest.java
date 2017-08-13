package hardware;

public class HandTest {

	public static void main(String[] args) {
		byte[] arg1 = new byte[] {(byte) 0XFF,(byte) 0X0F};
		byte[] arg2 = new byte[] {(byte) 0XFF,(byte) 0X0F};
		Adder adder = Adder.getInstance();
		byte[] ans = adder.subWordWithCarry(arg1, arg2, true);
		boolean HC = adder.hasHalfCarry();
		boolean CY = adder.hasCarry();
		System.out.printf("HC = %s, CY = %s  , %02X - %02X %n",HC,CY,ans[0],ans[1]);
	}//main

}//HandTest
