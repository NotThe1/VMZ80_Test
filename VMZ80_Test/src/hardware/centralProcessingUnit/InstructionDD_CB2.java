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

public class InstructionDD_CB2 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

//	Register[] registers = new Register[] { Register.B, Register.C, Register.D, Register.E, Register.H, Register.L,
//			Register.A };

	HashMap<Byte, FileFlag> mapRRC = new HashMap<>();
	HashMap<Byte, FileFlag> mapRRcy = new HashMap<>();
	HashMap<Byte, FileFlag> mapRRnc = new HashMap<>();
	FileFlag ffRRC, ffRRcy, ffRRnc;
	
	int instructionBase = 0X1000;
//	int hlRegisterValue = 0X0100; // (HL) - m
	int valueBase = 0X2000;

//	Register thisRegister;

//	String message;
	
	byte value, sourceValue,targetValue;
	byte code0,code1,code2;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		String sSource;
		byte source, result;
		String flags ;
		boolean sign, zero, halfCarry,  parity, nFlag, carry;

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

				// RC(cy = 0)
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapRRnc.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RC(cy = 1)
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapRRcy.put(source, new FileFlag(source, result, sign, zero, halfCarry, parity, nFlag, carry));

				// RLC
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
	public void testRR_file() {
		testRR_file(Register.IX,true);
		testRR_file(Register.IX,false);
		testRR_file(Register.IY,true);
		testRR_file(Register.IY,false);
	}//testRR_file
	
	
	public void testRR_file(Register regIXY, boolean carry) {
		String reg ="none";
		String carryNote = carry? " CY ":" NC ";
		HashMap<Byte, FileFlag> map;
		FileFlag ff;
		code1 = (byte) 0xCB;
		code2 = (byte) 0x1E;
		if ( regIXY.equals(Register.IX)) {
			code0 = (byte) 0xDD;
			reg = "IX";
		}else {
			code0 = (byte) 0xFD;
			reg = "IY";
		}//if
		
		map = carry?mapRRcy:mapRRnc;
		
		loadInstructions(code0,code1,code2);
		wrs.setDoubleReg(regIXY, valueBase);
		for (int v = 0; v < 0X100; v++) {
			sourceValue = (byte) v;
			ff = map.get(sourceValue);
			wrs.setProgramCounter(instructionBase);
			loadData(valueBase,sourceValue);
			targetValue = ff.getResult();
				for (int d = 0; d < 0x100; d++) {
					ccr.setCarryFlag(carry);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(reg + carryNote + "RL "+ sourceValue,targetValue,equalTo(cpuBuss.read(valueBase + (byte) d)));
				
				assertThat(reg + carryNote + "RL sign :" + sourceValue, ff.sign, equalTo(ccr.isSignFlagSet()));
				assertThat(reg + carryNote + "RL Zero :" + sourceValue, ff.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat(reg + carryNote + "RL halfFlag :" + sourceValue, ff.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(reg + carryNote + "RL parity :" + sourceValue, ff.parity, equalTo(ccr.isPvFlagSet()));
				assertThat(reg + carryNote + "RL nFlag :" + sourceValue, ff.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(reg + carryNote + "RL carry :" + sourceValue, ff.carry, equalTo(ccr.isCarryFlagSet()));

			}//for d - each displacement
		}// for v - value
		
	}//testRR_file
	
	@Test
	public void testRRC_file() {
		testRRC_file(Register.IX);
		testRRC_file(Register.IY);
	}//testRR_file
	
	
	public void testRRC_file(Register regIXY) {
		String reg ="none";
		
		code1 = (byte) 0xCB;
		code2 = (byte) 0x0E;
		if ( regIXY.equals(Register.IX)) {
			code0 = (byte) 0xDD;
			reg = "IX";
		}else {
			code0 = (byte) 0xFD;
			reg = "IY";
		}//if 
		loadInstructions(code0,code1,code2);
		wrs.setDoubleReg(regIXY, valueBase);
		for (int v = 0; v < 0X100; v++) {
			sourceValue = (byte) v;
			ffRRC = mapRRC.get(sourceValue);
			wrs.setProgramCounter(instructionBase);
			loadData(valueBase,sourceValue);
			targetValue = ffRRC.getResult();
				for (int d = 0; d < 0x100; d++) {
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(reg + "RLC "+ sourceValue,targetValue,equalTo(cpuBuss.read(valueBase + (byte) d)));
				
				assertThat(reg + "RLC sign :" + sourceValue, ffRRC.sign, equalTo(ccr.isSignFlagSet()));
				assertThat(reg + "RLC Zero :" + sourceValue, ffRRC.zero, equalTo(ccr.isZeroFlagSet()));
				assertThat(reg + "RLC halfFlag :" + sourceValue, ffRRC.halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(reg + "RLC parity :" + sourceValue, ffRRC.parity, equalTo(ccr.isPvFlagSet()));
				assertThat(reg + "RLC nFlag :" + sourceValue, ffRRC.nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(reg + "RLC carry :" + sourceValue, ffRRC.carry, equalTo(ccr.isCarryFlagSet()));

			}//for d - each displacement
		}// for v - value
		
	}//testRRC_file
	


	

	////////////////////////////////////////////////////////////////////////
	
	// puts displacement at second from end of codes, 
	private void loadInstructions(byte code0,byte code1,byte code2) {
	int instructionLocation = instructionBase;
	for (int i = 0; i < 0X100; i++) {
		cpuBuss.write(instructionLocation++, code0);
		cpuBuss.write(instructionLocation++, code1);
		cpuBuss.write(instructionLocation++, (byte) i);
		cpuBuss.write(instructionLocation++, code2);
	} // for
	wrs.setProgramCounter(instructionBase);
}// loadInstructions

	
	private void loadData(int valueBase,byte value) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, value);
//			System.out.printf("location-> %02X,  result -> %02X%n", valueBase + i,(byte) (128 + i));
		} // for i
	}// loadData


	
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue



}//class InstructionDD_CB2
