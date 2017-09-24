package hardware.arithmeticUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import hardware.ArithmeticUnit;

public class ByteAddArithmeticUnit {
	ArithmeticUnit au = ArithmeticUnit.getInstance();
	
	byte arg1,arg2,sum,ans;
	boolean sign,zero,halfCarry,overflow,nFlag,carry;
	boolean carryState;
	String flags,message;

	
	private static TestUtilityArithmeticUnit atu = TestUtilityArithmeticUnit.getInstance();
	int value1, value2, answer;
	byte aByte1,aByte2;
	byte[] bite1 = new byte[] { (byte) 0X00 };
	byte[] bite2 = new byte[] { (byte) 0X00 };
	byte[] word1 = new byte[] { (byte) 0X00, (byte) 0X00 };
	byte[] word2 = new byte[] { (byte) 0X00, (byte) 0X00 };

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		au = ArithmeticUnit.getInstance();
		// adder.clearSets();
	}// setUp
	
	
	@Test
	public void testADDfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/AddOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				sum = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %02X %s ",arg1,arg2,sum,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file ADD -> %d - %d = %02X", arg1,arg2,sum);
				assertThat("ans: " + message,sum,equalTo(au.add(arg1, arg2)));
				assertThat("sign: " +  message,sign,equalTo(au.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(au.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.hasHalfCarry()));
				assertThat("overFlow: " +  message,overflow,equalTo(au.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.hasCarry()));			
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testADDfile
	
	@Test
	public void testADCfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/AdcOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				sum = getValue(scanner.next());
				flags = scanner.next();
				
				carryState = false;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %02X %s ",arg1,arg2,sum,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file ADC NC -> %d - %d = %02X", arg1,arg2,sum);
				assertThat("ans: " + message,sum,equalTo(au.addWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(au.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(au.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.hasHalfCarry()));
				assertThat("overFlow: " +  message,overflow,equalTo(au.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.hasCarry()));
				
				//---------------------------------------------------------------------------------
				
				sum = getValue(scanner.next());
				flags = scanner.next();
				
				carryState = true;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %02X %s ",arg1,arg2,sum,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file ADC CY -> %d - %d = %02X", arg1,arg2,sum);
				assertThat("ans: " + message,sum,equalTo(au.addWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(au.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(au.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.hasHalfCarry()));
				assertThat("overFlow: " +  message,overflow,equalTo(au.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.hasCarry()));
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testADCfile
	
	
	@Test
	public void testINCfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/IncOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				sum = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X  %02X %s ",arg1,sum,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file INC -> %d  = %02X", arg1,sum);
				assertThat("ans: " + message,sum,equalTo(au.increment(arg1)));
				assertThat("sign: " +  message,sign,equalTo(au.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(au.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.hasHalfCarry()));
//				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
//				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));			
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testINCfile
	
	
	
	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue


}// class AdderTest2
