package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

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
	public void testSBC() {
		testSBC(Register.IX, false);
		testSBC(Register.IX, true);
		testSBC(Register.IY, false);
		testSBC(Register.IY, true);
	}// testSBC

	public void testSBC(Register ixy, boolean carryIn) {
		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;
		byte opCode2 = (byte) 0X9E;
		byte arg1, arg2, difference;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags = "", message = "";

		try {

			InputStream inputStream = this.getClass().getResourceAsStream("/SbcOriginal.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			wrs.setDoubleReg(ixy, valueBase);
			while (scanner.hasNextLine()) {
				// cannot skip any lines
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				if (arg2 == (byte) 0X00) {
					loadData(valueBase);
					loadInstructions(opcode1, opCode2);
				} // reload memory
				if (!carryIn) {
					difference = getValue(scanner.next());
					flags = scanner.next();
					scanner.next();
					scanner.next();
				} else {
					scanner.next();
					scanner.next();
					difference = getValue(scanner.next());
					flags = scanner.next();
				} // if carryIn

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s, CY = %s SBCC  -> %02X - %02X = %02X", ixy, carryIn, arg1, arg2, difference);

				wrs.setAcc(arg1);
				ccr.setCarryFlag(carryIn);
				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat(message, difference, equalTo(wrs.getAcc()));

				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow: " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry: " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// testADC

	
	 @Test
	public void testSUB() {
		testADD(Register.IX);
		testADD(Register.IY);
	}// testSUB

	public void testSUB(Register ixy) {
		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;
		byte opCode2 = (byte) 0X96;
		byte arg1, arg2, difference;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags, message;

		try {

			InputStream inputStream = this.getClass().getResourceAsStream("/SubOriginal.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			wrs.setDoubleReg(ixy, valueBase);
			while (scanner.hasNextLine()) {
				// cannot skip any lines
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				if (arg2 == (byte) 0X00) {
					loadData(valueBase);
					loadInstructions(opcode1, opCode2);
				} // reload memory
				difference = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s,  SUB -> %02X - %02X = %02X", ixy, arg1, arg2, difference);

				wrs.setAcc(arg1);
				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat(message, difference, equalTo(wrs.getAcc()));

				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow: " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry: " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// testSUB


	
	@Test
	public void testADC() {
		testADC(Register.IX, false);
		testADC(Register.IX, true);
		testADC(Register.IY, false);
		testADC(Register.IY, true);
	}// testADC

	public void testADC(Register ixy, boolean carryIn) {
		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;
		byte opCode2 = (byte) 0X8E;
		byte arg1, arg2, sum;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags = "", message = "";

		try {

			InputStream inputStream = this.getClass().getResourceAsStream("/AdcOriginal.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			wrs.setDoubleReg(ixy, valueBase);
			while (scanner.hasNextLine()) {
				// cannot skip any lines
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				if (arg2 == (byte) 0X00) {
					loadData(valueBase);
					loadInstructions(opcode1, opCode2);
				} // reload memory
				if (!carryIn) {
					sum = getValue(scanner.next());
					flags = scanner.next();
					scanner.next();
					scanner.next();
				} else {
					scanner.next();
					scanner.next();
					sum = getValue(scanner.next());
					flags = scanner.next();
				} // if carryIn

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s, CY = %s ADC  -> %02X + %02X = %02X", ixy, carryIn, arg1, arg2, sum);

				wrs.setAcc(arg1);
				ccr.setCarryFlag(carryIn);
				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat(message, sum, equalTo(wrs.getAcc()));

				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow: " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry: " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// testADC

	 @Test
	public void testADD() {
		testADD(Register.IX);
		testADD(Register.IY);
	}// testADD

	public void testADD(Register ixy) {
		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;
		byte opCode2 = (byte) 0X86;
		byte arg1, arg2, sum;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags, message;

		try {

			InputStream inputStream = this.getClass().getResourceAsStream("/AddOriginal.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			wrs.setDoubleReg(ixy, valueBase);
			while (scanner.hasNextLine()) {
				// cannot skip any lines
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				if (arg2 == (byte) 0X00) {
					loadData(valueBase);
					loadInstructions(opcode1, opCode2);
				} // reload memory
				sum = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s,  ADD -> %02X + %02X = %02X", ixy, arg1, arg2, sum);

				wrs.setAcc(arg1);
				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat(message, sum, equalTo(wrs.getAcc()));

				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow: " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry: " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// testADD

	 @Test
	public void testLoadToReg() {
		new Random().nextBytes(values);
		int targetLocation;
		Register activeRegister;
		byte[] regCodes = new byte[] { (byte) 0X46, (byte) 0X4E, (byte) 0X56, (byte) 0X5E, (byte) 0X66, (byte) 0X6E,
				(byte) 0X7E };
		// IX
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XDD, regCodes[r]);
			wrs.setIX(valueBase);
			for (int i = -128; i < 128; i++) {
				targetLocation = valueBase + i;
				cpuBuss.write(targetLocation, values[128 + i]);
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X", activeRegister, i,
						values[128 + i], targetLocation);

				// System.out.println(message);

				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("IX " + message, values[128 + i], equalTo(wrs.getReg(activeRegister)));
			} // for
		} // for r - registers

		// IY
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XFD, regCodes[r]);
			wrs.setIY(valueBase);
			for (int i = -128; i < 128; i++) {
				targetLocation = valueBase + i;
				cpuBuss.write(targetLocation, values[128 + i]);
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X", activeRegister, i,
						values[128 + i], targetLocation);

				// System.out.println(message);

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
				(byte) 0X77 };
		// IX
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XDD, regCodes[r]);
			wrs.setIX(valueBase);
			for (int i = -128; i < 128; i++) {
				wrs.setReg(activeRegister, values[128 + i]);
				targetLocation = valueBase + i;
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X", activeRegister, i,
						values[128 + i], targetLocation);

				// System.out.println(message);

				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("IX ", values[128 + i], equalTo(cpuBuss.read(targetLocation)));
			} // for
		} // for r - registers

		// IY
		for (int r = 0; r < registers.length; r++) {
			activeRegister = registers[r];
			loadInstructions((byte) 0XFD, regCodes[r]);
			wrs.setIY(valueBase);
			for (int i = -128; i < 128; i++) {
				wrs.setReg(activeRegister, values[128 + i]);
				targetLocation = valueBase + i;
				message = String.format("reg = %s,i= %02X,value = %02X,location = %04X", activeRegister, i,
						values[128 + i], targetLocation);

				// System.out.println(message);

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

	private void loadData(int valueBase) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, (byte) (128 + i));
		} // for i
	}// loadData

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

	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

}// class InstructionDD_IXY4
