import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import hardware.Instruction;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class testMainInstructions {
	IoBuss ioBuss = IoBuss.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	Instruction instruction;
	byte values[];
	int location;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		// adder.clearSets();
	}// setUp
@Test
	public void testPage0() {// DD (E1,E5,E9 and E3,F9)
		/* @formatter:off 		*/
		/* 
		  0001: 0000 ; testCodes00_0F.asm 
		  0002: 0000 
		  0003: 0000 00			NOP 
		  0004: 0001 01 34 12	LD BC,1234H 
		  0005: 0004 02			LD	(BC),A 
		  0006: 0005 03			INC BC 
		  0007: 0006 04			INC B 
		  0008: 0007 05			DEC B 
		  0009: 0008 06 12		LD B,12H 
		  0010: 000A 07			RLCA 
		  0011: 000B 08			EX AF,AF' 
		  0012: 000C 09			ADD HL,BC
		  0013: 000D 0A			LD A,(BC)
		  0014: 000E 0B			DEC BC 
		  0015: 000F 0C			INC C 
		  0016: 0010 0D			DEC C 
		  0017: 0011 0E 12		LD C,12H 
		  0018: 0013 0F			RRCA
		 */
		
		int location = 0000;
		values = new byte[] { (byte) 0X00,
				(byte) 0X01, (byte) 0X34, (byte) 0X12,
				(byte) 0X03,
				(byte) 0X04,
				(byte) 0X05,
				(byte) 0X06, (byte)  0X12,
				(byte) 0X07,
				(byte) 0X08,
				(byte) 0X09,
				(byte) 0X0A,
				(byte) 0X0B,
				(byte) 0X0C,
				(byte) 0X0D,
				(byte) 0X0E, (byte)  0X12,
				(byte) 0X0F};
		
		 /* @formatter:on	 */
		setUpMemory(location, values);
		
		wrs.incrementProgramCounter(1);
		// a NOP does nothing

		instruction = new Instruction();
		assertThat("LD BC,1234H", Z80.Register.BC, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD BC,1234H", 0X1234, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("LD (BC),A", Z80.Register.BC, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (BC),A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		wrs.incrementProgramCounter(1);
		
		
	}// testPage0

	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory

}// testMainInstructions
