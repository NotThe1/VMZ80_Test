package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;


public class InstructionsMainPage01_01 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	int instructionBase = 0X1000;
	int valueBase = 0X2000; // (HL) - m
	
	String message;
	
	@Test
	public void testHALT() {
		int testCount = 0x10;
		int instructionLocation = instructionBase;
		for ( int i = 0; i < testCount; i++) {
			ioBuss.write(instructionLocation++, (byte) 0x00); // NOP
		}// for i testCount
		ioBuss.write(instructionLocation, (byte) 0x76); // HALT
		wrs.setProgramCounter(instructionBase);
		boolean run = true;
		while(run) {
			run = cpu.startInstruction();
		}//while
		assertThat("Halt Instruction",instructionBase + testCount+1,equalTo(wrs.getProgramCounter()));
	}//testHalt

	@Test
	public void testLD_d_r() {
		Register regSource, regDestination;
		byte opCode, value;
		for (int v = 0; v < 0x100; v++) {
			value = (byte) v;

			for (int i = 0x40; i < 0x80; i++) {
				if (i == 0x76) {
					continue;
				} // if halt skip
				opCode = (byte) i;
				loadInstructions(opCode);
				regSource = Z80.getSingleRegister(i & 0b0000_0111);
				wrs.setReg(regSource, value);
				if (regSource.equals(Register.M)) {
					wrs.setDoubleReg(Register.M, valueBase);
					ioBuss.write(valueBase, value);
				} // if source is (HL)
				wrs.setReg(regSource, value);

				regDestination = Z80.getSingleRegister((i & 0b0011_1000) >> 3);
				if (regDestination.equals(Register.M)) {
					if (regSource.equals(Register.L) || regSource.equals(Register.H)) {
						continue;
					} // if reg H or L
					wrs.setDoubleReg(Register.M, valueBase);
				} // if source is (HL)

				message = String.format("opCode: %02X, r: %s, r': %s, value: %02X%n", i, regDestination, regSource,
						value);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(message, value, equalTo(wrs.getReg(regDestination)));
				if (regDestination.equals(Register.M)) {
					assertThat("* " + message, value, equalTo(ioBuss.read(valueBase)));
				}// if destination M
				// System.out.printlf(message);
			} // for i
		} // for v value

	}// testLD_d_r

	////////////////////////////////////////////
	private void loadInstructions(byte code) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < 0x0100; i++) {
			ioBuss.write(instructionLocation++, code);
		} // for codes

		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

}// class InstructionsMainPage01_01
