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

public class InstructionDD_IXY5 {

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
	final static String OPERATION_AND = "and";
	final static String OPERATION_OR = "or";
	final static String OPERATION_XOR = "xor";

	@Before
	public void setUp() throws Exception {
		assertThat("X", 0, equalTo(0));
	}// setUp
	
	@Test
	public void testLDSP() {
		int testCount = 0X100;
		Random random = new Random();
		int value1,value2;
		//IX
		loadInstructions(testCount, (byte) 0xDD, (byte) 0xF9);
		for (int  i = 0; i < testCount;i++) {
			value1 = random.nextInt(0x10000);
			value2 = random.nextInt(0x10000);
			wrs.setStackPointer(value2);
			assertThat("IX ",value2,equalTo(wrs.getStackPointer()));
			wrs.setIX( value1);
			assertThat("IX 1",value1,equalTo(wrs.getIX()));
			
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IX 2",value1,equalTo(wrs.getStackPointer()));

		} // for i - testCount

		//IY
		loadInstructions(testCount, (byte) 0xFD, (byte) 0xF9);
		for (int  i = 0; i < testCount;i++) {
			value1 = random.nextInt(0x10000);
			value2 = random.nextInt(0x10000);
			wrs.setStackPointer(value2);
			assertThat("IY ",value2,equalTo(wrs.getStackPointer()));
			wrs.setIY( value1);
			assertThat("IY 1",value1,equalTo(wrs.getIY()));
			
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IY 2",value1,equalTo(wrs.getStackPointer()));

		} // for i - testCount

	}//testLDSP
	
	@Test
	public void testEXSP() {
		int testCount = 0X100;
		Random random = new Random();
		int value1,value2;
		value1 = 0x3988;
		value2 = 0x4890;
		loadInstructions(1, (byte) 0xDD, (byte) 0xE3);
		
		wrs.setIX(value1);
		wrs.setStackPointer(0x0100);
		cpuBuss.writeWord(0x0100, (byte) 0x90, (byte) 0x48);
		cpu.executeInstruction(wrs.getProgramCounter());
//		System.out.printf("IX -> %04X%n", wrs.getIX());

////////////////////////////////////
		
		//IX
		loadInstructions(testCount, (byte) 0xDD, (byte) 0xE3);

		for (int  i = 0; i < testCount;i++) {
			value1 = random.nextInt(0x10000);
			byte[] array1 = makeArray(value1);
			value2 = random.nextInt(0x10000);
			byte[] array2 = makeArray(value2);
			
			wrs.setStackPointer(valueBase);
			wrs.setIX(value1);
			ioBuss.writeDMA(valueBase, array2);
			
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IX ",value2,equalTo(wrs.getIX()));
			assertThat("IX 0: ",array1[0],equalTo(cpuBuss.read(valueBase)));
			assertThat("IX 1: ",array1[1],equalTo(cpuBuss.read(valueBase +1)));
		} // for i - testCount
		
		//IY
		loadInstructions(testCount, (byte) 0xFD, (byte) 0xE3);

		for (int  i = 0; i < testCount;i++) {
			value1 = random.nextInt(0x10000);
			byte[] array1 = makeArray(value1);
			value2 = random.nextInt(0x10000);
			byte[] array2 = makeArray(value2);
			
			wrs.setStackPointer(valueBase);
			wrs.setIY(value1);
			ioBuss.writeDMA(valueBase, array2);
			
//			System.out.printf("value1 -> %04X, ans[0] = %02X, ans[1] = %02X ", value1,array1[0],array1[1]);
//			System.out.printf("value2 -> %04X, ans[0] = %02X, ans[1] = %02X%n", value2,array2[0],array2[1]);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IY ",value2,equalTo(wrs.getIY()));
			assertThat("IY 0: ",array1[0],equalTo(cpuBuss.read(valueBase)));
			assertThat("IY 1: ",array1[1],equalTo(cpuBuss.read(valueBase +1)));
		} // for i - testCount
		
	}//testEXSP
	
	private byte[] makeArray(int value) { // [lo] [hi]
		byte[] ans = new byte[2];
		ans[0] = (byte) (value & 0x00FF);	// lo
		ans [1] = (byte) ((value >>8) & 0x00FF);		//hi
		return ans;
	}//makeArray

	@Test
	public void testJP() {
		int[] targets = new int[] { 0x1234, 0xDEF0, 0x0000, 0xFFFF };
		int testCount = targets.length;

		// IX
		loadInstructions(testCount, (byte) 0xDD, (byte) 0xE9);
		for (int i = 0; i < testCount; i++) {
//			System.out.printf("targets[%d] -> %04X%n", i, targets[i]);
			wrs.setIX(targets[i]);
			wrs.setProgramCounter(instructionBase + (i * 2));
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IX JP ", targets[i], equalTo(wrs.getProgramCounter()));
		} // for i - testCount
		
		// IY
		loadInstructions(testCount, (byte) 0xFD, (byte) 0xE9);
		for (int i = 0; i < testCount; i++) {
//			System.out.printf("targets[%d] -> %04X%n", i, targets[i]);
			wrs.setIY(targets[i]);
			wrs.setProgramCounter(instructionBase + (i * 2));
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IY JP ", targets[i], equalTo(wrs.getProgramCounter()));
		} // for i - testCount
		
		
	}// testJP

	@Test
	public void testPUSH_POP() {
		int[] answers = new int[] { 0x1234, 0x5678, 0x9ABC, 0xDEF0 };
		int testCount = answers.length;
		wrs.setStackPointer(valueBase);

		loadInstructions(testCount, (byte) 0xDD, (byte) 0xE5);
		for (int i = 0; i < testCount; i++) {
			wrs.setIX(answers[i]);
			cpu.executeInstruction(wrs.getProgramCounter());
		} // for i - testCount

		assertThat("Push: PC", instructionBase + (2 * testCount), equalTo(wrs.getProgramCounter()));
		assertThat("Push: SP", valueBase - (2 * testCount), equalTo(wrs.getStackPointer()));

		loadInstructions(testCount, (byte) 0xFD, (byte) 0xE1);
		for (int i = testCount - 1; i >= 0; i--) {
//			System.out.printf("answers[%d] -> %04X%n", i, answers[i]);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("POP:", answers[i], equalTo(wrs.getIY()));
		} // for i - testCount

	}// testPUSH_POP

	 @Test
	public void testPUSH() {
		byte[] zeros = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00 };

		byte[] values = new byte[] { (byte) 0x34, (byte) 0x12, (byte) 0x78, (byte) 0x56, (byte) 0xBC, (byte) 0x9A,
				(byte) 0xF0, (byte) 0xDE };
		int[] answers = new int[] { 0x1234, 0x5678, 0x9ABC, 0xDEF0 };
		int testCount = answers.length;
		int valueIndex;
		// IX
		ioBuss.writeDMA(valueBase, zeros);
		wrs.setStackPointer(valueBase);
		loadInstructions(testCount, (byte) 0xDD, (byte) 0xE5);
		valueIndex = 0;
		for (int i = 0; i < testCount; i++) {
			wrs.setIX(answers[i]);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IX: SP", valueBase - (2 * (i + 1)), equalTo(wrs.getStackPointer()));
			assertThat("IX: data 0", values[valueIndex++], equalTo(cpuBuss.read(wrs.getStackPointer() + 0)));
			assertThat("IX: data 1", values[valueIndex++], equalTo(cpuBuss.read(wrs.getStackPointer() + 1)));
		} // for i - testCount

		// IY
		ioBuss.writeDMA(valueBase, zeros);
		wrs.setStackPointer(valueBase);
		loadInstructions(testCount, (byte) 0xFD, (byte) 0xE5);
		valueIndex = 0;
		for (int i = 0; i < testCount; i++) {
			wrs.setIY(answers[i]);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IY: SP", valueBase - (2 * (i + 1)), equalTo(wrs.getStackPointer()));
			assertThat("IY: data 0", values[valueIndex++], equalTo(cpuBuss.read(wrs.getStackPointer() + 0)));
			assertThat("IY: data 1", values[valueIndex++], equalTo(cpuBuss.read(wrs.getStackPointer() + 1)));
		} // for i - testCount

	}// testPUSH

	 @Test
	public void testPOP() {
		byte[] values = new byte[] { (byte) 0x34, (byte) 0x12, (byte) 0x78, (byte) 0x56, (byte) 0xBC, (byte) 0x9A,
				(byte) 0xF0, (byte) 0xDE };
		ioBuss.writeDMA(valueBase, values);
		int[] answers = new int[] { 0x1234, 0x5678, 0x9ABC, 0xDEF0 };
		int testCount = answers.length;
		// IX
		wrs.setIX(-1);
		wrs.setStackPointer(valueBase);
		loadInstructions(testCount, (byte) 0xDD, (byte) 0xE1);
		for (int i = 0; i < testCount; i++) {
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("POP IX", answers[i], equalTo(wrs.getIX()));
		} // for i - testCount
			// IY
		wrs.setIY(-1);
		wrs.setStackPointer(valueBase);
		loadInstructions(testCount, (byte) 0xFD, (byte) 0xE1);
		for (int i = 0; i < testCount; i++) {
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("POP IY", answers[i], equalTo(wrs.getIY()));
		} // for i - testCount
	}// testPOP_IXY

	 @Test
	public void testAND() {
		testDriverAndOrXor(Register.IX, OPERATION_AND);
		testDriverAndOrXor(Register.IY, OPERATION_AND);
	}// testAND

	 @Test
	public void testOR() {
		testDriverAndOrXor(Register.IX, OPERATION_OR);
		testDriverAndOrXor(Register.IY, OPERATION_OR);
	}// testAND

	 @Test
	public void testXOR() {
		testDriverAndOrXor(Register.IX, OPERATION_XOR);
		testDriverAndOrXor(Register.IY, OPERATION_XOR);
	}// testAND

	 @Test
	public void testCP() {
		testDriverCP(Register.IX);
		testDriverCP(Register.IY);
	}// testAND

	public void testDriverCP(Register ixy) {

		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;
		byte opCode2 = (byte) 0XBE;

		byte arg1, arg2, result = (byte) 0X00;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags = "", message = "";

		try {
			loadData(valueBase);
			loadInstructions(opcode1, opCode2);

			InputStream inputStream = this.getClass().getResourceAsStream("/CpOriginal.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			wrs.setDoubleReg(ixy, valueBase);
			// int index =0;
			while (scanner.hasNextLine()) {
				// cannot skip any lines
				arg2 = getValue(scanner.next());
				arg1 = getValue(scanner.next());
				flags = scanner.next();

				if (arg1 == (byte) 0X00) {
					loadData(valueBase, arg2);
					loadInstructions(opcode1, opCode2);
				} // reload memory

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s,   %02X CP %02X = %02X", ixy, arg1, arg2, result);
				// System.out.printf("index -> %02X, Acc -> %02X%n",index++,arg1);
				wrs.setAcc(arg1);
				cpu.executeInstruction(wrs.getProgramCounter());

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

	}// testDriverCP

	public void testDriverAndOrXor(Register ixy, String operation) {
		byte opCode2 = (byte) 0X00;
		switch (operation) {
		case OPERATION_AND:
			opCode2 = (byte) 0XA6;
			break;
		case OPERATION_XOR:
			opCode2 = (byte) 0XAE;
			break;
		case OPERATION_OR:
			opCode2 = (byte) 0XB6;
			break;
		}// switch - opcode1
		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;

		byte arg1, arg2, result = (byte) 0X00;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags = "", message = "";

		try {

			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal.txt");
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
				switch (operation) {
				case OPERATION_AND:
					result = getValue(scanner.next());
					flags = scanner.next();
					scanner.next();
					scanner.next();
					scanner.next();
					scanner.next();
					break;
				case OPERATION_OR:
					scanner.next();
					scanner.next();
					result = getValue(scanner.next());
					flags = scanner.next();
					scanner.next();
					scanner.next();
					break;
				case OPERATION_XOR:
					scanner.next();
					scanner.next();
					scanner.next();
					scanner.next();
					result = getValue(scanner.next());
					flags = scanner.next();
					break;
				}// switch - opcode1

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s,   %02X %s %02X = %02X", ixy, arg1, operation, arg2, result);

				wrs.setAcc(arg1);
				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat(message, result, equalTo(wrs.getAcc()));

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

	}// testDriverAndOrXor

	////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

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

	// load instructions count times ( no indexes)
	private void loadInstructions(int count, byte... codes) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < count; i++) {
			for (byte code : codes) {
				cpuBuss.write(instructionLocation++, code);
			} // for codes
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	private void loadData(int valueBase) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, (byte) (128 + i));
		} // for i
	}// loadData

	private void loadData(int valueBase, byte value) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, value);
		} // for i
	}// loadData

}// class InstructionDD_IXY5
