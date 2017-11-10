package hardware.centralProcessingUnit;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;

import org.junit.Test;

import codeSupport.Z80;
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
	boolean cy,hc,cy1,hc1;
	byte arg1, arg2, sum, diff, ans, key;
	String flags, sArg1;

	byte[] instructions = new byte[0x0100];
	
	@Test
	public void testDAAadd() {
		Arrays.fill(instructions, (byte) 0x2F);
		wrs.setProgramCounter(instructionBase);
	    BitSet bs = new BitSet(8);
	    //	1000	80			ADD A,B
	    //	1001	27			DAA
	    //	1002	C3 00 10	JP 1000H
	    byte[] daaInstructions = new byte[] {(byte) 0x80,(byte) 0x27,(byte) 0xC3,(byte) 0x00,(byte) 0x10};
	    loadInstructions(daaInstructions);
	    wrs.setProgramCounter(instructionBase);
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/daaAddOriginal.txt");
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.equals(";")) {
					scanner.nextLine();
					continue;
				} // if - skip line
				arg1 = getValue(sArg1);
				arg2 = getValue(scanner.next());
				sum = getValue(scanner.next());
				cy = scanner.next().equals("1")?true:false;
				hc = scanner.next().equals("1")?true:false;
				ans = getValue(scanner.next());
				cy1 = scanner.next().equals("1")?true:false;
				hc1 = scanner.next().equals("1")?true:false;
				
				
				message = String.format("%2X\t%2X\t%02X\t%s\t%s\t%2X\t%s\t%s",
						arg1,arg2,sum,cy,hc,ans,cy1,hc1);//parity
				
				
				System.out.println(message);
				if (arg2==(byte) 0x99) {
					break;
				}// if limit
				
				sign = (ans & Z80.BIT_SIGN) == Z80.BIT_SIGN;
				zero= ans==(byte) 0x00? true:false;
				bs = BitSet.valueOf(new byte[] {ans});
				parity = (bs.cardinality() % 2) == 0 ? true : false;
				
				wrs.setAcc(arg1);
				wrs.setReg(Register.B, arg2);
				cpu.executeInstruction(wrs.getProgramCounter());
//				assertThat("sum: " + message, sum, equalTo(wrs.getAcc()));
//
//				assertThat("ans: " + message, sum, equalTo(wrs.getAcc()));
//				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
//				assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
//
				scanner.nextLine();
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}//testDAAadd
	////////////////////////////////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	private void loadInstructions(byte[] codes) {
		int instructionLocation = instructionBase;
		for (byte code : codes) {
			ioBuss.write(instructionLocation++, code);
		} // for codes
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions




}//class InstructionsMainPage0_04
