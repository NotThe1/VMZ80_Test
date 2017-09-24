package hardware.arithmeticUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import hardware.ArithmeticUnit;

public class DAATestArithmeticUnit {
	ArithmeticUnit au = ArithmeticUnit.getInstance();

	byte arg1,arg2,diff,sum,daa,ans;
	boolean CY,HC,CY1,HC1;
	int intDiff,intSum;
	String message;
	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		au = ArithmeticUnit.getInstance();
	}// setUp

	@Test
	public void testAfterSUB() {
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/daaSubOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine();
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				
				diff = getValue(scanner.next());
				CY= getState(scanner.nextInt());
				HC= getState(scanner.nextInt());
				daa = getValue(scanner.next());

				CY1= getState(scanner.nextInt());
				HC1= getState(scanner.nextInt());
				intDiff = scanner.nextInt();
				
//				System.out.printf("%d  %d %2X %s %s %02X %s %s %d%n",arg1,arg2,diff,CY,HC,daa,CY1,HC1,intDiff);
//				System.out.printf("%02X  %02X %2X %s %s %02X %s %s %d%n",arg1,arg2,diff,CY,HC,daa,CY1,HC1,intDiff);
				
				
				message = String.format("SUB -> %d - %d = %02X", arg1,arg2,diff);
				ans = au.sub(arg1, arg2);
				assertThat("ans: " + message,diff,equalTo(au.sub(arg1, arg2)));
				assertThat("CY: " +  message,CY,equalTo(au.hasCarry()));
				assertThat("HC: " +  message,HC,equalTo(au.hasHalfCarry()));
				assertThat("DAA: " +  message,daa,equalTo(au.daa(ans, true, CY, HC)));
				assertThat("CY1: " +  message,CY1,equalTo(au.hasCarry()));
				assertThat("HC1: " +  message,HC1,equalTo(au.hasHalfCarry()));
									
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			fail("testAfterSUB");
	//		System.out.printf("%d  %d %2X %s %s %02X %s %s %d%n",arg1,arg2,diff,CY,HC,daa,CY1,HC1,intDiff);
		}
	}//testAftrSUB
	
	@Test
	public void testAfterADD() {
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/daaAddOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine();
			while (scanner.hasNextLine()){
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				
				sum = getValue(scanner.next());
				CY= getState(scanner.nextInt());
				HC= getState(scanner.nextInt());
				daa = getValue(scanner.next());

				CY1= getState(scanner.nextInt());
				HC1= getState(scanner.nextInt());
//				intSum = scanner.nextInt();
				
//				System.out.printf("%d  %d %2X %s %s %02X %s %s %d%n",arg1,arg2,sum,CY,HC,daa,CY1,HC1,intSum);
//				System.out.printf("%02X  %02X %2X %s %s %02X %s %s %d%n",arg1,arg2,sum,CY,HC,daa,CY1,HC1,intSum);
				
				message = String.format("ADD -> %d + %d = %02X", arg1,arg2,sum);
				ans = au.add(arg1, arg2);
				assertThat("ans: " + message,sum,equalTo(au.add(arg1, arg2)));
				assertThat("CY: " +  message,CY,equalTo(au.hasCarry()));
				assertThat("HC: " +  message,HC,equalTo(au.hasHalfCarry()));
				assertThat("DAA: " +  message,daa,equalTo(au.daa(ans, false, CY, HC)));
				assertThat("CY1: " +  message,CY1,equalTo(au.hasCarry()));
				assertThat("HC1: " +  message,HC1,equalTo(au.hasHalfCarry()));
							
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			fail("testAfterADD");
//			System.out.printf("%d  %d %2X %s %s %02X %s %s %d%n",arg1,arg2,sum,CY,HC,daa,CY1,HC1,intSum);
		}//try
	}//testAftrADD
	
	
	private boolean getState(int value){
		return value==1?true:false;
	}//getState

	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue

}//class AdderDAATest
