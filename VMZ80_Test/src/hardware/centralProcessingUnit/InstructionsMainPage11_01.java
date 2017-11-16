package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.ConditionCode;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionsMainPage11_01 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();
	int testCount;
	int instructionBase = 0X1000;
	int valueBase = 0X2000; // (HL) - m

	Register reg;
	byte arg1, arg2, result;
	byte[] wordParts;
	int wordValue;
	boolean sign, zero, halfCarry, overflow, nFlag, carry;
	boolean carryState;
	String sArg1, flags, message;
	


	@Test
	public void testConditionlRET() {
		testConditionlRET((byte) 0xC0, ConditionCode.NZ, false); // NZ
		testConditionlRET((byte) 0xC8, ConditionCode.Z, true); // Z
		testConditionlRET((byte) 0xD0, ConditionCode.NC, false); // NC
		testConditionlRET((byte) 0xD8, ConditionCode.C, true); // C
		testConditionlRET((byte) 0xE0, ConditionCode.PO, false); // PO
		testConditionlRET((byte) 0xE8, ConditionCode.PE, true); // PE
		testConditionlRET((byte) 0xF0, ConditionCode.P, false); // P
		testConditionlRET((byte) 0xF8, ConditionCode.M, true); // M
	}// testConditionlRET

	public void testConditionlRET(byte opCode, ConditionCode cc, boolean codeState) {

		/* @formatter:off */
		/*
		0000                        ORG    1000H                
		0006: 1000             Start:                          
		0007: 1000 CD 07 10               CALL   Target0              
		0008: 1003 CD 09 10               CALL   Target1              
		0009: 1006 76                     HALT                        
		0010: 1007             Target0:                        
		0011: 1007 C0                     RET    NZ                   
		0012: 1008 76                     HALT                        
		0013: 1009             Target1:                        
		0014: 1009 C0                     RET    NZ                   
		0015: 100A 76                     HALT                         */
		
		byte[] instructions = new byte[] {  (byte) 0xCD,(byte) 0x07,(byte) 0x10,
											(byte) 0xCD,(byte) 0x09,(byte) 0x10,
											(byte) 0x76,
											opCode,
											(byte) 0x76,
											opCode,
											(byte) 0x76};			/* @formatter:on  */

		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
		
		wrs.setStackPointer(0x4000);// CALL Target0
		assertThat("PC: ", 0x1000, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));
		

//		setConditionCodeState(cc, codeState);
		cpu.startInstruction(); // RET cc
		assertThat("PC: ", 0x1007, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));

		setConditionCodeState(cc, codeState);
		cpu.startInstruction(); // CALL Target1
		assertThat("PC: ", 0x1003, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));
		
		cpu.startInstruction(); // RET cc
		assertThat("PC: ", 0x1009, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));

		setConditionCodeState(cc, !codeState);
		cpu.startInstruction(); // CALL Target1
		assertThat("PC: ", 0x100A, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));
		
//		 System.out.printf("PC -> %04X, SP ->%04X%n", wrs.getProgramCounter(), wrs.getStackPointer());

	}// testConditionlRET
	
	
	@Test
	public void testConditionlJUMP() {
		testConditionlJUMP((byte) 0xC2, ConditionCode.NZ, false); // NZ
		testConditionlJUMP((byte) 0xCA, ConditionCode.Z, true); // Z
		testConditionlJUMP((byte) 0xD2, ConditionCode.NC, false); // NC
		testConditionlJUMP((byte) 0xDA, ConditionCode.C, true); // C
		testConditionlJUMP((byte) 0xE2, ConditionCode.PO, false); // PO
		testConditionlJUMP((byte) 0xEA, ConditionCode.PE, true); // PE
		testConditionlJUMP((byte) 0xF2, ConditionCode.P, false); // P
		testConditionlJUMP((byte) 0xFA, ConditionCode.M, true); // M
	}// testConditionlCALL

	public void testConditionlJUMP(byte opCode, ConditionCode cc, boolean codeState) {

		/* @formatter:off */
		/*
		0000                        ORG    1000H                
		0006: 1000             Start:                          
		0007: 1000 C4 04 10               JP     NZ,Target0           
		0008: 1003 76                     HALT                        
		0009: 1004             Target0:                        
		0010: 1004 C4 08 10               JP     NZ,Target1           
		0011: 1007 76                     HALT                         
		0012: 1008             Target1:                        
		0013: 1008 76                     HALT     */ 
		
		byte[] instructions = new byte[] {   opCode,(byte) 0x04,(byte) 0x10,
											(byte) 0x76,
											opCode,(byte) 0x08,(byte) 0x10,
											(byte) 0xC9,
											(byte) 0x76};			/* @formatter:on  */

		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
//		setConditionCodeState(cc, codeState);
		
		wrs.setStackPointer(0x4000);// CALL Target0
		assertThat("PC: ", 0x1000, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));
		

		setConditionCodeState(cc, codeState);
		cpu.startInstruction(); // CALL Target1
		assertThat("PC: ", 0x1004, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));

		setConditionCodeState(cc, !codeState);
		cpu.startInstruction(); // CALL Target1
		assertThat("PC: ", 0x1007, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));

//		 System.out.printf("PC -> %04X, SP ->%04X%n", wrs.getProgramCounter(), wrs.getStackPointer());

	}// testConditionlCALL

	@Test
	public void testConditionlCALL() {
		testConditionlCALL((byte) 0xC4, ConditionCode.NZ, false); // NZ
		testConditionlCALL((byte) 0xCC, ConditionCode.Z, true); // Z
		testConditionlCALL((byte) 0xD4, ConditionCode.NC, false); // NC
		testConditionlCALL((byte) 0xDC, ConditionCode.C, true); // C
		testConditionlCALL((byte) 0xE4, ConditionCode.PO, false); // PO
		testConditionlCALL((byte) 0xEC, ConditionCode.PE, true); // PE
		testConditionlCALL((byte) 0xF4, ConditionCode.P, false); // P
		testConditionlCALL((byte) 0xFC, ConditionCode.M, true); // M
	}// testConditionlCALL

	public void testConditionlCALL(byte opCode, ConditionCode cc, boolean codeState) {

		/* @formatter:off */
		/*
		0000                        ORG    1000H                
		0006: 1000             Start:                          
		0007: 1000 C4 04 10               CALL   NZ,Target0           
		0008: 1003 76                     HALT                        
		0009: 1004             Target0:                        
		0010: 1004 C4 08 10               CALL   NZ,Target1           
		0011: 1007 C9                     RET                         
		0012: 1008             Target1:                        
		0013: 1008 76                     HALT     */ 
		
		byte[] instructions = new byte[] {   opCode,(byte) 0x04,(byte) 0x10,
											(byte) 0x76,
											opCode,(byte) 0x04,(byte) 0x10,
											(byte) 0xC9,
											(byte) 0x76};			/* @formatter:on  */

		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
//		setConditionCodeState(cc, codeState);
		
		wrs.setStackPointer(0x4000);// CALL Target0
		assertThat("PC: ", 0x1000, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));
		

		setConditionCodeState(cc, codeState);
		cpu.startInstruction(); // CALL Target1
		assertThat("PC: ", 0x1004, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));

		setConditionCodeState(cc, !codeState);
		cpu.startInstruction(); // RET
		assertThat("PC: ", 0x1007, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); //HALT
		assertThat("PC: ", 0x1003, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));

//		 System.out.printf("PC -> %04X, SP ->%04X%n", wrs.getProgramCounter(), wrs.getStackPointer());

	}// testConditionlCALL

	private void setConditionCodeState(ConditionCode cc, boolean codeState) {
		ccr.clearAllCodes();
		switch (cc) {
		case Z:
		case NZ:
			ccr.setZeroFlag(codeState);
			break;
		case C:
		case NC:
			ccr.setCarryFlag(codeState);
			break;
		case PO:
		case PE:
			ccr.setPvFlag(codeState);
			break;
		case P:
		case M:
			ccr.setSignFlag(codeState);
			break;
		}// switch
		return ;
	}// setConditionCodeState

	@Test
	public void testPUSH_POP() {
		int[] regValues = new int[] { 0x0123, 0x4567, 0x89AB, 0xCDD7 }; // note F reg does not set bits 3 & 5
		byte[] instructions = new byte[] { (byte) 0xC5, (byte) 0xD5, (byte) 0xE5, (byte) 0xF5 }; // PUSH
		loadInstructions(1, instructions);
		Register reg;
		wrs.setStackPointer(valueBase);
		// PUSH
		for (int r = 0; r < Z80.doubleRegisters2.length; r++) {
			reg = Z80.doubleRegisters2[r];
			wordValue = regValues[r];
			wrs.setDoubleReg(reg, wordValue);
			wordParts = splitWord(wordValue);
			message = String.format("hi: %02X, lo: %02X, int: %04X%n", wordParts[1], wordParts[0], wordValue);
			// System.out.printf("hi: %02x, lo: %02X, int: %04X%n", wordParts[1],wordParts[0],wordValue);
			assertThat("SP, " + message, valueBase - (2 * (r)), equalTo(wrs.getStackPointer()));
			cpu.startInstruction();
			assertThat("SP + 0, " + message, wordParts[0], equalTo(ioBuss.read(wrs.getStackPointer())));
			assertThat("SP + 1, " + message, wordParts[1], equalTo(ioBuss.read(wrs.getStackPointer() + 1)));
			// System.out.println(wrs.getStackPointer());
		} // for r - reg

		// POP
		instructions = new byte[] { (byte) 0xC1, (byte) 0xD1, (byte) 0xE1, (byte) 0xF1 }; // POP
		loadInstructions(1, instructions);
		for (int r = 0; r < Z80.doubleRegisters2.length; r++) {
			reg = Z80.doubleRegisters2[r];
			wrs.setDoubleReg(reg, -1); // reset all registers
		} // for r - reg

		int stackLocation = valueBase - (2 * Z80.doubleRegisters2.length);
		wrs.setStackPointer(stackLocation);
		// System.out.print(stackLocation);
		for (int r = 0; r < Z80.doubleRegisters2.length; r++) {
			reg = Z80.doubleRegisters2[r];
			cpu.startInstruction();
			assertThat(reg.toString() + "POP ", stackLocation + (2 * (r + 1)), equalTo(wrs.getStackPointer()));
		} // for r - reg

	}// testPush_POP

	@Test
	public void testCALL_RET() {
		/* @formatter:off */
		/*
0000                        ORG    1000H                
0006: 1000                                             
0007: 1000 CD 08 10               CALL   Target0              
0008: 1003 CD 09 10               CALL   Target1              
0009: 1006 76                     HALT                        
0010: 1007                                             
0011: 1007 00                     DB     0                    
0012: 1008                                             
0013: 1008             Target0:                        
0014: 1008 C9                     RET                         
0015: 1009             Target1:                        
0016: 1009 CD 0D 10               CALL   Target2              
0017: 100C C9                     RET                         
0018: 100D             Target2:                        
0019: 100D C9                     RET                         
0020: 100E             Target3:                        
0021: 100E C9                     RET      

0013: Target0         1008   0007 
0015: Target1         1009   0008 
0018: Target2         100D   0016 
0020: Target3         100E   									*/

		
		byte[] instructions = new byte[] {(byte) 0xCD, (byte) 0x08, (byte) 0x10,
 										  (byte) 0xCD, (byte) 0x09, (byte) 0x10,
 										  (byte) 0x76,
 										  (byte) 0x00,          
 										  (byte) 0xC9,         
 										  (byte) 0xCD, (byte) 0x0D, (byte) 0x10,
 										  (byte) 0xC9,
 										  (byte) 0xC9,    
 										  (byte) 0xC9};	  /* @formatter:on  */

		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);

		wrs.setStackPointer(0x4000);// CALL Target0
		assertThat("PC: ", 0x1000, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); // RET
		assertThat("PC: ", 0x1008, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); // CALL Target1
		assertThat("PC: ", 0x1003, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); // CALL Target2
		assertThat("PC: ", 0x1009, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); // RET
		assertThat("PC: ", 0x100D, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFC, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); // RET
		assertThat("PC: ", 0x100C, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x3FFE, equalTo(wrs.getStackPointer()));

		cpu.startInstruction(); // HALT
		assertThat("PC: ", 0x1006, equalTo(wrs.getProgramCounter()));
		assertThat("SP: ", 0x4000, equalTo(wrs.getStackPointer()));

		// System.out.printf("PC -> %04X, SP ->%04X%n", wrs.getProgramCounter(), wrs.getStackPointer());

	}// testCALL_RET

	///////////////////////////////////////////////////////////////////

	private byte[] splitWord(int wordValue) { // [0] -> lo, [1] -> hi
		byte[] ans = new byte[2];
		ans[0] = (byte) (wordValue & Z80.WORD_MASK); // lo
		ans[1] = (byte) ((wordValue >> 8) & Z80.WORD_MASK);
		return ans;
	}

	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	private void loadInstructions(int count, byte[] opCodes) {
		int numberOfCodes = opCodes.length;
		for (int i = 0; i < count; i++) {
			ioBuss.writeDMA(instructionBase + (i * numberOfCodes), opCodes);
		} // for i - count
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

}// class InstructionsMainPage11_01
