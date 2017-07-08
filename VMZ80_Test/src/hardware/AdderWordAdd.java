package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class AdderWordAdd {
	private static AdderTestUtility atu = AdderTestUtility.getInstance();
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
			atu.fixFlagsADD(value1, value2, AdderTestUtility.WORD_ARG, false);
			int answer = ((value1 + value2) & 0XFFFF);
			word1 = atu.loadWord(value1);
			word2 = atu.loadWord(value2);
			// byte[] result = adder.addWord(word1, word2);
			int ans = atu.getWordValue(adder.addWord(word1, word2));
			assertThat("Full AddWord:  " + value1 + " " + value2, answer, equalTo(ans));
			assertThat("Full AddWord Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
			assertThat("Full AddWord Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
			assertThat("Full AddWord HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full AddWord Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
			assertThat("Full AddWord Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full AddWord nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
			assertThat("Full AddWord Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));

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
			atu.fixFlagsADD(value1, value2, AdderTestUtility.WORD_ARG, carryState);
			int answer = ((value1 + value2 + carryValue) & 0XFFFF);
			word1 = atu.loadWord(value1);
			word2 = atu.loadWord(value2);
			// byte[] result = adder.addWord(word1, word2);
			int ans = atu.getWordValue(adder.addWordWithCarry(word1, word2,carryState));
			assertThat("Full AddWord/WC:  " + value1 + " " + value2, answer, equalTo(ans));
			assertThat("Full AddWord/WC Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
			assertThat("Full AddWord/WC Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
			assertThat("Full AddWord/WC HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full AddWord/WC Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
			assertThat("Full AddWord/WC Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full AddWord/WC nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
			assertThat("Full AddWord/WC Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordAddWithCarry
	
	@Test
	public void testWordIncrement() {
			for (value1 = 0; value1 < 0XFFFF; value1++) {
			atu.fixFlagsADD(value1, 1, AdderTestUtility.WORD_ARG, false);
			int answer = ((value1 + 1) & 0XFFFF);
			word1 = atu.loadWord(value1);
			int ans = atu.getWordValue(adder.incrementWord(word1));
			assertThat("Full WordIncrement:  " + value1, answer, equalTo(ans));
			assertThat("Full WordIncrement Sign:  " + value1, atu.mSign, equalTo(adder.hasSign()));
			assertThat("Full WordIncrement Zero:  " + value1, atu.mZero, equalTo(adder.isZero()));
			assertThat("Full WordIncrement HalfCarry:  " + value1, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full WordIncrement Parity:  " + value1, atu.mParity, equalTo(adder.hasParity()));
			assertThat("Full WordIncrement Overflow:  " + value1, atu.mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full WordIncrement nFlag:  " + value1, atu.mNFlag, equalTo(adder.isNFlagSet()));
			assertThat("Full WordIncrement Carry:  " + value1, atu.mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordIncrement

}// class AdderTest2
