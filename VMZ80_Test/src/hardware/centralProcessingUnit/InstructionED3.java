package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class InstructionED3 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		// au = ArithmeticUnit.getInstance();
	}// setUp

	@Test
	public void testCPIfile() {
		byte arg1, arg2;
		String sArg0, flags, message;
		boolean sign, zero, halfCarry, pvFlag;
		int memoryBase = 0X1000;
		int memoryCount = 0X100;
		int offset = 0;

		for (int i = 0; i < memoryCount; i++) {
			ioBuss.write((i * 2), (byte) 0XED); // write the instruction
			ioBuss.write((i * 2) + 1, (byte) 0XA1);
		} // for set Instructions & memory

		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/CpOriginal.txt");
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg0 = scanner.next();
				if (sArg0.startsWith(";")) {
					scanner.nextLine();
					continue;
				} // skip line starting with semicolon
				arg2 = getValue(sArg0);
				arg1 = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;

				// System.out.printf("arg1 = %02X,arg2 = %02X %s ", arg1, arg2, flags);
				// System.out.printf(" %s %s %s :", sign, zero, halfCarry);

				wrs.setAcc(arg1);

				if (arg1 == (byte) 0X00) {
					wrs.setProgramCounter(0);
					wrs.setDoubleReg(Register.HL, memoryBase);
					wrs.setDoubleReg(Register.BC, memoryCount-1);
					
					offset = 0;

					for (int i = 0; i < memoryCount; i++) {
						ioBuss.write(i + memoryBase, (byte) arg2);// write data
					} // for set memory

				} // inner loop
				
				offset++;

				message = String.format("file CPI -> %02X <-> %02X", arg1, arg2);
				cpu.executeInstruction(wrs.getProgramCounter());
				// au.compare(arg1, arg2);
				
				assertThat("HL: " + message,memoryBase+offset,equalTo(wrs.getDoubleReg(Register.HL)));
				
				int bcCount = (memoryCount-(offset+1)) & Z80.WORD_MASK;
				assertThat("BC: " + message,bcCount ,equalTo(wrs.getDoubleReg(Register.BC)));		
				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				pvFlag = wrs.getDoubleReg(Register.BC) != 0 ? true : false;
				assertThat("P/V: " + message, pvFlag, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag: " + message, true, equalTo(ccr.isNFlagSet()));


//				if (ccr.isZeroFlagSet() || !ccr.isPvFlagSet()) {
//					System.out.printf("\tbc = %04X, hl = %04X, memValue = %02X, accValue = %02X%n",
//							wrs.getDoubleReg(Register.BC), wrs.getDoubleReg(Register.HL), arg1, arg2);
//				} // if a ==(hl)

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// testCPIfile
	

	@Test
	public void testCPIR() {
		byte arg1;
		boolean  zero,  pvFlag;
		int memoryBase = 0X1000;
		int memoryCount = 0X100;
		byte opcode1 = (byte) 0XED;
		byte opcode2 = (byte) 0XB1;
		for (int i = 0; i < memoryCount; i++) {
			ioBuss.write(i * 2, opcode1); // write the instruction
			ioBuss.write((i * 2) + 1, opcode2);

			ioBuss.write(i + memoryBase, (byte) i);// write data
		} // for set Instructions & memory

		arg1 = (byte) 0X7F;
		setAcc(arg1,memoryBase,memoryCount);
		cpu.executeInstruction(wrs.getProgramCounter());
//		System.out.printf("HL : %04X, ", wrs.getDoubleReg(Register.HL));
//		System.out.printf("BC : %04X, ", wrs.getDoubleReg(Register.BC));
//		System.out.printf("Acc : %02X%n", wrs.getAcc());
		
		assertThat("Sign: arg1 = " + arg1,true,equalTo(ccr.isZeroFlagSet()));
		assertThat("Value: arg1 = " + arg1,arg1,equalTo(ioBuss.read(wrs.getDoubleReg(Register.HL)-1)));

		arg1 = (byte) 0XFF;
		setAcc(arg1,memoryBase,memoryCount);
		wrs.setDoubleReg(Register.BC, 0X10);
		
		cpu.executeInstruction(wrs.getProgramCounter());
		assertThat("BC: arg1 = " + arg1,00,equalTo(wrs.getDoubleReg(Register.BC)));
			
	}// testCPIR	

	//---------------------------------------------------------------------------------
	
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	private void setAcc(byte value,int memoryBase,int memoryCount) {
		wrs.setAcc(value);
		wrs.setProgramCounter(0);
		wrs.setDoubleReg(Register.HL, memoryBase);
		wrs.setDoubleReg(Register.BC, memoryCount - 1);	
	}//setAcc




}//class InstructionED3
