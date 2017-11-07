package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionsMainPage0_01 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();
	int testCount;
	int instructionBase = 0X1000;
	int valueBase = 0X2000; // (HL) - m
	Register thisRegister;
	Random random = new Random();
	String message;
	boolean sign, zero, halfCarry, parity, overflow, nFlag, carry;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
	}// setUp

	@Test
	public void testLDrrnn() {
		/* @formatter:off */
		byte[] instructions = new byte[] {(byte) 0x01,(byte) 00,(byte) 00,
				                          (byte) 0x11,(byte) 00,(byte) 00,
				                          (byte) 0x21,(byte) 00,(byte) 00,
				                          (byte) 0x31,(byte) 00,(byte) 00,};
         /* @formatter:on  */
		int instructionCount = instructions.length / 3;

		byte[] valueArray = new byte[] { (byte) 00, (byte) 00 };
		byte hi, lo;
		for (int value = 0; value < 65535; value++) {
			hi = (byte) ((value >> 8) & Z80.BYTE_MASK);
			lo = (byte) (value & Z80.BYTE_MASK);
			valueArray[0] = lo;
			valueArray[1] = hi;
			// put nn into the instructions LD rr,nn
			for (int i = 0; i < instructionCount; i++) {
				instructions[(i * 3) + 1] = lo;
				instructions[(i * 3) + 2] = hi;
			} // for

			loadInstructions(instructions);

			for (Register r : Z80.doubleRegisters1) {
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(r.toString(), value, equalTo(wrs.getDoubleReg(r)));
				// System.out.printf("r -> %s, value -> %04X%n",r,wrs.getDoubleReg(r));
			} // for set values
			assertThat(" PC ", instructionBase + instructions.length, equalTo(wrs.getProgramCounter()));

		} //  for value 

	}// testLDrrnn

	@Test
	public void testJR_Conditional_Carry() {
		byte codeSet = 0x38; // Z
		byte codeReset = 0x30; // NC

		byte[] instructions = new byte[] { codeSet, (byte) 0x00, codeReset, (byte) 0x00 };

		ioBuss.writeDMA(instructionBase, instructions);

		byte displacement;
		for (int i = -128; i < 128; i++) {
			displacement = (byte) i;
			ioBuss.write(instructionBase + 1, displacement);
			ioBuss.write(instructionBase + 3, displacement);
			ioBuss.write(instructionBase + 5, displacement);
			ioBuss.write(instructionBase + 7, displacement);
			ccr.clearAllCodes();

			wrs.setProgramCounter(instructionBase);
			ccr.setCarryFlag(true);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("1: i -> " + i, instructionBase + i + 2, equalTo(wrs.getProgramCounter()));

			wrs.setProgramCounter(instructionBase);
			ccr.setCarryFlag(false);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("2: i -> " + i, instructionBase + 2, equalTo(wrs.getProgramCounter()));

			ccr.clearAllCodes();
			ccr.setCarryFlag(false);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("3: i -> " + i, instructionBase + i + 4, equalTo(wrs.getProgramCounter()));

			wrs.setProgramCounter(instructionBase + 2);
			ccr.setCarryFlag(true);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("4: i -> " + i, instructionBase + 4, equalTo(wrs.getProgramCounter()));

			// System.out.printf("i = %02X, net address: %04X%n",i, instructionBase + i+2);
		} // for

	}// testJR_Conditional_Carry

	@Test
	public void testJR_Conditional_Zero() {
		byte codeSet = 0x28; // Z
		byte codeReset = 0x20; // NZ

		byte[] instructions = new byte[] { codeSet, (byte) 0x00, codeReset, (byte) 0x00 };

		ioBuss.writeDMA(instructionBase, instructions);

		byte displacement;
		for (int i = -128; i < 128; i++) {
			displacement = (byte) i;
			ioBuss.write(instructionBase + 1, displacement);
			ioBuss.write(instructionBase + 3, displacement);
			ioBuss.write(instructionBase + 5, displacement);
			ioBuss.write(instructionBase + 7, displacement);
			ccr.clearAllCodes();

			wrs.setProgramCounter(instructionBase);
			ccr.setZeroFlag(true);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("1: i -> " + i, instructionBase + i + 2, equalTo(wrs.getProgramCounter()));

			wrs.setProgramCounter(instructionBase);
			ccr.setZeroFlag(false);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("2: i -> " + i, instructionBase + 2, equalTo(wrs.getProgramCounter()));

			ccr.clearAllCodes();
			ccr.setZeroFlag(false);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("3: i -> " + i, instructionBase + i + 4, equalTo(wrs.getProgramCounter()));

			wrs.setProgramCounter(instructionBase + 2);
			ccr.setZeroFlag(true);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("4: i -> " + i, instructionBase + 4, equalTo(wrs.getProgramCounter()));

			// System.out.printf("i = %02X, net address: %04X%n",i, instructionBase + i+2);
		} // for

	}// testJR_Conditional_Zero

	@Test
	public void testDJNZ() {
		byte[] instructions = new byte[] { (byte) 0x10, (byte) 0x80, (byte) 0xC3, (byte) 0x00, (byte) 0x10 };
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setReg(Register.B, (byte) 0x80);
		wrs.setProgramCounter(instructionBase);

		for (byte i = 0x7F; i >= 1; i--) {
			cpu.executeInstruction(wrs.getProgramCounter());
			// System.out.printf("(PC) -> %04X, (B) -> %02X%n", wrs.getProgramCounter(), wrs.getReg(Register.B));

			assertThat("i -> " + i, i, equalTo(wrs.getReg(Register.B)));
			assertThat("pc ", 0x1002, equalTo(wrs.getProgramCounter()));
			cpu.executeInstruction(wrs.getProgramCounter());
		} // while
		cpu.executeInstruction(wrs.getProgramCounter());

		assertThat("bc ==0 -> ", (byte) 0x00, equalTo(wrs.getReg(Register.B)));
		assertThat("pc ", 0x0F82, equalTo(wrs.getProgramCounter()));

	}// testDJNZ

	@Test
	public void testJR() {
		ioBuss.write(instructionBase, (byte) 0x18);
		byte displacement;
		for (int i = -128; i < 128; i++) {
			displacement = (byte) i;
			ioBuss.write(instructionBase + 1, displacement);
			wrs.setProgramCounter(instructionBase);
			cpu.executeInstruction(instructionBase);
			// System.out.printf("i = %02X, net address: %04X%n",i, instructionBase + i+2);
			assertThat("i -> " + i, instructionBase + i + 2, equalTo(wrs.getProgramCounter()));
		} // for
	}// testJR

	@Test
	public void testEX_AFAFp() {
		testCount = 0x800;
		byte[] valuesAcc = new byte[testCount + 2];
		byte[] valuesFlags = new byte[testCount + 2];
		random.nextBytes(valuesAcc);
		random.nextBytes(valuesFlags);
		byte work, flags;
		for (int i = 0; i < valuesFlags.length; i++) {
			work = valuesFlags[i];
			work = (byte) (work & (byte) 0b11010111);
			valuesFlags[i] = work;
		} // for
			// displayArray("Acc ",valuesAcc);
			// displayArray("Flags",valuesFlags);

		loadInstructions(testCount + 2, (byte) 0x08);

		// seed the AF'
		wrs.setAcc(valuesAcc[0]);
		ccr.setConditionCode(valuesFlags[0]);
		cpu.executeInstruction(wrs.getProgramCounter());

		for (int i = 0; i < testCount; i++) {
			wrs.setAcc(valuesAcc[i + 1]);
			ccr.setConditionCode(valuesFlags[i + 1]);
			assertThat("Hard Test", valuesFlags[i + 1], equalTo(ccr.getConditionCode()));
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("Acc   i -> " + i, valuesAcc[i], equalTo(wrs.getAcc()));
			assertThat("Flags i -> " + i, valuesFlags[i], equalTo(ccr.getConditionCode()));

			flags = valuesFlags[i];
			sign = (flags & Z80.BIT_SIGN) == Z80.BIT_SIGN;
			assertThat("Sign i -> " + i, sign, equalTo(ccr.isSignFlagSet()));
			zero = (flags & Z80.BIT_ZERO) == Z80.BIT_ZERO;
			assertThat("Zero i -> " + i, zero, equalTo(ccr.isZeroFlagSet()));
			halfCarry = (flags & Z80.BIT_AUX) == Z80.BIT_AUX;
			assertThat("HalfCarry i -> " + i, halfCarry, equalTo(ccr.isHFlagSet()));
			parity = (flags & Z80.BIT_PV) == Z80.BIT_PV;
			assertThat("P/V i -> " + i, parity, equalTo(ccr.isPvFlagSet()));
			nFlag = (flags & Z80.BIT_N) == Z80.BIT_N;
			assertThat("nFlag i -> " + i, nFlag, equalTo(ccr.isNFlagSet()));
			carry = (flags & Z80.BIT_CARRY) == Z80.BIT_CARRY;
			assertThat("carry i -> " + i, carry, equalTo(ccr.isCarryFlagSet()));
		} // for testCount
	}//

	@Test
	public void testNOP() {

		loadInstructions(testCount, (byte) 0x00);
		for (int i = 0; i < testCount; i++) {
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("count = " + i, instructionBase + i + 1, equalTo(wrs.getProgramCounter()));
		} // for testCount
	}// testNOP

	/////////////////////////////////////////

	private void loadInstructions(byte[] codes) {
		int instructionLocation = instructionBase;
		for (byte code : codes) {
			ioBuss.write(instructionLocation++, code);
		} // for codes
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	private void loadInstructions(int repeatCount, byte... codes) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < repeatCount; i++) {
			for (byte code : codes) {
				ioBuss.write(instructionLocation++, code);
			} // for codes
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	private void displayArray(String name, byte[] values) {
		System.out.print(name + " :");
		for (byte v : values) {
			System.out.print(" " + v + ",");
		} // for
		System.out.println();
	}// displayArray

}// class InstructionsMainPage0
