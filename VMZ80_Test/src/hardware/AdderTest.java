package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class AdderTest {
	Adder adder;
	byte[] bite1 = new byte[] { (byte) 00 };
	byte[] bite2 = new byte[] { (byte) 00 };
	byte[] word1 = new byte[] { (byte) 00, (byte) 00 };
	byte[] word2 = new byte[] { (byte) 00, (byte) 00 };
	byte[] answers;
	int val1, val2, val3, val4, ansByte, ansWord;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		// adder.clearSets();
	}// setUp

	@Test
	public void testByteAdd() {
		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				byte answer = (byte) ((val1 + val2) & 0XFF);
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				assertThat("Full Add:  " + val1 + " " + val2, answer, equalTo(adder.add(bite1, bite2)));
			} // for val2
		} // for val1
	}// simpleTestOfAdd

	@Test
	public void testByteAddWithCarry() {
		Random random = new Random();
		boolean carryVar;
		int carry;
		byte answer;
		String msg;
		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				carryVar = random.nextBoolean();
				carry = carryVar ? 1 : 0;
				answer = (byte) ((val1 + val2 + carry) & 0XFF);
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				msg = String.format("Full Add With Carry, var1 = %02x, var2 = %02X, Carry = %s", val1, val2, carryVar);
				assertThat(msg, answer, equalTo(adder.addWithCarry(bite1, bite2,carryVar)));
			} // for val2
		} // for val1
	}// simpleTestOfAdd

	@Test
	public void testWordAdd() {
		int total, result;
		Random random = new Random();
		int COUNT = 3000;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			word1 = loadWord(val1);
			val2 = random.nextInt(0XFFFF);
			word2 = loadWord(val2);
			// System.out.printf("val1 = %04X, word1[1] = %02X, word1[0] = %02X%n", val1, word1[1], word1[0]);
			// System.out.printf("val2 = %04X, word2[1] = %02X, word2[0] = %02X%n", val2, word2[1], word2[0]);
			total = (val1 + val2) & 0XFFFF;
			answers = adder.addWord(word1, word2);
			result = ((answers[1] * 0X100) + (answers[0] & 0XFF)) & 0XFFFF;
			// System.out.printf("total = %04X, result = %04X%n", total,result);
			assertThat("Word Add (" + i + ") " + val1 + " + " + val2, total, equalTo(result));
		} // for
	}// testWordAdd

	@Test
	public void testWordAddWithCarry() {
		int total, result;
		boolean carryVar;
		int carry;
		Random random = new Random();
		int COUNT = 1;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			word1 = loadWord(val1);
			val2 = random.nextInt(0XFFFF);
			word2 = loadWord(val2);
			carryVar = random.nextBoolean();
			carry = carryVar?1:0;
			// System.out.printf("val1 = %04X, word1[1] = %02X, word1[0] = %02X%n", val1, word1[1], word1[0]);
			// System.out.printf("val2 = %04X, word2[1] = %02X, word2[0] = %02X%n", val2, word2[1], word2[0]);
			total = (val1 + val2 + carry) & 0XFFFF;
			answers = adder.addWordWithCarry(word1, word2,carryVar);
			result = ((answers[1] * 0X100) + (answers[0] & 0XFF)) & 0XFFFF;
			// System.out.printf("total = %04X, result = %04X%n", total,result);
			assertThat("Word Add (" + i + ") " + val1 + " + " + val2, total, equalTo(result));
		} // for
	}// testWordAdd

	@Test
	public void testByteIncrement() {
		for (val1 = 0; val1 < 0X100; val1++) {
			byte answer = (byte) ((val1 + 1) & 0XFF);
			bite1[0] = (byte) val1;
			assertThat("Byte increment:  " + val1, answer, equalTo(adder.increment(bite1)));
		} // for val1
	}// testByteIncrement

	@Test
	public void testWordIncrement() {
		int total, result;
		Random random = new Random();
		int COUNT = 1;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			word1 = loadWord(val1);

			// System.out.printf("val1 = %04X, word1[1] = %02X, word1[0] = %02X%n", val1, word1[1], word1[0]);
			// System.out.printf("val2 = %04X, word2[1] = %02X, word2[0] = %02X%n", val2, word2[1], word2[0]);
			total = (val1 + 1) & 0XFFFF;
			answers = adder.incrementWord(word1);
			result = ((answers[1] * 0X100) + (answers[0] & 0XFF)) & 0XFFFF;
			// System.out.printf("total = %04X, result = %04X%n", total,result);
			assertThat("Word Add (" + i + ") " + val1, total, equalTo(result));
		} // for
	}// testWordIncrement
	
	@Test
	public void testWordDecrement() {
		int total, result;
		Random random = new Random();
		int COUNT = 1;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			word1 = loadWord(val1);
			total = (val1 - 1) & 0XFFFF;
			answers = adder.decrementWord(word1);
			result = ((answers[1] * 0X100) + (answers[0] & 0XFF)) & 0XFFFF;
			// System.out.printf("total = %04X, result = %04X%n", total,result);
			assertThat("Word dec(" + i + ") " + val1, total, equalTo(result));
		} // for
	}// testWordDecrement
	

	@Test
	public void testByteSub() {
		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				byte answer = (byte) ((val1 - val2) & 0XFF);
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				assertThat("Full SUB:  " + val1 + " - " + val2, answer, equalTo(adder.sub(bite1, bite2)));
			} // for val2
		} // for val1
	}// simpleTestOfAdd

	@Test
	public void testByteSubWithCarry() {
		Random random = new Random();
		boolean carryVar;
		int carry;
		byte answer;
		String msg;
		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				carryVar = random.nextBoolean();
				carry = carryVar ? 1 : 0;
				answer = (byte) ((val1 - (val2 + carry)) & 0XFF);
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				msg = String.format("Byte Sub With Carry, var1 = %02x, var2 = %02X, Carry = %s", val1, val2, carryVar);
				assertThat(msg, answer, equalTo(adder.subWithCarry(bite1, bite2,carryVar)));
			} // for val2
		} // for val1
	}// simpleTestOfAdd
	@Test
	public void testWordSubWithCarry() {
		int total, result;
		boolean carryVar;
		int carry;
		Random random = new Random();
		int COUNT = 1;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			word1 = loadWord(val1);
			val2 = random.nextInt(0XFFFF);
			word2 = loadWord(val2);
			carryVar = random.nextBoolean();
			carry = carryVar?1:0;
			total = (val1 - (val2 + carry)) & 0XFFFF;
			answers = adder.subWordWithCarry(word1, word2,carryVar);
			result = ((answers[1] * 0X100) + (answers[0] & 0XFF)) & 0XFFFF;
			assertThat("Word Sub with Carry (" + i + ") " + val1 + " + " + val2, total, equalTo(result));
		} // for
	}// testWordAdd



	@Test
	public void testByteDecrement() {
		for (val1 = 0; val1 < 0X100; val1++) {
			byte answer = (byte) ((val1 - 1) & 0XFF);
			bite1[0] = (byte) val1;
			assertThat("Byte decrement:  " + val1, answer, equalTo(adder.decrement(bite1)));
		} // for val1
	}// testByteIncrement



	@Test
	public void testZFlag() {
		Random random = new Random();
		int COUNT = 3000;
		boolean zeroFlag;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFF);
			val2 = random.nextInt(0XFF);
			if (random.nextInt(10) == 1) {
				val2 = -val1;
			} // if
			zeroFlag = (((val1 + val2) & 0XFF) == 0) ? true : false;
			bite1[0] = (byte) val1;
			bite2[0] = (byte) val2;
			adder.add(bite1, bite2);
			assertThat("testZFlag byte:  " + val1 + " + " + val2 + " = " + (val2 + val1), zeroFlag,
					equalTo(adder.isZero()));
		} // for

		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			val2 = random.nextInt(0XFFFF);
			if (random.nextInt(10) == 1) {
				val2 = -val1;
			} // if
			zeroFlag = (((val1 + val2) & 0XFFFF) == 0) ? true : false;
			word1 = loadWord(val1);
			word2 = loadWord(val2);
			adder.addWord(word1, word2);
			assertThat("testZFlag word:  " + val1 + " + " + val2 + " = " + (val2 + val1), zeroFlag,
					equalTo(adder.isZero()));
		} // for

	}// testZFlag

	@Test
	public void testSign() {
		Random random = new Random();
		int COUNT = 3000;
		boolean signFlag;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFF);
			val2 = random.nextInt(0XFF);

			signFlag = (((val1 + val2) & 0X80) == 0X80) ? true : false;
			bite1[0] = (byte) val1;
			bite2[0] = (byte) val2;
			adder.add(bite1, bite2);
			assertThat("testSign byte:  " + val1 + " + " + val2 + " = " + (val2 + val1), signFlag,
					equalTo(adder.hasSign()));
		} // for

		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			val2 = random.nextInt(0XFFFF);
			signFlag = (((val1 + val2) & 0X8000) == 0X8000) ? true : false;
			word1 = loadWord(val1);
			word2 = loadWord(val2);
			adder.addWord(word1, word2);
			assertThat("testSign word:  " + val1 + " + " + val2 + " = " + (val2 + val1), signFlag,
					equalTo(adder.hasSign()));
		} // for
	}// testSign

	@Test
	public void testHalfCarry() {

		Random random = new Random();
		int COUNT = 3000;
		boolean halfCarry;
		int nibbleMask = 0X0F;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFF);
			val2 = random.nextInt(0XFF);
			halfCarry = ((val1 & nibbleMask) + (val2 & nibbleMask)) > nibbleMask ? true : false;
			bite1[0] = (byte) val1;
			bite2[0] = (byte) val2;
			adder.add(bite1, bite2);
			assertThat("testHalfCarry byte:  " + val1 + " + " + val2 + " = " + (val2 + val1), halfCarry,
					equalTo(adder.hasHalfCarry()));
		} // for

		int byteAndNibbleMask = 0X0FFF;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			val2 = random.nextInt(0XFFFF);
			halfCarry = ((val1 & byteAndNibbleMask) + (val2 & byteAndNibbleMask)) > byteAndNibbleMask ? true : false;
			word1 = loadWord(val1);
			word2 = loadWord(val2);
			adder.addWord(word1, word2);
			assertThat("testHalfCarry word:  " + val1 + " + " + val2 + " = " + (val2 + val1), halfCarry,
					equalTo(adder.hasHalfCarry()));
		} // for

	}// testHalfCarry

	@Test
	public void testParity() {
		Random random = new Random();
		int COUNT = 3000;
		boolean parityFlag;
		String s;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFF);
			val2 = random.nextInt(0XFF);
			s = Integer.toBinaryString((val1 + val2) & 0XFF);
			s = s.replaceAll("0", "");
			parityFlag = (s.length() % 2) == 0 ? true : false;
			bite1[0] = (byte) val1;
			bite2[0] = (byte) val2;
			adder.add(bite1, bite2);
			assertThat("testParity byte:  " + val1 + " + " + val2 + " = " + (val2 + val1), parityFlag,
					equalTo(adder.hasParity()));
		} // for

		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			val2 = random.nextInt(0XFFFF);
			s = Integer.toBinaryString((val1 + val2) & 0XFFFF);
			s = s.replaceAll("0", "");
			parityFlag = (s.length() % 2) == 0 ? true : false;

			word1 = loadWord(val1);
			word2 = loadWord(val2);
			adder.addWord(word1, word2);
			assertThat("testParity word:  " + val1 + " + " + val2 + " = " + (val2 + val1), parityFlag,
					equalTo(adder.hasParity()));
		} // for
	}// testParity

	@Test
	public void testOverflow() {
		Random random = new Random();
		int COUNT = 3000;
		boolean overFlow;

		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFF);
			val2 = random.nextInt(0XFF);
			int ans = (val1 + val2) & 0X0FF;

			boolean sign1 = (val1 & 0X80) == 0X080;
			boolean sign2 = (val2 & 0X80) == 0X080;
			boolean signAns = (ans & 0X80) == 0X080;
			boolean overflow = false;
			if (!(sign1 ^ sign2)) {
				overflow = sign1 ^ signAns;
			} // if
			// System.out.printf("overflow = %s,sign1 = %s,sign2 = %s,signAns = %s%n",overflow,sign1,sign2,signAns);

			bite1[0] = (byte) val1;
			bite2[0] = (byte) val2;
			adder.add(bite1, bite2);
			assertThat("testOverflow byte:  " + val1 + " + " + val2 + " = " + (val2 + val1), overflow,
					equalTo(adder.hasOverflow()));
		} // for

		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			val2 = random.nextInt(0XFFFF);
			int ans = (val1 + val2) & 0X0FFFF;

			boolean sign1 = (val1 & 0X8000) == 0X8000;
			boolean sign2 = (val2 & 0X8000) == 0X8000;
			boolean signAns = (ans & 0X8000) == 0X8000;

			boolean overflow = false;
			if (!(sign1 ^ sign2)) {
				overflow = sign1 ^ signAns;
			} // if

			word1 = loadWord(val1);
			word2 = loadWord(val2);
			adder.addWord(word1, word2);
			assertThat("testOverflow word:  " + val1 + " + " + val2 + " = " + (val2 + val1), overflow,
					equalTo(adder.hasOverflow()));
		} // for
	}// testOverflow

	@Test
	public void testCarry() {

		Random random = new Random();
		int COUNT = 3000;
		boolean carry;
		int byteMask = 0XFF;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFF);
			val2 = random.nextInt(0XFF);
			carry = ((val1 & byteMask) + (val2 & byteMask)) > byteMask ? true : false;
			bite1[0] = (byte) val1;
			bite2[0] = (byte) val2;
			adder.add(bite1, bite2);
			assertThat("testCarry byte:  " + val1 + " + " + val2 + " = " + (val2 + val1), carry,
					equalTo(adder.hasCarry()));
		} // for

		int wordMask = 0X0FFFF;
		for (int i = 0; i < COUNT; i++) {
			val1 = random.nextInt(0XFFFF);
			val2 = random.nextInt(0XFFFF);
			carry = ((val1 & wordMask) + (val2 & wordMask)) > wordMask ? true : false;
			word1 = loadWord(val1);
			word2 = loadWord(val2);
			adder.addWord(word1, word2);
			assertThat("testCarry word:  " + val1 + " + " + val2 + " = " + (val2 + val1), carry,
					equalTo(adder.hasCarry()));
		} // for

	}// testHalfCarry

	@Test
	public void testAND() {

		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				answers = new byte[] { (byte) ((val1 & val2) & 0XFF) };
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				assertThat("Full AND:  " + val1 + " " + val2, answers[0], equalTo(adder.and(bite1, bite2)[0]));
			} // for val2
		} // for val1

	}// testAND

	@Test
	public void testOR() {

		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				answers = new byte[] { (byte) ((val1 | val2) & 0XFF) };
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				assertThat("Full OR:  " + val1 + " " + val2, answers[0], equalTo(adder.or(bite1, bite2)[0]));
			} // for val2
		} // for val1

	}// testAND

	@Test
	public void testXOR() {

		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				answers = new byte[] { (byte) ((val1 ^ val2) & 0XFF) };
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				assertThat("Full XOR:  " + val1 + " " + val2, answers[0], equalTo(adder.xor(bite1, bite2)[0]));
			} // for val2
		} // for val1

	}// testAND

	@Test
	public void testCPL() {
		// one's complement
		for (val1 = 0; val1 < 0X100; val1++) {
			answers = new byte[] { (byte) (~val1 & 0XFF) };
			bite1[0] = (byte) val1;
			assertThat("Full CPL:  " + val1, answers[0], equalTo(adder.complement(bite1)[0]));
		} // for val1
	}// testCPL

	@Test
	public void testNegate() {
		// Two's complement
		for (val1 = 0; val1 < 0X100; val1++) {
			byte answer =  (byte) ((~val1+1) & 0XFF) ;
			bite1[0] = (byte) val1;
			assertThat("Full Negate:  " + val1, answer, equalTo(adder.negate(bite1)));
		} // for val1
	}// testCPL

	// ---------------------------------------------------------------------------------------
	// returns LSB in index 0 and MSB in index 1
	private byte[] loadWord(int value) {
		return new byte[] { (byte) (value & 0XFF), (byte) ((value & 0XFF00) >> 8) };
	}// loadWord

	private void showAnswer(int val1, int val2, byte[] ans) {
		for (int i = 0; i < ans.length; i++) {
			System.out.printf("%02X + %02X =  %02X  ", val1, val2, ans[i]);
		} // for
		System.out.println();
	}// showAnswer

	private void showAnswer(byte[] ans) {
		System.out.println();
		for (int i = 0; i < ans.length; i++) {
			System.out.printf("%02X ", ans[i]);
		} // for
		System.out.println();
		System.out.println();

	}// showAnswer

	// @Test
	// public void test() {
	// fail("Not yet implemented");
	// }//

}// class AdderTest
