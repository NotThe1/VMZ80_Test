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

public class instructionsMainPage11_03 {
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
	public void testADC_SBC() {
		testADC_SBC("ADC", false);
		 testADC_SBC("ADC",true);
		 testADC_SBC("SBC",false);
		 testADC_SBC("SBC",true);
	}// testADC_SBCfile()

	public void testADC_SBC(String operation, boolean carryIn) {
		// assume an ADC
		String fileName = "/AdcOriginal.txt";
		String operator = "+";
		String carryString = carryIn ? "CY" : "NC";
		byte[] instructions = new byte[] { (byte) 0xCE, (byte) 0x00, (byte) 0x76 };

		if (operation.equals("SBC")) {
			fileName = "/SbcOriginal.txt";
			operator = "-";
			instructions = new byte[] { (byte) 0xDE, (byte) 0x00, (byte) 0x76 };
		} // if operation

		ioBuss.writeDMA(instructionBase, instructions);

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
				} //
				arg1 = getValue(sArg1);
				arg2 = getValue(scanner.next());
				if (carryIn) {
					scanner.next();
					scanner.next();
					result = getValue(scanner.next());
					flags = scanner.next();
				} else {
					result = getValue(scanner.next());
					flags = scanner.next();
					scanner.nextLine();
				} // if carry flag

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				wrs.setProgramCounter(instructionBase);

				message = String.format("%s [%s]: %02X %s %02X = %02X", operation, carryString, arg1, operator, arg2,
						result);
				wrs.setAcc(arg1);
				ioBuss.write(instructionBase + 1, arg2);
				ccr.setCarryFlag(carryIn);
				cpu.executeInstruction(wrs.getProgramCounter());

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
	}// testADC_SBC

	@Test
	public void testADD_SUB() {
		testADD_SUB("ADD");
		testADD_SUB("SUB");
	}// testADD_SUBfile()

	public void testADD_SUB(String operation) {
		// assume an ADD
		byte[] instructions = new byte[] { (byte) 0xC6, (byte) 0x00, (byte) 0x76 };
		String fileName = "/AddOriginal.txt";
		String operator = "+";

		if (operation.equals("SUB")) {
			instructions = new byte[] { (byte) 0xD6, (byte) 0x00, (byte) 0x76 };
			fileName = "/SubOriginal.txt";
			operator = "-";
		} // if operation

		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);

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
				} //
				arg1 = getValue(sArg1);
				arg2 = getValue(scanner.next());
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				wrs.setProgramCounter(instructionBase);

				message = String.format("%s : %02X %s %02X = %02X", operation, arg1, operator, arg2, result);
				wrs.setAcc(arg1);

				ioBuss.write(instructionBase + 1, arg2);

				cpu.executeInstruction(wrs.getProgramCounter());

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
	}// testADD_SUBfile

	///////////////////////////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue
}// class instructionsMainPage11_03
