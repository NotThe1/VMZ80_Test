package misc;

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


public class YY {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	// Register[] registers = new Register[] { Z80.Register.BC, Z80.Register.DE, Z80.Register.SP };
	// String message;

	String sSource;
	byte source, result;
	String flags, message;
	boolean sign, zero, halfCarry, overflow, parity, nFlag, carry;
	HashMap<Byte, FileFlag> mapRLC = new HashMap<>();
	HashMap<Byte, FileFlag> mapRLcy = new HashMap<>();
	HashMap<Byte, FileFlag> mapRLnc = new HashMap<>();
	FileFlag ffRLC, ffRLcy, ffRLnc;
	int instructioBase = 0X1000;
	int hlRegisterValue = 0X0100; // (HL) - m

	Register thisRegister;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/RotateLeftOriginal.txt");
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
				mapRLnc.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RC(cy = 1)
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapRLcy.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RLC
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapRLC.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// setUp

	@Test
	public void testRLcy_file() {
		byte[] instruction2 = new byte[] { (byte) 0X10, (byte) 0X11, (byte) 0X12, (byte) 0X13, (byte) 0X14, (byte) 0X15,
				(byte) 0X16, (byte) 0X17 };

		wrs.setDoubleReg(Z80.Register.HL, hlRegisterValue); // (HL) - m
		
		Collection<FileFlag> valuesRLcy = mapRLcy.values();
		
		for (int r = 0; r < 8; r++) {
			thisRegister = Z80.getSingleRegister(r);
			loadInstructionCB(instruction2[r]);
			
			wrs.setProgramCounter(instructioBase);
			for (FileFlag ff : valuesRLcy) {
				 System.out.printf("source %02X <-> result %02X%n", ff.getSource(),ff.getResult());
				wrs.setReg(thisRegister, ff.getSource());
				ccr.setCarryFlag(true);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat("RLC :" + ff.getSource(), ff.getResult(), equalTo(wrs.getReg(thisRegister)));
				assertThat("RLC sign :" + ff.getSource(), ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat("RLC Zero :" + ff.getSource(), ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("RLC halfFlag :" + ff.getSource(), ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("RLC parity :" + ff.getSource() + thisRegister, ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat("RLC nFlag :" + ff.getSource(), ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("RLC carry :" + ff.getSource(), ff.carry, equalTo(ccr.isCarryFlagSet()));
			} // for each result set		
			System.out.printf("index = %d, Register = %s%n", r,thisRegister);
		} // for each register

	}// testRLcy_file

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

	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////

	class FileFlag {
		public boolean sign, zero, halfCarry, parity, nFlag, carry;
		private byte source, result;

		public FileFlag(byte source, byte result) {
			this.source = source;
			this.result = result;
		}// Constructor

		public FileFlag(byte source, byte result, boolean sign, boolean zero, boolean halfCarry, boolean parity,
				boolean nFlag, boolean carry) {
			this.source = source;
			this.result = result;
			this.sign = sign;
			this.zero = zero;
			this.halfCarry = halfCarry;
			this.parity = parity;
			this.nFlag = nFlag;
			this.carry = carry;
		}// Constructor

		public byte getSource() {
			return this.source;
		}// getSource

		public byte getResult() {
			return this.result;
		}// getSource
	}// FileFlag

}// class YY
