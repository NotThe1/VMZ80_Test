package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class AdderByteSubtract {
	byte arg1,arg2,diff,ans;
	boolean sign,zero,halfCarry,overflow,nFlag,carry;
	boolean carryState;
	String flags,message;
	
	
	private static AdderTestUtility atu = AdderTestUtility.getInstance();
	Adder adder = Adder.getInstance();
	int value1, value2, answer;
	byte aByte1,aByte2;
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
	
//	@Test
//	public void testSUBfile() {
//		
//		try {
//			InputStream inputStream = this.getClass().getResourceAsStream("/SubOriginal.txt");
////			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
//			Scanner scanner = new Scanner(inputStream);
//			scanner.nextLine(); // skip header
//			while (scanner.hasNextLine()){
//				arg1 = getValue(scanner.next());
//				arg2 = getValue(scanner.next());
//				diff = getValue(scanner.next());
//				flags = scanner.next();
//				
//				sign = flags.subSequence(0, 1).equals("1")?true:false;
//				zero = flags.subSequence(1, 2).equals("1")?true:false;
//				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
//				overflow = flags.subSequence(3, 4).equals("1")?true:false;
//				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
//				carry = flags.subSequence(5, 6).equals("1")?true:false;
//
////				System.out.printf("%02X %02X %02X %s ",arg1,arg2,diff,flags);
////				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
//						
//				message = String.format("file SUB -> %d - %d = %02X", arg1,arg2,diff);
//				ans = adder.sub(arg1, arg2);
//				assertThat("ans: " + message,diff,equalTo(adder.sub(arg1, arg2)));
//				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
//				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
//				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
//				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
//				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
//				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));			
//			}//while
//			scanner.close();
//			inputStream.close();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			fail(e.getMessage());
//		}//try
//	}//testSUBfile
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
				ans = adder.subWithCarry(arg1, arg2,carryState);
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
				ans = adder.subWithCarry(arg1, arg2,carryState);
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
	
//	private boolean getState(int value){
//		return value==1?true:false;
//	}//getState

	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue


	
//	@Test
//	public void testByteSUB() {
//		for (value1 = 0; value1 < 0XFF; value1++) {
//			for (value2 = 0; value2 < 0XFF; value2++) {
//				atu.fixFlagsSUB(value1, value2, AdderTestUtility.BYTE_ARG, false);
//				byte answer = (byte) ((value1 - value2) & 0XFF);
//				bite1[0] = (byte) value1;
//				bite2[0] = (byte) value2;
//				
//				aByte1 = (byte) value1;
//				aByte2 = (byte) value2;
//				answer =  (byte) (value1 - value2);
//				
//				assertThat("Full Sub:  " + value1 + " " + value2, answer, equalTo(adder.sub(aByte1, aByte2)));
//				assertThat("Full Sub Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
//				assertThat("Full Sub Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
//				assertThat("Full Sub HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
//				assertThat("Full Sub Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
//				assertThat("Full Sub Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
//				assertThat("Full Sub nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
//				assertThat("Full Sub Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
//			} // for val2
//		} // for val1
//	}// simpleTestOfSUB
//
//	@Test
//	public void testByteSUBWithCarry() {
//		for (value1 = 0; value1 < 0XFF; value1++) {
//			for (value2 = 0; value2 < 0XFF; value2++) {
//				atu.fixFlagsSUB(value1, value2, AdderTestUtility.BYTE_ARG, false);
//				byte answer = (byte) ((value1 - (value2 +0)) & 0XFF);
//				bite1[0] = (byte) value1;
//				bite2[0] = (byte) value2;
//				
//				aByte1 = (byte) value1;
//				aByte2 = (byte) value2;
//				answer =  (byte) (value1 - value2);
//
//				assertThat("Full Sub/WCf:  " + value1 + " " + value2, answer, equalTo(adder.subWithCarry(aByte1, aByte2,false)));
//				assertThat("Full Sub/WCf Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
//				assertThat("Full Sub/WCf Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
//				assertThat("Full Sub/WCf HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
//				assertThat("Full Sub/WCf Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
//				assertThat("Full Sub/WCf Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
//				assertThat("Full Sub/WCf nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
//				assertThat("Full Sub/WCf Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
//			} // for val2
//		} // for val1
//		
//		for (value1 = 0; value1 < 0XFF; value1++) {
//			for (value2 = 0; value2 < 0XFF; value2++) {
//				atu.fixFlagsSUB(value1, value2, AdderTestUtility.BYTE_ARG, true);
//				byte answer = (byte) ((value1 - (value2 +1)) & 0XFF);
//				bite1[0] = (byte) value1;
//				bite2[0] = (byte) (value2 );
//				aByte1 = (byte) value1;
//				aByte2 = (byte) value2;
//				answer =  (byte) (value1 - (value2+1));
//
//				assertThat("Full Sub/WCt:  " + value1 + " " + value2, answer, equalTo(adder.subWithCarry(aByte1, aByte2,true)));
//				assertThat("Full Sub/WCt Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
//				assertThat("Full Sub/WCt Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
//				assertThat("Full Sub/WCt HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
//				assertThat("Full Sub/WCt Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
//				assertThat("Full Sub/WCt Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
//				assertThat("Full Sub/WCt nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
//				assertThat("Full Sub/WCt Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
//			} // for val2
//		} // for val1	
//	}// testByteSUBWithCarry
//	
//	@Test
//	public void testByteDecrement() {
//		for (value1 = 0; value1 < 0XFF; value1++) {
//				atu.fixFlagsSUB(value1, 1, AdderTestUtility.BYTE_ARG, false);
//				byte answer = (byte) ((value1 - 1) & 0XFF);
//				bite1[0] = (byte) value1;
//				
//				aByte1 = (byte) value1;
//				answer =  (byte) (value1 - 1);
//				
//				assertThat("Full DEC:  " + value1, answer, equalTo(adder.decrement(aByte1)));
//				assertThat("Full DEC Sign:  " + value1, atu.mSign, equalTo(adder.hasSign()));
//				assertThat("Full DEC Zero:  " + value1, atu.mZero, equalTo(adder.isZero()));
//				assertThat("Full DEC HalfCarry:  " + value1, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
//				assertThat("Full DEC Parity:  " + value1, atu.mParity, equalTo(adder.hasParity()));
//				assertThat("Full DEC Overflow:  " + value1, atu.mOverflow, equalTo(adder.hasOverflow()));
//				assertThat("Full DEC/WCf nFlag:  " + value1, atu.mNFlag, equalTo(adder.isNFlagSet()));
//				assertThat("Full DEC Carry:  " + value1, atu.mCarry, equalTo(adder.hasCarry()));
//		} // for val1
//	}// testByteIncrement

}// class AdderTest2
