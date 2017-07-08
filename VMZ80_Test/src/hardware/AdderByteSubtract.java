package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderByteSubtract {
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
	public void testByteSUB() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsSUB(value1, value2, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 - value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full Sub:  " + value1 + " " + value2, answer, equalTo(adder.sub(bite1, bite2)));
				assertThat("Full Sub Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Sub Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Sub HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full Sub Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Sub Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Sub nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full Sub Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
	}// simpleTestOfSUB

	@Test
	public void testByteSUBWithCarry() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsSUB(value1, value2, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 - (value2 +0)) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				assertThat("Full Sub/WCf:  " + value1 + " " + value2, answer, equalTo(adder.subWithCarry(bite1, bite2,false)));
				assertThat("Full Sub/WCf Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Sub/WCf Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Sub/WCf HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full Sub/WCf Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Sub/WCf Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Sub/WCf nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full Sub/WCf Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
		
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsSUB(value1, value2, AdderTestUtility.BYTE_ARG, true);
				byte answer = (byte) ((value1 - (value2 +1)) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) (value2 );
				assertThat("Full Sub/WCt:  " + value1 + " " + value2, answer, equalTo(adder.subWithCarry(bite1, bite2,true)));
				assertThat("Full Sub/WCt Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Sub/WCt Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Sub/WCt HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full Sub/WCt Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Sub/WCt Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Sub/WCt nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full Sub/WCt Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1	
	}// testByteSUBWithCarry
	
	@Test
	public void testByteDecrement() {
		for (value1 = 0; value1 < 0XFF; value1++) {
				atu.fixFlagsSUB(value1, 1, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 - 1) & 0XFF);
				bite1[0] = (byte) value1;
				assertThat("Full DEC:  " + value1, answer, equalTo(adder.decrement(bite1)));
				assertThat("Full DEC Sign:  " + value1, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full DEC Zero:  " + value1, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full DEC HalfCarry:  " + value1, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full DEC Parity:  " + value1, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full DEC Overflow:  " + value1, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full DEC/WCf nFlag:  " + value1, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full DEC Carry:  " + value1, atu.mCarry, equalTo(adder.hasCarry()));
		} // for val1
	}// testByteIncrement

}// class AdderTest2
