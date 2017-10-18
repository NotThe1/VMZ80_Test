package misc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class YY {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	Register[] registers = new Register[] { Z80.Register.BC, Z80.Register.DE, Z80.Register.SP };
	String message;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
	}// setUp

	@Test
	public void testCPIandCPIR() {
		int numberOfBytes = 0X03;

		Register bc = Z80.Register.BC;
		Register hl = Z80.Register.HL;

		int memoryBase = 0x1000;
		byte[] memoryValues = new byte[] { (byte) 0X00, (byte) 0X7F, (byte) 0XFF };
		setUpMemory(memoryBase, memoryValues);

		int instructionLocation = 0X0000;
		byte[] opCode = new byte[] { (byte) 0XED, (byte) 0XA1, (byte) 0XED, (byte) 0XA1, (byte) 0XED, (byte) 0XA1 };// CPI

		setUpMemory(instructionLocation, opCode);

		wrs.setDoubleReg(hl, memoryBase); // Set HL - 1000
		wrs.setDoubleReg(bc, numberOfBytes);// Set BC

		byte accValue = (byte) 0X7F;

		int i = 0;

		wrs.setProgramCounter(instructionLocation);
		wrs.setAcc(accValue);
		
		cpu.executeInstruction(instructionLocation);

//		System.out.printf("bc = %04X,hl = %04X,memValue = %02X accValue = %02X%n", wrs.getDoubleReg(Register.BC),
//				wrs.getDoubleReg(Register.HL), memoryValues[i], accValue);

		assertThat(i + ": CPI ", instructionLocation + 2, equalTo(wrs.getProgramCounter()));
		assertThat(i + ": CPI - HL", memoryBase + i + 1, equalTo(wrs.getDoubleReg(hl)));
		assertThat(i + ": CPI - BC", numberOfBytes - i - 1, equalTo(wrs.getDoubleReg(bc)));
	
		assertThat(i + ": CPI - Sign", false, equalTo(ccr.isSignFlagSet()));
		assertThat(i + ": CPI - Zero", false, equalTo(ccr.isZeroFlagSet()));
		assertThat(i + ": CPI - Half Carry", false, equalTo(ccr.isHFlagSet()));
		assertThat(i + ": CPI - P/V", true, equalTo(ccr.isPvFlagSet()));
		assertThat(i + ": CPI - N flag", true, equalTo(ccr.isNFlagSet()));
		i++;
		

		System.out.printf("%s\tSign%n", ccr.isSignFlagSet());
		System.out.printf("%s\tZero%n", ccr.isZeroFlagSet());
		System.out.printf("%s\tHalf Carry%n", ccr.isHFlagSet());
		System.out.printf("%s\tP/V%n", ccr.isPvFlagSet());
		System.out.printf("%s\tN flag%n", ccr.isNFlagSet());
		
		
		cpu.executeInstruction(instructionLocation);
		assertThat(i + ": CPI ", instructionLocation + 4 , equalTo(wrs.getProgramCounter()));
		assertThat(i + ": CPI - HL", memoryBase + i + 1, equalTo(wrs.getDoubleReg(hl)));
		assertThat(i + ": CPI - BC", numberOfBytes - i - 1, equalTo(wrs.getDoubleReg(bc)));
	
		assertThat(i + ": CPI - Sign", false, equalTo(ccr.isSignFlagSet()));
		assertThat(i + ": CPI - Zero", true, equalTo(ccr.isZeroFlagSet()));
		assertThat(i + ": CPI - Half Carry", false, equalTo(ccr.isHFlagSet()));
		assertThat(i + ": CPI - P/V", true, equalTo(ccr.isPvFlagSet()));
		assertThat(i + ": CPI - N flag", true, equalTo(ccr.isNFlagSet()));
		i++;


		cpu.executeInstruction(instructionLocation);
		assertThat(i + ": CPI ", instructionLocation + 6 , equalTo(wrs.getProgramCounter()));
		assertThat(i + ": CPI - HL", memoryBase + i + 1, equalTo(wrs.getDoubleReg(hl)));
		assertThat(i + ": CPI - BC", numberOfBytes - i - 1, equalTo(wrs.getDoubleReg(bc)));
	
		assertThat(i + ": CPI - Sign", true, equalTo(ccr.isSignFlagSet()));
		assertThat(i + ": CPI - Zero", false, equalTo(ccr.isZeroFlagSet()));
		assertThat(i + ": CPI - Half Carry", false, equalTo(ccr.isHFlagSet()));
		assertThat(i + ": CPI - P/V", false, equalTo(ccr.isPvFlagSet()));
		assertThat(i + ": CPI - N flag", true, equalTo(ccr.isNFlagSet()));


		///////////////////////////////////////////////////////////////////////////////
		
		

	}// testCPIandCPIR

	// private byte getValue(String value) {
	// int tempInt;
	// tempInt = Integer.valueOf(value, 16);
	// return (byte) tempInt;
	// }// getValue

	// private void setUpWordRegisters(int registerIndex, byte[] arg1, byte[] arg2, boolean carryState) {
	// wrs.setDoubleReg(Z80.Register.HL, arg1); // Set HL
	// wrs.setDoubleReg(registers[registerIndex], arg2);// Set rr
	// ccr.setCarryFlag(carryState);
	// cpu.executeInstruction(wrs.getProgramCounter());// do the ADCcy
	// }// setUpWordRegisters
	//
	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory
		//
		// private byte[] getValueArray(String value) {
		// int workingValue = Integer.valueOf(value, 16);
		// byte msb = (byte) ((workingValue & 0XFF00) >> 8);
		// byte lsb = (byte) ((byte) workingValue & 0X00FF);
		// return new byte[] { lsb, msb };
		// }// getValueArray

}// class YY
