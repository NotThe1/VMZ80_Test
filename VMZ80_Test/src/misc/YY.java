package misc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

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
//	Register[] registers = new Register[] { Z80.Register.BC, Z80.Register.DE, Z80.Register.SP };
//	String message;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
	}// setUp


	@Test
	public void testCPDR() {
		byte arg1;
		int memoryBase = 0X1000;
		int memoryCount = 0X100;
		byte opcode1 = (byte) 0XED;
		byte opcode2 = (byte) 0XB9;
		for (int i = 0; i < memoryCount; i++) {
			ioBuss.write(i * 2, opcode1); // write the instruction
			ioBuss.write((i * 2) + 1, opcode2);

			ioBuss.write(i + memoryBase, (byte) i);// write data
		} // for set Instructions & memory

		arg1 = (byte) 0X7F;
		setAcc(arg1,memoryBase+memoryCount-1,memoryCount);
		cpu.executeInstruction(wrs.getProgramCounter());
//		System.out.printf("HL : %04X, ", wrs.getDoubleReg(Register.HL));
//		System.out.printf("BC : %04X, ", wrs.getDoubleReg(Register.BC));
//		System.out.printf("Acc : %02X%n", wrs.getAcc());
		
		assertThat("Sign: arg1 = " + arg1,true,equalTo(ccr.isZeroFlagSet()));
		assertThat("Value: arg1 = " + arg1,arg1,equalTo(ioBuss.read(wrs.getDoubleReg(Register.HL)+1)));

		arg1 = (byte) 0XFF;
		setAcc(arg1,memoryBase,memoryCount);
		wrs.setDoubleReg(Register.BC, 0X10);
		
		cpu.executeInstruction(wrs.getProgramCounter());
		assertThat("BC: arg1 = " + arg1,00,equalTo(wrs.getDoubleReg(Register.BC)));
			
	}// testCPDR
	
	
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue


	
	private void setAcc(byte value,int memoryBase,int memoryCount) {
		wrs.setAcc(value);
		wrs.setProgramCounter(0);
		wrs.setDoubleReg(Register.HL, memoryBase);
		wrs.setDoubleReg(Register.BC, memoryCount - 1);	
	}//setAcc

	//////////////////////////////////////////////////////////////////////////////////////

//	private byte getValue(String value) {
//		int tempInt;
//		tempInt = Integer.valueOf(value, 16);
//		return (byte) tempInt;
//	}// getValue

	// private void setUpWordRegisters(int registerIndex, byte[] arg1, byte[] arg2, boolean carryState) {
	// wrs.setDoubleReg(Z80.Register.HL, arg1); // Set HL
	// wrs.setDoubleReg(registers[registerIndex], arg2);// Set rr
	// ccr.setCarryFlag(carryState);
	// cpu.executeInstruction(wrs.getProgramCounter());// do the ADCcy
	// }// setUpWordRegisters
	//
//	private void setUpMemory(int location, byte[] newValues) {
//		wrs.setProgramCounter(location);
//		// int size = newValues.length;
//		ioBuss.writeDMA(location, newValues);
//	}// setUpMemory
		//
		// private byte[] getValueArray(String value) {
		// int workingValue = Integer.valueOf(value, 16);
		// byte msb = (byte) ((workingValue & 0XFF00) >> 8);
		// byte lsb = (byte) ((byte) workingValue & 0X00FF);
		// return new byte[] { lsb, msb };
		// }// getValueArray

}// class YY
