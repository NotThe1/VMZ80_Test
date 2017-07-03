package hardware;

import hardware.Adder;

public class AdderTestLite {
	static Adder adder = Adder.getInstance();

	public static void main(String[] args) {
		checkNOT();
		checkAnd();
		checkOverFlow();
		checkParity();
		addWord();
		addByte();
	}// main
	
	private static void checkNOT(){
		byte b = (byte) 0XA5;
		byte bb = (byte)~b;
		System.out.printf("b = %s,\tbb= %s%n",Integer.toBinaryString(b),Integer.toBinaryString(bb));
	}
	
	private static void checkAnd(){
		int val1 = 0X7A;
		int val2 = 0XAA;
		byte[] bite1 = new byte[]{(byte) val1};
		byte[] bite2 = new byte[]{(byte) val2};
		byte[] ans = adder.and(bite1, bite2);
		
		System.out.printf("parity = %s,\tans[0] = %02X,\t val1 = %02X,\t val2 = %02X%n",adder.hasParity(), ans[0],val1,val2);
	}

	private static void checkOverFlow() {
		int val1 = 0XBF;
		int val2 = 0X95;

		int ans = (val1 + val2) & 0X0FF;

		boolean sign1 = (val1 & 0X80) == 0X080;
		boolean sign2 = (val2 & 0X80) == 0X080;
		boolean signAns = (ans & 0X80) == 0X080;
		boolean overflow = false;
		if (!(sign1 ^ sign2)) {
			overflow = sign1 ^ signAns;
		} // if		
		System.out.printf("overflow = %s,\tsign1 = %s,\tsign2 = %s\t,signAns = %s%n",overflow,sign1,sign2,signAns);
		
		int maskIn = 0X7F;
		int maskOut = 0XFF;
		boolean carryIn = ((val1 & maskIn)+ (val2 & maskIn) >maskIn);
		boolean carryOut = ((val1 & maskOut)+ (val2 & maskOut) >maskOut);
		overflow = carryIn ^ carryOut;
		System.out.printf("overflow = %s,\tcarryIn = %s,\tcarryOut = %s%n",overflow,carryIn,carryOut);
		
		byte[] bite1 = new byte[]{(byte) val1};
		byte[] bite2 = new byte[]{(byte) val2};
		
		adder.add(bite1, bite2);
		System.out.printf("adder.hasOverflow() = %s%n",adder.hasOverflow());

	}// checkOverFlow

	private static void checkParity() {
		int val1 = 7473;
		int val2 = 42214;
		String s = Integer.toBinaryString((val1 + val2) & 0XFFFF);
		s = s.replaceAll("0", "");
		boolean parityFlag = (s.length() % 2) == 0 ? true : false;

		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);
		adder.add(word1, word2);
		boolean pf = adder.hasParityWord();
		if (pf == parityFlag) {
			int a = 1;
		}
	}// countBits

	private static void addWord() {
		int val1 = 3096;
		int val2 = -val1;
		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);
		adder.add(word1, word2);
		boolean zeroFlag = (((val1 + val2) & 0XFFFF) == 0) ? true : false;
		boolean zflag = adder.isZero();
		boolean x = true;
	}// addWord

	private static void addByte() {
		byte[] bite1 = new byte[] { (byte) 0X4D };
		byte[] bite2 = new byte[] { (byte) 0X33 };

		adder.add(bite1, bite2);
		boolean zflag = adder.isZero();
	}// addByte

	private static byte[] loadWord(int value) {
		return new byte[] { (byte) (value & 0XFF), (byte) ((value & 0XFF00) >> 8) };
	}// loadWord

}// AdderTestLite
