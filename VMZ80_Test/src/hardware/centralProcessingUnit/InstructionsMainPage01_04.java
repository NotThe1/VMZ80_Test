package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import org.junit.Test;

import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionsMainPage01_04 {

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
	String bitString;
	boolean sign, zero, halfCarry, parity, overflow, nFlag, carry;
	boolean cy, hc, cy1, hc1;
	byte arg1, arg2, result, daaResult, ans, key;
	String flags, flagsDAA, sArg1;

	byte[] instructions = new byte[0x0100];

	@Test
	public void testDAA() {
		testDAA("ADD");
		 testDAA("SUB");
	}

	// @Test
	public void testDAA(String priorOperation) {
		// assume its ADD
		String fileName = "/daaAddRevision01.txt";
		String operator = "+";
		byte[] instructions = new byte[] { (byte) 0x80, (byte) 0x27 };
		if (priorOperation.equals("SUB")) {
			instructions = new byte[] { (byte) 0x90, (byte) 0x27 };
			fileName = "/daaSubRevision01.txt";
			operator = "-";
		} // if prior operation

		loadInstructions(instructions);

		try {
			InputStream inputStream = this.getClass().getResourceAsStream(fileName);
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				wrs.setProgramCounter(instructionBase);
				sArg1 = scanner.next();
				if (sArg1.equals(";")) {
					scanner.nextLine();
					continue;
				} // if - skip the line

				arg1 = getValue(sArg1);
				arg2 = getValue(scanner.next());

				result = getValue(scanner.next());
				flags = scanner.next();

				// sign = flags.subSequence(0, 1).equals("1") ? true : false;
				// zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				// parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				daaResult = getValue(scanner.next());

				flagsDAA = scanner.next();

				message = String.format("%02X %s %02X = %02X", arg1, operator, arg2, result);
//				System.out.println(message);
				// prior operation
				wrs.setAcc(arg1);
				wrs.setReg(Register.B, arg2);
				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat("result: " + message, result, equalTo(wrs.getAcc()));
				assertThat("CY: " + message, carry, equalTo(ccr.isCarryFlagSet()));
				assertThat("HC: " + message, halfCarry, equalTo(ccr.isHFlagSet()));

				// DAA
				// ccr.setCarryFlag(carry);
				// ccr.setHFlag(halfCarry);
				// ccr.setNFlag(nFlag);

				sign = flagsDAA.subSequence(0, 1).equals("1") ? true : false;
				zero = flagsDAA.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flagsDAA.subSequence(2, 3).equals("1") ? true : false;
				parity = flagsDAA.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flagsDAA.subSequence(4, 5).equals("1") ? true : false;
				carry = flagsDAA.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s %02X --daa--> %02X", priorOperation, result, daaResult);
//				System.out.println(message);

				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat("result: " + message, daaResult, equalTo(wrs.getAcc()));
				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("parity: " + message, parity, equalTo(ccr.isPvFlagSet()));

				assertThat("carry: " + message, carry, equalTo(ccr.isCarryFlagSet()));

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			fail("testAfterADD");
		} // try
	}// testDAA

	////////////////////////////////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	private void loadInstructions(byte[] codes) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < 0x0100; i++) {
			for (byte code : codes) {
				ioBuss.write(instructionLocation++, code);
			} // for codes

		} // for i
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

}// class InstructionsMainPage0_04
