package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderByteAdd {
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
	public void testByteAdd() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsADD(value1, value2, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 + value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full Add:  " + value1 + " " + value2, answer, equalTo(adder.add(bite1, bite2)));
				assertThat("Full Add Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Add Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Add HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full Add Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Add Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Add Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
	}// testByteAdd

	@Test
	public void testByteAddWithCarry() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsADD(value1, value2, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 + value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full Add/WC:  " + value1 + " " + value2, answer,
						equalTo(adder.addWithCarry(bite1, bite2, false)));
				assertThat("Full Add/WC Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Add/WC Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Add/WC HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry,
						equalTo(adder.hasHalfCarry()));
				assertThat("Full Add/WC Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Add/WC Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Add/WC Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));

				atu.fixFlagsADD(value1, value2, AdderTestUtility.BYTE_ARG, true);
				answer = (byte) ((value1 + value2 + 1) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full Add/WC:  " + value1 + " " + value2, answer,
						equalTo(adder.addWithCarry(bite1, bite2, true)));
				assertThat("Full Add/WC Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Add/WC Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Add/WC HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry,
						equalTo(adder.hasHalfCarry()));
				assertThat("Full Add/WC Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Add/WC Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Add/WC Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));

			} // for val2
		} // for val1
	}// testByteAddWithCarry


	@Test
	public void testByteIncrement() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			atu.fixFlagsADD(value1, 1, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 + 1) & 0XFF);
				bite1[0] = (byte) value1;
				assertThat("Full INC:  " + value1, answer, equalTo(adder.increment(bite1)));
				assertThat("Full INC Sign:  " + value1, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full INC Zero:  " + value1, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full INC HalfCarry:  " + value1, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full INC Parity:  " + value1, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full INC Overflow:  " + value1, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full INC Carry:  " + value1, atu.mCarry, equalTo(adder.hasCarry()));
		} // for val1
	}// testByteIncrement

}// class AdderTest2
