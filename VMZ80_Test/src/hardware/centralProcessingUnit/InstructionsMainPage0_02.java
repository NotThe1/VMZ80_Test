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

public class InstructionsMainPage0_02 {
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
	public void testDECrr() {
	/* @formatter:off */
	byte[] instructions = new byte[] {(byte) 0x0B, (byte) 0x1B,(byte) 0x2B, (byte) 0x3B};
     /* @formatter:on  */
		int ans;
		for (int value = 0; value < 65535; value++) {
			ans = (value - 1) & 0xFFFF;
			loadInstructions(instructions);

			for (Register reg : Z80.doubleRegisters1) {
				wrs.setDoubleReg(reg, value);
				cpu.executeInstruction(wrs.getProgramCounter());
//				 System.out.printf("reg -> %s, value -> %04X, ans ->%04X%n",reg,wrs.getDoubleReg(reg),ans);
				assertThat(reg.toString(), ans, equalTo(wrs.getDoubleReg(reg)));
			} // for set values
			assertThat(" PC ", instructionBase + instructions.length, equalTo(wrs.getProgramCounter()));
			
		}// for value
	} // testDECrr

	 @Test
	public void testINCrr() {
	/* @formatter:off */
	byte[] instructions = new byte[] {(byte) 0x03, (byte) 0x13,(byte) 0x23, (byte) 0x33};
     /* @formatter:on  */
		int ans;
		for (int value = 0; value < 65535; value++) {
			ans = (value + 1) & 0xFFFF;
			loadInstructions(instructions);

			for (Register reg : Z80.doubleRegisters1) {
				wrs.setDoubleReg(reg, value);
				cpu.executeInstruction(wrs.getProgramCounter());
				// System.out.printf("reg -> %s, value -> %04X, ans ->%n",reg,wrs.getDoubleReg(reg),ans);
				assertThat(reg.toString(), ans, equalTo(wrs.getDoubleReg(reg)));
			} // for set values
			assertThat(" PC ", instructionBase + instructions.length, equalTo(wrs.getProgramCounter()));
		} // for value

	}// testINCrr

	 @Test
	public void testLD_Acc_inn() {// LD (nn),A
		byte value;
		int valueLocation;
		int valueLimit = 0xFFFF - (instructionBase + 0X100);

		/* @formatter:off */
		byte[] instructions = new byte[] {(byte) 0x32,(byte) 00,(byte) 00  };
         /* @formatter:on  */

		byte hi, lo, hiLoc, loLoc;
		for (int i = 0; i < 0x0100; i++) {
			value = (byte) i;
			valueLocation = random.nextInt(valueLimit) + (instructionBase + 0X100);
			hiLoc = (byte) ((valueLocation >> 8) & Z80.BYTE_MASK);
			loLoc = (byte) (valueLocation & Z80.BYTE_MASK);
			// put nn into the instructions LD rr,nn
			instructions[1] = loLoc;
			instructions[2] = hiLoc;
			loadInstructions(instructions);

			wrs.setAcc(value);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("PC ", instructionBase + 3, equalTo(wrs.getProgramCounter()));
			assertThat("Loc ", value, equalTo(cpuBuss.read(valueLocation)));
		} // for value
	}// testLD_Acc_inn

	 @Test
	public void testLD_inn_Acc() {// LD A,(nn)
		byte value;
		int valueLocation;
		int valueLimit = 0xFFFF - (instructionBase + 0X100);

		/* @formatter:off */
		byte[] instructions = new byte[] {(byte) 0x3A,(byte) 00,(byte) 00  };
         /* @formatter:on  */

		byte hi, lo, hiLoc, loLoc;
		for (int i = 0; i < 0x0100; i++) {
			value = (byte) i;
			valueLocation = random.nextInt(valueLimit) + (instructionBase + 0X100);
			hiLoc = (byte) ((valueLocation >> 8) & Z80.BYTE_MASK);
			loLoc = (byte) (valueLocation & Z80.BYTE_MASK);
			// put nn into the instructions LD rr,nn
			instructions[1] = loLoc;
			instructions[2] = hiLoc;
			loadInstructions(instructions);

			cpuBuss.write(valueLocation, value);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("PC ", instructionBase + 3, equalTo(wrs.getProgramCounter()));
			assertThat("Acc ", value, equalTo(wrs.getAcc()));
		} // for value
	}// testLD_inn_Acc

	@Test
	public void testLD_HL_inn() {// LD HL,(nn)
		int valueLocation;
		int valueLimit = 0xFFFF - (instructionBase + 0X100);

		/* @formatter:off */
		byte[] instructions = new byte[] {(byte) 0x2A,(byte) 00,(byte) 00  };
         /* @formatter:on  */

		byte hi, lo, hiLoc, loLoc;
		for (int value = 0; value < 65535; value++) {
			valueLocation = random.nextInt(valueLimit) + (instructionBase + 0X100);
			hiLoc = (byte) ((valueLocation >> 8) & Z80.BYTE_MASK);
			loLoc = (byte) (valueLocation & Z80.BYTE_MASK);
			// put nn into the instructions LD rr,nn
			instructions[1] = loLoc;
			instructions[2] = hiLoc;
			loadInstructions(instructions);

			hi = (byte) ((value >> 8) & Z80.BYTE_MASK);
			lo = (byte) (value & Z80.BYTE_MASK);
			// valueArray[0] = lo;
			// valueArray[1] = hi;

			cpuBuss.writeWord(valueLocation, hi, lo);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("Value ", value, equalTo(wrs.getDoubleReg(Register.HL)));
			assertThat("PC ", instructionBase + 3, equalTo(wrs.getProgramCounter()));

		} // for value

	}// testLD_HL_inn

	 @Test
	public void testLD_acc_iRR() {// LD A,(rr)
		byte[] instructions = new byte[] { (byte) 0x0A, (byte) 0x1A };
		loadInstructions(instructions);

		byte value;

		int valueLocation;
		int valueLimit = 0xFFFF - (instructionBase + 0X100);
		testCount = 0x0100;

		for (int tc = 0; tc < testCount; tc++) {
			valueLocation = random.nextInt(valueLimit) + (instructionBase + 0X100);
			for (int i = 0; i < 0x100; i++) {
				wrs.setProgramCounter(instructionBase);
				value = (byte) i;
				wrs.setDoubleReg(Register.BC, valueLocation);
				cpuBuss.write(valueLocation, value);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(" LD (BC),A ", value, equalTo(wrs.getAcc()));

				wrs.setDoubleReg(Register.DE, valueLocation);
				cpuBuss.write(valueLocation, value);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(" LD (DE),A ", value, equalTo(wrs.getAcc()));

				assertThat("PC ", instructionBase + 2, equalTo(wrs.getProgramCounter()));

				// System.out.printf(" value = %1$02X [%1$d] %n", value);
			} // for value
		} // for tc - testCount

	}// testLD_acc_iRR

	@Test
	public void testLD_inn_HL() {// LD (nn),HL
		int valueLocation;
		int valueLimit = 0xFFFF - (instructionBase + 0X100);

		/* @formatter:off */
		byte[] instructions = new byte[] {(byte) 0x22,(byte) 00,(byte) 00  };
         /* @formatter:on  */

		byte hi, lo, hiLoc, loLoc;
		for (int value = 0; value < 65535; value++) {
			valueLocation = random.nextInt(valueLimit) + (instructionBase + 0X100);
			hiLoc = (byte) ((valueLocation >> 8) & Z80.BYTE_MASK);
			loLoc = (byte) (valueLocation & Z80.BYTE_MASK);
			// put nn into the instructions LD rr,nn
			instructions[1] = loLoc;
			instructions[2] = hiLoc;
			loadInstructions(instructions);

			hi = (byte) ((value >> 8) & Z80.BYTE_MASK);
			lo = (byte) (value & Z80.BYTE_MASK);
			// valueArray[0] = lo;
			// valueArray[1] = hi;

			wrs.setDoubleReg(Register.HL, value);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("Value lo loc ", lo, equalTo(cpuBuss.read(valueLocation)));
			assertThat("Value hi loc ", hi, equalTo(cpuBuss.read(valueLocation + 1)));
			assertThat("PC ", instructionBase + 3, equalTo(wrs.getProgramCounter()));

		} // for value

	}// testLD_inn_HL

	@Test
	public void testLD_iRR_acc() {// LD (rr),A
		byte[] instructions = new byte[] { (byte) 0x02, (byte) 0x12 };
		loadInstructions(instructions);

		byte value;
		int valueLocation;
		int valueLimit = 0xFFFF - (instructionBase + 0X100);
		testCount = 0x0100;

		for (int tc = 0; tc < testCount; tc++) {
			valueLocation = random.nextInt(valueLimit) + (instructionBase + 0X100);
			for (int i = 0; i < 0x100; i++) {
				wrs.setProgramCounter(instructionBase);
				value = (byte) i;
				wrs.setDoubleReg(Register.BC, valueLocation);
				wrs.setAcc(value);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(" LD (BC),A ", value, equalTo(cpuBuss.read(valueLocation)));

				wrs.setDoubleReg(Register.DE, valueLocation);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(" LD (DE),A ", value, equalTo(cpuBuss.read(valueLocation)));

				assertThat("PC ", instructionBase + 2, equalTo(wrs.getProgramCounter()));

				// System.out.printf(" value = %1$02X [%1$d] %n", value);
			} // for value
		} // for tc - testCount

	}// testLDiRRacc

	/////////////////////////////////////////

	private void loadInstructions(byte[] codes) {
		int instructionLocation = instructionBase;
		for (byte code : codes) {
			ioBuss.write(instructionLocation++, code);
		} // for codes
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

}// class InstructionsMainPage0_02
