package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.IoBuss;


public class InstructionED2 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();

	String message;
	boolean sign, zero, parity;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		// au = ArithmeticUnit.getInstance();
	}// setUp

	@Test
	public void testRegistersIandR() {
		int limit = 0X0500;
		Random random = new Random();
		byte[] regValues = new byte[limit * 2];
		random.nextBytes(regValues);
		boolean iFF2;
		boolean iSign, iZero, rSign, rZero;
		/* @formatter:off */
		/*
0001: 0000         ; testIandR.asm
0002: 0000                                             
0003: 0000 ED 47                  LD     I,A                  
0004: 0002 ED 4F                  LD     R,A                  
0005: 0004                                             
0006: 0004 ED 57                  LD     A,I                  
0007: 0006 ED 4F                  LD     R,A                  
0008: 0008 00                     NOP    
  */
		
		int location = 0000;
		byte[] values = new byte[] { (byte) 0XED, (byte) 0X47,
				              (byte) 0XED, (byte) 0X4F,

				              (byte) 0XED, (byte) 0X57,
				              (byte) 0XED, (byte) 0X5F};

		/* @formatter:on	 */

		setUpMemory(location, values);
		byte iValue, rValue;
		for (int i = 0; i < limit; i++) {
			iValue = regValues[i * 2];
			rValue = regValues[(i * 2) + 1];
			iSign = (iValue & Z80.BIT_7) == Z80.BIT_7;
			iZero = iValue == (byte) 00;
			rSign = (rValue & Z80.BIT_7) == Z80.BIT_7;
			rZero = rValue == (byte) 00;
			wrs.setProgramCounter(0X00);
			wrs.setReg(Z80.Register.A, iValue);
			cpu.executeInstruction(wrs.getProgramCounter()); // LD I,A
			wrs.setReg(Z80.Register.A, rValue);
			cpu.executeInstruction(wrs.getProgramCounter()); // LD R,A

			iFF2 = random.nextBoolean();
			wrs.setIFF2(iFF2);
			cpu.executeInstruction(wrs.getProgramCounter()); // LD A,I
			message = String.format("  iValue = %02X", iValue);
			assertThat(message, iValue, equalTo(wrs.getAcc()));

			assertThat("signFlag" + message, iSign, equalTo(ccr.isSignFlagSet()));
			assertThat("zeroFlag" + message, iZero, equalTo(ccr.isZeroFlagSet()));
			assertThat("hFlag" + message, false, equalTo(ccr.isHFlagSet()));
			assertThat("IFF2" + message, iFF2, equalTo(ccr.isPvFlagSet()));
			assertThat("nFlag" + message, false, equalTo(ccr.isNFlagSet()));

			iFF2 = random.nextBoolean();
			wrs.setIFF2(iFF2);
			cpu.executeInstruction(wrs.getProgramCounter()); // LD A,R
			message = String.format("rValue = %02X", rValue);
			assertThat(message, rValue, equalTo(wrs.getAcc()));

			assertThat("signFlag" + message, rSign, equalTo(ccr.isSignFlagSet()));
			assertThat("zeroFlag" + message, rZero, equalTo(ccr.isZeroFlagSet()));
			assertThat("hFlag" + message, false, equalTo(ccr.isHFlagSet()));
			assertThat("IFF2" + message, iFF2, equalTo(ccr.isPvFlagSet()));
			assertThat("nFlag" + message, false, equalTo(ccr.isNFlagSet()));

		} // for limit

	}// testRegistersIandR

	@Test
	public void testRRD() {
		byte accBefore, memBefore, accAfter, memAfter;
		int limit = 0X0500;
		int memBase = 0x0100;
		Random random = new Random();
		byte[] accValues = new byte[limit];
		random.nextBytes(accValues);

		byte[] memValues = new byte[limit];
		random.nextBytes(memValues);
		setUpMemory(memBase, memValues);

		int location = 0X0000;
		byte[] opCode = new byte[] { (byte) 0XED, (byte) 0X67 };
		setUpMemory(location, opCode);

		byte n1, n2, n3, n4;
		for (int i = 0; i < limit; i++) {
			memBefore = ioBuss.read(i + memBase);
			accBefore = accValues[i];
			n1 = (byte) (accBefore & 0XF0);
			n2 = (byte) (accBefore & 0X0F);
			n3 = (byte) (memBefore & 0XF0);
			n4 = (byte) (memBefore & 0X0F);

			accAfter = (byte) (n1 | n4);
			memAfter = (byte) (((n2 << 4) & 0XF0) | ((n3 >> 4) & 0X0F));

			message = String.format("acc - mem : %02X, %02X, - %02X, %02X", accBefore, memBefore, accAfter, memAfter);
			// System.out.println(message);
			wrs.setProgramCounter(location);
			wrs.setDoubleReg(Z80.Register.HL, i + memBase);
			wrs.setAcc(accBefore);
			cpu.executeInstruction(wrs.getProgramCounter());

			// System.out.printf("Acc -> %02X :", wrs.getAcc());
			// System.out.printf("Mem -> %02X %n",ioBuss.read(i + memBase) );
			assertThat(message, accAfter, equalTo(wrs.getAcc()));
			assertThat(message, memAfter, equalTo(ioBuss.read(i + memBase)));
			
			sign = (accAfter & Z80.BIT_7) == Z80.BIT_7;
			zero = accAfter == 0;
			parity = Integer.bitCount(accAfter) % 2 == 0;

			assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
			assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
			assertThat("halfCarry: " + message, false, equalTo(ccr.isHFlagSet()));
			assertThat("parity: " + message, parity, equalTo(ccr.isPvFlagSet()));
			assertThat("nFlag: " + message, false, equalTo(ccr.isNFlagSet()));

		} // for

	}// testRRD

	@Test
	public void testRLD() {
		byte accBefore, memBefore, accAfter, memAfter;
		int limit = 0X0500;
		int memBase = 0x0100;
		Random random = new Random();
		byte[] accValues = new byte[limit];
		random.nextBytes(accValues);

		byte[] memValues = new byte[limit];
		random.nextBytes(memValues);
		setUpMemory(memBase, memValues);

		int location = 0X0000;
		byte[] opCode = new byte[] { (byte) 0XED, (byte) 0X6F };
		setUpMemory(location, opCode);

		byte n1, n2, n3, n4;
		for (int i = 0; i < limit; i++) {
			memBefore = ioBuss.read(i + memBase);
			accBefore = accValues[i];
			n1 = (byte) (accBefore & 0XF0);
			n2 = (byte) (accBefore & 0X0F);
			n3 = (byte) (memBefore & 0XF0);
			n4 = (byte) (memBefore & 0X0F);

			accAfter = (byte) (n1 | ((n3 >> 4) & 0X0F));
			memAfter = (byte) (((n4 << 4) & 0XF0) | n2);

			message = String.format("acc - mem : %02X, %02X, - %02X, %02X", accBefore, memBefore, accAfter, memAfter);
			// System.out.println(message);
			wrs.setProgramCounter(location);
			wrs.setDoubleReg(Z80.Register.HL, i + memBase);
			wrs.setAcc(accBefore);
			cpu.executeInstruction(wrs.getProgramCounter());

			// System.out.printf("Acc -> %02X :", wrs.getAcc());
			// System.out.printf("Mem -> %02X %n",ioBuss.read(i + memBase) );
			assertThat(message, accAfter, equalTo(wrs.getAcc()));
			assertThat(message, memAfter, equalTo(ioBuss.read(i + memBase)));

			sign = (accAfter & Z80.BIT_7) == Z80.BIT_7;
			zero = accAfter == 0;
			parity = Integer.bitCount(accAfter) % 2 == 0;

			assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
			assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
			assertThat("halfCarry: " + message, false, equalTo(ccr.isHFlagSet()));
			assertThat("parity: " + message, parity, equalTo(ccr.isPvFlagSet()));
			assertThat("nFlag: " + message, false, equalTo(ccr.isNFlagSet()));

		} // for

	}// testRRD
	
	
	
	@Test
	public void testLDIandLDIR() {
//		int limit = 0X05;
		
		int numberOfBytes = 0X500;
		
		Register bc = Z80.Register.BC;
		Register de = Z80.Register.DE;
		Register hl = Z80.Register.HL;
		
		Random random = new Random();
		
		int hlBase = 0x1000;
		byte[] hlValues = new byte[numberOfBytes];
		random.nextBytes(hlValues);
		setUpMemory(hlBase, hlValues);
		
		int deBase = 0x2000;
		byte[] deValues = new byte[numberOfBytes];
		random.nextBytes(deValues);
		setUpMemory(deBase, deValues);

		int location = 0X0000;
		byte[] opCode = new byte[] { (byte) 0XED, (byte) 0XA0 };//LDI
		setUpMemory(location, opCode);
		
		wrs.setDoubleReg(hl, hlBase);	// Set HL
		wrs.setDoubleReg(de, deBase);	// Set DE
		wrs.setDoubleReg(bc, numberOfBytes+1);// Set BC
		
//		boolean bcIsZero;
		int bcCount;
		for (int i = 0;i <numberOfBytes;i++) {
			wrs.setProgramCounter(location);
			cpu.executeInstruction(location);

			assertThat("LDI ",location + 2,equalTo(wrs.getProgramCounter()));
			assertThat(i + ": LDI - value",hlValues[i],equalTo(ioBuss.read(deBase+i)));
			assertThat(i + ": LDI - HL",hlBase + i+1,equalTo(wrs.getDoubleReg(hl)));
			assertThat(i + ": LDI - DE",deBase + i+1,equalTo(wrs.getDoubleReg(de)));
			
			bcCount =wrs.getDoubleReg(bc);
			assertThat(i + ": LDI - BC",numberOfBytes-i,equalTo(bcCount));
			assertThat(i + ": LDI - p/vFlag",bcCount!=0,equalTo(ccr.isPvFlagSet()));
			assertThat(i + ": LDI - hFlag",false,equalTo(ccr.isHFlagSet()));
			assertThat(i + ": LDI - nFlag",false,equalTo(ccr.isNFlagSet()));
			
			assertThat(i + ": LDI - zero",bcCount!=0,equalTo(ccr.isPvFlagSet()));
			
			
//			System.out.printf("BC = %04X, DE = %04X, HL = %04X%n",
//					wrs.getDoubleReg(bc),wrs.getDoubleReg(de),wrs.getDoubleReg(hl));
		}//for
		
		//----------LDIR--------------------
		
		 numberOfBytes = 0X500;

		opCode = new byte[] { (byte) 0XED, (byte) 0XB0 };//LDIR
		setUpMemory(location, opCode);
		
		random.nextBytes(hlValues);
		setUpMemory(hlBase, hlValues);

		random.nextBytes(deValues);
		setUpMemory(deBase, deValues);
		
		wrs.setDoubleReg(hl, hlBase);	// Set HL
		wrs.setDoubleReg(de, deBase);	// Set DE
		wrs.setDoubleReg(bc, numberOfBytes);// Set BC
		
		wrs.setProgramCounter(location);
		cpu.executeInstruction(wrs.getProgramCounter());
		assertThat("LDIR values ",hlValues,equalTo(ioBuss.readDMA(deBase, numberOfBytes)));
		assertThat("LDIR Program Counter ",location + 2,equalTo(wrs.getProgramCounter()));
		
		assertThat("LDIR - BC ",0,equalTo(wrs.getDoubleReg(bc)));
		assertThat("LDIR - DE ",deBase + numberOfBytes,equalTo(wrs.getDoubleReg(de)));
		assertThat("LDIR - HL ",hlBase + numberOfBytes,equalTo(wrs.getDoubleReg(hl)));
		
		
//		System.out.printf("BC = %04X, DE = %04X, HL = %04X%n",
//		wrs.getDoubleReg(bc),wrs.getDoubleReg(de),wrs.getDoubleReg(hl));

	}//testLDIandLDIR
	
	
	@Test
	public void testLDDandLDDR() {
//		int limit = 0X05;
		
		int numberOfBytes = 0X500;
		
		Register bc = Z80.Register.BC;
		Register de = Z80.Register.DE;
		Register hl = Z80.Register.HL;
		
		Random random = new Random();
		
		int hlBase = 0x1000;
		byte[] hlValues = new byte[numberOfBytes];
		random.nextBytes(hlValues);
		setUpMemory(hlBase, hlValues);
		
		int deBase = 0x2000;
		byte[] deValues = new byte[numberOfBytes];
		random.nextBytes(deValues);
		setUpMemory(deBase, deValues);

		int location = 0X0000;
		byte[] opCode = new byte[] { (byte) 0XED, (byte) 0XA8 };//LDD
		setUpMemory(location, opCode);
		
		wrs.setDoubleReg(hl, hlBase + numberOfBytes- 1);	// Set HL
		wrs.setDoubleReg(de, deBase + numberOfBytes -1);	// Set DE
		wrs.setDoubleReg(bc, numberOfBytes);// Set BC
		
//		boolean bcIsZero;
		int bcCount;
		for (int i = numberOfBytes -1;i >=0;i--) {
			wrs.setProgramCounter(location);
			cpu.executeInstruction(location);

			assertThat("LDD ",location + 2,equalTo(wrs.getProgramCounter()));
//			System.out.printf(" hlValues[%04X] = %04X, ", i,hlValues[i]);
//			System.out.printf("deBase = %04X, ioBuss.read(%04X) = %02X%n", deBase,deBase+i,ioBuss.read(deBase+i));
			assertThat(i + ": LDD - value",hlValues[i],equalTo(ioBuss.read(deBase+i)));
			
			
			assertThat(i + ": LDD - HL",hlBase + i-1,equalTo(wrs.getDoubleReg(hl)));
			assertThat(i + ": LDD - DE",deBase + i-1,equalTo(wrs.getDoubleReg(de)));
			
			bcCount =wrs.getDoubleReg(bc);
			assertThat(i + ": LDD - BC",i,equalTo(bcCount));
			assertThat(i + ": LDD - zero",bcCount!=0,equalTo(ccr.isPvFlagSet()));
			
//			System.out.printf("BC = %04X, DE = %04X, HL = %04X%n",
//					wrs.getDoubleReg(bc),wrs.getDoubleReg(de),wrs.getDoubleReg(hl));
		}//for
		
		//----------LDDR--------------------
		
		 numberOfBytes = 0X500;

		opCode = new byte[] { (byte) 0XED, (byte) 0XB8 };//LDDR
		setUpMemory(location, opCode);
		
		random.nextBytes(hlValues);
		setUpMemory(hlBase, hlValues);

		random.nextBytes(deValues);
		setUpMemory(deBase, deValues);
		
		wrs.setDoubleReg(hl, hlBase + numberOfBytes- 1);	// Set HL
		wrs.setDoubleReg(de, deBase + numberOfBytes- 1);	// Set DE
		wrs.setDoubleReg(bc, numberOfBytes);// Set BC
		
		wrs.setProgramCounter(location);
		cpu.executeInstruction(wrs.getProgramCounter());
		assertThat("LDDR values ",hlValues,equalTo(ioBuss.readDMA(deBase, numberOfBytes)));
		assertThat("LDDR Program Counter ",location + 2,equalTo(wrs.getProgramCounter()));
		assertThat("LDDR p/vFlag ",false,equalTo(ccr.isPvFlagSet()));
		
		assertThat("LDDR - BC ",0,equalTo(wrs.getDoubleReg(bc)));
		assertThat("LDIR - DE ",deBase - 1,equalTo(wrs.getDoubleReg(de)));
		assertThat("LDDR - HL ",hlBase  - 1,equalTo(wrs.getDoubleReg(hl)));
		
		
//		System.out.printf("BC = %04X, DE = %04X, HL = %04X%n",
//		wrs.getDoubleReg(bc),wrs.getDoubleReg(de),wrs.getDoubleReg(hl));

	}//testLDDandLDDR

	// -----------------------------------------------------

	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory

}// class InstructionED2
