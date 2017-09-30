package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
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

	// -----------------------------------------------------

	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory

}// class InstructionED2
