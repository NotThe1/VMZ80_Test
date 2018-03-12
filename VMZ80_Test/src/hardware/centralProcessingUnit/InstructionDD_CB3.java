package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;
 
public class InstructionDD_CB3 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	// Register[] registers = new Register[] { Register.B, Register.C, Register.D, Register.E, Register.H, Register.L,
	// Register.A };

	HashMap<Byte, FileFlag> mapSLA = new HashMap<>();
	HashMap<Byte, FileFlag> mapSRA = new HashMap<>();
	HashMap<Byte, FileFlag> mapSRL = new HashMap<>();
	FileFlag ffSLA, ffSRA, ffSRL;

	int instructionBase = 0X1000;
	// int hlRegisterValue = 0X0100; // (HL) - m
	int valueBase = 0X2000;

	// Register thisRegister;

	// String message;

	byte value, sourceValue, targetValue;
	byte code0, code1, code2;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		String sSource;
		byte source, result;
		String flags;
		boolean sign, zero, halfCarry, parity, nFlag, carry;

		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/ShiftOriginal.txt");
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

				// SLA
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapSLA.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				scanner.next(); //skip
				scanner.next(); //skip
				
				// SRA
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapSRA.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				scanner.next(); //skip
				scanner.next(); //skip

				// SRL
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapSRL.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));
				scanner.nextLine(); //skip

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
//			System.out.println(source  + " " + result+ " " + flags);
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// setUp

	@Test
	public void testALL_file() {
		testAll(Register.IX, mapSRL);
		 testAll(Register.IX, mapSRL);
		 testAll(Register.IY, mapSRA);
		 testAll(Register.IY, mapSRA);
		 testAll(Register.IX, mapSLA);
		 testAll(Register.IX, mapSLA);

	}// testRR_file

	public void testAll(Register regIXY, HashMap<Byte, FileFlag> map) {
		String regNote = "none";
		String instNote = "none";
		// String carryNote = carry? " CY ":" NC ";
		// HashMap<Byte, FileFlag> map;
		FileFlag ff;
		if (regIXY.equals(Register.IX)) {
			code0 = (byte) 0xDD;
			regNote = " IX ";
		} else {
			code0 = (byte) 0xFD;
			regNote = " IY ";
		} // if regIXY

		code1 = (byte) 0xCB;

		if (map.equals(mapSLA)) {
			code2 = (byte) 0x26;
			instNote = " SLA ";
		} else if (map.equals(mapSRA)) {
			code2 = (byte) 0x2E;
			instNote = " SRA ";
		} else {
			code2 = (byte) 0x3E; //SRL
			instNote = " SRL ";
		} // if map

		// map = carry?mapSRA:mapSRL;

		loadInstructions(code0, code1, code2);
		wrs.setDoubleReg(regIXY, valueBase);
		for (int v = 0; v < 0X100; v++) {
			sourceValue = (byte) v;
			ff = map.get(sourceValue);
			wrs.setProgramCounter(instructionBase);
			loadData(valueBase, sourceValue);
			targetValue = ff.getResult();
			for (int d = 0; d < 0x100; d++) {
				cpu.executeInstruction(wrs.getProgramCounter());
//				System.out.println(instNote + regNote + sourceValue + " " + targetValue);
				assertThat( instNote + regNote + sourceValue, targetValue,
						equalTo(cpuBuss.read(valueBase + (byte) d)));

				assertThat(instNote + regNote + sourceValue + "sign :", ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat(instNote + regNote + sourceValue + "zero :" + sourceValue, ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat(instNote + regNote + sourceValue + "halfFlag :" + sourceValue, ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(instNote + regNote + sourceValue + "parity :" + sourceValue, ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat(instNote + regNote + sourceValue + "nFlag :" + sourceValue, ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(instNote + regNote + sourceValue + "carry :" + sourceValue, ff.carry, equalTo(ccr.isCarryFlagSet()));

			} // for d - each displacement
		} // for v - value

	}// testRR_file



	////////////////////////////////////////////////////////////////////////

	// puts displacement at second from end of codes,
	private void loadInstructions(byte code0, byte code1, byte code2) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < 0X100; i++) {
			cpuBuss.write(instructionLocation++, code0);
			cpuBuss.write(instructionLocation++, code1);
			cpuBuss.write(instructionLocation++, (byte) i);
			cpuBuss.write(instructionLocation++, code2);
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	private void loadData(int valueBase, byte value) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, value);
			// System.out.printf("location-> %02X, result -> %02X%n", valueBase + i,(byte) (128 + i));
		} // for i
	}// loadData

	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

}// class InstructionDD_CB3
