package hardware;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderShift {
	Adder adder = Adder.getInstance();
	AdderTestUtility atu = AdderTestUtility.getInstance();
	byte arg, ans;
	boolean carryIn, orginalBit7;
	byte bit0Mask = (byte) 0X01;
	byte bit0NotMask = (byte) 0XFE;
	byte bit7Mask = (byte) 0X80;
	byte bit7NotMask = (byte) 0X7F;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		// adder.clearSets();
	}// setUp
	
	@Test
	public void testSRL() {
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			boolean orginalBit0 = (arg & bit0Mask) == bit0Mask;
			ans = (byte) (arg >> 1);
			
			ans = (byte) (ans & bit7NotMask);
				// System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("SRL " + arg, ans, equalTo(adder.shiftSRL(arg)));
			assertThat("SRL Cy" + arg, orginalBit0, equalTo(adder.hasCarry()));
		} // for

	}// testSRL
	@Test
	
	public void testSRA() {
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			boolean orginalBit0 = (arg & bit0Mask) == bit0Mask;
			boolean orginalBit7 = (arg & bit7Mask) == bit7Mask;
			ans = (byte) (arg >> 1);
			
			if (orginalBit7){
				ans = (byte) (ans | bit7Mask);
			}else{
				ans = (byte) (ans & bit7NotMask);
			}//if
					
				// System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("SRL " + arg, ans, equalTo(adder.shiftSRA(arg)));
			assertThat("SRL Cy" + arg, orginalBit0, equalTo(adder.hasCarry()));
		} // for

	}// testSRA
	
	@Test
	public void testSLA() {
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			boolean orginalBit7 = (arg & bit7Mask) == bit7Mask;
			ans = (byte) (arg << 1);
			
			ans = (byte) (ans & bit0NotMask);
				// System.out.printf("Arg = %02X, ans = %02X%n", arg, ans);
			assertThat("SRL " + arg, ans, equalTo(adder.shiftSLA(arg)));
			assertThat("SRL Cy" + arg, orginalBit7, equalTo(adder.hasCarry()));
		} // for

	}// testSLA
	


}//class AdderShift
