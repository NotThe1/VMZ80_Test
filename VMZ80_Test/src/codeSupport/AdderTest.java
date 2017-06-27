package codeSupport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import hardware.Adder;

public class AdderTest {
	Adder adder;
	byte[] bite1 = new byte[] { (byte) 00 };
	byte[] bite2 = new byte[] { (byte) 00 };
	byte[] word1 = new byte[] { (byte) 00, (byte) 00 };
	byte[] word2 = new byte[] { (byte) 00, (byte) 00 };
	byte[] answers;
	// BitSet ans1;
	// BitSet ans2;
	int val1, val2, val3, val4, ansByte, ansWord;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		adder.clearSets();
	}// setUp

	// @Test
	// public void simpleTestOfAdd() {
	// int sum1, sum2;
	// for (val1 = 0; val1 < 0X100; val1++) {
	// for (val2 = 0; val2 < 0X100; val2++) {
	// sum1 = val1 + val2;
	// sum2 = (sum1 & 0X100) >> 8;
	// if (sum2 == 0) {
	// answers = new byte[] { (byte) (sum1 & 0XFF) };
	// } else {
	// answers = new byte[] { (byte) (sum1 & 0XFF), (byte) sum2 };
	// }
	// bite1[0] = (byte) val1;
	// bite2[0] = (byte) val2;
	// adder.setArguments(bite1, bite2);
	// adder.add();
	// byte[] ans = adder.getSum();
	// assertThat("Simple Add: " + val1 + " " + val2, answers, equalTo(adder.getSum()));
	//// showAnswer(val1, val2, ans);
	// } // for val2
	// } // for val1
	// }// simpleTestOfAdd

	@Test
	public void testOfByteAdd() {
		
		int sum1;
		for (val1 = 0; val1 < 0X100; val1++) {
			for (val2 = 0; val2 < 0X100; val2++) {
				answers = new byte[] { (byte) ((val1 + val2) & 0XFF) };
				bite1[0] = (byte) val1;
				bite2[0] = (byte) val2;
				assertThat("Full Add:  " + val1 + " " + val2, answers[0], equalTo(adder.add(bite1, bite2)[0]));
			} // for val2
		} // for val1
	}// simpleTestOfAdd

	@Test
	public void testWordAdd() {
		int total,result;
		Random random = new Random();
		int COUNT = 3000;
		for( int i= 0;i< COUNT; i++){
			val1 = random.nextInt(0XFFFF);
//			val1 = 0XFF00;
			word1[0] = (byte)( val1 &0XFF);
			word1[1] = (byte)(( val1 &0XFF00)>>8);
			val2 = random.nextInt(0XFFFF);
//			val2 = 0XFF;
			word2[0] = (byte)( val2 &0XFF);
			word2[1] = (byte)(( val2 &0XFF00)>>8);
//			System.out.printf("val1 = %04X, word1[1] = %02X, word1[0] = %02X%n", val1, word1[1], word1[0]);
//			System.out.printf("val2 = %04X, word2[1] = %02X, word2[0] = %02X%n", val2, word2[1], word2[0]);
			total = (val1 + val2) & 0XFFFF;
			answers = adder.add(word1, word2);
			result = ((answers[1] * 0X100) + (answers[0] & 0XFF)) & 0XFFFF;
//			System.out.printf("total = %04X,  result = %04X%n", total,result);
			assertThat("Word Add (" + i + ") " + val1 + " + " + val2,total,equalTo(result));
		}//for
		
	}// testWordAdd

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
