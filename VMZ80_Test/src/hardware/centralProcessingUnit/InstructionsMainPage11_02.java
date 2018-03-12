package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;


public class InstructionsMainPage11_02 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();
	int testCount;
	int instructionBase = 0X1000;
	int valueBase = 0X2000; // (HL) - m

	Register reg;
	byte arg1, arg2, result;
	byte[] wordParts;
	int wordValue;
	boolean sign, zero, halfCarry, overflow, nFlag, carry;
	boolean carryState;
	String sArg1, flags, message;

	@Test
	public void testDI_EI() {
		testCount = 0x20;
		int location = instructionBase;
		for (int i = 0; i < testCount; i++) {
			ioBuss.write(location++, (byte) 0xF3);
			ioBuss.write(location++, (byte) 0xFB);
		} // for i - testCount
		wrs.setProgramCounter(instructionBase);
		
		cpu.startInstruction();	//Disable
		assertThat("DI 1 " , false,equalTo(wrs.isIFF1Set()));
		assertThat("DI 2 " , false,equalTo(wrs.isIFF2Set()));

		cpu.startInstruction();	//Enable
		assertThat("EI 3 " , true,equalTo(wrs.isIFF1Set()));
		assertThat("EI 4 " , true,equalTo(wrs.isIFF2Set()));
		
		cpu.startInstruction();	//Disable
		assertThat("DI 5 " , false,equalTo(wrs.isIFF1Set()));
		assertThat("DI 6 " , false,equalTo(wrs.isIFF2Set()));

		cpu.startInstruction();	//Enable
		assertThat("EI 7 " , true,equalTo(wrs.isIFF1Set()));
		assertThat("EI 8 " , true,equalTo(wrs.isIFF2Set()));
		
	}// testDI_EI

	@Test
	public void testEX_DE_HL() {
		testCount = 0x20;
		Random random = new Random();
		int value1, value2;
		byte[] instructions = new byte[testCount];
		Arrays.fill(instructions, (byte) 0xEB);
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
		for (int i = 0; i < testCount; i++) {
			value1 = random.nextInt(0x10000);
			value2 = random.nextInt(0x10000);
			wrs.setDoubleReg(Register.DE, value1);
			wrs.setDoubleReg(Register.HL, value2);
			assertThat("DE before", value1, equalTo(wrs.getDoubleReg(Register.DE)));
			assertThat("HL before", value2, equalTo(wrs.getDoubleReg(Register.HL)));
			cpu.startInstruction();
			assertThat("DE before", value2, equalTo(wrs.getDoubleReg(Register.DE)));
			assertThat("HL before", value1, equalTo(wrs.getDoubleReg(Register.HL)));
		} // for i - testCount

	}// testEX_DE_HL

	@Test
	public void testEX_iSP_HL() {
		int hlStart = 0x7012;
		testCount = 0x2;
		byte[] values = new byte[] { (byte) 0x11, (byte) 0x22 };
		ioBuss.writeDMA(valueBase, values);

		byte[] instructions = new byte[testCount];
		Arrays.fill(instructions, (byte) 0xE3);
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);

		wrs.setStackPointer(valueBase);

		wrs.setDoubleReg(Register.HL, hlStart);
		cpu.startInstruction();

		assertThat("SP ", valueBase, equalTo(wrs.getStackPointer()));
		assertThat("HL ", 0x2211, equalTo(wrs.getDoubleReg(Register.HL)));
		System.out.printf("cpuBuss.readWord(%04X) = %04X%n", valueBase, cpuBuss.readWord(valueBase));
		assertThat("Memory ", 0x1270, equalTo(cpuBuss.readWord(valueBase)));
	}// testEX_iSP_HL

	@Test
	public void testLD_SP_HL() {
		Random random = new Random();
		testCount = 0x100;
		byte[] instructions = new byte[0x0100];
		Arrays.fill(instructions, (byte) 0xF9);
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);

		int value;
		for (int i = 0; i < testCount; i++) {
			wrs.setStackPointer(0x0000);
			value = random.nextInt(0x10000);
			wrs.setDoubleReg(Register.HL, value);
			cpu.startInstruction();
			assertThat("HL ", value, equalTo(wrs.getDoubleReg(Register.HL)));
			assertThat("SP ", value, equalTo(wrs.getStackPointer()));
		} // for i - test count

	}// testJMPiHL

	@Test
	public void testJMPiHL() {
		// testCount = 0x100;
		byte[] instructions = new byte[0x0100];
		Arrays.fill(instructions, (byte) 0xE9);
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);

		assertThat("0x1000 ", instructionBase, equalTo(wrs.getProgramCounter()));

		wrs.setDoubleReg(Register.HL, 0x1010);
		cpu.startInstruction();
		assertThat("0x1010 ", 0x1010, equalTo(wrs.getProgramCounter()));
		assertThat(" HL=PC 0x1010", wrs.getProgramCounter(), equalTo(wrs.getDoubleReg(Register.HL)));

		wrs.setDoubleReg(Register.HL, 0x1020);
		cpu.startInstruction();
		assertThat("0x1020 ", 0x1020, equalTo(wrs.getProgramCounter()));
		assertThat(" HL=PC 0x1020", wrs.getProgramCounter(), equalTo(wrs.getDoubleReg(Register.HL)));

	}// testJMPiHL

	@Test
	public void testEXX() {
		testCount = 0x100;
		byte[] instructions = new byte[testCount + 5];
		Arrays.fill(instructions, (byte) 0xD9);
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
		Random random = new Random();
		int[] valuesBC = new int[testCount + 1];
		int[] valuesDE = new int[testCount + 1];
		int[] valuesHL = new int[testCount + 1];

		for (int i = 0; i < testCount + 1; i++) {
			valuesBC[i] = random.nextInt(0x10000);
			valuesDE[i] = random.nextInt(0x10000);
			valuesHL[i] = random.nextInt(0x10000);
		} // for i - filling reg arrays

		wrs.setDoubleReg(Register.BC, valuesBC[0]);
		wrs.setDoubleReg(Register.DE, valuesDE[0]);
		wrs.setDoubleReg(Register.HL, valuesHL[0]);
		cpu.startInstruction();

		for (int i = 1; i < testCount; i++) {
			wrs.setDoubleReg(Register.BC, valuesBC[i]);
			wrs.setDoubleReg(Register.DE, valuesDE[i]);
			wrs.setDoubleReg(Register.HL, valuesHL[i]);
			cpu.startInstruction();
			assertThat(i + " BC ", valuesBC[i - 1], equalTo(wrs.getDoubleReg(Register.BC)));
			assertThat(i + " DE ", valuesDE[i - 1], equalTo(wrs.getDoubleReg(Register.DE)));
			assertThat(i + " HL ", valuesHL[i - 1], equalTo(wrs.getDoubleReg(Register.HL)));

		} // for i - testCount

	}// testEXX

}// class InstructionsMainPage11_02
