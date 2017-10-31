package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionDD_IXY5 {
	
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	Register[] registers = new Register[] { Register.B, Register.C, Register.D, Register.E, Register.H, Register.L,
			Register.A };

	String message;
	int instructionBase = 0X1000;
	int valueBase = 0X2000;

	byte[] values = new byte[0X0101];
	final static String OPERATION_AND = "and";
	final static String OPERATION_OR = "or";
	final static String OPERATION_XOR = "xor";
	
	@Before
	public void setUp() throws Exception {
		assertThat("X", 0, equalTo(0));
	}// setUp

	 @Test
	public void testAND() {
		 testDriverAndOrXor(Register.IX,OPERATION_AND);
		 testDriverAndOrXor(Register.IY,OPERATION_AND);
	}// testAND
	
	 @Test
	public void testOR() {
		 testDriverAndOrXor(Register.IX,OPERATION_OR);
		 testDriverAndOrXor(Register.IY,OPERATION_OR);
	}// testAND

		
	 @Test
	public void testXOR() {
		 testDriverAndOrXor(Register.IX,OPERATION_XOR);
		 testDriverAndOrXor(Register.IY,OPERATION_XOR);
	}// testAND
	 
	 @Test
	public void testCP() {
		 testDriverCP(Register.IX);
		 testDriverCP(Register.IY);
	}// testAND

	public void testDriverCP(Register ixy) {
		
		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;
		byte opCode2 = (byte) 0XBE;
		
		byte arg1, arg2, result= (byte) 0X00;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags = "", message="";

		try {
					loadData(valueBase);
					loadInstructions(opcode1, opCode2);

			InputStream inputStream = this.getClass().getResourceAsStream("/CpOriginal.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			wrs.setDoubleReg(ixy, valueBase);
			int index =0;
			while (scanner.hasNextLine()) {
				// cannot skip any lines
				arg2 = getValue(scanner.next());
				arg1 = getValue(scanner.next());
				flags = scanner.next();
				
				if (arg1 == (byte) 0X00) {
					loadData(valueBase,arg2);
					loadInstructions(opcode1, opCode2);
				} // reload memory
				
				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s,   %02X CP %02X = %02X", ixy, arg1, arg2, result);
//				System.out.printf("index -> %02X, Acc -> %02X%n",index++,arg1);
				wrs.setAcc(arg1);
				cpu.executeInstruction(wrs.getProgramCounter());

//				assertThat(message, result, equalTo(wrs.getAcc()));

				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow: " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry: " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// testDriverCP

	public void testDriverAndOrXor(Register ixy,String operation) {
		byte opCode2 = (byte) 0X00;
		switch(operation){
		case OPERATION_AND:
			opCode2 = (byte) 0XA6;
			break;
		case OPERATION_XOR:
			opCode2 = (byte) 0XAE;
			break;
		case OPERATION_OR:
			opCode2 = (byte) 0XB6;
			break;
		}//switch - opcode1
		byte opcode1 = ixy == Register.IX ? (byte) 0XDD : (byte) 0XFD;
		
		byte arg1, arg2, result= (byte) 0X00;
		boolean sign, zero, halfCarry, overflow, nFlag, carry;
		// boolean carryState;
		String flags = "", message="";

		try {

			InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			wrs.setDoubleReg(ixy, valueBase);
			while (scanner.hasNextLine()) {
				// cannot skip any lines
				arg1 = getValue(scanner.next());
				arg2 = getValue(scanner.next());
				if (arg2 == (byte) 0X00) {
					loadData(valueBase);
					loadInstructions(opcode1, opCode2);
				} // reload memory
				switch(operation){
				case OPERATION_AND:
					result = getValue(scanner.next());
					flags = scanner.next();
					scanner.next();
					scanner.next();
					scanner.next();
					scanner.next();
					break;
				case OPERATION_OR:
					scanner.next();
					scanner.next();
					result = getValue(scanner.next());
					flags = scanner.next();
					scanner.next();
					scanner.next();
					break;
				case OPERATION_XOR:
					scanner.next();
					scanner.next();
					scanner.next();
					scanner.next();
					result = getValue(scanner.next());
					flags = scanner.next();
					break;
				}//switch - opcode1

//				result = getValue(scanner.next());
//				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				overflow = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				message = String.format("%s,   %02X %s %02X = %02X", ixy, arg1,operation, arg2, result);

				wrs.setAcc(arg1);
				cpu.executeInstruction(wrs.getProgramCounter());

				assertThat(message, result, equalTo(wrs.getAcc()));

				assertThat("sign: " + message, sign, equalTo(ccr.isSignFlagSet()));
				assertThat("zero: " + message, zero, equalTo(ccr.isZeroFlagSet()));
				assertThat("halfCarry: " + message, halfCarry, equalTo(ccr.isHFlagSet()));
				assertThat("overFlow: " + message, overflow, equalTo(ccr.isPvFlagSet()));
				assertThat("nFlag: " + message, nFlag, equalTo(ccr.isNFlagSet()));
				assertThat("carry: " + message, carry, equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// testDriverAndOrXor

////////////////////////////////////////////
	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	// // puts displacement at end of code
	private void loadInstructions(byte... codes) {
		int instructionLocation = instructionBase;
		for (int i = -128; i < 128; i++) {
			for (byte code : codes) {
				cpuBuss.write(instructionLocation++, code);
			} // for codes
			cpuBuss.write(instructionLocation++, (byte) i);
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions


	private void loadData(int valueBase) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, (byte) (128 + i));
		} // for i
	}// loadData

	private void loadData(int valueBase,byte value) {
		for (int i = -128; i < 128; i++) {
			cpuBuss.write(valueBase + i, value);
		} // for i
	}// loadData



}//class InstructionDD_IXY5
