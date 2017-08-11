package hardware;

public class HandTest {

	public static void main(String[] args) {
		Adder adder = Adder.getInstance();
		byte ans = adder.subWithCarry((byte)00, (byte) 0X0F, true);
	}//main

}//HandTest
