package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderRotateRight {
	Adder adder = Adder.getInstance();
	AdderTestUtility atu = AdderTestUtility.getInstance();
	byte arg, ans;
	boolean carryIn, orginalBit0;
	byte bit0Mask = (byte) 0X01;
	byte bit7Mask = (byte) 0X80;
	byte bit7NotMask = (byte) 0X7F;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		// adder.clearSets();
	}// setUp

	@Test
	public void testRRCA() {
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			orginalBit0 = (arg & bit0Mask) == bit0Mask;
			ans = (byte) (arg >> 1);
			if (orginalBit0) {
				ans = (byte) (ans | bit7Mask);
			} else {
				ans = (byte) (ans & bit7NotMask);
			} // if bit7
//			System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RRCA " + arg, ans, equalTo(adder.rotateRight(arg)));
			assertThat("RRCA Cy" + arg, orginalBit0, equalTo(adder.hasCarry()));
		} // for
	}// testRRCA

	@Test
	public void testRRA() {
		boolean carryResult;
		byte ansWithCarry = 00;
		byte ansWithoutCarry = 00;
		
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			orginalBit0 = (arg & bit0Mask) == bit0Mask;
			ans = (byte) (arg >> 1);	
			carryResult = orginalBit0;
			ansWithCarry = (byte) (ans | bit7Mask);
			ansWithoutCarry = (byte) (ans & bit7NotMask);
//			System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RRA " + arg, ansWithCarry, equalTo(adder.rotateRightThru(arg,true)));
			assertThat("RRA Cy" + arg, orginalBit0, equalTo(adder.hasCarry()));
//			System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RRA " + arg, ansWithoutCarry, equalTo(adder.rotateRightThru(arg,false)));
			assertThat("RRA Cy" + arg, orginalBit0, equalTo(adder.hasCarry()));
		} // for
	}// testRLA

	
	@Test
	public void testRRC() {
		boolean carryResult;
		byte ansWithCarry = 00;
		byte ansWithoutCarry = 00;
		boolean isZero, hasSign, hasParity;
		byte signMask = (byte) 0X80;
		
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			orginalBit0 = (arg & bit0Mask) == bit0Mask;
			ans = (byte) (arg >> 1);	
			
			ansWithoutCarry = (byte) (ans & bit7NotMask);
			carryResult = orginalBit0;
			isZero = (ansWithoutCarry == 0);
			hasSign = (ansWithoutCarry & signMask) == signMask;
			hasParity = atu.getParity(ansWithoutCarry);	
//			System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RRC " + arg, ansWithoutCarry, equalTo(adder.rotateRightThru(arg,false)));
			assertThat("RRC w/oc Cy" + arg, orginalBit0, equalTo(adder.hasCarry()));
			assertThat("RRC w/oc  Sign " + arg, hasSign, equalTo(adder.hasSign()));
			assertThat("RRC w/oc Zero " + arg, isZero, equalTo(adder.isZero()));
			assertThat("RRC w/oc Parity " + arg, hasParity, equalTo(adder.hasParity()));

			ansWithCarry = (byte) (ans | bit7Mask);
			isZero = (ansWithCarry == 0);
			hasSign = (ansWithCarry & signMask) == signMask;
			hasParity = atu.getParity(ansWithCarry);
//			System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RRC " + arg, ansWithCarry, equalTo(adder.rotateRightThru(arg,true)));
			assertThat("RRC Cy carry " + arg, orginalBit0, equalTo(adder.hasCarry()));
			assertThat("RRC Cy  Sign " + arg, hasSign, equalTo(adder.hasSign()));
			assertThat("RRC Cy  Zero " + arg, isZero, equalTo(adder.isZero()));
			assertThat("RRC Cy  Parity " + arg, hasParity, equalTo(adder.hasParity()));
		} // for
	}// testRLA
}// class AdderRotateRight
