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
//	int value1, value2, answer;
//	byte[] bite1 = new byte[] { (byte) 0X00 };
//	byte[] bite2 = new byte[] { (byte) 0X00 };
//	byte[] word1 = new byte[] { (byte) 0X00, (byte) 0X00 };
//	byte[] word2 = new byte[] { (byte) 0X00, (byte) 0X00 };

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

	@Test
	public void testWordDecrement() {
		// no flags are affected
		byte[] word1;
		for (int value1 = 0; value1 <0XFFFF ; value1++) {
			
			int answer = (value1 -1) & 0XFFFF;
			word1 = loadWord(value1);
			int ans = getWordValue(adder.decrementWord(word1));
			assertThat("Full DecWord:  " + value1, answer, equalTo(ans));

		} // for val1
	}// testWordDecrement
	
	public byte[] loadWord(int value) {
		return new byte[] { (byte) (value & 0XFF), (byte) ((value & 0XFF00) >> 8) };
	}// loadWord
	
	public int getWordValue(byte[] word) {
		return ((word[1] << 8) & 0XFF00) + (word[0] & 0X00FF);
	}// getWordValue



}// class AdderTest2
