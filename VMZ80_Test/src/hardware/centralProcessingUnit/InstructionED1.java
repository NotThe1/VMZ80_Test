package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class InstructionED1 {
	// Also Word Add 09,19,29,39 ADD HL,DE[,HL],SP
	
	// ArithmeticUnit au = ArithmeticUnit.getInstance();
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	Register[] registers = new Register[] {Z80.Register.BC,Z80.Register.DE,Z80.Register.SP};


	
	int arg1, arg2, ans;
	int sumADD, sumADCnc, sumADCcy;


	boolean sign, zero, halfCarry, overflow, nFlag, carry;
	boolean carryState;
	String message;

	byte values[];

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		// au = ArithmeticUnit.getInstance();
	}// setUp
	
	@Test
	public void testIM_0_1_2() {
int instructionBase = 0x1000;

		/* @formatter:off */
		/*
		0006: 1000             Start:                          
		0007: 1000 ED 46                  IM 0              
		0008: 1002 ED 56                  IM 1
		0008: 1004 ED 5E                  IM 2             
		0009: 1006 76                     HALT                        
		                       
                    */
		
		byte[] instructions = new byte[] {  (byte) 0xED,(byte) 0x46,
											(byte) 0xED,(byte) 0x56,
											(byte) 0xED,(byte) 0x5E,
										
											(byte) 0x76};			/* @formatter:on  */

		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
		
		wrs.setStackPointer(0x4000);// IM 0
		assertThat("PC: ", 0x1000, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));
		

		cpu.startInstruction(); // IM 1
		assertThat("PC: ", 0x1002, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); // IM 2
		assertThat("PC: ", 0x1004, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));
		
		cpu.startInstruction(); // HALT
		assertThat("PC: ", 0x1006, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));



	}// testConditionlRET


	

	@Test
	public void testWordADDs_NCfile() {
		registers = new Register[] {Z80.Register.BC,Z80.Register.DE,Z80.Register.SP};
		String flagsADD, flagsADCnc, flagsADCcy;
		byte[] aArg1, aArg2;
		byte[] aSumADD, aSumADCnc, aSumADCcy;
		// String sArg1,sArg2,sSum;
		String sArg1, sArg2;
		String sSumADD, sSumADCnc, sSumADCcy;

		//Not testing ADD HL,HL ADC HL,HL the data source does not support.
		
		/* @formatter:off */
		/*
0001: 0000         ; testCodesAddAdc.asm
0002: 0000                                             
0003: 0000 09                     ADD    HL,BC                
0004: 0001 ED 4A                  ADC    HL,BC                
0005: 0003 ED 4A                  ADC    HL,BC                
0006: 0005                                             
0007: 0005 19                     ADD    HL,DE                
0008: 0006 ED 5A                  ADC    HL,DE                
0009: 0008 ED 5A                  ADC    HL,DE                
0010: 000A                                             
0011: 000A 39                     ADD    HL,SP                
0012: 000B ED 7A                  ADC    HL,SP                
0013: 000D ED 7A                  ADC    HL,SP       
		/* @formatter:off  */
		int location = 0000;
		values = new byte[] { (byte) 0X09,
				              (byte) 0XED, (byte) 0X4A,
				              (byte) 0XED, (byte) 0X4A,

				              (byte) 0X19,
				              (byte) 0XED, (byte) 0X5A,
				              (byte) 0XED, (byte) 0X5A,

				              (byte) 0X39,
				              (byte) 0XED, (byte) 0X7A,
				              (byte) 0XED, (byte) 0X7A, };

		/* @formatter:on	 */
		
		
		setUpMemory(location, values);

		
		int registerIndex = 0;
		wrs.setProgramCounter(0X00);
		try {
			 InputStream inputStream = this.getClass().getResourceAsStream("/AddAdcWordOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.startsWith(";")){
					scanner.nextLine();
					continue;
				}// check for comment line
				sArg2 = scanner.next();

				sSumADD = scanner.next();
				flagsADD = scanner.next();

				sSumADCnc = scanner.next();
				flagsADCnc = scanner.next();

				sSumADCcy = scanner.next();
				flagsADCcy = scanner.next();

//				 System.out.printf("wrs.getProgramCounter(): %04X %s - ", wrs.getProgramCounter(),registers[registerIndex].toString());
//				 System.out.printf("%s %s %s %s  %n", sArg1, sArg2, sSumADD, flagsADD);
//				 System.out.printf(" %s %s %s %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);

				aArg1 = getValueArray(sArg1);
				aArg2 = getValueArray(sArg2);
				aSumADD = getValueArray(sSumADD);
				setUpWordRegisters(registerIndex, aArg1, aArg2, false);
				message = String.format("file WORD ADD  %s + %s = %s", sArg1, sArg2, sSumADD);				
				assertThat(message, aSumADD, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));
				
				nFlag = flagsADD.subSequence(4, 5).equals("1") ? true : false;
				halfCarry = flagsADD.subSequence(2, 3).equals("1") ? true : false;
				carry = flagsADD.subSequence(5, 6).equals("1") ? true : false;
				message = String.format("file Word ADD - N: %s,HC: %s,CY: %s",nFlag,halfCarry,carry);
				assertThat(message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(message, carry, equalTo(ccr.isCarryFlagSet()));


				aSumADCnc = getValueArray(sSumADCnc);
				setUpWordRegisters(registerIndex, aArg1, aArg2, false);
				message = String.format("file WORD ADCnc %s + %s = %s", sArg1, sArg2, sSumADCnc);
				assertThat(message, aSumADCnc, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));
				
				nFlag = flagsADCnc.subSequence(4, 5).equals("1") ? true : false;
				halfCarry = flagsADCnc.subSequence(2, 3).equals("1") ? true : false;
				carry = flagsADCnc.subSequence(5, 6).equals("1") ? true : false;
				message = String.format("file Word ADD - N: %s,HC: %s,CY: %s",nFlag,halfCarry,carry);
				assertThat(message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(message, carry, equalTo(ccr.isCarryFlagSet()));
				
				
				aSumADCcy = getValueArray(sSumADCcy);
				setUpWordRegisters(registerIndex, aArg1, aArg2, true);
				message = String.format("file WORD ADCcy %s + %s = %s", sArg1, sArg2, sSumADCcy);
				assertThat(message, aSumADCcy, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));
				
				nFlag = flagsADCcy.subSequence(4, 5).equals("1") ? true : false;
				halfCarry = flagsADCcy.subSequence(2, 3).equals("1") ? true : false;
				carry = flagsADCcy.subSequence(5, 6).equals("1") ? true : false;
				message = String.format("file Word ADD - N: %s,HC: %s,CY: %s",nFlag,halfCarry,carry);
				assertThat(message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(message, carry, equalTo(ccr.isCarryFlagSet()));


				registerIndex++;
				
				if ((wrs.getProgramCounter() % 0X0F) == 0) {
					registerIndex = 0;
					wrs.setProgramCounter(0X00);
				} // if

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// testWordADC_NCfile
	
	
	@Test
	public void testWordSBC_file() {
		 registers = new Register[] {Z80.Register.BC,Z80.Register.DE,Z80.Register.SP};
		String  flagsSBCnc, flagsSBCcy;

		byte[] aArg1, aArg2;
		byte[]  aDiffNC, aDiffCY;
		// String sArg1,sArg2,sSum;
		String sArg1, sArg2;
		String  sDiffNC, sDiffCY;

		//Not testing ADD HL,HL ADC HL,HL the data source does not support.
		
		/* @formatter:off */
		/*
0001: 0000         ; testCodesSBC.asm
0002: 0000                                             
0003: 0000 ED 42                  SBC    HL,BC                
0004: 0002 ED 42                  SBC    HL,BC                
0005: 0004                                             
0006: 0004 ED 52                  SBC    HL,DE                
0007: 0006 ED 52                  SBC    HL,DE                
0008: 0008                                             
0009: 0008 ED 72                  SBC    HL,SP                
0010: 000A ED 72                  SBC    HL,SP 
      
		/* @formatter:off  */
		int location = 0000;
		values = new byte[] {  (byte) 0XED, (byte) 0X42,
				               (byte) 0XED, (byte) 0X42,

				               (byte) 0XED, (byte) 0X52,
				               (byte) 0XED, (byte) 0X52,

				               (byte) 0XED, (byte) 0X72,
				               (byte) 0XED, (byte) 0X72, };

		/* @formatter:on	 */
		
		
		setUpMemory(location, values);

		
		int registerIndex = 0;
		wrs.setProgramCounter(0X00);
		try {
			 InputStream inputStream = this.getClass().getResourceAsStream("/SbcWordOriginal.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.startsWith(";")){
					scanner.nextLine();
					continue;
				}// check for comment line
				sArg2 = scanner.next();

//				sSumADD = scanner.next();
//				flagsADD = scanner.next();

				sDiffNC = scanner.next();
				flagsSBCnc = scanner.next();

				sDiffCY = scanner.next();
				flagsSBCcy = scanner.next();

//				 System.out.printf("wrs.getProgramCounter(): %04X %s - ", wrs.getProgramCounter(),registers[registerIndex].toString());
//				 System.out.printf("%s %s %s %s  %n", sArg1, sArg2, sDiffNC, flagsSBCnc);
//				 System.out.printf(" %s %s %s %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);


				aArg1 = getValueArray(sArg1);
				aArg2 = getValueArray(sArg2);

				aDiffNC = getValueArray(sDiffNC);
				setUpWordRegisters(registerIndex, aArg1, aArg2, false);
				message = String.format("file WORD SBCnc %s - %s = %s", sArg1, sArg2, sDiffNC);
				assertThat(message, aDiffNC, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));
				
				nFlag = flagsSBCnc.subSequence(4, 5).equals("1") ? true : false;
				halfCarry = flagsSBCnc.subSequence(2, 3).equals("1") ? true : false;
				carry = flagsSBCnc.subSequence(5, 6).equals("1") ? true : false;
				message = String.format("file Word SBCnc - N: %s,HC: %s,CY: %s",nFlag,halfCarry,carry);
				assertThat(message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(message, carry, equalTo(ccr.isCarryFlagSet()));
				
				
				aDiffCY = getValueArray(sDiffCY);
				setUpWordRegisters(registerIndex, aArg1, aArg2, true);
				message = String.format("file WORD SBCcy %s - %s = %s", sArg1, sArg2, sDiffNC);
				assertThat(message, aDiffCY, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));
				
				nFlag = flagsSBCcy.subSequence(4, 5).equals("1") ? true : false;
				halfCarry = flagsSBCcy.subSequence(2, 3).equals("1") ? true : false;
				carry = flagsSBCcy.subSequence(5, 6).equals("1") ? true : false;
				message = String.format("file Word ADD - N: %s,HC: %s,CY: %s",nFlag,halfCarry,carry);
				assertThat(message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat(message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat(message, carry, equalTo(ccr.isCarryFlagSet()));


				registerIndex++;
				
				if ((wrs.getProgramCounter() % 0X0C) == 0) {
					registerIndex = 0;
					wrs.setProgramCounter(0X00);
				} // if

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// testWordSBC_file
	
	@Test
	public void testLDbbnn() {
		int testLimit = 0X0100;

		Random random = new Random();
//		byte[] values = new byte[10];

		registers = new Register[] { Z80.Register.HL, Z80.Register.BC, Z80.Register.DE, Z80.Register.HL,
				Z80.Register.SP };
		int regSize = registers.length;
		/* @formatter:off */
		/*
0001: 0000         ; testCodesLDbbnn.asm
0002: 0000                                             
0003: 0000 22 00 01		LD     (0100H),HL           ; rnd0
0004: 0003 ED 43 10 01	LD     (0110H),BC           ; rnd1
0005: 0007 ED 53 20 01	LD     (0120H),DE           ; rnd2
0006: 000B ED 63 30 01	LD     (0130H),HL           ; rnd3
0007: 000F ED 73 40 01	LD     (0140H),SP           ; rnd4
0008: 0013                                             
0009: 0013 2A 40 01		LD     HL,(0140H)           ; rnd4
0010: 0016 ED 4B 30 01	LD     BC,(0130H)           ; rnd3
0011: 001A ED 5B 20 01	LD     DE,(0120H)           ; rnd2
0012: 001E ED 6B 10 01	LD     HL,(0110H)           ; rnd1
0013: 0022 ED 7B 00 01	LD     SP,(0100H)           ; rnd0
0014: 0026 00              NOP                         
   
       
		/* @formatter:off  */
		int location = 0000;
		values = new byte[] { (byte) 0X22, (byte) 0X00, (byte) 0X01,
				              (byte) 0XED, (byte) 0X43, (byte) 0X10, (byte) 0X01,
				              (byte) 0XED, (byte) 0X53, (byte) 0X20, (byte) 0X01,
				              (byte) 0XED, (byte) 0X63, (byte) 0X30, (byte) 0X01,
				              (byte) 0XED, (byte) 0X73, (byte) 0X40, (byte) 0X01,

				              (byte) 0X2A, (byte) 0X40, (byte) 0X01,
				              (byte) 0XED, (byte) 0X4B, (byte) 0X30, (byte) 0X01,
				              (byte) 0XED, (byte) 0X5B, (byte) 0X20, (byte) 0X01,
				              (byte) 0XED, (byte) 0X6B, (byte) 0X10, (byte) 0X01,
				              (byte) 0XED, (byte) 0X7B, (byte) 0X00, (byte) 0X01 };

		/* @formatter:on	 */

		setUpMemory(location, values);

		for (int testCount = 0; testCount < testLimit; testCount++) {

			int registerIndex = 0;
			wrs.setProgramCounter(0X00);
			random.nextBytes(values);
			byte hi, lo;
			byte[] aAns = new byte[] { 0X00, 0X00 };
			for (registerIndex = 0; registerIndex < regSize; registerIndex++) {
				Register reg = registers[registerIndex];
				hi = values[(registerIndex * 2)];
				lo = values[(registerIndex * 2) + 1];
				wrs.setDoubleReg(reg, hi, lo);
//				System.out.printf("PC = %04X    %s,  %02X,  %02X  %n", wrs.getProgramCounter(), reg.toString(), hi, lo);
				cpu.executeInstruction(wrs.getProgramCounter()); // LD (xx),rr

			} // for registerIndex

			System.out.println();

			for (registerIndex = 0; registerIndex < regSize; registerIndex++) {
				Register reg = registers[registerIndex];
				// returns lo,hi
				aAns[1] = values[((regSize - registerIndex) * 2) - 1];
				aAns[0] = values[((regSize - registerIndex) * 2) - 2];

//				System.out.printf("PC = %04X    %s,  %02X,  %02X  %n", wrs.getProgramCounter(), reg.toString(), aAns[0],
//						aAns[1]);
				cpu.executeInstruction(wrs.getProgramCounter()); // LD rr,(xx)

				message = String.format("LD|rr|bb %s: hi= %02X, lo = %02X.", reg.toString(), aAns[1], aAns[0]);
				assertThat(message, aAns, equalTo(wrs.getDoubleRegArray(reg)));

			} // for registerIndex

		} // for testCount

	}// testLDbbnn
	
	

	@Test
	public void testNEGfile() {
		int location = 0X0000;
		byte arg1, result;
		String flags, sArg1;
		boolean sign, zero, halfCarry, parity, nFlag, carry;
		byte[] instruction = new byte[] { (byte) 0XED, (byte) 0X44 };
		for (int i = 0; i < 0X110; i++) {
			ioBuss.writeDMA(i * 2, instruction);
		} // for

		wrs.setProgramCounter(location);

		try {
			 InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal1.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.startsWith(";")) {
					scanner.nextLine();
					continue;
				} // check for comment line
				arg1 = getValue(sArg1);

				scanner.next();// Skip CPL result
				scanner.next();// Skip CPL flags

				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				// System.out.printf("%02X %02X %s ",arg1,arg2,flags);
				// System.out.printf(" %s %s %s %s %s %s %n", sign,zero,halfCarry,parity,nFlag,carry);

				message = String.format("file NEG -> %02X NEG -> %02X", arg1, result);

				wrs.setReg(Z80.Register.A, arg1);
				cpu.executeInstruction(wrs.getProgramCounter());
				
				
				assertThat("result: " + message, result, equalTo(wrs.getAcc()));
				 assertThat("sign: " + message,sign,equalTo(ccr.isSignFlagSet()));
				 assertThat("zero: " + message,zero,equalTo(ccr.isZeroFlagSet()));
				 assertThat("halfCarry: " + message,halfCarry,equalTo(ccr.isHFlagSet()));
				 assertThat("parity: " + message,parity,equalTo(ccr.isPvFlagSet()));
				 assertThat("nFlag: " + message,nFlag,equalTo(ccr.isNFlagSet()));
				 assertThat("carry: " + message,carry,equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// testNEGfile


	// -----------------------------------------------------------
	
	
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	private void setUpWordRegisters(int registerIndex, byte[] arg1, byte[] arg2, boolean carryState) {
		wrs.setDoubleReg(Z80.Register.HL, arg1); // Set HL
		wrs.setDoubleReg(registers[registerIndex], arg2);// Set rr
		ccr.setCarryFlag(carryState);
		cpu.executeInstruction(wrs.getProgramCounter());// do the ADCcy
	}// setUpWordRegisters

	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory

	private byte[] getValueArray(String value) {
		int workingValue = Integer.valueOf(value, 16);
		byte msb = (byte) ((workingValue & 0XFF00) >> 8);
		byte lsb = (byte) ((byte) workingValue & 0X00FF);
		return new byte[] { lsb, msb };
	}// getValueArray

//	private int getValue(String value) {
//		return Integer.valueOf(value, 16);
//
//	}// getValue

}// InstructionED
