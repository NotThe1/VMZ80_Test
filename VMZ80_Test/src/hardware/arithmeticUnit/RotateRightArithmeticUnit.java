package hardware.arithmeticUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import hardware.ArithmeticUnit;

public class RotateRightArithmeticUnit {
		
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
	public void testRR_file() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/RotateRightOriginal.txt");
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
						
				message = String.format("file RR NC -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.rotateRightThru(arg1, false)));
				assertThat("sign: " +  message,sign,equalTo(au.isSignFlagSet()));
				assertThat("zero: " +  message,zero,equalTo(au.isZeroFlagSet()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(au.isHCarryFlagSet()));
				assertThat("parity: " +  message,parity,equalTo(au.isParityFlagSet()));
				assertThat("nFlag: " +  message,nFlag,equalTo(au.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(au.isCarryFlagSet()));
// Carry = True				
				result = getValue(scanner.next());
				flags = scanner.next();
						
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;
				
				scanner.next();//	RRC_CY result
				scanner.next();//	RRC_CY flags
				
//				System.out.printf("%02X  %02X %s ",arg1,result,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);

				message = String.format("file RR CY -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.rotateRightThru(arg1, true)));
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
	}//testRR_file
	@Test
	public void testRRC_file() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/RotateRightOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
// Carry = False
				
				scanner.next();//	RR_NC result
				scanner.next();//	RR_NC flags
				
				scanner.next();//	RR_CY result
				scanner.next();//	RR_CY flags
				
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
						
				message = String.format("file RRC -> %d  = %02X", arg1,result);
				assertThat("ans: " + message,result,equalTo(au.rotateRight(arg1)));
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
	}//testRRC_file
	
	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue

}// class AdderRotateRight
