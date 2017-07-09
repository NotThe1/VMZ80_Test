package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderRotateLeft {
	Adder adder = Adder.getInstance();
	byte arg, ans;
	boolean carryIn, orginalBit7;
	byte bit7Mask = (byte) 0X80;
	byte bit0Mask = (byte) 0X01;
	byte bit0NotMask = (byte) 0XFE;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		// adder.clearSets();
	}// setUp

	@Test
	public void testRLCA() {
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			orginalBit7 = (arg & bit7Mask) == bit7Mask;
			ans = (byte) (arg << 1);
			if (orginalBit7) {
				ans = (byte) (ans | bit0Mask);
			} else {
				ans = (byte) (ans & bit0NotMask);
			} // if bit7
				// System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RLCA " + arg, ans, equalTo(adder.rotateLeft(arg)));
			assertThat("RLCA Cy" + arg, orginalBit7, equalTo(adder.hasCarry()));
		} // for

	}// testRLCA

	@Test
	public void testRLA() {
		boolean carryResult;
		byte ansWithCarry = 00;
		byte ansWithoutCarry = 00;
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			orginalBit7 = (arg & bit7Mask) == bit7Mask;
			ans = (byte) (arg << 1);
			carryResult = orginalBit7;
			ansWithCarry = (byte) (ans | bit0Mask);
			ansWithoutCarry = (byte) (ans & bit0NotMask);
			// System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RLA w/0 Carry" + arg, ansWithoutCarry, equalTo(adder.rotateLeftThru(arg, false)));
			assertThat("RLA  w/0 Carry" + arg, carryResult, equalTo(adder.hasCarry()));
			// System.out.printf("Arg = %02X, ans = %02X%n", arg, ansWithCarry);
			assertThat("RLA with Carry" + arg, ansWithCarry, equalTo(adder.rotateLeftThru(arg, true)));
			assertThat("RLA  with Carry" + arg, carryResult, equalTo(adder.hasCarry()));
		} // for

	}// testRLA

	@Test
	public void testRLC() {
		boolean carryResult;
		byte ansWithCarry = 00;
		byte ansWithoutCarry = 00;
		boolean isZero, hasSign, hasParity;
		byte signMask = (byte) 0X80;

		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			orginalBit7 = (arg & bit7Mask) == bit7Mask;
			ans = (byte) (arg << 1);
			carryResult = orginalBit7;
			ansWithoutCarry = (byte) (ans & bit0NotMask);
			isZero = (ansWithoutCarry == 0);
			hasSign = (ansWithoutCarry & signMask) == signMask;
			hasParity = getParity(ansWithoutCarry);
//			System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("RLA w/oc " + arg, ansWithoutCarry, equalTo(adder.rotateLeftThru(arg, false)));
			assertThat("RLA  w/oc Carry " + arg, carryResult, equalTo(adder.hasCarry()));
			assertThat("RLA  w/oc Sign " + arg, hasSign, equalTo(adder.hasSign()));
			assertThat("RLA  w/oc Zero " + arg, isZero, equalTo(adder.isZero()));
			assertThat("RLA  w/oc Parity " + arg, hasParity, equalTo(adder.hasParity()));

			ansWithCarry = (byte) (ans | bit0Mask);
			isZero = (ansWithCarry == 0);
			hasSign = (ansWithCarry & signMask) == signMask;
			hasParity = getParity(ansWithCarry);
			// System.out.printf("Arg = %02X, ans = %02X%n", arg, ansWithCarry);
			assertThat("RLA with " + arg, ansWithCarry, equalTo(adder.rotateLeftThru(arg, true)));
			assertThat("RLA  with Carry" + arg, carryResult, equalTo(adder.hasCarry()));
			assertThat("RLA  with Sign " + arg, hasSign, equalTo(adder.hasSign()));
			assertThat("RLA  with Zero " + arg, isZero, equalTo(adder.isZero()));
			assertThat("RLA  with Parity " + arg, hasParity, equalTo(adder.hasParity()));

		} // for

	}// testRLC

	private boolean getParity(byte value) {
		String s = Integer.toBinaryString(value);
		s = s.replace("0", "");
		return s.length() % 2 == 0;
	}

}// class AdderRotateLeft
