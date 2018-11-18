package hardware.centralProcessingUnit;

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
 
public class InstructionCB4 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();

	int instructionBase = 0X1000;
	int hlRegisterValue = 0X0100; // (HL) - m
	Register thisRegister;

	String message;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
	}// setUp

	@Test
	public void testBIT() {
		int numberOfRegisters = 8; // 8;
		int numberOfValues = 0X100; // 0X100;
		byte opCodeBase = (byte) 0X40;

		boolean bitState;

		for (int r = 0; r < numberOfRegisters; r++) {
			thisRegister = Z80.getSingleRegister(r);
			for (int value = 0; value < numberOfValues; value++) {
				loadInstructionCB(opCodeBase);
				wrs.setProgramCounter(instructionBase);
				for (int testBit = 0; testBit < 8; testBit++) {
					wrs.setReg(thisRegister, (byte) value);
					bitState = (Z80.getBit(testBit) & (byte) value) != Z80.getBit(testBit) ? true : false;
					message = String.format("reg = %s, value = %02X, bit = %d", thisRegister, value, testBit);
					// System.out.printf(message + ", Set = %s,\topCodeBase = %02X%n", bitState, opCodeBase);
					cpu.executeInstruction(wrs.getProgramCounter());
					assertThat(message, bitState, equalTo(ccr.isZeroFlagSet()));
					assertThat("Hflag: " + message, true, equalTo(ccr.isHFlagSet()));
					assertThat("Nflag: " + message, false, equalTo(ccr.isNFlagSet()));
				} // for b bits
				wrs.setProgramCounter(instructionBase);
			} // for v value
			opCodeBase++;
		} // for register
	}// testBIT

	@Test
	public void testRES() {
		int numberOfRegisters = 8; // 8;
		int numberOfValues = 0X100; // 0X100;
		byte opCodeBase = (byte) 0X80;
		byte result;

		for (int r = 0; r < numberOfRegisters; r++) {
			thisRegister = Z80.getSingleRegister(r);
			for (int value = 0; value < numberOfValues; value++) {
				loadInstructionCB(opCodeBase);
				wrs.setProgramCounter(instructionBase);
				for (int testBit = 0; testBit < 8; testBit++) {
					wrs.setReg(thisRegister, (byte) value);
					result = (byte) (value & Z80.getBitNot(testBit));
					message = String.format("reg = %s, value = %02X, bit = %d", thisRegister, value, testBit);
					// System.out.printf(message + ", result = %02X,\topCodeBase = %02X%n", result, opCodeBase);
					cpu.executeInstruction(wrs.getProgramCounter());
					assertThat(message, result, equalTo(wrs.getReg(thisRegister)));
				} // for b bits
				wrs.setProgramCounter(instructionBase);
			} // for v value
			opCodeBase++;
		} // for register
	}// testRES


	@Test
	public void testSET() {
		int numberOfRegisters = 8; // 8;
		int numberOfValues = 0X100; // 0X100;
		byte opCodeBase = (byte) 0XC0;
		byte result;

		for (int r = 0; r < numberOfRegisters; r++) {
			thisRegister = Z80.getSingleRegister(r);
			for (int value = 0; value < numberOfValues; value++) {
				loadInstructionCB(opCodeBase);
				wrs.setProgramCounter(instructionBase);
				for (int testBit = 0; testBit < 8; testBit++) {
					wrs.setReg(thisRegister, (byte) value);
					result = (byte) (value | Z80.getBit(testBit));
					message = String.format("reg = %s, value = %02X, bit = %d", thisRegister, value, testBit);
					// System.out.printf(message + ", result = %02X,\topCodeBase = %02X%n", result, opCodeBase);
					cpu.executeInstruction(wrs.getProgramCounter());
					assertThat(message, result, equalTo(wrs.getReg(thisRegister)));
				} // for b bits
				wrs.setProgramCounter(instructionBase);
			} // for v value
			opCodeBase++;
		} // for register
	}// testSET

	private void loadInstructionCB(byte base) {
		byte cb = (byte) 0XCB;
		for (int i = 0; i < 0X08; i++) {
			ioBuss.write(instructionBase + (i * 2), cb);
			ioBuss.write(instructionBase + (i * 2) + 1, (byte) (base + (i * 8)));

			// System.out.printf("%04X, [%02X] %04X,[%02X]%n", (i * 2),cb,(i * 2) + 1,(byte) (base + (i *8)));
		} // for
	}// loadInstructionCB

}// class InstructionCB4
