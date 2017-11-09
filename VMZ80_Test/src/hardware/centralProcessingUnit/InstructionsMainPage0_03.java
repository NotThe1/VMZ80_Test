package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
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

public class InstructionsMainPage0_03 {

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
	byte arg1, arg2, sum, diff, ans, key;
	String flags, sArg1;

	byte[] instructions = new byte[0x0100];
	// Left
	HashMap<Byte, FileFlag> mapRLC = new HashMap<>();
	HashMap<Byte, FileFlag> mapRLcy = new HashMap<>();
	HashMap<Byte, FileFlag> mapRLnc = new HashMap<>();
	FileFlag ffRLC, ffRLcy, ffRLnc;
	// Right
	HashMap<Byte, FileFlag> mapRRC = new HashMap<>();
	HashMap<Byte, FileFlag> mapRRcy = new HashMap<>();
	HashMap<Byte, FileFlag> mapRRnc = new HashMap<>();
	FileFlag ffRRC, ffRRcy, ffRRnc;

	@Before
	public void setUp() throws Exception {
		setUp(false);
		setUp(true);
	}// SetUp

	public void setUp(boolean rightSet) throws Exception {
		// assume its the left set
		String fileName = "/RotateLeftOriginal.txt";
		HashMap<Byte, FileFlag> map1 = mapRLC;
		HashMap<Byte, FileFlag> map2 = mapRLcy;
		HashMap<Byte, FileFlag> map3 = mapRLnc;
		if (rightSet) {
			fileName = "/RotateRightOriginal.txt";
			map1 = mapRRC;
			map2 = mapRRcy;
			map3 = mapRRnc;
		} // if left or right

		assertThat("keep imports", 1, equalTo(1));
		String sSource;
		byte source, result;
		boolean sign, zero, halfCarry,  parity, nFlag, carry;
		String flags;
		try {
			InputStream inputStream = this.getClass().getResourceAsStream(fileName);
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sSource = scanner.next();
				if (sSource.startsWith(";")) {
					scanner.nextLine();
					continue;
				} // if skip line
				source = getValue(sSource);

				// RC(cy = 0)
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				map3.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RC(cy = 1)
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				map2.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RLC
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				map1.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// setUp

	@Test
	public void testRotateAcc() {
		testRotateAcc(mapRLcy, (byte) 0x17, true);
		testRotateAcc(mapRLnc, (byte) 0x17, false);
		testRotateAcc(mapRLC, (byte) 0x07, true);

		testRotateAcc(mapRRcy, (byte) 0x1F, true);
		testRotateAcc(mapRRnc, (byte) 0x1F, false);
		testRotateAcc(mapRRC, (byte) 0x0F, true);

	}// testRLX

	public void testRotateAcc(HashMap<Byte, FileFlag> map, byte opCode, boolean carryIn) {
		String codeString = "xx";
		switch (opCode) {
		case 0x07:
			codeString = "RLCA ";
			break;
		case 0x0F:
			codeString = "RRCA ";
			break;
		case 0x17:
			codeString = "RLA ";
			break;
		case 0x1F:
			codeString = "RRA ";
			break;
		}// switch
		Arrays.fill(instructions, opCode);
		String carryString = carryIn ? "Cy = 1 " : "CY = 0 ";
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
		FileFlag ff;
		for (int arg = 0; arg < 0x0100; arg++) {
			ff = map.get((byte) arg);
			// System.out.printf("arg -> %02X, arg1 -> %02X, result -> %02X%n",arg, ff.getSource(),ff.getResult());
			message = String.format("%s %s: source = %02X,result = %02X", codeString, carryString, ff.getSource(),
					ff.getResult());
			ccr.setCarryFlag(carryIn);
			wrs.setAcc(ff.getSource());
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("Result: " + message, ff.getResult(), equalTo(wrs.getAcc()));
			assertThat("H Flag: " + message, ff.halfCarry, equalTo(ccr.isHFlagSet()));
			assertThat("N Flag: " + message, ff.nFlag, equalTo(ccr.isNFlagSet()));
			assertThat("Carry:: " + message, ff.carry, equalTo(ccr.isCarryFlagSet()));
		} // for
		assertThat("PC :", instructionBase + 0x0100, equalTo(wrs.getProgramCounter()));
	}// testRotateAcc


	////////////////////////////////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

}// InstructionsMainPage0_03
