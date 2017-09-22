import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import hardware.Instruction;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class TestMainInstructionsPage00 {
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
	public void testInstructionsFrom00To0F() {
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
		values = new byte[] {
				(byte) 0X00,
				(byte) 0X01, (byte) 0X34, (byte) 0X12,
				(byte) 0X02,
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
		
		instruction = new Instruction();
		assertThat("INC BC", Z80.Register.BC, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("INC B", Z80.Register.B, equalTo(instruction.getSingleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("DEC B", Z80.Register.B, equalTo(instruction.getSingleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat(" LD B,12H", Z80.Register.B, equalTo(instruction.getSingleRegister1()));
		assertThat(" LD B,12H", (byte)0X12, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RLCA", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		wrs.incrementProgramCounter(1);
		
		// skip  EX     AF,AF'
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("ADD HL,BC", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD HL,BC", Z80.Register.BC, equalTo(instruction.getDoubleRegister2()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("LD A,(BC)", Z80.Register.BC, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD A,(BC)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		wrs.incrementProgramCounter(1);

		instruction = new Instruction();
		assertThat("DEC BC", Z80.Register.BC, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("INC C", Z80.Register.C, equalTo(instruction.getSingleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("INC C", Z80.Register.C, equalTo(instruction.getSingleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat(" LD C,12H", Z80.Register.C, equalTo(instruction.getSingleRegister1()));
		assertThat(" LD C,12H", (byte)0X12, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RRCA", Z80.Register.A, equalTo(instruction.getSingleRegister1()));

	}// testPage0

@Test
public void testInstructionsFrom10To1F() {
	/* @formatter:off */
	/*
0001: 0000         ; testCodes10_1F.asm
0002: 0000                                             
0003: 0000 10 FB                  DJNZ   -5                   
0004: 0002 11 34 12               LD     DE,01234H            
0005: 0005 12                     LD     (DE),A               
0006: 0006 13                     INC    DE                   
0007: 0007 14                     INC    D                    
0008: 0008 15                     DEC    D                    
0009: 0009 16 07                  LD     D,07                 
0010: 000B 17                     RLA                         
0011: 000C 18 05                  JR     5                    
0012: 000E 19                     ADD    HL,DE                
0013: 000F 1A                     LD     A,(DE)               
0014: 0010 1B                     DEC    DE                   
0015: 0011 1C                     INC    E                    
0016: 0012 1D                     DEC    E                    
0017: 0013 1E FF                  LD     E,0FFH               
0018: 0015 1F                     RRA                         
	 */
	
	
	int location = 0000;
	values = new byte[] {
			(byte) 0X10,(byte) 0XFB,
			(byte) 0X11, (byte) 0X34, (byte) 0X12,
			(byte) 0X12,
			(byte) 0X13,
			(byte) 0X14,
			(byte) 0X15,
			(byte) 0X16, (byte)  0X07,
			(byte) 0X17,
			(byte) 0X18,  (byte) 0X05,
			(byte) 0X19,
			(byte) 0X1A,
			(byte) 0X1B,
			(byte) 0X1C,
			(byte) 0X1D,
			(byte) 0X1E, (byte)  0XFF,
			(byte) 0X1F};
	/* @formatter:on  */
	setUpMemory(location, values);
	
	wrs.incrementProgramCounter(2);
	// skip DNNZ

	instruction = new Instruction();
	assertThat("LD DE,1234H", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
	assertThat("LD BC,1234H", 0X1234, equalTo(instruction.getImmediateWord()));
	wrs.incrementProgramCounter(3);
	
	instruction = new Instruction();
	assertThat("LD (DE),A", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
	assertThat("LD (DE),A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC DE", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC D", Z80.Register.D, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("DEC D", Z80.Register.D, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat(" LD D,07H", Z80.Register.D, equalTo(instruction.getSingleRegister1()));
	assertThat(" LD D,07H", (byte)0X07, equalTo(instruction.getImmediateByte()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("RLA", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("JR 5", (byte) 0X05, equalTo(instruction.getImmediateByte()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("ADD HL,DE", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	assertThat("ADD HL,DE", Z80.Register.DE, equalTo(instruction.getDoubleRegister2()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("LD A,(DE)", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
	assertThat("LD A,(DE)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);

	instruction = new Instruction();
	assertThat("DEC DE", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC E", Z80.Register.E, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC E", Z80.Register.E, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat(" LD E,0FFH", Z80.Register.E, equalTo(instruction.getSingleRegister1()));
	assertThat(" LD E,0FFH", (byte)0X0FF, equalTo(instruction.getImmediateByte()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("RRA", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	
}//testInstructionsFrom10To1F

@Test
public void testInstructionsFrom20To2F() {
	/* @formatter:off */
	/*
0001: 0000         ; testCodes20_2F.asm
0002: 0000                                             
0003: 0000 20 AA                  JR     NZ,0AAH                 
0004: 0002 21 34 12               LD     HL,01234H            
0005: 0005 22 CD AB               LD     (0ABCDH),HL          
0006: 0008 23                     INC    HL                   
0007: 0009 24                     INC    H                    
0008: 000A 25                     DEC    H                    
0009: 000B 26 07                  LD     H,07                 
0010: 000D 27                     DAA                         
0011: 000E 28 05                  JR     Z,5                  
0012: 0010 29                     ADD    HL,HL                
0013: 0011 2A 55 AA               LD     HL,(0AA55H)          
0014: 0014 2B                     DEC    HL                   
0015: 0015 2C                     INC    L                    
0016: 0016 2D                     DEC    L                    
0017: 0017 2E FF                  LD     L,0FFH               
0018: 0019 2F                     CPL                                      
	 */
	
	
	int location = 0000;
	values = new byte[] {
			(byte) 0X20,(byte) 0XAA,
			(byte) 0X21, (byte) 0X34, (byte) 0X12,
			(byte) 0X22, (byte) 0XCD, (byte) 0XAB,
			(byte) 0X23,
			(byte) 0X24,
			(byte) 0X25,
			(byte) 0X26, (byte)  0X07,
			(byte) 0X27,
			(byte) 0X28, (byte) 0X05,
			(byte) 0X29,
			(byte) 0X2A, (byte) 0X55, (byte) 0XAA,
			(byte) 0X2B,
			(byte) 0X2C,
			(byte) 0X2D,
			(byte) 0X2E, (byte)  0XFF,
			(byte) 0X2F};
	/* @formatter:on  */
	setUpMemory(location, values);
	
	instruction = new Instruction();
	assertThat("JR NZ,0AAH", (byte) 0X0AA, equalTo(instruction.getImmediateByte()));
	assertThat("JR NZ,0AAH", Z80.ConditionCode.NZ, equalTo(instruction.getConditionCode()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("LD HL,1234H", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	assertThat("LD HL,1234H", 0X1234, equalTo(instruction.getImmediateWord()));
	wrs.incrementProgramCounter(3);
	
	instruction = new Instruction();
	assertThat("LD (0ABCDH),HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	assertThat("LD (0ABCDH),HL", 0X0ABCD, equalTo(instruction.getImmediateWord()));
	wrs.incrementProgramCounter(3);
	
	instruction = new Instruction();
	assertThat("INC HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC H", Z80.Register.H, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("DEC H", Z80.Register.H, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat(" LD H,07H", Z80.Register.H, equalTo(instruction.getSingleRegister1()));
	assertThat(" LD H,07H", (byte)0X07, equalTo(instruction.getImmediateByte()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("DAA", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("JR Z,5", (byte) 0X05, equalTo(instruction.getImmediateByte()));
	assertThat("JR Z,5", Z80.ConditionCode.Z, equalTo(instruction.getConditionCode()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("ADD HL,HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	assertThat("ADD HL,HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister2()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("LD HL,(0AA55H)", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	assertThat("LD HL,(0AA55H)",  0X0AA55, equalTo(instruction.getImmediateWord()));
	wrs.incrementProgramCounter(3);

	instruction = new Instruction();
	assertThat("DEC HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC L", Z80.Register.L, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("DEC L", Z80.Register.L, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat(" LD L,0FFH", Z80.Register.L, equalTo(instruction.getSingleRegister1()));
	assertThat(" LD L,0FFH", (byte)0X0FF, equalTo(instruction.getImmediateByte()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("CPL", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		
}//testInstructionsFrom20To2F

@Test
public void testInstructionsFrom30To3F() {
	/* @formatter:off */
	/*
0001: 0000         ; testCodes30_3F.asm
0002: 0000                                             
0003: 0000 30 AA                  JR     NC,0AAH              
0004: 0002 31 34 12               LD     SP,01234H            
0005: 0005 32 CD AB               LD     (0ABCDH),A           
0006: 0008 33                     INC    SP                   
0007: 0009 34                     INC    (HL)                 
0008: 000A 35                     DEC    (HL)                 
0009: 000B 36 04                  LD     (HL),04H             
0010: 000D 37                     SCF                         
0011: 000E 38 05                  JR     C,5                  
0012: 0010 39                     ADD    HL,SP                
0013: 0011 3A 55 AA               LD     A,(0AA55H)           
0014: 0014 3B                     DEC    SP                   
0015: 0015 3C                     INC    A                    
0016: 0016 3D                     DEC    A                    
0017: 0017 3E FF                  LD     A,0FFH               
0018: 0019 3F                     CCF                         
                                
	 */
	
	
	int location = 0000;
	values = new byte[] {
			(byte) 0X30,(byte) 0XAA,
			(byte) 0X31, (byte) 0X34, (byte) 0X12,
			(byte) 0X32, (byte) 0XCD, (byte) 0XAB,
			(byte) 0X33,
			(byte) 0X34,
			(byte) 0X35,
			(byte) 0X36, (byte)  0X04,
			(byte) 0X37,
			(byte) 0X38, (byte) 0X05,
			(byte) 0X39,
			(byte) 0X3A, (byte) 0X55, (byte) 0XAA,
			(byte) 0X3B,
			(byte) 0X3C,
			(byte) 0X3D,
			(byte) 0X3E, (byte)  0XFF,
			(byte) 0X3F};
	/* @formatter:on  */
	setUpMemory(location, values);
	
	instruction = new Instruction();
	assertThat("JR NC,0AAH", (byte) 0X0AA, equalTo(instruction.getImmediateByte()));
	assertThat("JR NC,0AAH", Z80.ConditionCode.NC, equalTo(instruction.getConditionCode()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("LD SP,1234H", Z80.Register.SP, equalTo(instruction.getDoubleRegister1()));
	assertThat("LD SP,1234H", 0X1234, equalTo(instruction.getImmediateWord()));
	wrs.incrementProgramCounter(3);
	
	instruction = new Instruction();
	assertThat("LD (0ABCDH),A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	assertThat("LD (0ABCDH),A", 0X0ABCD, equalTo(instruction.getImmediateWord()));
	wrs.incrementProgramCounter(3);
	
	instruction = new Instruction();
	assertThat("INC SP", Z80.Register.SP, equalTo(instruction.getDoubleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC (HL)", Z80.Register.M, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("DEC (HL)", Z80.Register.M, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat(" LD (HL),04H", Z80.Register.M, equalTo(instruction.getSingleRegister1()));
	assertThat(" LD (HL),04H", (byte)0X04, equalTo(instruction.getImmediateByte()));
	wrs.incrementProgramCounter(2);
	
	//skip SCF
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("JR C,5", (byte) 0X05, equalTo(instruction.getImmediateByte()));
	assertThat("JR C,5", Z80.ConditionCode.C, equalTo(instruction.getConditionCode()));
	wrs.incrementProgramCounter(2);
	
	instruction = new Instruction();
	assertThat("ADD HL,SP", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
	assertThat("ADD HL,SP", Z80.Register.SP, equalTo(instruction.getDoubleRegister2()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("LD A,(0AA55H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	assertThat("LD A,(0AA55H)",  0X0AA55, equalTo(instruction.getImmediateWord()));
	wrs.incrementProgramCounter(3);

	instruction = new Instruction();
	assertThat("DEC SP", Z80.Register.SP, equalTo(instruction.getDoubleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("INC A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat("DEC A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	wrs.incrementProgramCounter(1);
	
	instruction = new Instruction();
	assertThat(" LD A,0FFH", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
	assertThat(" LD A,0FFH", (byte)0X0FF, equalTo(instruction.getImmediateByte()));
	wrs.incrementProgramCounter(2);
	
	// skip CCF
		
}//testInstructionsFrom30To3F

	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory

}// testMainInstructions
