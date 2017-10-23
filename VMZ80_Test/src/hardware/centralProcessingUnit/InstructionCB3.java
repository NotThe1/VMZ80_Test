package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class InstructionCB3 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();

	String sSource;
	byte source, result;
	String flags, message;
	boolean sign, zero, halfCarry, overflow, parity, nFlag, carry;
	HashMap<Byte, FileFlag> mapSLA = new HashMap<>();
	HashMap<Byte, FileFlag> mapSRA = new HashMap<>();
	HashMap<Byte, FileFlag> mapSRL = new HashMap<>();
	FileFlag ffSLA, ffSRA, ffSRL;
	int instructioBase = 0X1000;
	int hlRegisterValue = 0X0100; // (HL) - m

	Register thisRegister;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
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
			System.out.println(source  + " " + result+ " " + flags);
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// setUp
	
	@Test
	public void testSLA_file() {
		byte[] instruction2 = new byte[] { (byte) 0X20, (byte) 0X21, (byte) 0X22, (byte) 0X23, (byte) 0X24, (byte) 0X25,
				(byte) 0X26, (byte) 0X27 };

		wrs.setDoubleReg(Z80.Register.HL, hlRegisterValue); // (HL) - m
		
		Collection<FileFlag> valuesRLC = mapSLA.values();
		
		for (int r = 0; r < 8; r++) {
			thisRegister = Z80.singleRegisters[r];
			loadInstructionCB(instruction2[r]);
			
			wrs.setProgramCounter(instructioBase);
			for (FileFlag ff : valuesRLC) {
//				 System.out.printf("source %02X <-> result %02X%n", ff.getSource(),ff.getResult());
				wrs.setReg(thisRegister, ff.getSource());
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("SLA :" + ff.getSource(), ff.getResult(), equalTo(wrs.getReg(thisRegister)));
				assertThat("SLA sign :" + ff.getSource(), ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat("SLA Zero :" + ff.getSource(), ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("SLA halfFlag :" + ff.getSource(), ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("SLA parity :" + ff.getSource(), ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat("SLA nFlag :" + ff.getSource(), ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("SLA carry :" + ff.getSource(), ff.carry, equalTo(ccr.isCarryFlagSet()));
			} // for each result set		
//			System.out.printf("index = %d, Register = %s%n", r,thisRegister);
		} // for each register

	}// testSLA_file
	
	
	@Test
	public void testSRA_file() {
		byte[] instruction2 = new byte[] { (byte) 0X28, (byte) 0X29, (byte) 0X2A, (byte) 0X2B, (byte) 0X2C, (byte) 0X2D,
				(byte) 0X2E, (byte) 0X2F };

		wrs.setDoubleReg(Z80.Register.HL, hlRegisterValue); // (HL) - m
		
		Collection<FileFlag> valuesRLC = mapSRA.values();
		
		for (int r = 0; r < 8; r++) {
			thisRegister = Z80.singleRegisters[r];
			loadInstructionCB(instruction2[r]);
			
			wrs.setProgramCounter(instructioBase);
			for (FileFlag ff : valuesRLC) {
//				 System.out.printf("source %02X <-> result %02X%n", ff.getSource(),ff.getResult());
				wrs.setReg(thisRegister, ff.getSource());
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("SRA :" + ff.getSource(), ff.getResult(), equalTo(wrs.getReg(thisRegister)));
				assertThat("SRA sign :" + ff.getSource(), ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat("SRA Zero :" + ff.getSource(), ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("SRA halfFlag :" + ff.getSource(), ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("SRA parity :" + ff.getSource(), ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat("SRA nFlag :" + ff.getSource(), ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("SRA carry :" + ff.getSource(), ff.carry, equalTo(ccr.isCarryFlagSet()));
			} // for each result set		
//			System.out.printf("index = %d, Register = %s%n", r,thisRegister);
		} // for each register

	}// testSRA_file
	
	
	@Test
	public void testSRL_file() {
		byte[] instruction2 = new byte[] { (byte) 0X38, (byte) 0X39, (byte) 0X3A, (byte) 0X3B, (byte) 0X3C, (byte) 0X3D,
				(byte) 0X3E, (byte) 0X3F };

		wrs.setDoubleReg(Z80.Register.HL, hlRegisterValue); // (HL) - m
		
		Collection<FileFlag> valuesRLC = mapSRL.values();
		
		for (int r = 0; r < 8; r++) {
			thisRegister = Z80.singleRegisters[r];
			loadInstructionCB(instruction2[r]);
			
			wrs.setProgramCounter(instructioBase);
			for (FileFlag ff : valuesRLC) {
//				 System.out.printf("source %02X <-> result %02X%n", ff.getSource(),ff.getResult());
				wrs.setReg(thisRegister, ff.getSource());
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("SRL :" + ff.getSource(), ff.getResult(), equalTo(wrs.getReg(thisRegister)));
				assertThat("SRL sign :" + ff.getSource(), ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat("SRL Zero :" + ff.getSource(), ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("SRL halfFlag :" + ff.getSource(), ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("SRL parity :" + ff.getSource(), ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat("SRL nFlag :" + ff.getSource(), ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("SRL carry :" + ff.getSource(), ff.carry, equalTo(ccr.isCarryFlagSet()));
			} // for each result set		
//			System.out.printf("index = %d, Register = %s%n", r,thisRegister);
		} // for each register

	}// testSRL_file
	


	////////////////////////////////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	private void loadInstructionCB(byte instruction) {
		byte cb = (byte) 0XCB;
		for (int i = 0; i < 0X101; i++) {
			ioBuss.write(instructioBase + (i * 2), cb);
			ioBuss.write(instructioBase + (i * 2) + 1, instruction);
		} // for
	}// loadInstructionCB


}//class InstructionCB3
