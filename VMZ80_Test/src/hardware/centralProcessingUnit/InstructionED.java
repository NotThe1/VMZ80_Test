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

public class InstructionED {
	// ArithmeticUnit au = ArithmeticUnit.getInstance();
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();

	Register[] registers = new Register[] {Z80.Register.BC,Z80.Register.DE,Z80.Register.SP};

	
	int arg1, arg2, ans;
	int sumADD, sumADCnc, sumADCcy;

	byte[] aArg1, aArg2, aAns;
	byte[] aSumADD, aSumADCnc, aSumADCcy;
	// String sArg1,sArg2,sSum;
	String sArg1, sArg2;
	String sSumADD, sSumADCnc, sSumADCcy;

	boolean sign, zero, halfCarry, overflow, nFlag, carry;
	boolean carryState;
	String message;
	String flagsADD, flagsADCnc, flagsADCcy;

	byte values[];

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		// au = ArithmeticUnit.getInstance();
	}// setUp

	@Test
	public void testWordADDs_NCfile() {
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
		/* @formatter:on  */
		int location = 0000;
		values = new byte[] { (byte) 0X09, (byte) 0XED, (byte) 0X4A, (byte) 0XED, (byte) 0X4A,

				(byte) 0X19, (byte) 0XED, (byte) 0X5A, (byte) 0XED, (byte) 0X5A,

				(byte) 0X39, (byte) 0XED, (byte) 0X7A, (byte) 0XED, (byte) 0X7A, };

		/* @formatter:on	 */
		
		
		setUpMemory(location, values);

		// int registerCount = 0;
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

				sign = flagsADD.subSequence(0, 1).equals("1") ? true : false;
				zero = flagsADD.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flagsADD.subSequence(2, 3).equals("1") ? true : false;
				overflow = flagsADD.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flagsADD.subSequence(4, 5).equals("1") ? true : false;
				carry = flagsADD.subSequence(5, 6).equals("1") ? true : false;

				System.out.printf("wrs.getProgramCounter(): %04X %s - ", wrs.getProgramCounter(),registers[registerIndex].toString());
				System.out.printf("%s %s %s %s  %n", sArg1, sArg2, sSumADD, flagsADD);
				// System.out.printf(" %s %s %s %s %s %s %n", sign,zero,halfCarry,overflow,nFlag,carry);

				aArg1 = getValueArray(sArg1);
				aArg2 = getValueArray(sArg2);

				aSumADD = getValueArray(sSumADD);
				message = String.format("file WORD ADD  %s + %s = %s", sArg1, sArg2, sSumADD);

				setUpWordRegisters(registerIndex, aArg1, aArg2, false);
				assertThat(message, aSumADD, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));

				System.out.printf("wrs.getProgramCounter(): %04X %s - ", wrs.getProgramCounter(),registers[registerIndex].toString());
				System.out.printf("%s %s %s %s  %n", sArg1, sArg2, sSumADCnc, flagsADCnc);

				aSumADCnc = getValueArray(sSumADCnc);
				message = String.format("file WORD ADCnc %s + %s = %s", sArg1, sArg2, sSumADCnc);

				setUpWordRegisters(registerIndex, aArg1, aArg2, false);
				assertThat(message, aSumADCnc, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));

				System.out.printf("wrs.getProgramCounter(): %04X %s - ", wrs.getProgramCounter(),registers[registerIndex].toString());
				System.out.printf("%s %s %s %s  %n", sArg1, sArg2, sSumADCcy, flagsADCcy);

				aSumADCcy = getValueArray(sSumADCcy);
				message = String.format("file WORD ADCcy %s + %s = %s", sArg1, sArg2, sSumADCcy);

				setUpWordRegisters(registerIndex, aArg1, aArg2, true);
				assertThat(message, aSumADCcy, equalTo(wrs.getDoubleRegArray(Z80.Register.HL)));

				registerIndex++;
				
				if ((wrs.getProgramCounter() % 0X0F) == 0) {
					registerIndex = 0;
					wrs.setProgramCounter(0X00);
				} // if

				// message = String.format("file WORD ADC CY=0 -> %s - %s = %s", sArg1,sArg2,sSum);

				// assertThat("sum: " + message,sum,equalTo(au.addWordWithCarry(arg1, arg2,carryState)));
				// assertThat("sign: " + message,sign,equalTo(au.hasSign()));
				// assertThat("zero: " + message,zero,equalTo(au.isZero()));
				// assertThat("halfCarry: " + message,halfCarry,equalTo(au.hasHalfCarry()));
				// assertThat("overFlow: " + message,overflow,equalTo(au.hasOverflow()));
				// assertThat("nFlag: " + message,nFlag,equalTo(au.isNFlagSet()));
				// assertThat("carry: " + message,carry,equalTo(au.hasCarry()));

			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// testWordADC_NCfile

	private void setUpWordRegisters(int registerIndex, byte[] arg1, byte[] arg2, boolean carryState) {
		wrs.setDoubleReg(Z80.Register.HL, arg1); // Set HL
		wrs.setDoubleReg(registers[registerIndex], arg2);// Set rr
		ccr.setCarryFlag(carryState);
		cpu.executeInstruction(wrs.getProgramCounter());// do the ADCcy
	}// setUpWordRegisters

	// -----------------------------------------------------------
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

	private int getValue(String value) {
		return Integer.valueOf(value, 16);

	}// getValue

}// InstructionED
