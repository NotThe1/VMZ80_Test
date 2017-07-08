package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class AdderWordSubtract {
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
	public void testWordSubWithCarry() {
		Random random = new Random();
		boolean carryState;
		int carryValue;
		int COUNT = 8000;
		for (int i = 0; i < COUNT; i++) {
			carryState = random.nextBoolean();
			carryValue = carryState?1:0;
			value1 = random.nextInt(0XFFFF);
			value2 = random.nextInt(0XFFFF);
			atu.fixFlagsSUB(value1, value2, AdderTestUtility.WORD_ARG, carryState);
			int answer = ((value1 - (value2 + carryValue)) & 0XFFFF);
			word1 = atu.loadWord(value1);
			word2 = atu.loadWord(value2);
			// byte[] result = adder.addWord(word1, word2);
			int ans = atu.getWordValue(adder.subWordWithCarry(word1, word2,carryState));
			assertThat("Full SubWord/WC:  " + value1 + " " + value2, answer, equalTo(ans));
			assertThat("Full SubWord/WC Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
			assertThat("Full SubWord/WC Zero:  " + value1 + " " + value2,atu. mZero, equalTo(adder.isZero()));
			assertThat("Full SubWord/WC HalfCarry:  " + value1 + " " + value2, atu. mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full SubWord/WC Parity:  " + value1 + " " + value2, atu. mParity, equalTo(adder.hasParity()));
			assertThat("Full SubWord/WC Overflow:  " + value1 + " " + value2, atu. mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full SubWord nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
			assertThat("Full SubWord/WC Carry:  " + value1 + " " + value2, atu. mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordSubWithCarry
	
	@Test
	public void testWordDecrement() {
		for (int value1 = 0; value1 <0XFFFF ; value1++) {
			
			atu.fixFlagsSUB(value1, 1, AdderTestUtility.WORD_ARG, false);
			int answer = (value1 -1) & 0XFFFF;
			word1 = atu.loadWord(value1);
			int ans = atu.getWordValue(adder.decrementWord(word1));
			assertThat("Full DecWord:  " + value1, answer, equalTo(ans));
			assertThat("Full DecWord Sign:  " + value1, atu.mSign, equalTo(adder.hasSign()));
			assertThat("Full DecWord Zero:  " + value1, atu.mZero, equalTo(adder.isZero()));
			assertThat("Full DecWord HalfCarry:  " + value1, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
			assertThat("Full DecWord Parity:  " + value1, atu.mParity, equalTo(adder.hasParity()));
			assertThat("Full DecWord Overflow:  " + value1, atu.mOverflow, equalTo(adder.hasOverflow()));
			assertThat("Full DecWord nFlag:  " + value1, atu.mNFlag, equalTo(adder.isNFlagSet()));
			assertThat("Full DecWord Carry:  " + value1, atu.mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordAdd


}// class AdderTest2
