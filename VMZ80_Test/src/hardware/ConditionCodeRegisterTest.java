package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ConditionCodeRegisterTest {
	ConditionCodeRegister ccr;
	@Before
	public void setUp() throws Exception {
		assertThat("keep imports",1,equalTo(1));
		ccr = ConditionCodeRegister.getInstance();
	}

	@Test
	public void testOffOn() {
		ccr.clearAllCodes();
		assertThat(" 01 ",false,equalTo(ccr.isSignFlagSet()));
		assertThat(" 02 ",false,equalTo(ccr.isZeroFlagSet()));
		assertThat(" 03 ",false,equalTo(ccr.isAuxFlagSet()));
		assertThat(" 04 ",false,equalTo(ccr.isPvFlagSet()));
		assertThat(" 05 ",false,equalTo(ccr.isNFlagSet()));
		assertThat(" 06 ",false,equalTo(ccr.isCarryFlagSet()));
		ccr.setSignFlag(true);
		assertThat(" 010 ",true,equalTo(ccr.isSignFlagSet()));
		ccr.setZeroFlag(true);
		assertThat(" 020 ",true,equalTo(ccr.isZeroFlagSet()));
		ccr.setAuxFlag(true);
		assertThat(" 030 ",true,equalTo(ccr.isAuxFlagSet()));
		ccr.setPvFlag(true);
		assertThat(" 040 ",true,equalTo(ccr.isPvFlagSet()));
		ccr.setNFlag(true);
		assertThat(" 050 ",true,equalTo(ccr.isNFlagSet()));
		ccr.setCarryFlag(true);
		assertThat(" 060 ",true,equalTo(ccr.isCarryFlagSet()));
		
		
		
	}//testOffOn

}
