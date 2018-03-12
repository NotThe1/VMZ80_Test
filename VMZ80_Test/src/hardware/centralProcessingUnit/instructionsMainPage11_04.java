package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;


public class instructionsMainPage11_04 {
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
	boolean sign, zero, halfCarry, overflow, nFlag, carry;
	boolean carryState;
	String sArg1, flags, message;
	
	@Test
	public void testRST() {
		byte[]	instructions = new byte[] { (byte) 0xC7, (byte) 0xCF, (byte) 0xD7, (byte) 0xDF,
				(byte) 0xE7, (byte) 0xEF, (byte) 0xF7, (byte) 0xFF};
		ioBuss.writeDMA(instructionBase, instructions);
		int[] targetLocations = new int[] {0x0000,0x0008,0x0010,0x0018,0x0020,0x0028,0x0030,0x0038};
		for ( int i = 0; i < instructions.length;i++) {
			wrs.setProgramCounter(instructionBase + i);
			assertThat(i + "PC before ",instructionBase + i,equalTo(wrs.getProgramCounter()));
			cpu.startInstruction();
			assertThat(i + "PC after ",targetLocations[i],equalTo(wrs.getProgramCounter()));
			
		}//for i - each instruction

	}//testRST

	@Test
	public void test_AND_OR_XOR() {
		test_AND_OR_XOR("AND");
		test_AND_OR_XOR("OR");
		test_AND_OR_XOR("XOR");
	}// test_AND_OR_XOR

	public void test_AND_OR_XOR(String operation) {
		byte[] instructions = new byte[3];
		switch (operation) {
		case "AND":
			instructions = new byte[] { (byte) 0xE6, (byte) 0x00, (byte) 0x76 };
			break;
		case "OR":
			instructions = new byte[] { (byte) 0xF6, (byte) 0x00, (byte) 0x76 };
			break;
		case "XOR":
			instructions = new byte[] { (byte) 0xEE, (byte) 0x00, (byte) 0x76 };
			break;
		}// switch
		ioBuss.writeDMA(instructionBase, instructions);
		// loadInstructions(Z80.singleRegisters.length, instructions);

		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal.txt");
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.equals(";")) {
					scanner.nextLine();
					continue;
				} // if skip line
				arg1 = getValue(sArg1);
				arg2 = getValue(scanner.next());

				switch (operation) {
				case "AND":
					result = getValue(scanner.next());
					flags = scanner.next();
					scanner.nextLine();
					break;
				case "OR":
					scanner.next();
					scanner.next();
					result = getValue(scanner.next());
					flags = scanner.next();
					scanner.nextLine();

					break;
				case "XOR":
					scanner.next();
					scanner.next();
					scanner.next();
					scanner.next();
					result = getValue(scanner.next());
					flags = scanner.next();

					break;
				}// switch

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				wrs.setProgramCounter(instructionBase);

				message = String.format(" %02X %s %02X = %02X", arg1, operation, arg2, result);

				wrs.setAcc(arg1);
				ioBuss.write(instructionBase + 1, arg2);
				cpu.startInstruction();

				assertThat("ans " + message, result, equalTo(wrs.getAcc()));

				assertThat("sign " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// test_AND_OR_XOR

	@Test
	public void testCP() {
		byte[] instructions = new byte[3];
		instructions = new byte[] { (byte) 0xFE, (byte) 0x00, (byte) 0x76 };
		ioBuss.writeDMA(instructionBase, instructions);

		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/CpOriginal.txt");
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.equals(";")) {
					scanner.nextLine();
					continue;
				} // if skip line
				arg2 = getValue(sArg1);
				arg1 = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				wrs.setProgramCounter(instructionBase);
				message = String.format("%02X CP %02X = %02X",  arg1, arg2, result);
				wrs.setAcc(arg1);
				ioBuss.write(instructionBase + 1, arg2);
				cpu.startInstruction();

				assertThat("sign " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// testCP

	///////////////////////////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

//	private void loadInstructions(int count, byte[] opCodes) {
//		int numberOfCodes = opCodes.length;
//		for (int i = 0; i < count; i++) {
//			ioBuss.writeDMA(instructionBase + (i * numberOfCodes), opCodes);
//		} // for i - count
//		wrs.setProgramCounter(instructionBase);
//	}// loadInstructions

}// class instructionsMainPage11_04
