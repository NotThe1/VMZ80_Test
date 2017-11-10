package hardware.arithmeticUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import hardware.ArithmeticUnit;

public class WordAddArithmeticUnit {
	ArithmeticUnit au = ArithmeticUnit.getInstance();
	byte[] arg1,arg2,sum,ans;
	String sArg1,sArg2,sSum;
	
	boolean sign,zero,halfCarry,overflow,nFlag,carry;
	boolean carryState;
	String flags,message;
	

	int value1, value2, answer;
	byte[] bite1 = new byte[] { (byte) 0X00 };
	byte[] bite2 = new byte[] { (byte) 0X00 };
	byte[] word1 = new byte[] { (byte) 0X00, (byte) 0X00 };
	byte[] word2 = new byte[] { (byte) 0X00, (byte) 0X00 };

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		au = ArithmeticUnit.getInstance();
	}// setUp
//AddAdcWordOriginal.txt
	
	@Test
	public void testWordADDfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/AddAdcWordOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				sArg1 = scanner.next();
				arg1 = getValue(sArg1);
				sArg2 = scanner.next();
				arg2 = getValue(sArg2);
				
				sSum = scanner.next();
				sum = getValue(sSum);
				flags = scanner.next();
				
				scanner.next();//	skip sum ADC CY = 0
				scanner.next();//	skip flags ADC CY = 0
				scanner.next();//	skip sum ADC CY = 1
				scanner.next();//	skip flags ADC CY = 1

				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%s %s %s %s ",sArg1,sArg2,sSum,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file WORD ADD -> %s - %s = %s", sArg1,sArg2,sSum);
				assertThat("sum: " + message,sum,equalTo(au.addWord(arg1, arg2)));
//				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
//				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
//				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
				
				//---------------------------------------------------------------------------------
				
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testWordSBCfile
	
	@Test
	public void testWordADC_NCfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/AddAdcWordOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				sArg1 = scanner.next();
				arg1 = getValue(sArg1);
				sArg2 = scanner.next();
				arg2 = getValue(sArg2);
				
				scanner.next();//	skip sum ADD
				scanner.next();//	skip flags ADD
				
				sSum = scanner.next();
				sum = getValue(sSum);
				flags = scanner.next();
				
				scanner.next();//	skip sum ADC CY = 1
				scanner.next();//	skip flags ADC CY = 1
				
				
				carryState = false;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%s %s %s %s ",sArg1,sArg2,sSum,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file WORD ADC CY=0 -> %s - %s = %s", sArg1,sArg2,sSum);
				assertThat("sum: " + message,sum,equalTo(au.addWordWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("overFlow: " +  message,overflow,equalTo(au.isOverflowFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
				
				
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testWordADC_NCfile
	
	@Test
	public void testWordADC_CYfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/AddAdcWordOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				sArg1 = scanner.next();
				arg1 = getValue(sArg1);
				sArg2 = scanner.next();
				arg2 = getValue(sArg2);
				
				scanner.next();//	skip sum ADD
				scanner.next();//	skip flags ADD
				scanner.next();//	skip sum ADC CY = 0
				scanner.next();//	skip flags ADC CY = 0
				
				sSum = scanner.next();
				sum = getValue(sSum);
				flags = scanner.next();
				
				
				
				carryState = true;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%s %s %s %s ",sArg1,sArg2,sSum,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file WORD ADC CY=0 -> %s - %s = %s", sArg1,sArg2,sSum);
				assertThat("sum: " + message,sum,equalTo(au.addWordWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("overFlow: " +  message,overflow,equalTo(au.isOverflowFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
				
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testWordADC_CYfile
	
	
	private byte[] getValue(String value){
		int workingValue  = Integer.valueOf(value,16);
		byte msb = (byte) ((workingValue & 0XFF00)>>8);
		byte lsb = (byte) ((byte) workingValue & 0X00FF);
		return  new byte[] {lsb,msb};
	}//getValue
	
	@Test
	public void testWordIncrement() {
		// no flags are affected
		byte[] word1;
		for (int value1 = 0; value1 <0XFFFF ; value1++) {
			
			int answer = (value1 +1) & 0XFFFF;
			word1 = loadWord(value1);
			int ans = getWordValue(au.incrementWord(word1));
			assertThat("Full IncWord:  " + value1, answer, equalTo(ans));

		} // for val1
	}// testWordIncrement

	public byte[] loadWord(int value) {
		return new byte[] { (byte) (value & 0XFF), (byte) ((value & 0XFF00) >> 8) };
	}// loadWord
	
	public int getWordValue(byte[] word) {
		return ((word[1] << 8) & 0XFF00) + (word[0] & 0X00FF);
	}// getWordValue



	

}// class AdderTest2
