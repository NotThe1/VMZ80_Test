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
		int COUNT = 16000;
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
			assertThat("Full SubWord/WC Carry:  " + value1 + " " + value2, atu. mCarry, equalTo(adder.hasCarry()));

		} // for val1
	}// testWordSubWithCarry

}// class AdderTest2
