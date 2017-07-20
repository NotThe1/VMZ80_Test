package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class AdderDAATest {
	Adder adder = Adder.getInstance();

	byte arg1,arg2,diff,daa,ans;
	boolean CY,HC,CY1,HC1;
	int intDiff;
	String message;
	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		// adder.clearSets();
	}// setUp

	@Test
	public void testAfterSUB() {
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/daaSubOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
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
				ans = adder.sub(arg1, arg2);
				assertThat("ans: " + message,diff,equalTo(adder.sub(arg1, arg2)));
				assertThat("CY: " +  message,CY,equalTo(adder.hasCarry()));
				assertThat("HC: " +  message,HC,equalTo(adder.hasHalfCarry()));
				assertThat("DAA: " +  message,daa,equalTo(adder.daa(ans, true, CY, HC)));
				assertThat("CY1: " +  message,CY1,equalTo(adder.hasCarry()));
				assertThat("HC1: " +  message,HC1,equalTo(adder.hasHalfCarry()));
							
				
				
			}//while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.printf("%d  %d %2X %s %s %02X %s %s %d%n",arg1,arg2,diff,CY,HC,daa,CY1,HC1,intDiff);
		}
	}//testAftrSUB
	
	
	
	private boolean getState(int value){
		return value==1?true:false;
	}//getState

	private byte getValue(String value){
		int tempInt;
		tempInt = Integer.valueOf(value,16);
		return(byte)tempInt;
	}//getValue

}//class AdderDAATest
