package misc;

import java.util.BitSet;
import java.util.Random;
import java.util.stream.IntStream;

import codeSupport.Z80.Register;
import hardware.ArithmeticUnit;
import hardware.CentralProcessingUnit;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class ZZ {
	static ArithmeticUnit au = ArithmeticUnit.getInstance();

	public static void main(String[] args) {

		 byte arg1 = (byte) 0X80;
		 byte arg2 = (byte) 0X00;
		 int iarg1 = arg1;
		 int x = 0X1000;
		 int ans = arg1 + x;
		  System.out.printf("byte -> %02X, int -> %08X%n", arg1,iarg1);
		  System.out.printf("arg1 -> %04x, x -> %d, ans -> %04d%n", arg1,x,ans);
		  System.out.printf("iarg1 -> %04X, x -> %04X, ans1 -> %04X%n", iarg1,x,iarg1 + x);
		
		 for (int i = -128; i < 128; i++) {
		 arg1 = (byte) i;
		 // System.out.printf("i -> %02X, arg1 -> %02X, loc -> %04X%n",i,arg1,x+i);
		
		 // System.out.printf("i -> %2d, arg1 -> %02X, loc -> %04X%n",i,arg1,x+i);
		 System.out.printf("i -> %2d, arg1 -> %02X, loc -> %d%n", i, arg1, x + i);
		 }

		 compare(arg1,arg2);
		 compare(arg1,(byte)0X7F);
		 compare(arg1,(byte)0XFF);

		 arg1 = (byte) 0X00;
		 rotateLeftThruCarry(arg1,false);
		 arg1 = (byte) 0XFF;
		 rotateLeftThruCarry(arg1,false);

		 test2RandomArrays();
		 checkDisplacement();
		 checkADDIX();
		 checkPOP();
		 simpleLoop();
		testBitsetPrint();

	}// main

	private static void testBitsetPrint() {
		final int SIZE = 16;
		BitSet augend = new BitSet(SIZE);
		augend = BitSet.valueOf(new byte[] { 0X5A });
		String message = String.format("augend = %s%n", augend);
		System.out.println(message);
	}

	private static void simpleLoop() {
		// for (int i = 0; i <0X100; i++) {
		for (int i = -128; i < 128; i++) {
			System.out.printf("i -> %1$02X  [%1$d], byte - > %2$02X %n", i, (byte) i);
		} // for
	}// simpleLoop

	private static void checkPOP() {
		CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
		WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
		// ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
		// CpuBuss cpuBuss = CpuBuss.getInstance();
		IoBuss ioBuss = IoBuss.getInstance();
		int topOfStack = 0x1000;
		int instructionBase = 0X0100;
 
		byte[] instructions = new byte[] { (byte) 0xDD, (byte) 0xE1 };
		byte[] values = new byte[] { (byte) 0x34, (byte) 0x12 };
		ioBuss.writeDMA(instructionBase, instructions);
		ioBuss.writeDMA(topOfStack, values);
		wrs.setStackPointer(topOfStack);

		wrs.setProgramCounter(instructionBase);

		cpu.executeInstruction(wrs.getProgramCounter());

		int ans = wrs.getIX();

		System.out.printf("IX -> %04X%n", ans);

	}// checkPOP

	private static void checkADDIX() {
		CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
		WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
//		ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
		IoBuss ioBuss = IoBuss.getInstance();
		int instructionBase = 0X1000;
		int valueBase = 0X2000;
//		int start, result;
//		Register regIX = Z80.Register.IX;

		byte[] instrucions = new byte[] { (byte) 0XDD, (byte) 0X86, (byte) 0X00, (byte) 0XDD, (byte) 0X86, (byte) 0X01,
				(byte) 0XDD, (byte) 0X86, (byte) 0X02, (byte) 0XDD, (byte) 0X86, (byte) 0X03, (byte) 0XDD, (byte) 0X86,
				(byte) 0X04, };

		byte[] values = new byte[] { (byte) 0XE5, (byte) 0Xe6, (byte) 0Xe7, (byte) 0XFF, (byte) 0X80, (byte) 0XFE,
				(byte) 0XFF };
		ioBuss.writeDMA(instructionBase, instrucions);
		ioBuss.writeDMA(valueBase, values);
		wrs.setProgramCounter(instructionBase);
		wrs.setIX(valueBase);

		for (int i = 0; i < values.length; i++) {
			wrs.setAcc((byte) 0XE6);

			cpu.executeInstruction(wrs.getProgramCounter());
			System.out.printf("ans = %02X[%d]%n", wrs.getAcc(), wrs.getAcc());
		} // for i
	}// checkADDIX private static void checkDisplacement() {

	private static void checkDisplacement() {
		CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
		WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
//		ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
		IoBuss ioBuss = IoBuss.getInstance();
		int instructionBase = 0X1000;
		int  result;
//		Register regIX = Z80.Register.IX;

		byte[] instrucions = new byte[] { (byte) 0XDD, (byte) 0X34, (byte) 0X00, (byte) 0XDD, (byte) 0X34, (byte) 0X01,
				(byte) 0XDD, (byte) 0X34, (byte) 0X10, (byte) 0XDD, (byte) 0X34, (byte) 0X7F, (byte) 0XDD, (byte) 0X34,
				(byte) 0X80, (byte) 0XDD, (byte) 0X34, (byte) 0XFE, (byte) 0XDD, (byte) 0X34, (byte) 0XFF, };

		byte[] displacements = new byte[] { (byte) 0X00, (byte) 0X01, (byte) 0X10, (byte) 0X7F, (byte) 0X80,
				(byte) 0XFE, (byte) 0XFF };
		int starts[] = new int[] { 0X1000, 0X0000 };
		for (int i = 0; i < starts.length; i++) {
			ioBuss.writeDMA(instructionBase, instrucions);
			wrs.setProgramCounter(instructionBase);
			wrs.setIX(starts[i]);
			for (int j = 0; j < displacements.length; j++) {
				cpu.executeInstruction(wrs.getProgramCounter());
				result = wrs.getDoubleReg(Register.HL);
				System.out.printf("IX -> %04X, displacements -> %02X, result -> %04X%n", starts[i], displacements[j],
						result);

			} // for j
		} // for i

	}// checkDisplacement

	private static void test2RandomArrays() {
		int testSize = 0x10;

		IntStream intStream = new Random().ints((long) testSize, 0X0000, 0X10000);
		int[] ixValues = intStream.toArray();
		intStream = new Random().ints((long) testSize, 0X0000, 0X10000);// (long)testSize
		int[] iyValues = intStream.toArray();
		for (int i = 0; i < testSize; i++) {
			System.out.printf("i = %02X, IX = %04X, IY = %04X%n", i, ixValues[i], iyValues[i]);
		}
	}// test2RandomArrays

	private static void rotateLeftThruCarry(byte arg1, boolean carry) {
		au.rotateLeftThru(arg1, carry);

		System.out.printf("%nArg1 = %02X, carry = %s%n", arg1, carry);
		System.out.printf("%s\tSign%n", au.isSignFlagSet());
		System.out.printf("%s\tZero%n", au.isZeroFlagSet());
		System.out.printf("%s\tHalf Carry%n", au.isHCarryFlagSet());
		System.out.printf("%s\tParity%n", au.isParityFlagSet());

	}

	private static void compare(byte arg1, byte arg2) {
		au.compare(arg1, arg2);

		System.out.printf("%nArg1 = %02X, Arg2 = %02X%n", arg1, arg2);
		System.out.printf("%s\tSign%n", au.isSignFlagSet());
		System.out.printf("%s\tZero%n", au.isZeroFlagSet());
		System.out.printf("%s\tHalf Carry%n", au.isHCarryFlagSet());

	}// compare

}
