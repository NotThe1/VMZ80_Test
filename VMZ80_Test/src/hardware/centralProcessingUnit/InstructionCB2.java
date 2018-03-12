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
 
public class InstructionCB2 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();

	String sSource;
	byte source, result;
	String flags, message;
	boolean sign, zero, halfCarry, overflow, parity, nFlag, carry;
	HashMap<Byte, FileFlag> mapRRC = new HashMap<>();
	HashMap<Byte, FileFlag> mapRRcy = new HashMap<>();
	HashMap<Byte, FileFlag> mapRRnc = new HashMap<>();
	FileFlag ffRRC, ffRRcy, ffRRnc;
	int instructioBase = 0X1000;
	int hlRegisterValue = 0X0100; // (HL) - m

	Register thisRegister;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/RotateRightOriginal.txt");
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

				// RR(cy = 0)
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapRRnc.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RR(cy = 1)
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapRRcy.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RRC
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapRRC.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// setUp

	@Test
	public void testRRC_file() {
		byte[] instruction2 = new byte[] { (byte) 0X08, (byte) 0X09, (byte) 0X0A, (byte) 0X0B, (byte) 0X0C, (byte) 0X0D,
				(byte) 0X0E, (byte) 0X0F };

		wrs.setDoubleReg(Z80.Register.HL, hlRegisterValue); // (HL) - m
		
		Collection<FileFlag> valuesRLC = mapRRC.values();
		
		for (int r = 0; r < 8; r++) {
			thisRegister = Z80.singleRegisters[r];
			loadInstructionCB(instruction2[r]);
			
			wrs.setProgramCounter(instructioBase);
			for (FileFlag ff : valuesRLC) {
//				 System.out.printf("source %02X <-> result %02X%n", ff.getSource(),ff.getResult());
				wrs.setReg(thisRegister, ff.getSource());
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("RRC :" + ff.getSource(), ff.getResult(), equalTo(wrs.getReg(thisRegister)));
				assertThat("RRC sign :" + ff.getSource(), ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat("RRC Zero :" + ff.getSource(), ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("RRC halfFlag :" + ff.getSource(), ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("RRC parity :" + ff.getSource(), ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat("RRC nFlag :" + ff.getSource(), ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("RRC carry :" + ff.getSource(), ff.carry, equalTo(ccr.isCarryFlagSet()));
			} // for each result set		
//			System.out.printf("index = %d, Register = %s%n", r,thisRegister);
		} // for each register

	}// testRRC_file
	

	@Test
	public void testRRnc_file() {
		byte[] instruction2 = new byte[] { (byte) 0X18, (byte) 0X19, (byte) 0X1A, (byte) 0X1B, (byte) 0X1C, (byte) 0X1D,
				(byte) 0X1E, (byte) 0X1F };

		wrs.setDoubleReg(Z80.Register.HL, hlRegisterValue); // (HL) - m
		
		Collection<FileFlag> valuesRLnc = mapRRnc.values();
		
		for (int r = 0; r < 8; r++) {
			thisRegister = Z80.singleRegisters[r];
			loadInstructionCB(instruction2[r]);
			
			wrs.setProgramCounter(instructioBase);
			for (FileFlag ff : valuesRLnc) {
//				 System.out.printf("source %02X <-> result %02X%n", ff.getSource(),ff.getResult());
				wrs.setReg(thisRegister, ff.getSource());
				ccr.setCarryFlag(false);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("RR cy = 0 :" + ff.getSource(), ff.getResult(), equalTo(wrs.getReg(thisRegister)));
				assertThat("RR cy = 0  sign :" + ff.getSource(), ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat("RR cy = 0  Zero :" + ff.getSource(), ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("RR cy = 0  halfFlag :" + ff.getSource(), ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("RR cy = 0  parity :" + ff.getSource() + thisRegister, ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat("RR cy = 0  nFlag :" + ff.getSource(), ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("RR cy = 0  carry :" + ff.getSource(), ff.carry, equalTo(ccr.isCarryFlagSet()));
			} // for each result set		
//			System.out.printf("index = %d, Register = %s%n", r,thisRegister);
		} // for each register

	}// testRRnc_file


	@Test
	public void testRRcy_file() {
		byte[] instruction2 = new byte[] { (byte) 0X18, (byte) 0X19, (byte) 0X1A, (byte) 0X1B, (byte) 0X1C, (byte) 0X1D,
				(byte) 0X1E, (byte) 0X1F };

		wrs.setDoubleReg(Z80.Register.HL, hlRegisterValue); // (HL) - m
		
		Collection<FileFlag> valuesRLcy = mapRRcy.values();
		
		for (int r = 0; r < 8; r++) {
			thisRegister = Z80.singleRegisters[r];
			loadInstructionCB(instruction2[r]);
			
			wrs.setProgramCounter(instructioBase);
			for (FileFlag ff : valuesRLcy) {
//				 System.out.printf("source %02X <-> result %02X%n", ff.getSource(),ff.getResult());
				wrs.setReg(thisRegister, ff.getSource());
				ccr.setCarryFlag(true);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("RR cy = 1  :" + ff.getSource(), ff.getResult(), equalTo(wrs.getReg(thisRegister)));
				assertThat("RR cy = 1   sign :" + ff.getSource(), ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat("RR cy = 1   Zero :" + ff.getSource(), ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("RR cy = 1   halfFlag :" + ff.getSource(), ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("RR cy = 1   parity :" + ff.getSource() + thisRegister, ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat("RR cy = 1   nFlag :" + ff.getSource(), ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("RR cy = 1   carry :" + ff.getSource(), ff.carry, equalTo(ccr.isCarryFlagSet()));
			} // for each result set		
//			System.out.printf("index = %d, Register = %s%n", r,thisRegister);
		} // for each register

	}// testRRcy_file



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

}//class Instruction2
