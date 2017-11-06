package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;

public class ConditionCodeRegisterTest {
	ConditionCodeRegister ccr;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		ccr = ConditionCodeRegister.getInstance();
	}//setUp
	
	@Test
	public void testConditionCode() {
		byte flag = 0b0000_0000;
		ccr.clearAllCodes();
		assertThat(" 01 ",flag,equalTo(ccr.getConditionCode()));
		
		flag = (byte) 0b1101_0111;
		ccr.setConditionCode((byte) 0xFF);
		assertThat(" 02 ",flag,equalTo(ccr.getConditionCode()));
		
		flag = (byte) 0b0000_0000;
		ccr.setConditionCode((byte) 0x00);
		assertThat(" 02 ",flag,equalTo(ccr.getConditionCode()));
				
	}//testConditionCode

	@Test
	public void testOffOn() {
		ccr.clearAllCodes();
		assertThat(" 01 ", false, equalTo(ccr.isSignFlagSet()));
		assertThat(" 02 ", false, equalTo(ccr.isZeroFlagSet()));
		assertThat(" 03 ", false, equalTo(ccr.isHFlagSet()));
		assertThat(" 04 ", false, equalTo(ccr.isPvFlagSet()));
		assertThat(" 05 ", false, equalTo(ccr.isNFlagSet()));
		assertThat(" 06 ", false, equalTo(ccr.isCarryFlagSet()));
		ccr.setSignFlag(true);
		assertThat(" 10 ", true, equalTo(ccr.isSignFlagSet()));
		ccr.setZeroFlag(true);
		assertThat(" 11 ", true, equalTo(ccr.isZeroFlagSet()));
		ccr.setHFlag(true);
		assertThat(" 12 ", true, equalTo(ccr.isHFlagSet()));
		ccr.setPvFlag(true);
		assertThat(" 13 ", true, equalTo(ccr.isPvFlagSet()));
		ccr.setNFlag(true);
		assertThat(" 14 ", true, equalTo(ccr.isNFlagSet()));
		ccr.setCarryFlag(true);
		assertThat(" 15 ", true, equalTo(ccr.isCarryFlagSet()));
		ccr.clearAllCodes();
		assertThat(" 21 ", false, equalTo(ccr.isSignFlagSet()));
		assertThat(" 22 ", false, equalTo(ccr.isZeroFlagSet()));
		assertThat(" 23 ", false, equalTo(ccr.isHFlagSet()));
		assertThat(" 24 ", false, equalTo(ccr.isPvFlagSet()));
		assertThat(" 25 ", false, equalTo(ccr.isNFlagSet()));
		assertThat(" 26 ", false, equalTo(ccr.isCarryFlagSet()));
	}// testOffOn

	@Test
	public void testEachCode() {
		ccr.clearAllCodes();
		
		byte value = Z80.BIT_SIGN;
		ccr.setSignFlag(true);
		assertThat(" 01 ", value, equalTo(ccr.getConditionCode()));
		ccr.setSignFlag(false);
		
		 value = Z80.BIT_ZERO;
		ccr.setZeroFlag(true);
		assertThat(" 02 ", value, equalTo(ccr.getConditionCode()));
		ccr.setZeroFlag(false);
		
		 value = Z80.BIT_AUX;
		ccr.setHFlag(true);
		assertThat(" 03 ", value, equalTo(ccr.getConditionCode()));
		ccr.setHFlag(false);

		 value = Z80.BIT_PV;
		ccr.setPvFlag(true);
		assertThat(" 04 ", value, equalTo(ccr.getConditionCode()));
		ccr.setPvFlag(false);

		 value = Z80.BIT_N;
		ccr.setNFlag(true);
		assertThat(" 01 ", value, equalTo(ccr.getConditionCode()));
		ccr.setNFlag(false);

		 value = Z80.BIT_CARRY;
		ccr.setCarryFlag(true);
		assertThat(" 01 ", value, equalTo(ccr.getConditionCode()));
		ccr.setCarryFlag(false);
		
	}// testEachCode
	
	@Test
	public void testZSP() {
		ccr.clearAllCodes();
		byte value = (byte) 0X00;
		ccr.setZSP(value);
		assertThat(" 01 ", true, equalTo(ccr.isZeroFlagSet()));
		assertThat(" 02 ", false, equalTo(ccr.isSignFlagSet()));
		assertThat(" 03 ", true, equalTo(ccr.isPvFlagSet()));

		 value = (byte) 0X01;
		ccr.setZSP(value);
		assertThat(" 11 ", false, equalTo(ccr.isZeroFlagSet()));
		assertThat(" 12 ", false, equalTo(ccr.isSignFlagSet()));
		assertThat(" 13 ", false, equalTo(ccr.isPvFlagSet()));

		 value = (byte) 0X81;
		ccr.setZSP(value);
		assertThat(" 21 ", false, equalTo(ccr.isZeroFlagSet()));
		assertThat(" 22 ", true, equalTo(ccr.isSignFlagSet()));
		assertThat(" 23 ", true, equalTo(ccr.isPvFlagSet()));
		
		
		ccr.clearAllCodes();
		ccr.setCarryFlag(true);
		ccr.setHFlag(true);
		
		assertThat(" 31 ", true, equalTo(ccr.isCarryFlagSet()));
		assertThat(" 32 ", true, equalTo(ccr.isHFlagSet()));
		
		 value = (byte) 0XFF;
		ccr.setZSPclearCYandAUX(value);
		assertThat(" 33 ", true, equalTo(ccr.isSignFlagSet()));
		assertThat(" 34 ", false, equalTo(ccr.isZeroFlagSet()));
		assertThat(" 35 ", false, equalTo(ccr.isHFlagSet()));
		assertThat(" 36 ", true, equalTo(ccr.isPvFlagSet()));
		assertThat(" 37 ", false, equalTo(ccr.isNFlagSet()));
		assertThat(" 38 ", false, equalTo(ccr.isCarryFlagSet()));

				
	}//testZSP
	
}// ConditionCodeRegisterTest
