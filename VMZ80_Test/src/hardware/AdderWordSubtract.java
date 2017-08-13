package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class AdderWordSubtract {
	Adder adder = Adder.getInstance();
	byte[] arg1,arg2,diff,ans;
	String sArg1,sArg2,sDiff;
	
	boolean sign,zero,halfCarry,overflow,nFlag,carry;
	boolean carryState;
	String flags,message;

	
	private static AdderTestUtility atu = AdderTestUtility.getInstance();
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
	public void testWordSBCfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/SbcWordOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				sArg1 = scanner.next();
				arg1 = getValue(sArg1);
				sArg2 = scanner.next();
				arg2 = getValue(sArg2);
				sDiff = scanner.next();
				diff = getValue(sDiff);
				flags = scanner.next();
				
				carryState = false;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%s %s %s %s ",sArg1,sArg2,sDiff,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SBC NC -> %s - %s = %s", sArg1,sArg2,sDiff);
				assertThat("ans: " + message,diff,equalTo(adder.subWordWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));
				
				//---------------------------------------------------------------------------------
				
				sDiff = scanner.next();
				diff = getValue(sDiff);
				flags = scanner.next();
				
				carryState = true;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%s %s %s %s ",sArg1,sArg2,sDiff,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SBC CY -> %s - %s = %s", sArg1,sArg2,sDiff);
				assertThat("ans: " + message,diff,equalTo(adder.subWordWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testWordSBCfile
	
	private byte[] getValue(String value){
		int workingValue  = Integer.valueOf(value,16);
		byte msb = (byte) ((workingValue & 0XFF00)>>8);
		byte lsb = (byte) ((byte) workingValue & 0X00FF);
		return  new byte[] {lsb,msb};
	}//getValue


	
	//---------------------------------------------------------------------

//	@Test
//	public void testWordSubWithCarry() {
//		Random random = new Random();
//		boolean carryState;
//		int carryValue;
//		int COUNT = 8000;
//		for (int i = 0; i < COUNT; i++) {
//			carryState = random.nextBoolean();
//			carryValue = carryState?1:0;
//			value1 = random.nextInt(0XFFFF);
//			value2 = random.nextInt(0XFFFF);
//			atu.fixFlagsSUB(value1, value2, AdderTestUtility.WORD_ARG, carryState);
//			int answer = ((value1 - (value2 + carryValue)) & 0XFFFF);
//			word1 = atu.loadWord(value1);
//			word2 = atu.loadWord(value2);
//			// byte[] result = adder.addWord(word1, word2);
//			int ans = atu.getWordValue(adder.subWordWithCarry(word1, word2,carryState));
//			assertThat("Full SubWord/WC:  " + value1 + " " + value2, answer, equalTo(ans));
//			assertThat("Full SubWord/WC Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
//			assertThat("Full SubWord/WC Zero:  " + value1 + " " + value2,atu. mZero, equalTo(adder.isZero()));
//			assertThat("Full SubWord/WC HalfCarry:  " + value1 + " " + value2, atu. mHalfCarry, equalTo(adder.hasHalfCarry()));
//			assertThat("Full SubWord/WC Parity:  " + value1 + " " + value2, atu. mParity, equalTo(adder.hasParity()));
//			assertThat("Full SubWord/WC Overflow:  " + value1 + " " + value2, atu. mOverflow, equalTo(adder.hasOverflow()));
//			assertThat("Full SubWord nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
//			assertThat("Full SubWord/WC Carry:  " + value1 + " " + value2, atu. mCarry, equalTo(adder.hasCarry()));
//
//		} // for val1
//	}// testWordSubWithCarry
//	
//	@Test
//	public void testWordDecrement() {
//		for (int value1 = 0; value1 <0XFFFF ; value1++) {
//			
//			atu.fixFlagsSUB(value1, 1, AdderTestUtility.WORD_ARG, false);
//			int answer = (value1 -1) & 0XFFFF;
//			word1 = atu.loadWord(value1);
//			int ans = atu.getWordValue(adder.decrementWord(word1));
//			assertThat("Full DecWord:  " + value1, answer, equalTo(ans));
//			assertThat("Full DecWord Sign:  " + value1, atu.mSign, equalTo(adder.hasSign()));
//			assertThat("Full DecWord Zero:  " + value1, atu.mZero, equalTo(adder.isZero()));
//			assertThat("Full DecWord HalfCarry:  " + value1, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
//			assertThat("Full DecWord Parity:  " + value1, atu.mParity, equalTo(adder.hasParity()));
//			assertThat("Full DecWord Overflow:  " + value1, atu.mOverflow, equalTo(adder.hasOverflow()));
//			assertThat("Full DecWord nFlag:  " + value1, atu.mNFlag, equalTo(adder.isNFlagSet()));
//			assertThat("Full DecWord Carry:  " + value1, atu.mCarry, equalTo(adder.hasCarry()));
//
//		} // for val1
//	}// testWordDecrement


}// class AdderTest2
