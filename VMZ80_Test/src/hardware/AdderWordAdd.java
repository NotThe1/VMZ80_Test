package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class AdderWordAdd {
	Adder adder = Adder.getInstance();
	int value1, value2, answer;
	byte[] bite1 = new byte[] { (byte) 0X00 };
	byte[] bite2 = new byte[] { (byte) 0X00 };
	byte[] word1 = new byte[] { (byte) 0X00, (byte) 0X00 };
	byte[] word2 = new byte[] { (byte) 0X00, (byte) 0X00 };

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		// adder.clearSets();
	}// setUp

	@Test
	public void testWordAdd() {
		Random random = new Random();
		int COUNT = 8000;
		for (int i = 0; i < COUNT; i++) {
			value1 = random.nextInt(0XFFFF);
			value2 = random.nextInt(0XFFFF);
			fixFlagsADD(value1, value2, WORD_ARG, false);
			int answer = ((value1 + value2) & 0XFFFF);
			word1 = loadWord(value1);
			word2 = loadWord(value2);
			// byte[] result = adder.addWord(word1, word2);
			int ans = getWordValue(adder.addWord(word1, word2));
			assertThat("Full AddWord:  " + value1 + " " + value2, answer, equalTo(ans));
			assertThat("Full AddWord Sign:  " + value1 + " " + value2, mSign, equalTo(adder.hasSign()));
			assertThat("Full AddWord Zero:  " + value1 + " " + value2, mZero, equalTo(adder.isZero()));
			assertThat("Full AddWord HalfCarry:  " + value1 + " " + value2, mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full AddWord Parity:  " + value1 + " " + value2, mParity, equalTo(adder.hasParity()));
			assertThat("Full AddWord Overflow:  " + value1 + " " + value2, mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full AddWord Carry:  " + value1 + " " + value2, mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordAdd

	@Test
	public void testWordAddWithCarry() {
		Random random = new Random();
		boolean carryState;
		int carryValue;
		int COUNT = 8000;
		for (int i = 0; i < COUNT; i++) {
			carryState = random.nextBoolean();
			carryValue = carryState?1:0;
			value1 = random.nextInt(0XFFFF);
			value2 = random.nextInt(0XFFFF);
			fixFlagsADD(value1, value2, WORD_ARG, carryState);
			int answer = ((value1 + value2 + carryValue) & 0XFFFF);
			word1 = loadWord(value1);
			word2 = loadWord(value2);
			// byte[] result = adder.addWord(word1, word2);
			int ans = getWordValue(adder.addWordWithCarry(word1, word2,carryState));
			assertThat("Full AddWord/WC:  " + value1 + " " + value2, answer, equalTo(ans));
			assertThat("Full AddWord/WC Sign:  " + value1 + " " + value2, mSign, equalTo(adder.hasSign()));
			assertThat("Full AddWord/WC Zero:  " + value1 + " " + value2, mZero, equalTo(adder.isZero()));
			assertThat("Full AddWord/WC HalfCarry:  " + value1 + " " + value2, mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full AddWord/WC Parity:  " + value1 + " " + value2, mParity, equalTo(adder.hasParity()));
			assertThat("Full AddWord/WC Overflow:  " + value1 + " " + value2, mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full AddWord/WC Carry:  " + value1 + " " + value2, mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordAddWithCarry
	
	@Test
	public void testWordIncrement() {
		Random random = new Random();
		int COUNT = 8000;
		for (int i = 0; i < COUNT; i++) {
			value1 = random.nextInt(0XFFFF);
			fixFlagsADD(value1, 1, WORD_ARG, false);
			int answer = ((value1 + 1) & 0XFFFF);
			word1 = loadWord(value1);
			int ans = getWordValue(adder.incrementWord(word1));
			assertThat("Full AddWord:  " + value1 + " " + value2, answer, equalTo(ans));
			assertThat("Full AddWord Sign:  " + value1 + " " + value2, mSign, equalTo(adder.hasSign()));
			assertThat("Full AddWord Zero:  " + value1 + " " + value2, mZero, equalTo(adder.isZero()));
			assertThat("Full AddWord HalfCarry:  " + value1 + " " + value2, mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full AddWord Parity:  " + value1 + " " + value2, mParity, equalTo(adder.hasParity()));
			assertThat("Full AddWord Overflow:  " + value1 + " " + value2, mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full AddWord Carry:  " + value1 + " " + value2, mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordIncrement
	

	private int getWordValue(byte[] word) {
		return ((word[1] << 8) & 0XFF00) + (word[0] & 0X00FF);
	}// getWordValue

	// returns LSB in index 0 and MSB in index 1
	private byte[] loadWord(int value) {
		return new byte[] { (byte) (value & 0XFF), (byte) ((value & 0XFF00) >> 8) };
	}// loadWord

	private void fixFlagsADD(int arg1, int arg2, String argSize, boolean carryArg) {

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
		mCarry = ((arg1 & sizeMask) + (arg2 & sizeMask) + valueCarry) > sizeMask;

	}// fixFlags
	private void fixFlagsSUB(int arg1, int arg2, String argSize, boolean carryArg) {

		int valueCarry = carryArg ? 1 : 0;
		
		int halfCarryMask = (argSize == BYTE_ARG) ? 0X0F : 0X0FFF;
		int signMask = (argSize == BYTE_ARG) ? 0X80 : 0X8000;
		int sizeMask = (argSize == BYTE_ARG) ? 0XFF : 0XFFFF;
		int argument1 = arg1 & sizeMask;
		int argument2 = (arg2 + valueCarry) & sizeMask;
		
		
		int ans = (argument1 - argument2 ) & sizeMask;

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
		
		//Two's complement
		int notArg2 = ~arg2 & sizeMask;
		boolean halfCarry1 = ((notArg2 & halfCarryMask) + 1) > halfCarryMask;
		boolean carry1 = ((notArg2 & sizeMask) + 1) > sizeMask;
		
		notArg2 = (notArg2+1) & sizeMask;	// make the arg two's complement
		
		//Actual add
		boolean halfCarry2 = (((argument1 & halfCarryMask) + (notArg2 & halfCarryMask)) > halfCarryMask);
		boolean carry2 = (((argument1 & sizeMask) + (notArg2 & sizeMask) ) >  sizeMask);
		
		
		
		mHalfCarry = !(halfCarry0 | halfCarry1 | halfCarry2);
		mCarry = !(carry0 | carry1 | carry2);

	}// fixFlags

	private boolean mSign;
	private boolean mZero;
	private boolean mHalfCarry;
	private boolean mParity;
	private boolean mOverflow;
	private boolean mCarry;

	private static final String BYTE_ARG = "ByteArg";
	private static final String WORD_ARG = "WordArg";

}// class AdderTest2
