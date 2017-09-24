package hardware.arithmeticUnit;

import hardware.ArithmeticUnit;

public class TestUtilityArithmeticUnit {
	private static TestUtilityArithmeticUnit instance = new TestUtilityArithmeticUnit();

	public static TestUtilityArithmeticUnit getInstance() {
		return instance;
	}// Constructor

	public int getWordValue(byte[] word) {
		return ((word[1] << 8) & 0XFF00) + (word[0] & 0X00FF);
	}// getWordValue

	// returns LSB in index 0 and MSB in index 1
	public byte[] loadWord(int value) {
		return new byte[] { (byte) (value & 0XFF), (byte) ((value & 0XFF00) >> 8) };
	}// loadWord

	public void fixFlagsAND(int arg1, int arg2) {

		int signMask =  0X80;
		int sizeMask =  0XFF;
		int argument1 = arg1 & sizeMask;
		int argument2 = arg2 & sizeMask;

		int ans = (argument1 & argument2 ) & sizeMask;

		mSign = (ans & signMask) == signMask;
		mZero = ans == 0;
		mHalfCarry = true;

		String bits = Integer.toBinaryString(ans);
		bits = bits.replace("0", "");
		mParity = (bits.length() % 2) == 0;

		mNFlag = false;
		mCarry = false;

	}// fixFlags
	
	public void fixFlagsOR(int arg1, int arg2) {

		int signMask =  0X80;
		int sizeMask =  0XFF;
		int argument1 = arg1 & sizeMask;
		int argument2 = arg2 & sizeMask;

		int ans = (argument1 | argument2 ) & sizeMask;

		mSign = (ans & signMask) == signMask;
		mZero = ans == 0;
		mHalfCarry = false;

		String bits = Integer.toBinaryString(ans);
		bits = bits.replace("0", "");
		mParity = (bits.length() % 2) == 0;

		mNFlag = false;
		mCarry = false;

	}// fixFlags
	
	public void fixFlagsXOR(int arg1, int arg2) {

		int signMask =  0X80;
		int sizeMask =  0XFF;
		int argument1 = arg1 & sizeMask;
		int argument2 = arg2 & sizeMask;

		int ans = (argument1 ^ argument2 ) & sizeMask;

		mSign = (ans & signMask) == signMask;
		mZero = ans == 0;
		mHalfCarry = false;

		String bits = Integer.toBinaryString(ans);
		bits = bits.replace("0", "");
		mParity = (bits.length() % 2) == 0;

		mNFlag = false;
		mCarry = false;

	}// fixFlags
	

	
	public void fixFlagsADD(int arg1, int arg2, String argSize, boolean carryArg) {

		int halfCarryMask = (argSize == BYTE_ARG) ? 0X0F : 0X0FFF;
		int signMask = (argSize == BYTE_ARG) ? 0X80 : 0X8000;
		int sizeMask = (argSize == BYTE_ARG) ? 0XFF : 0XFFFF;
		int argument1 = arg1 & sizeMask;
		int argument2 = arg2 & sizeMask;
		int valueCarry = carryArg ? 1 : 0;
		int ans = (argument1 + argument2 + valueCarry) & sizeMask;

		mSign = (ans & signMask) == signMask;
		mZero = ans == 0;
		mHalfCarry = ((arg1 & halfCarryMask) + (arg2 & halfCarryMask) + valueCarry) > halfCarryMask;

		String bits = Integer.toBinaryString(ans);
		bits = bits.replace("0", "");
		mParity = (bits.length() % 2) == 0;

		boolean sign1 = (argument1 & signMask) == signMask;
		boolean sign2 = (argument2 & signMask) == signMask;
		boolean signAns = (ans & signMask) == signMask;
		mOverflow = false;
		if (!(sign1 ^ sign2)) {
			mOverflow = sign1 ^ signAns;
		} // if
		mNFlag = false;
		mCarry = ((arg1 & sizeMask) + (arg2 & sizeMask) + valueCarry) > sizeMask;

	}// fixFlags
	



	public void fixFlagsSUB(int arg1, int arg2, String argSize, boolean carryArg) {

		int valueCarry = carryArg ? 1 : 0;

		int halfCarryMask = (argSize == BYTE_ARG) ? 0X0F : 0X0FFF;
		int signMask = (argSize == BYTE_ARG) ? 0X80 : 0X8000;
		int sizeMask = (argSize == BYTE_ARG) ? 0XFF : 0XFFFF;
		int argument1 = arg1 & sizeMask;
		int argument2 = (arg2 + valueCarry) & sizeMask;

		int ans = (argument1 - argument2) & sizeMask;

		mSign = (ans & signMask) == signMask;
		mZero = ans == 0;

		String bits = Integer.toBinaryString(ans);
		bits = bits.replace("0", "");
		mParity = (bits.length() % 2) == 0;

		boolean sign1 = (argument1 & signMask) == signMask;
		boolean sign2 = (arg2 & signMask) == signMask;
		boolean signAns = (ans & signMask) == signMask;

		mOverflow = false;
		if ((sign1 ^ sign2)) {
			mOverflow = sign2 == signAns;
		} // if

		// carry ?
		boolean halfCarry0 = ((arg2 & halfCarryMask) + valueCarry) > halfCarryMask;
		boolean carry0 = ((arg2 & sizeMask) + valueCarry) > sizeMask;
		arg2 = (arg2 + valueCarry);

		// Two's complement
		int notArg2 = ~arg2 & sizeMask;
		boolean halfCarry1 = ((notArg2 & halfCarryMask) + 1) > halfCarryMask;
		boolean carry1 = ((notArg2 & sizeMask) + 1) > sizeMask;

		notArg2 = (notArg2 + 1) & sizeMask; // make the arg two's complement

		// Actual add
		boolean halfCarry2 = (((argument1 & halfCarryMask) + (notArg2 & halfCarryMask)) > halfCarryMask);
		boolean carry2 = (((argument1 & sizeMask) + (notArg2 & sizeMask)) > sizeMask);

		mNFlag= true;
		mHalfCarry = !(halfCarry0 | halfCarry1 | halfCarry2);
		mCarry = !(carry0 | carry1 | carry2);

	}// fixFlags

	public void showFlags(ArithmeticUnit adder) {
		String sign = adder.hasSign() ? "S" : "s";
		String zero = adder.isZero() ? "Z" : "z";
		String bit5 = adder.hasSign() ? "." : ".";
		String half = adder.hasHalfCarry() ? "H" : "h";
		String bit3 = adder.hasSign() ? "." : ".";
		String PV = adder.hasParity() ? "P" : "p";
		String AS = adder.isNFlagSet() ? "N" : "n";
		String carry = adder.hasCarry() ? "C" : "c";
		System.out.printf("   %s%s%s%s %s%s%s%s%n", sign, zero, bit5, half, bit3, PV, AS, carry);
	}// showFlags

	public void showFlagsV(ArithmeticUnit adder) {
		String sign = adder.hasSign() ? "S" : "s";
		String zero = adder.isZero() ? "Z" : "z";
		String bit5 = adder.hasSign() ? "." : ".";
		String half = adder.hasHalfCarry() ? "H" : "h";
		String bit3 = adder.hasSign() ? "." : ".";
		String PV = adder.hasOverflow() ? "V" : "v";
		String AS = adder.isNFlagSet() ? "N" : "n";
		String carry = adder.hasCarry() ? "C" : "c";
		System.out.printf("   %s%s%s%s %s%s%s%s%n", sign, zero, bit5, half, bit3, PV, AS, carry);
	}// showFlags

	public void showFixedFlagsP() {
		String sign = mSign ? "S" : "s";
		String zero = mZero ? "Z" : "z";
		String bit5 = ".";
		String half = mHalfCarry ? "H" : "h";
		String bit3 = ".";
		String PV = mParity ? "P" : "p";
		String AS = mNFlag ? "N" : "n";
		String carry = mCarry ? "C" : "c";
		System.out.printf("   %s%s%s%s %s%s%s%s%n", sign, zero, bit5, half, bit3, PV, AS, carry);
	}// showFixedFlags

	public void showFixedFlagsV() {
		String sign = mSign ? "S" : "s";
		String zero = mZero ? "Z" : "z";
		String bit5 = ".";
		String half = mHalfCarry ? "H" : "h";
		String bit3 = ".";
		String PV = mOverflow ? "V" : "v";
		String AS = mNFlag ? "N" : "n";
		String carry = mCarry ? "C" : "c";
		System.out.printf("   %s%s%s%s %s%s%s%s%n", sign, zero, bit5, half, bit3, PV, AS, carry);
	}// showFixedFlags
	
	public boolean getParity(byte value) {
		String s = Integer.toBinaryString(value);
		s = s.replace("0", "");
		return s.length() % 2 == 0;
	}//getParity

	

	public boolean mSign;
	public boolean mZero;
	public boolean mHalfCarry;
	public boolean mParity;
	public boolean mOverflow;
	public boolean mNFlag;
	public boolean mCarry;

	public static final String BYTE_ARG = "ByteArg";
	public static final String WORD_ARG = "WordArg";

}// class AdderTestUtility
