package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderByteAdd {
	
	byte arg1,arg2,sum,ans;
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
//	public void testADDfile() {
//		
//		try {
//			InputStream inputStream = this.getClass().getResourceAsStream("/AddOriginal.txt");
////			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
//			Scanner scanner = new Scanner(inputStream);
//			scanner.nextLine(); // skip header
//			while (scanner.hasNextLine()){
//				arg1 = getValue(scanner.next());
//				arg2 = getValue(scanner.next());
//				sum = getValue(scanner.next());
//				flags = scanner.next();
//				
//				sign = flags.subSequence(0, 1).equals("1")?true:false;
//				zero = flags.subSequence(1, 2).equals("1")?true:false;
//				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
//				overflow = flags.subSequence(3, 4).equals("1")?true:false;
//				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
//				carry = flags.subSequence(5, 6).equals("1")?true:false;
//
////				System.out.printf("%02X %02X %02X %s ",arg1,arg2,sum,flags);
////				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
//						
//				message = String.format("file ADD -> %d - %d = %02X", arg1,arg2,sum);
//				assertThat("ans: " + message,sum,equalTo(adder.add(arg1, arg2)));
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
//	}//testADDfile
	
//	@Test
//	public void testADCfile() {
//		
//		try {
//			InputStream inputStream = this.getClass().getResourceAsStream("/AdcOriginal.txt");
////			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
//			Scanner scanner = new Scanner(inputStream);
//			scanner.nextLine(); // skip header
//			while (scanner.hasNextLine()){
//				arg1 = getValue(scanner.next());
//				arg2 = getValue(scanner.next());
//				sum = getValue(scanner.next());
//				flags = scanner.next();
//				
//				carryState = false;
//				
//				sign = flags.subSequence(0, 1).equals("1")?true:false;
//				zero = flags.subSequence(1, 2).equals("1")?true:false;
//				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
//				overflow = flags.subSequence(3, 4).equals("1")?true:false;
//				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
//				carry = flags.subSequence(5, 6).equals("1")?true:false;
//
////				System.out.printf("%02X %02X %02X %s ",arg1,arg2,sum,flags);
////				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
//						
//				message = String.format("file ADC NC -> %d - %d = %02X", arg1,arg2,sum);
//				assertThat("ans: " + message,sum,equalTo(adder.addWithCarry(arg1, arg2,carryState)));
//				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
//				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
//				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
//				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
//				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
//				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));
//				
//				//---------------------------------------------------------------------------------
//				
//				sum = getValue(scanner.next());
//				flags = scanner.next();
//				
//				carryState = true;
//				
//				sign = flags.subSequence(0, 1).equals("1")?true:false;
//				zero = flags.subSequence(1, 2).equals("1")?true:false;
//				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
//				overflow = flags.subSequence(3, 4).equals("1")?true:false;
//				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
//				carry = flags.subSequence(5, 6).equals("1")?true:false;
//
////				System.out.printf("%02X %02X %02X %s ",arg1,arg2,sum,flags);
////				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
//						
//				message = String.format("file ADC CY -> %d - %d = %02X", arg1,arg2,sum);
//				assertThat("ans: " + message,sum,equalTo(adder.addWithCarry(arg1, arg2,carryState)));
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
//	}//testADCfile
	
	
//	@Test
//	public void testINCfile() {
//		
//		try {
//			InputStream inputStream = this.getClass().getResourceAsStream("/IncOrignal.txt");
////			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
//			Scanner scanner = new Scanner(inputStream);
//			scanner.nextLine(); // skip header
//			while (scanner.hasNextLine()){
//				arg1 = getValue(scanner.next());
//				sum = getValue(scanner.next());
//				flags = scanner.next();
//				
//				sign = flags.subSequence(0, 1).equals("1")?true:false;
//				zero = flags.subSequence(1, 2).equals("1")?true:false;
//				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
//				overflow = flags.subSequence(3, 4).equals("1")?true:false;
//				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
//				carry = flags.subSequence(5, 6).equals("1")?true:false;
//
////				System.out.printf("%02X %02X %02X %s ",arg1,arg2,sum,flags);
////				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
//						
//				message = String.format("file INC -> %d  = %02X", arg1,sum);
//				assertThat("ans: " + message,sum,equalTo(adder.increment(arg1)));
//				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
//				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
//				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
////				assertThat("overFlow: " +  message,overflow,equalTo(adder.hasOverflow()));
//				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
////				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));			
//			}//while
//			scanner.close();
//			inputStream.close();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			fail(e.getMessage());
//		}//try
//	}//testINCfile
	
	
	
	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue


	
	//--------------------------------------------------------------

	@Test
	public void testByteAdd() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsADD(value1, value2, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 + value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				aByte1 = (byte) value1;
				aByte2 = (byte) value2;
				answer =  (byte) (value1 + value2);

//				assertThat("Full Add:  " + value1 + " " + value2, answer, equalTo(adder.add(bite1, bite2)));
				assertThat("Full Add:  " + value1 + " " + value2, answer, equalTo(adder.add(aByte1, aByte2)));
				assertThat("Full Add Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Add Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Add HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full Add Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Add Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Add nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full Add Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));
			} // for val2
		} // for val1
	}// testByteAdd

	@Test
	public void testByteAddWithCarry() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			for (value2 = 0; value2 < 0XFF; value2++) {
				atu.fixFlagsADD(value1, value2, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 + value2) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				
				aByte1 = (byte) value1;
				aByte2 = (byte) value2;
				answer =  (byte) (value1 + value2);
//				assertThat("Full Add/WC:  " + value1 + " " + value2, answer,
//						equalTo(adder.addWithCarry(bite1, bite2, false)));
				assertThat("Full Add/WC:  " + value1 + " " + value2, answer,
						equalTo(adder.addWithCarry(aByte1, aByte2, false)));
				assertThat("Full Add/WC Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Add/WC Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Add/WC HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry,
						equalTo(adder.hasHalfCarry()));
				assertThat("Full Add/WC Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Add/WC Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Add/WC nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full Add/WC Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));

				atu.fixFlagsADD(value1, value2, AdderTestUtility.BYTE_ARG, true);
				answer = (byte) ((value1 + value2 + 1) & 0XFF);
				bite1[0] = (byte) value1;
				bite2[0] = (byte) value2;
				aByte1 = (byte) value1;
				aByte2 = (byte) value2;
				answer =  (byte) (value1 + value2+1);
//				assertThat("Full Add/WC:  " + value1 + " " + value2, answer,
//						equalTo(adder.addWithCarry(bite1, bite2, true)));
				assertThat("Full Add/WCt:  " + value1 + " " + value2, answer,
						equalTo(adder.addWithCarry(aByte1, aByte2, true)));
				assertThat("Full Add/WCt Sign:  " + value1 + " " + value2, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full Add/WCt Zero:  " + value1 + " " + value2, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full Add/WCt HalfCarry:  " + value1 + " " + value2, atu.mHalfCarry,
						equalTo(adder.hasHalfCarry()));
				assertThat("Full Add/WCt Parity:  " + value1 + " " + value2, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full Add/WCt Overflow:  " + value1 + " " + value2, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full Add/WCt nFlag:  " + value1 + " " + value2, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full Add/WCt Carry:  " + value1 + " " + value2, atu.mCarry, equalTo(adder.hasCarry()));

			} // for val2
		} // for val1
	}// testByteAddWithCarry


	@Test
	public void testByteIncrement() {
		for (value1 = 0; value1 < 0XFF; value1++) {
			atu.fixFlagsADD(value1, 1, AdderTestUtility.BYTE_ARG, false);
				byte answer = (byte) ((value1 + 1) & 0XFF);
				bite1[0] = (byte) value1;
				
				aByte1 = (byte) value1;
				answer = (byte) (aByte1 +1);
				assertThat("Full INC:  " + value1, answer, equalTo(adder.increment(aByte1)));
				assertThat("Full INC Sign:  " + value1, atu.mSign, equalTo(adder.hasSign()));
				assertThat("Full INC Zero:  " + value1, atu.mZero, equalTo(adder.isZero()));
				assertThat("Full INC HalfCarry:  " + value1, atu.mHalfCarry, equalTo(adder.hasHalfCarry()));
				assertThat("Full INC Parity:  " + value1, atu.mParity, equalTo(adder.hasParity()));
				assertThat("Full INC Overflow:  " + value1, atu.mOverflow, equalTo(adder.hasOverflow()));
				assertThat("Full INC nFlag:  " + value1, atu.mNFlag, equalTo(adder.isNFlagSet()));
				assertThat("Full INC Carry:  " + value1, atu.mCarry, equalTo(adder.hasCarry()));
		} // for val1
	}// testByteIncrement

}// class AdderTest2
