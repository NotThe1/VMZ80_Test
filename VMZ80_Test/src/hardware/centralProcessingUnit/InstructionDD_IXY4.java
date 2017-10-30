package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionDD_IXY4 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	Register[] registers = new Register[] { Register.B, Register.C, Register.D, Register.E, Register.H, Register.L,
			Register.A };

	String message;
	int instructionBase = 0X1000;
	int valueBase = 0X2000;

	byte[] values = new byte[0X0101];

	@Before
	public void setUp() throws Exception {
		assertThat("X", 0, equalTo(0));
	}// setUp

	@Test
	public void testLoadToReg() {
		new Random().nextBytes(values);
		int targetLocation;
		Register activeRegister;
		byte[] regCodes = new byte[] { (byte) 0X46, (byte) 0X4E, (byte) 0X56, (byte) 0X5E, (byte) 0X66, (byte) 0X6E,
				(byte) 0X7E};
		//IX
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XDD, regCodes[r]);
			wrs.setIX(valueBase);
			for (int i = -128; i < 128; i++) {
				targetLocation = valueBase + i;
				cpuBuss.write(targetLocation, values[128 + i]);
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X",
						activeRegister,i,values[128 + i],targetLocation);
				
//				System.out.println(message);
				
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("IX " + message, values[128 + i], equalTo(wrs.getReg(activeRegister)));
			} // for
		} // for r - registers
		
		//IY
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XFD, regCodes[r]);
			wrs.setIY(valueBase);
			for (int i = -128; i < 128; i++) {
				targetLocation = valueBase + i;
				cpuBuss.write(targetLocation, values[128 + i]);
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X",
						activeRegister,i,values[128 + i],targetLocation);
				
//				System.out.println(message);
				
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("IX " + message, values[128 + i], equalTo(wrs.getReg(activeRegister)));
			} // for
		} // for r - registers

	}// testLoadToReg

	@Test
	public void testLoadFromReg() {
		new Random().nextBytes(values);
		int targetLocation;
		Register activeRegister;
		byte[] regCodes = new byte[] { (byte) 0X70, (byte) 0X71, (byte) 0X72, (byte) 0X73, (byte) 0X74, (byte) 0X75,
				(byte) 0X77};
		//IX
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XDD, regCodes[r]);
			wrs.setIX(valueBase);
			for (int i = -128; i < 128; i++) {
				wrs.setReg(activeRegister, values[128 + i]);
				targetLocation = valueBase + i;
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X",
						activeRegister,i,values[128 + i],targetLocation);
				
//				System.out.println(message);
				
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("IX ", values[128 + i], equalTo(cpuBuss.read(targetLocation)));
			} // for
		} // for r - registers
		
		//IY
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XFD, regCodes[r]);
			wrs.setIY(valueBase);
			for (int i = -128; i < 128; i++) {
				wrs.setReg(activeRegister, values[128 + i]);
				targetLocation = valueBase + i;
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X",
						activeRegister,i,values[128 + i],targetLocation);
				
//				System.out.println(message);
				
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("IY ", values[128 + i], equalTo(cpuBuss.read(targetLocation)));
			} // for
		} // for r - registers
		
	}// testLoadFromReg

	@Test
	public void testLoadImmediate() {
		new Random().nextBytes(values);
		int targetLocation;
		// IX
		loadInstructions(values, (byte) 0XDD, (byte) 0X36);
		wrs.setIX(valueBase);
		for (int i = -128; i < 128; i++) {
			targetLocation = valueBase + i;
			// System.out.printf("i -> %02X,targetLocation -> %04X%n", i,targetLocation);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IX ", values[128 + i], equalTo(cpuBuss.read(targetLocation)));
		} // for

		// IY
		loadInstructions(values, (byte) 0XFD, (byte) 0X36);
		wrs.setIY(valueBase);
		for (int i = -128; i < 128; i++) {
			targetLocation = valueBase + i;
			// System.out.printf("i -> %02X,targetLocation -> %04X%n", i,targetLocation);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IY ", values[128 + i], equalTo(cpuBuss.read(targetLocation)));
		} // for
	}// testLoadImmediate

	///////////////////////////////////////////////////////////////////

	// // puts displacement at end of code
	private void loadInstructions(byte... codes) {
		int instructionLocation = instructionBase;
		for (int i = -128; i < 128; i++) {
			for (byte code : codes) {
				cpuBuss.write(instructionLocation++, code);
			} // for codes
			cpuBuss.write(instructionLocation++, (byte) i);
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	// puts displacement at end of code, followed by a value from array
	private void loadInstructions(byte[] values, byte... codes) {
		int instructionLocation = instructionBase;
		for (int i = -128; i < 128; i++) {
			for (byte code : codes) {
				cpuBuss.write(instructionLocation++, code);
			} // for codes
			cpuBuss.write(instructionLocation++, (byte) i);
			cpuBuss.write(instructionLocation++, values[128 + i]);
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

}// class InstructionDD_IXY4
