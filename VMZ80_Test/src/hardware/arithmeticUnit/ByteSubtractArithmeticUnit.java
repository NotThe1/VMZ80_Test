package hardware.arithmeticUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import hardware.ArithmeticUnit;

public class ByteSubtractArithmeticUnit {
	ArithmeticUnit au = ArithmeticUnit.getInstance();
	
	byte arg1,arg2,diff,ans;
	boolean sign,zero,halfCarry,overflow,nFlag,carry;
	boolean carryState;
	String flags,message;
	
	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		au = ArithmeticUnit.getInstance();
		// adder.clearSets();
	}// setUp
	
	@Test
	public void testSUBfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/SubOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				diff = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %02X %s ",arg1,arg2,diff,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SUB -> %d - %d = %02X", arg1,arg2,diff);
				assertThat("ans: " + message,diff,equalTo(au.sub(arg1, arg2)));
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
	}//testSUBfile
	
	
	@Test
	public void testSBCfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/SbcOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				diff = getValue(scanner.next());
				flags = scanner.next();
				
				carryState = false;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %02X %s ",arg1,arg2,diff,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SBC NC -> %d - %d = %02X", arg1,arg2,diff);
				assertThat("ans: " + message,diff,equalTo(au.subWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(au.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(au.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.hasHalfCarry()));
				assertThat("overFlow: " +  message,overflow,equalTo(au.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.hasCarry()));
				
				//---------------------------------------------------------------------------------
				
				diff = getValue(scanner.next());
				flags = scanner.next();
				
				carryState = true;
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %02X %s ",arg1,arg2,diff,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SBC CY -> %d - %d = %02X", arg1,arg2,diff);
				assertThat("ans: " + message,diff,equalTo(au.subWithCarry(arg1, arg2,carryState)));
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
	}//testSBCfile
	
	@Test
	public void testDECfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/DecOrignal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				diff = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %02X %s ",arg1,arg2,diff,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file DEC -> %d  = %02X", arg1,diff);
				assertThat("ans: " + message,diff,equalTo(au.decrement(arg1)));
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
	}//testDECfile
	
	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue


	


}// class AdderTest2
