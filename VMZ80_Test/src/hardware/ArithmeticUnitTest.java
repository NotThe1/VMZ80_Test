package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class ArithmeticUnitTest {
	ConditionCodeRegister ccr;
	ArithmeticUnit au;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		ccr = ConditionCodeRegister.getInstance();
		au = ArithmeticUnit.getInstance();
	}// setUp

	@Test
	public void test8BitAddValues() {
		int op1 = 00;
		int op2 = 00;
		byte ans;
		String title;
		for (op1 = 0; op1 < 0X100; op1++) {
			for (op2 = 0; op2 < 0X100; op2++) {
				ans = (byte) ((op1 + op2) & MASK_BYTE);
				title = String.format("8 Bit Values %02X and %02X", op1, op2);
				assertThat(title, ans, equalTo(au.addBytes((byte) op1, (byte) op2)));
			} // for op2
		} // for op1
	}// test8BitAddValue

	@Test
	public void test8BitAddFlags() {
		/**
		 * For addition, operands with different signs never cause Overflow. When adding operands with like signs and
		 * the result has a different sign, the Overflow Flag(pvFlag) is set.
		 */
		ccr.clearAllCodes();
		byte op1 = (byte) 0XA0; // -
		byte op2 = (byte) 0X40; // +
		byte ans = (byte) (op1 + op2); // E0

		assertThat("Different signs - 1", ans, equalTo(au.addBytes(op1, op2)));
		assertThat("Different signs - 2", false, equalTo(ccr.isPvFlagSet()));
		assertThat("Different signs - 3", true, equalTo(ccr.isSignFlagSet()));
		assertThat("Different signs - 4", false, equalTo(ccr.isZeroFlagSet()));
		assertThat("Different signs - 4A", false, equalTo(ccr.isCarryFlagSet()));
		assertThat("Different signs - 4B", false, equalTo(ccr.isNFlagSet()));

		op1 = (byte) 0X50; // +
		op2 = (byte) 0X90; // - //E0
		ans = (byte) (op1 + op2);
		assertThat("Different signs - 5", ans, equalTo(au.addBytes(op1, op2)));
		assertThat("Different signs - 6", false, equalTo(ccr.isPvFlagSet()));
		assertThat("Different signs - 7", true, equalTo(ccr.isSignFlagSet()));
		assertThat("Different signs - 8", false, equalTo(ccr.isZeroFlagSet()));
		assertThat("Different signs - 8A", false, equalTo(ccr.isCarryFlagSet()));
		assertThat("Different signs - 8B", false, equalTo(ccr.isNFlagSet()));

		op1 = (byte) 0X00; // +
		op2 = (byte) 0X10; // +
		ans = (byte) (op1 + op2); // + 10
		assertThat("Same signs - 1", ans, equalTo(au.addBytes(op1, op2)));
		assertThat("Same signs - 2", false, equalTo(ccr.isPvFlagSet()));
		assertThat("Same signs - 3", false, equalTo(ccr.isSignFlagSet()));
		assertThat("Same signs - 4", false, equalTo(ccr.isZeroFlagSet()));
		assertThat("Same signs - 4A", false, equalTo(ccr.isCarryFlagSet()));
		assertThat("Same signs - 4B", false, equalTo(ccr.isNFlagSet()));

		op1 = (byte) 0XFF; // -
		op2 = (byte) 0XFE; // -
		ans = (byte) (op1 + op2); // - 1FD
		assertThat("Same signs - 5", ans, equalTo(au.addBytes(op1, op2)));
		assertThat("Same signs - 6", false, equalTo(ccr.isPvFlagSet()));
		assertThat("Same signs - 7", true, equalTo(ccr.isSignFlagSet()));
		assertThat("Same signs - 8", false, equalTo(ccr.isZeroFlagSet()));
		assertThat("Same signs - 8A", true, equalTo(ccr.isCarryFlagSet()));
		assertThat("Same signs - 8B", false, equalTo(ccr.isNFlagSet()));

		op1 = (byte) 0X80; // -
		op2 = (byte) 0XFF; // -
		ans = (byte) (op1 + op2); // - 17F
		assertThat("Same signs - 9", ans, equalTo(au.addBytes(op1, op2)));
		assertThat("Same signs - 10", true, equalTo(ccr.isPvFlagSet()));
		assertThat("Same signs - 11", false, equalTo(ccr.isSignFlagSet()));
		assertThat("Same signs - 12", false, equalTo(ccr.isZeroFlagSet()));
		assertThat("Same signs - 12A", true, equalTo(ccr.isCarryFlagSet()));
		assertThat("Same signs - 12B", false, equalTo(ccr.isNFlagSet()));

		op1 = (byte) 0X7F; // +
		op2 = (byte) 0X7F; // +
		ans = (byte) (op1 + op2); // - FE
		assertThat("Same signs - 13", ans, equalTo(au.addBytes(op1, op2)));
		assertThat("Same signs - 14", true, equalTo(ccr.isPvFlagSet()));
		assertThat("Same signs - 15", true, equalTo(ccr.isSignFlagSet()));
		assertThat("Same signs - 16", false, equalTo(ccr.isZeroFlagSet()));
		assertThat("Same signs - 16A", false, equalTo(ccr.isCarryFlagSet()));
		assertThat("Same signs - 16B", false, equalTo(ccr.isNFlagSet()));

		op1 = (byte) 0X00; // +
		op2 = (byte) 0X00; // +
		ans = (byte) (op1 + op2); // - 00
		assertThat("Same signs - 17", ans, equalTo(au.addBytes(op1, op2)));
		assertThat("Same signs - 18", false, equalTo(ccr.isPvFlagSet()));
		assertThat("Same signs - 19", false, equalTo(ccr.isSignFlagSet()));
		assertThat("Same signs - 20", true, equalTo(ccr.isZeroFlagSet()));
		assertThat("Same signs - 20A", false, equalTo(ccr.isCarryFlagSet()));
		assertThat("Same signs - 20B", false, equalTo(ccr.isNFlagSet()));

	}// test8BitAdd()

	@Test
	public void test16BitAdd() {
		ccr.clearAllCodes();
		int op1 = 0X1;
		int op2 = 0Xffff;
		int ans = (op1 + op2) & MASK_WORD;

		assertThat("int Add - 1", ans, equalTo(au.addWords(op1, op2)));
		assertThat("int Add - 2", true, equalTo(ccr.isCarryFlagSet()));
		assertThat("int Add - 3", false, equalTo(ccr.isNFlagSet()));
		// assertThat("int Add - 2", true, equalTo(ccr.isHFlagSet()));

		op1 = 0X1;
		op2 = 0Xff;
		ans = (op1 + op2) & MASK_WORD;

		assertThat("int Add - 4", ans, equalTo(au.addWords(op1, op2)));
		assertThat("int Add - 5", false, equalTo(ccr.isCarryFlagSet()));
		assertThat("int Add - 6", false, equalTo(ccr.isNFlagSet()));

	}// test8BitAddCarryOut

	@Test
	public void test16BitAddValues() {
		ccr.clearAllCodes();
		Random random = new Random();
		int loopCount = 1000;
		int op1;
		int op2;
		int ans;
		String title;

		for (loopCount = 0; loopCount < 1000; loopCount++) {
			op1 = random.nextInt(65535);
			op2 = random.nextInt(65535);
			ans = (op1 + op2) & MASK_WORD;
			title = String.format("16 Bit Values %02X and %02X", op1, op2);
			assertThat(title, ans, equalTo(au.addWords(op1, op2)));
		} // for loopCount
	}// test8BitAddValue

	@Test
	public void test8BitADC() {
		ccr.clearAllCodes();
		byte op1 = (byte) 0X7F; // +
		byte op2 = (byte) 0X7F; // +
		ccr.setCarryFlag(true);
		byte ans = (byte) (op1 + op2 + 1); // - FE
		assertThat("8BitADC - 1", ans, equalTo(au.addBytesWithCarry(op1, op2)));
		assertThat("8BitADC - 2", true, equalTo(ccr.isPvFlagSet()));
		assertThat("8BitADC - 3", true, equalTo(ccr.isSignFlagSet()));
		assertThat("8BitADC - 4", false, equalTo(ccr.isZeroFlagSet()));
		assertThat("8BitADC - 5", false, equalTo(ccr.isCarryFlagSet()));
		assertThat("8BitADC - 6", false, equalTo(ccr.isNFlagSet()));

		ccr.clearAllCodes();
		op1 = (byte) 0X7F; // +
		op2 = (byte) 0X80; // +
		ccr.setCarryFlag(true);
		ans = (byte) (op1 + op2 + 1); // - 0
		assertThat("8BitADC - 11", ans, equalTo(au.addBytesWithCarry(op1, op2)));
		assertThat("8BitADC - 12", false, equalTo(ccr.isPvFlagSet()));
		assertThat("8BitADC - 13", false, equalTo(ccr.isSignFlagSet()));
		assertThat("8BitADC - 14", true, equalTo(ccr.isZeroFlagSet()));
		assertThat("8BitADC - 15", true, equalTo(ccr.isCarryFlagSet()));
		assertThat("8BitADC - 16", false, equalTo(ccr.isNFlagSet()));

		ccr.setCarryFlag(false);
		ans = (byte) (op1 + op2); // - FE
		assertThat("8BitADC - 21", ans, equalTo(au.addBytesWithCarry(op1, op2)));
		assertThat("8BitADC - 32", false, equalTo(ccr.isPvFlagSet()));

	}// test8BitADC

	@Test
	public void test8BitADCValues() {
		Random random = new Random();
		int op1 = 00;
		int op2 = 00;
		int carry = 0;
		byte ans;
		String title;
		for (op1 = 0; op1 < 0X100; op1++) {
			for (op2 = 0; op2 < 0X100; op2++) {
				if (random.nextBoolean()) {
					ccr.setCarryFlag(true);
					carry = 1;
				} else {
					ccr.setCarryFlag(false);
					carry = 0;
				}
				ans = (byte) ((op1 + op2 + carry) & MASK_BYTE);
				title = String.format("8 Bit Values %02X and %02X", op1, op2);
				assertThat(title, ans, equalTo(au.addBytesWithCarry((byte) op1, (byte) op2)));
			} // for op2
		} // for op1
	}// test8BitAddValue

	private static final int MASK_BYTE = 0X00FF;
	private static final int MASK_WORD = 0XFFFF;
}// class ArithmeticUnitTest
