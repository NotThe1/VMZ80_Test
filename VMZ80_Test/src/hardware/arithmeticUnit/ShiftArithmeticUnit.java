package hardware.arithmeticUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import hardware.ArithmeticUnit;

public class ShiftArithmeticUnit {
	ArithmeticUnit au = ArithmeticUnit.getInstance();
	
	byte arg1,arg2,result,ans;
	boolean sign,zero,halfCarry,overflow,parity,nFlag,carry;
	boolean carryState;
	String flags,message;


	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		au = ArithmeticUnit.getInstance();
		// adder.clearSets();
	}// setUp
	
	@Test
	public void testSLA_file() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/ShiftOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
// Carry = False
				result = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;
				

//				System.out.printf("%02X  %02X %s ",arg1,result,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SLA NC -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.shiftSLA(arg1)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " +  message,parity,equalTo(au.isParityFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
// Carry = True				
				result = getValue(scanner.next());
				flags = scanner.next();
						
				scanner.next();//	SRA_NC result
				scanner.next();//	SRA_NC flags
				scanner.next();//	SRA_CY result
				scanner.next();//	SRA_CY flags
				
				scanner.next();//	SRL_NC result
				scanner.next();//	SRL_NC flags
				scanner.next();//	SRL_NC result
				scanner.next();//	SRL_NC flags
			
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X  %02X %s ",arg1,result,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);

				message = String.format("file SLA CY -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.shiftSLA(arg1)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " +  message,parity,equalTo(au.isParityFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
				
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testSLA_file
	
	@Test
	public void testSRA_file() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/ShiftOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
// Carry = False
				
				
				scanner.next();//	SLA_NC result
				scanner.next();//	SLA_NC flags
				scanner.next();//	SLA_NC result
				scanner.next();//	SLA_NC flags
				

				result = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;
				

//				System.out.printf("%02X  %02X %s ",arg1,result,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SRA NC -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.shiftSRA(arg1)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " +  message,parity,equalTo(au.isParityFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
// Carry = True	
				result = getValue(scanner.next());
				flags = scanner.next();
				
//				scanner.next();//	SLA_CY result
//				scanner.next();//	SLA_CY flags
				
				scanner.next();//	SRL_NC result
				scanner.next();//	SRL_NC flags
				scanner.next();//	SRL_NC result
				scanner.next();//	SRL_NC flags

				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X  %02X %s ",arg1,result,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);

				message = String.format("file SRA CY -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.shiftSRA(arg1)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " +  message,parity,equalTo(au.isParityFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
				
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testSRA_file
	
	@Test
	public void testSRL_file() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/ShiftOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
// Carry = False
				
				
				scanner.next();//	SRA_NC result
				scanner.next();//	SRA_NC flags
				scanner.next();//	SRA_NC result
				scanner.next();//	SRA_NC flags
				
				scanner.next();//	SRL_NC result
				scanner.next();//	SRL_NC flags
				scanner.next();//	SRL_NC result
				scanner.next();//	SRL_NC flags


				

				result = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;
				

//				System.out.printf("%02X  %02X %s ",arg1,result,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file SRL NC -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.shiftSRL(arg1)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " +  message,parity,equalTo(au.isParityFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
// Carry = True	
				result = getValue(scanner.next());
				flags = scanner.next();
				
//				scanner.next();//	SLA_CY result
//				scanner.next();//	SLA_CY flags
				
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X  %02X %s ",arg1,result,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);

				message = String.format("file SRL CY -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.shiftSRL(arg1)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " +  message,parity,equalTo(au.isParityFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
				
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testSRL_file
	
	
	
	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue
	
}// class AdderShift
