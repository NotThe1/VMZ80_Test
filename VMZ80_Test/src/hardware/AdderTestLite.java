package hardware;

public class AdderTestLite {
	static Adder adder = Adder.getInstance();

	private static boolean mSign;
	private static boolean mZero;
	private static boolean mHalfCarry;
	private static boolean mParity;
	private static boolean mOverflow;
	private static boolean mCarry;
	private static AdderTestUtility atu = AdderTestUtility.getInstance();

	public static void main(String[] args) {
		checkShiftRight();
		// checkRotateRight();
		// checkRotateLeft();
		// checkFixedFlags2();
		// checkFixedFlags1();
		// checkFixedFlags0();
		// checkCompare();
		// checkNeg();
		// checkSubWord();
		// checkWordSignFlag();
		// checkSUB();
		// checkNOT();
		// checkAND();
		// checkOverFlow();
		// checkParity();
		// addWord();
		// addByte();
	}// main

	public static void checkShiftRight() {
		byte arg = (byte) 0Xff;
//		byte ans = adder.shiftSRL(arg);
//		byte ans = adder.shiftSRA(arg);
		byte ans = adder.shiftSLA(arg);
		System.out.printf("Arg = %02X, ans  = %02X, Carry = %s,%n", arg, ans, adder.hasCarry());
	}// checkRotateLeft
	
	public static void checkRotateRight() {
		byte arg = (byte) 0X10;
		byte ans = adder.rotateRight(arg);
		System.out.printf("Arg = %02X, ans  = %02X, Carry = %s,%n", arg, ans, adder.hasCarry());
	}// checkRotateLeft

	public static void checkRotateLeft() {
		byte arg = (byte) 0X01;
		byte ans = adder.rotateLeft(arg);
		System.out.printf("Arg = %02X, ans  = %02X, Carry = %s,%n", arg, ans, adder.hasCarry());
	}// checkRotateLeft

	public static void checkFixedFlags2() {
		int arg1 = 0X00;
		int arg2 = 0X7F;
		boolean hasCarry = true;
		// fixFlagsSUB(arg1, arg2, WORD_ARG, hasCarry);
		// showFixedFlagsV();
		atu.fixFlagsSUB(arg1, arg2, WORD_ARG, hasCarry);
		atu.showFixedFlagsV();

		int val1 = arg1;
		int val2 = arg2;
		byte[] bite1 = new byte[] { (byte) val1 };
		byte[] bite2 = new byte[] { (byte) val2 };

		// int ans = adder.subWithCarry(bite1, bite2, hasCarry) & 0XFF;

		// System.out.printf("%02X - %02X = %02X", val1, val2, ans);
		atu.showFlagsV(adder);
	}// checkFixedFlags2

	public static void checkFixedFlags1() {
		int arg1 = 0X0E11; // 3601
		int arg2 = 0X8000; // 32768
		boolean hasCarry = false;
		atu.fixFlagsSUB(arg1, arg2, WORD_ARG, hasCarry);
		atu.showFixedFlagsV();

		int val1 = arg1;
		int val2 = arg2;
		byte[] bite1 = new byte[] { (byte) val1 };
		byte[] bite2 = new byte[] { (byte) val2 };
		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);

		byte[] ans = adder.subWordWithCarry(word1, word2, hasCarry);

		System.out.printf("%02X - %02X = %02X", val1, val2, getWordValue(ans));
		atu.showFlagsV(adder);
	}// checkFixedFlags1

	public static void checkFixedFlags0() {
		int arg1 = 0X7800;
		int arg2 = 0X6900;
		boolean hasCarry = false;
		atu.fixFlagsADD(arg1, arg2, WORD_ARG, hasCarry);
		atu.showFixedFlagsV();
	}// checkFixedFlags0

	private static void checkCompare() {
		int val1 = 0X80;
		int val2 = 0XFF;
		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);
		// adder.compare(word1, word2);
		//
		// System.out.printf("%02X compare %02X", val1, val2);
		atu.showFlagsV(adder);
	}// checkCompare

	private static void checkNeg() {
		int val1 = 0X98;
		byte[] word1 = loadWord(val1);
		// byte ans = adder.negate(word1);
		// System.out.printf("%02X -> %02X%n", val1, ans);
	}// checkWordSignFlag

	private static void checkSubWord() {
		int val1 = 0X9999;
		int val2 = 0X1111;
		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);
		byte[] ans = adder.subWordWithCarry(word1, word2, true);
		int result = ((ans[1] << 8) + (ans[0] & 0XFF)) & 0XFFFF;

		System.out.printf("%02X - %02X = %02X", val1, val2, result);
		atu.showFlagsV(adder);
	}// checkSubWord

	private static void checkWordSignFlag() {
		int val1 = 25755;
		int val2 = 32203;
		boolean signFlag = (((val1 + val2) & 0X8000) == 0X8000) ? true : false;
		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);
		adder.addWord(word1, word2);
		boolean sign = adder.hasSign();
		// assertThat("testSign word: " + val1 + " + " + val2 + " = " + (val2 + val1), signFlag,
		// equalTo(adder.hasSign()));
	}// checkWordSignFlag

	private static void checkSUB() {
		int val1 = 0X7F;
		int val2 = 0XFF;
		byte[] bite1 = new byte[] { (byte) val1 };
		byte[] bite2 = new byte[] { (byte) val2 };
		// byte ans = adder.subWithCarry(bite1, bite2, false);

		// System.out.printf("%02X - %02X = %02X", val1, val2, ans);
		showFlags(adder);

		val1 = 0X30;
		val2 = 0X10;
		bite1 = new byte[] { (byte) val1 };
		bite2 = new byte[] { (byte) val2 };
		// ans = adder.sub(bite1, bite2);

		// System.out.printf("%02X - %02X = %02X", val1, val2, ans);
		showFlags(adder);

		val1 = 0X30;
		val2 = 0X30;
		bite1 = new byte[] { (byte) val1 };
		bite2 = new byte[] { (byte) val2 };
		// ans = adder.sub(bite1, bite2);

		// System.out.printf("%02X - %02X = %02X", val1, val2, ans);
		showFlags(adder);

		val1 = 0X30;
		val2 = 0X60;
		bite1 = new byte[] { (byte) val1 };
		bite2 = new byte[] { (byte) val2 };
		// ans = adder.sub(bite1, bite2);

		// System.out.printf("%02X - %02X = %02X", val1, val2, ans);
		showFlags(adder);

	}// checkSUB

	private static void checkNOT() {
		byte b = (byte) 0XA5;
		byte bb = (byte) ~b;
		System.out.printf("b = %s,\tbb= %s%n", Integer.toBinaryString(b), Integer.toBinaryString(bb));
	}// checkNOT

	private static void checkAND() {
		int val1 = 0X7A;
		int val2 = 0XAA;
		byte[] bite1 = new byte[] { (byte) val1 };
		byte[] bite2 = new byte[] { (byte) val2 };
		// byte ans = adder.and(bite1, bite2);
		// System.out.printf("parity = %s,\tans[0] = %02X,\t val1 = %02X,\t val2 = %02X%n", adder.hasParity(), ans,
		// val1, val2);
	}// checkAND

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
		System.out.printf("overflow = %s,\tsign1 = %s,\tsign2 = %s\t,signAns = %s%n", overflow, sign1, sign2, signAns);

		int maskIn = 0X7F;
		int maskOut = 0XFF;
		boolean carryIn = ((val1 & maskIn) + (val2 & maskIn) > maskIn);
		boolean carryOut = ((val1 & maskOut) + (val2 & maskOut) > maskOut);
		overflow = carryIn ^ carryOut;
		System.out.printf("overflow = %s,\tcarryIn = %s,\tcarryOut = %s%n", overflow, carryIn, carryOut);

		byte[] bite1 = new byte[] { (byte) val1 };
		byte[] bite2 = new byte[] { (byte) val2 };
		// adder.add(bite1, bite2);
		// System.out.printf("adder.hasOverflow() = %s%n", adder.hasOverflow());

	}// checkOverFlow

	private static void checkParity() {
		int val1 = 7473;
		int val2 = 42214;
		String s = Integer.toBinaryString((val1 + val2) & 0XFFFF);
		s = s.replaceAll("0", "");
		boolean parityFlag = (s.length() % 2) == 0 ? true : false;

		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);
		// adder.add(word1, word2);
		boolean pf = adder.hasParity();
		if (pf == parityFlag) {
			int a = 1;
		} // if
	}// checkParity

	private static void addWord() {
		int val1 = 25755;
		int val2 = 32203;
		byte[] word1 = loadWord(val1);
		byte[] word2 = loadWord(val2);
		adder.addWord(word1, word2);
		boolean zeroFlag = (((val1 + val2) & 0XFFFF) == 0) ? true : false;
		boolean zflag = adder.isZero();
		boolean x = true;
	}// addWord

	private static void addByte() {
		byte[] bite1 = new byte[] { (byte) 0X4D };
		byte[] bite2 = new byte[] { (byte) 0X33 };

		// adder.add(bite1, bite2);
		boolean zflag = adder.isZero();
	}// addByte

	private static byte[] loadWord(int value) {
		return new byte[] { (byte) (value & 0XFF), (byte) ((value & 0XFF00) >> 8) };
	}// loadWord

	private static int getWordValue(byte[] word) {
		return ((word[1] << 8) & 0XFF00) + (word[0] & 0X00FF);
	}// getWordValue

	private static void showFlags(Adder adder) {
		String sign = adder.hasSign() ? "S" : "s";
		String zero = adder.isZero() ? "Z" : "z";
		String bit5 = adder.hasSign() ? "." : ".";
		String half = adder.hasHalfCarry() ? "H" : "h";
		String bit3 = adder.hasSign() ? "." : ".";
		String PV = adder.hasParity() ? "P" : "p";
		String AS = adder.hasSign() ? "." : ".";
		String carry = adder.hasCarry() ? "C" : "c";
		System.out.printf("   %s%s%s%s %s%s%s%s%n", sign, zero, bit5, half, bit3, PV, AS, carry);
	}// showFlags

	private static final String BYTE_ARG = "ByteArg";
	private static final String WORD_ARG = "WordArg";
}// AdderTestLite
