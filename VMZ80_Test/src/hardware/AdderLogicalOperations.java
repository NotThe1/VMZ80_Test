package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderLogicalOperations {
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
	public void testByteAND() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsAND(value1, value2);
				byte answer = (byte) ((value1 & value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full And:  " + value1 + " " + value2, answer, equalTo(adder.and(bite1, bite2)));
				assertThat("Full And Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full And Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full And HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full And Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full And Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full And Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
	}// testByteAdd

	@Test
	public void testByteOR() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsOR(value1, value2);
				byte answer = (byte) ((value1 | value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full Or:  " + value1 + " " + value2, answer, equalTo(adder.or(bite1, bite2)));
				assertThat("Full Or Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Or Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Or HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full Or Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Or Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Or Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
	}// testByteAdd

	@Test
	public void testByteXOR() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsXOR(value1, value2);
				byte answer = (byte) ((value1 ^ value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full Xor:  " + value1 + " " + value2, answer, equalTo(adder.xor(bite1, bite2)));
				assertThat("Full Xor Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Xor Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Xor HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full Xor Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Xor Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Xor Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
	}// testByteAdd


	@Test
	public void testByteCP() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsSUB(value1, value2,AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 - value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				adder.compare(bite1, bite2);
				assertThat("Full CP Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full CP Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full CP HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full CP Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full CP Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full CP Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
	}// testByteAdd
	
	@Test
	public void testByteCPL() {
		for (value1 = 0; value1 < 0XFF; value1++) {	
				byte answer = (byte) (~value1 & 0XFF);
				bite1[0] = (byte) value1;
				
				assertThat("Full CPL:  " + value1 + " " + value2, answer, equalTo(adder.complement(bite1)[0]));	
				assertThat("Full CPL HalfCarry:  " + value1 + " " + value2, true, equalTo(adder.hasHalfCarry()));
		} // for val1
	}// testByteAdd

	@Test
	public void testByteNEG() {
		for (value1 = 0; value1 < 0XFF; value1++) {
				atu.fixFlagsSUB(0, value1,AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((0 - value1) & 0XFF);
				bite1[0] = (byte) value1;
				
				assertThat("Full CPL:  " + value1 + " " + value2, answer, equalTo(adder.negate(bite1)));	

				assertThat("Full CP Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full CP Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full CP HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full CP Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full CP Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full CP Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
		} // for val1
	}// testByteAdd
	

}// class AdderTest2
