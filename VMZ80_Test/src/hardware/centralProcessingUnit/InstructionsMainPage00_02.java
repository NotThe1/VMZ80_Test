package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;


public class InstructionsMainPage00_02 {
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
	byte arg1, arg2, sum, diff, ans;
	String flags, sArg1;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
	}// setUp

	@Test
	public void testLD_r_n() {// LD r,n
		Register reg;
		byte[] instructions = new byte[] { (byte) 0x06, (byte) 0x0E, (byte) 0x16, (byte) 0x1E, (byte) 0x26, (byte) 0x2E,
				(byte) 0x36, (byte) 0x3E };

		int limit = 0x0100;
		byte value;
		for (int r = 0; r < Z80.singleRegisters.length; r++) {
			loadInstructions(limit, instructions[r]);
			for (int v = 0; v < limit; v++) {
				value = (byte) v;
				reg = Z80.singleRegisters[r];
				if (reg.equals(Register.M)) {
					wrs.setDoubleReg(Register.HL, valueBase);
					ioBuss.write(valueBase, (byte) 0x00);
				} // reg
				message = String.format("Reg = %s, value = %02X", reg.toString(), v);
//				System.out.println(message);
				;
				cpu.executeInstruction(wrs.getProgramCounter());
				if (reg.equals(Register.M)) {
					wrs.setDoubleReg(Register.HL, valueBase);
					assertThat("ioBuss " + message, value, equalTo(ioBuss.read(valueBase)));
				} // reg

				assertThat(message, value, equalTo(wrs.getReg(reg)));
			} // for value
		} // for register
	}//testLD_r_n

	@Test
	public void testINC_DED_r() {
		byte[] instructions = new byte[] { (byte) 0x04, (byte) 0x0C, (byte) 0x14, (byte) 0x1C, (byte) 0x24, (byte) 0x2C,
				(byte) 0x34, (byte) 0x3C };
		testINC_DEC_r("INC", "/IncOriginal.txt", instructions);

		instructions = new byte[] { (byte) 0x05, (byte) 0x0D, (byte) 0x15, (byte) 0x1D, (byte) 0x25, (byte) 0x2D,
				(byte) 0x35, (byte) 0x3D };
		testINC_DEC_r("DEC", "/DecOrignal.txt", instructions);
	}// testINC_DED_r

	public void testINC_DEC_r(String operation, String fileName, byte[] instructions) {
		loadInstructions(instructions);
		try {
			InputStream inputStream = this.getClass().getResourceAsStream(fileName);
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.equals(";")) {
					scanner.nextLine();
					continue;
				} // if - skip line
				arg1 = getValue(sArg1);
				sum = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				wrs.setProgramCounter(instructionBase);

				for (Register reg : Z80.singleRegisters) {
					if (reg.equals(Register.M)) {
						wrs.setDoubleReg(Register.HL, valueBase);
						ioBuss.write(valueBase, arg1);
					} else {
						wrs.setReg(reg, arg1);
					} // if INC (m)
					message = String.format("file %s(%s) -> %d  = %02X", operation, reg.toString(), arg1, sum);
					// System.out.println(message);
					cpu.executeInstruction(wrs.getProgramCounter());
					if (reg.equals(Register.M)) {
						assertThat("ans: " + message, sum, equalTo(ioBuss.read(valueBase)));
					} // if INC (m)
					assertThat("ans: " + message, sum, equalTo(wrs.getReg(reg)));
					assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
					assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
					assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
					assertThat("overFlow: " + message, overflow, equalTo(ccr.isPvFlagSet()));
					assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
				} // for reg - each register

				// System.out.printf("%02X %02X %s ",arg1,sum,flags);
				// System.out.printf(" %s %s %s %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// testIncr

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
				// System.out.printf("reg -> %s, value -> %04X, ans ->%04X%n",reg,wrs.getDoubleReg(reg),ans);
				assertThat(reg.toString(), ans, equalTo(wrs.getDoubleReg(reg)));
			} // for set values
			assertThat(" PC ", instructionBase + instructions.length, equalTo(wrs.getProgramCounter()));

		} // for value
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

		byte hiLoc, loLoc;
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

		byte  hiLoc, loLoc;
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

	private void loadInstructions(int limit, byte opCode) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < limit; i++) {
			ioBuss.write(instructionLocation++, opCode);
			ioBuss.write(instructionLocation++, (byte) i);
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

}// class InstructionsMainPage0_02
