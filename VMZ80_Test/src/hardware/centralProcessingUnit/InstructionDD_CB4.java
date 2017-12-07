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
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionDD_CB4 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	int instructionBase = 0X1000;
	int valueBase = 0X2000;

	// Register thisRegister;

	String message;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
	}// setUp

	@Test
	public void testSET() {
		testSET(Register.IX);
		 testSET(Register.IY);
	}// testRES

	public void testSET(Register regIXY) {
		byte opCodeBase = (byte) 0XC6;
		byte code0, code3;
		byte targetValue, newValue;
		byte displacement;
		String regNote = "none";

		if (regIXY.equals(Register.IX)) {
			code0 = (byte) 0xDD;
			regNote = " IX ";
		} else {
			code0 = (byte) 0xFD;
			regNote = " IY ";
		} // if regIXY
		for (int value = 0; value < 0x0100; value++) {
			targetValue = (byte) value;
			loadData(valueBase, targetValue);
			wrs.setDoubleReg(regIXY, valueBase);
			for (int testBit = 0; testBit < 8; testBit++) {
				loadData(valueBase, targetValue);
				code3 = (byte) (opCodeBase + (testBit << 3));
				loadInstructions(code0, (byte) 0xCB, code3);
				newValue = (byte) (value | Z80.BITS[testBit]);
				for (int i = 0; i < 0x0100; i++) {
					displacement = (byte) i;
					message = String.format("reg-> %s, value = %02X, bit = %d, newValue = %02X", regNote, value,
							testBit, newValue);
//					System.out.println(message);
					cpu.executeInstruction(wrs.getProgramCounter());
					assertThat(message, newValue, equalTo(cpuBuss.read(valueBase + displacement)));
					// assertThat(message, bitState, equalTo(ccr.isZeroFlagSet()));
				} //
			} // for each bit
		} // for each value
	}// testRES


	@Test
	public void testRES() {
		testRES(Register.IX);
		 testRES(Register.IY);
	}// testRES

	public void testRES(Register regIXY) {
		byte opCodeBase = (byte) 0X86;
		byte code0, code3;
		byte targetValue, newValue;
		byte displacement;
		String regNote = "none";

		if (regIXY.equals(Register.IX)) {
			code0 = (byte) 0xDD;
			regNote = " IX ";
		} else {
			code0 = (byte) 0xFD;
			regNote = " IY ";
		} // if regIXY
		for (int value = 0; value < 0x0100; value++) {
			targetValue = (byte) value;
			loadData(valueBase, targetValue);
			wrs.setDoubleReg(regIXY, valueBase);
			for (int testBit = 0; testBit < 8; testBit++) {
				loadData(valueBase, targetValue);
				code3 = (byte) (opCodeBase + (testBit << 3));
				loadInstructions(code0, (byte) 0xCB, code3);
				newValue = (byte) (value & Z80.BITS_NOT[testBit]);
				for (int i = 0; i < 0x0100; i++) {
					displacement = (byte) i;
					message = String.format("reg-> %s, value = %02X, bit = %d, newValue = %02X", regNote, value,
							testBit, newValue);
//					System.out.println(message);
					cpu.executeInstruction(wrs.getProgramCounter());
					assertThat(message, newValue, equalTo(cpuBuss.read(valueBase + displacement)));
					// assertThat(message, bitState, equalTo(ccr.isZeroFlagSet()));
				} //
			} // for each bit
		} // for each value
	}// testRES

	 @Test
	public void testBIT() {
		testBIT(Register.IX);
		testBIT(Register.IY);
	}// testBIT

	public void testBIT(Register regIXY) {
		byte opCodeBase = (byte) 0X46;
		byte code0, code3;
		byte targetValue;
		boolean bitState;
		String regNote = "none";

		if (regIXY.equals(Register.IX)) {
			code0 = (byte) 0xDD;
			regNote = " IX ";
		} else {
			code0 = (byte) 0xFD;
			regNote = " IY ";
		} // if regIXY
		for (int value = 0; value < 0x0100; value++) {
			targetValue = (byte) value;
			loadData(valueBase, targetValue);
			wrs.setDoubleReg(regIXY, valueBase);
			for (int testBit = 0; testBit < 8; testBit++) {
				code3 = (byte) (opCodeBase + (testBit << 3));
				loadInstructions(code0, (byte) 0xCB, code3);
				bitState = (Z80.BITS[testBit] & (byte) value) != Z80.BITS[testBit] ? true : false;
				for (int i = 0; i < 0x0100; i++) {
					message = String.format("reg-> %s, value = %02X, bit = %d, bitState = %s", regNote, value, testBit,
							bitState);
					// System.out.println(message);
					cpu.executeInstruction(wrs.getProgramCounter());
					assertThat(message, bitState, equalTo(ccr.isZeroFlagSet()));
				} //
			} // for each bit
		} // for each value
	}// testBIT

	////////////////////////////////////////////////////////////

	// puts displacement at second from end of codes,
	private void loadInstructions(byte code0, byte code1, byte code2) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < 0X100; i++) {
			cpuBuss.write(instructionLocation++, code0);
			cpuBuss.write(instructionLocation++, code1);
			cpuBuss.write(instructionLocation++, (byte) i);
			cpuBuss.write(instructionLocation++, code2);
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	private void loadData(int valueBase, byte value) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, value);
			// System.out.printf("location-> %02X, result -> %02X%n", valueBase + i,(byte) (128 + i));
		} // for i
	}// loadData

}// class InstructionDD_CB4
