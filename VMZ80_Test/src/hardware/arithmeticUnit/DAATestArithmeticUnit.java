package hardware.arithmeticUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import hardware.ArithmeticUnit;
import hardware.ConditionCodeRegister;

public class DAATestArithmeticUnit {
	ArithmeticUnit au = ArithmeticUnit.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	byte arg1, arg2, result, daaResult, ans;
	boolean CY, HC, CY1, HC1;
	int intDiff, intSum;
	String message;

	boolean sign, zero, halfCarry, parity, nFlag, carry;
	boolean cy, hc;
	String flags, flagsDAA, sArg1;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		au = ArithmeticUnit.getInstance();
	}// setUp

	@Test
	public void testDAA() {
		testDAA("ADD");
		testDAA("SUB");
	}

	// @Test
	public void testDAA(String priorOperation) {
		// assume its ADD
		String fileName = "/daaAddRevision01.txt";
		String operator = "+";
		if (priorOperation.equals("SUB")) {
			fileName = "/daaSubRevision01.txt";
			operator = "-";
		} // if prior operation

		try {
			InputStream inputStream = this.getClass().getResourceAsStream(fileName);
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.equals(";")) {
					scanner.nextLine();
					continue;
				} // if - skip the line

				arg1 = getValue(sArg1);
				arg2 = getValue(scanner.next());

				result = getValue(scanner.next());
				flags = scanner.next();

				// sign = flags.subSequence(0, 1).equals("1") ? true : false;
				// zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				// parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				daaResult = getValue(scanner.next());

				flagsDAA = scanner.next();

				message = String.format("%02X %s %02X = %02X", arg1, operator, arg2, result);
				// System.out.println(message);
				// // prior operation
				if (priorOperation.equals("ADD")) {
					assertThat("ans: " + message, result, equalTo(au.add(arg1, arg2)));
				} else {
					assertThat("ans: " + message, result, equalTo(au.sub(arg1, arg2)));
				} // if - prior operation
				assertThat("CY: " + message, carry, equalTo(au.isCarryFlagSet()));
				assertThat("HC: " + message, halfCarry, equalTo(au.isHCarryFlagSet()));

				// DAA
				ccr.setCarryFlag(carry);
				ccr.setHFlag(halfCarry);
				ccr.setNFlag(nFlag);

				sign = flagsDAA.subSequence(0, 1).equals("1") ? true : false;
				zero = flagsDAA.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flagsDAA.subSequence(2, 3).equals("1") ? true : false;
				parity = flagsDAA.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flagsDAA.subSequence(4, 5).equals("1") ? true : false;
				carry = flagsDAA.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s %02X --daa--> %02X", priorOperation, result, daaResult);
//				System.out.println(message);

				assertThat("ans: " + message, daaResult,
						equalTo(au.daa(result, ccr.isNFlagSet(), ccr.isCarryFlagSet(), ccr.isHFlagSet())));
				assertThat("sign: " + message, sign, equalTo(au.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " + message, parity, equalTo(au.isParityFlagSet()));

				assertThat("carry: " + message, carry, equalTo(au.isCarryFlagSet()));

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			fail("testAfterADD");
		} // try
	}// testDAA

	///////////////////////////////////////////


	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

}// class AdderDAATest
