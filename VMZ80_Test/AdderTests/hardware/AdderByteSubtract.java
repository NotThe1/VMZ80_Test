package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class AdderByteSubtract {
	Adder adder = Adder.getInstance();
	
	byte arg1,arg2,diff,ans;
	boolean sign,zero,halfCarry,overflow,nFlag,carry;
	boolean carryState;
	String flags,message;
	
	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
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
				assertThat("ans: " + message,diff,equalTo(adder.sub(arg1, arg2)));
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
				assertThat("ans: " + message,diff,equalTo(adder.subWithCarry(arg1, arg2,carryState)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));
				
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
				assertThat("ans: " + message,diff,equalTo(adder.subWithCarry(arg1, arg2,carryState)));
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
				assertThat("ans: " + message,diff,equalTo(adder.decrement(arg1)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
//				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
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
