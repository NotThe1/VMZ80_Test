package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class AdderLogicalOperations {
	Adder adder = Adder.getInstance();
	
	byte arg1,arg2,result,ans;
	boolean sign,zero,halfCarry,overflow,parity,nFlag,carry;
	boolean carryState;
	String flags,message;	
	
	
	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
	}// setUp
	
	@Test
	public void testANDfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				result= getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %s ",arg1,arg2,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,parity,nFlag,carry);
						
				message = String.format("file AND -> %02X and %02X -> %02X", arg1,arg2,result);
				assertThat("result: " + message,result,equalTo(adder.and(arg1, arg2)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
				assertThat("parity: " +  message,parity,equalTo(adder.hasParity()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));
				scanner.next();//	Skip OR result
				scanner.next();//	Skip OR flags
				scanner.next();//	Skip XOR result
				scanner.next();//	Skip XOR flags
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testANDfile
	
	@Test
	public void testORfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				scanner.next();//	Skip AND result
				scanner.next();//	Skip AND flags
				result= getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %s ",arg1,arg2,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,parity,nFlag,carry);
						
				message = String.format("file OR -> %02X and %02X -> %02X", arg1,arg2,result);
				assertThat("result: " + message,result,equalTo(adder.or(arg1, arg2)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
				assertThat("parity: " +  message,parity,equalTo(adder.hasParity()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));
				
				scanner.next();//	Skip XOR result
				scanner.next();//	Skip XOR flags

			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testORfile
	
	
	@Test
	public void testXORfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				scanner.next();//	Skip AND result
				scanner.next();//	Skip AND flags
				scanner.next();//	Skip OR result
				scanner.next();//	Skip OR flags
				result= getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %s ",arg1,arg2,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,parity,nFlag,carry);
						
				message = String.format("file XOR -> %02X and %02X -> %02X", arg1,arg2,result);
				assertThat("result: " + message,result,equalTo(adder.xor(arg1, arg2)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
				assertThat("parity: " +  message,parity,equalTo(adder.hasParity()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));			
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testXORfile
	
	@Test
	public void testCPfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/CpOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg2 = getValue(scanner.next());
				arg1 = getValue(scanner.next());
				flags = scanner.next();
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				overflow = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %s ",arg1,arg2,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
						
				message = String.format("file CP -> %02X <-> %02X", arg1,arg2);
				adder.compare(arg1, arg2);
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
	}//testCPfile
	
	@Test
	public void testCPLfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal1.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				result= getValue(scanner.next());
				flags = scanner.next();
				
				scanner.next();//	Skip NEG result
				scanner.next();//	Skip NEG flags
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %s ",arg1,arg2,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,parity,nFlag,carry);
						
				message = String.format("file CPL -> %02X and %02X -> %02X", arg1,arg2,result);
				assertThat("result: " + message,result,equalTo(adder.complement(arg1)));
//				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
//				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
//				assertThat("parity: " +  message,parity,equalTo(adder.hasParity()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
//				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));			
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testCPLfile

	
	@Test
	public void testNEGfile() {
		
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal1.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				
				scanner.next();//	Skip CPL result
				scanner.next();//	Skip CPL flags
				
				result= getValue(scanner.next());
				flags = scanner.next();
				
				
				sign = flags.subSequence(0, 1).equals("1")?true:false;
				zero = flags.subSequence(1, 2).equals("1")?true:false;
				halfCarry = flags.subSequence(2, 3).equals("1")?true:false;
				parity = flags.subSequence(3, 4).equals("1")?true:false;
				nFlag = flags.subSequence(4, 5).equals("1")?true:false;
				carry = flags.subSequence(5, 6).equals("1")?true:false;

//				System.out.printf("%02X %02X %s ",arg1,arg2,flags);
//				System.out.printf("  %s %s %s   %s %s %s %n", sign,zero,halfCarry,parity,nFlag,carry);
						
				message = String.format("file NEG -> %02X and %02X -> %02X", arg1,arg2,result);
				assertThat("result: " + message,result,equalTo(adder.negate(arg1)));
				assertThat("sign: " +  message,sign,equalTo(adder.hasSign()));
				assertThat("zero: " +  message,zero,equalTo(adder.isZero()));
				assertThat("halfCarry: " +  message,halfCarry,equalTo(adder.hasHalfCarry()));
//				assertThat("parity: " +  message,parity,equalTo(adder.hasParity()));
				assertThat("nFlag: " +  message,nFlag,equalTo(adder.isNFlagSet()));
//				assertThat("carry: " +  message,carry,equalTo(adder.hasCarry()));			
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		}//try
	}//testNEGfile

	
	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue

	//-----------------------------------------------------------------------

	

}// class AdderTest2
