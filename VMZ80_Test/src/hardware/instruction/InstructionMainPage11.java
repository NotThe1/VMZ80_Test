package hardware.instruction;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import hardware.Instruction;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class InstructionMainPage11 {

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
	public void testInstructionsFromC0ToCF() {

	/* @formatter:off 		*/
	/* 
0001: 0000         ; testCodesC0_CF.asm
0002: 0000                                             
0003: 0000 C0                     RET    NZ                   
0004: 0001 C1                     POP    BC                   
0005: 0002 C2 67 45               JP     NZ,04567H            
0006: 0005 C3 CD AB               JP     0ABCDH               
0007: 0008 C4 05 00               CALL   NZ,0005H             
0008: 000B C5                     PUSH   BC                   
0009: 000C C6 07                  ADD    A,07H                
0010: 000E C7                     RST    00H                  
0011: 000F C8                     RET    Z                    
0012: 0010 C9                     RET                         
0013: 0011 CA 00 01               JP     Z,0100H              
0014: 0014         ; BITS
0015: 0014 CC 22 22               CALL   Z,02222H             
0016: 0017 CD 55 55               CALL   05555H               
0017: 001A CE 00                  ADC    A,00                 
0018: 001C CF                     RST    08H                
	 */
	
	int location = 0000;
	values = new byte[] {
			(byte) 0XC0,
			(byte) 0XC1,
			(byte) 0XC2, (byte) 0X67, (byte) 0X45,
			(byte) 0XC3, (byte) 0XCD, (byte) 0XAB,
			(byte) 0XC4, (byte) 0X05, (byte) 0X00,
			(byte) 0XC5,
			(byte) 0XC6, (byte)  0X07,
			(byte) 0XC7,
			(byte) 0XC8,
			(byte) 0XC9,
			(byte) 0XCA, (byte) 0X00, (byte) 0X01,
			
			(byte) 0XCC, (byte) 0X22, (byte) 0X22,
			(byte) 0XCD, (byte) 0X55, (byte) 0X55,
			(byte) 0XCE, (byte)  0X00,
			(byte) 0XCF};
	
	 /* @formatter:on	 */
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("RET NZ", Z80.ConditionCode.NZ, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("POP BC", Z80.Register.BC, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP NZ,04567H",  Z80.ConditionCode.NZ, equalTo(instruction.getConditionCode()));
		assertThat("JP NZ,04567H", 0X04567, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("JP 0XABCD", 0XABCD, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("CALL NZ,0005H",  Z80.ConditionCode.NZ, equalTo(instruction.getConditionCode()));
		assertThat("CALL NZ,0005H", 0X0005, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		instruction = new Instruction();
		assertThat("PUSH BC", Z80.Register.BC, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("ADD  A,07H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("ADD  A,07H", (byte)0X07, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 00H", (byte) 0X00, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("RET Z", Z80.ConditionCode.Z, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);

		//Skip RET
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP Z,0100H",  Z80.ConditionCode.Z, equalTo(instruction.getConditionCode()));
		assertThat("JP Z,0100H", 0X0100, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		

		instruction = new Instruction();
		assertThat("CALL Z,02222H",  Z80.ConditionCode.Z, equalTo(instruction.getConditionCode()));
		assertThat("CALL Z,02222H", 0X02222, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		instruction = new Instruction();
		assertThat("CALL 05555H", 0X05555, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		instruction = new Instruction();
		assertThat("ADC  A,00H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("ADC  A,00H", (byte)0X00, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 08H", (byte) 0X08, equalTo(instruction.getImmediateByte()));
			
	}// testInstructionsFromC0ToCF
	

	@Test
	public void testInstructionsFromD0ToDF() {

	/* @formatter:off 		*/
	/* 
0001: 0000         ; testCodesD0_DF.asm
0002: 0000                                             
0003: 0000 D0                     RET    NC                   
0004: 0001 D1                     POP    DE                   
0005: 0002 D2 67 45               JP     NC,04567H            
0006: 0005 D3 05                  OUT    (5),A                
0007: 0007 D4 05 00               CALL   NC,0005H             
0008: 000A D5                     PUSH   DE                   
0009: 000B D6 07                  SUB    07H                  
0010: 000D D7                     RST    10H                  
0011: 000E D8                     RET    C                    
0012: 000F D9                     EXX                         
0013: 0010 DA 00 01               JP     C,0100H              
0014: 0013 DB 05                  IN     A,(5)                
0015: 0015 DC 22 22               CALL   C,02222H             
0016: 0018         ; IX
0017: 0018 DE 00                  SBC    A,00                 
0018: 001A DF                     RST    18H             
	 */
	
	int location = 0000;
	values = new byte[] {
			(byte) 0XD0,
			(byte) 0XD1,
			(byte) 0XD2, (byte) 0X67, (byte) 0X45,
			(byte) 0XD3, (byte) 0X05,
			(byte) 0XD4, (byte) 0X05, (byte) 0X00,
			(byte) 0XD5,
			(byte) 0XD6, (byte) 0X07,
			(byte) 0XD7,
			(byte) 0XD8,
			(byte) 0XD9,
			(byte) 0XDA, (byte) 0X00, (byte) 0X01,
			(byte) 0XDB, (byte) 0X05,			
			(byte) 0XDC, (byte) 0X22, (byte) 0X22,

			(byte) 0XDE, (byte) 0X00,
			(byte) 0XDF};
	
	 /* @formatter:on	 */
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("RET NC", Z80.ConditionCode.NC, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("POP DE", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP NC,04567H",  Z80.ConditionCode.NC, equalTo(instruction.getConditionCode()));
		assertThat("JP NC,04567H", 0X04567, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("OUT (5),A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("OUT (5),A",(byte) 0X05, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("CALL NC,0005H",  Z80.ConditionCode.NC, equalTo(instruction.getConditionCode()));
		assertThat("CALL NC,0005H", 0X0005, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		instruction = new Instruction();
		assertThat("PUSH DE", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("SUB  07H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("SUB  07H", (byte)0X07, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 010H", (byte) 0X010, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("RET C", Z80.ConditionCode.C, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);

		//Skip EXX
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP C,0100H",  Z80.ConditionCode.C, equalTo(instruction.getConditionCode()));
		assertThat("JP C,0100H", 0X0100, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("IN A,(5)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("IN A,(5)",(byte) 0X05, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("CALL C,02222H",  Z80.ConditionCode.C, equalTo(instruction.getConditionCode()));
		assertThat("CALL C,02222H", 0X02222, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("SBC  A,00H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("SBC  A,00H", (byte)0X00, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 18H", (byte) 0X18, equalTo(instruction.getImmediateByte()));
			
	}// testInstructionsFromD0ToDF
	

	@Test
	public void testInstructionsFromE0ToEF() {

	/* @formatter:off 		*/
	/* 
0001: 0000         ; testCodesE0_EF.asm
0002: 0000                                             
0003: 0000 E0                     RET    PO                   
0004: 0001 E1                     POP    HL                   
0005: 0002 E2 67 45               JP     PO,04567H            
0006: 0005 E3                     EX     (SP),HL              
0007: 0006 E4 05 00               CALL   PO,0005H             
0008: 0009 E5                     PUSH   HL                   
0009: 000A E6 07                  AND    07H                  
0010: 000C E7                     RST    20H                  
0011: 000D E8                     RET    PE                   
0012: 000E E9                     JP     (HL)                 
0013: 000F EA 00 01               JP     PE,0100H             
0014: 0012 EB                     EX     DE,HL                
0015: 0013 EC 22 22               CALL   PE,02222H            
0016: 0016         ; EXTD
0017: 0016 EE 00                  XOR     00                   
0018: 0018 EF                     RST    28H              
	 */
	
	int location = 0000;
	values = new byte[] {
			(byte) 0XE0,
			(byte) 0XE1,
			(byte) 0XE2, (byte) 0X67, (byte) 0X45,
			(byte) 0XE3, 
			(byte) 0XE4, (byte) 0X05, (byte) 0X00,
			(byte) 0XE5,
			(byte) 0XE6, (byte) 0X07,
			(byte) 0XE7,
			(byte) 0XE8,
			(byte) 0XE9,
			(byte) 0XEA, (byte) 0X00, (byte) 0X01,
			(byte) 0XEB,			
			(byte) 0XEC, (byte) 0X22, (byte) 0X22,

			(byte) 0XEE, (byte) 0X00,
			(byte) 0XEF};
	
	 /* @formatter:on	 */
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("RET PO", Z80.ConditionCode.PO, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("POP HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP PO,04567H",  Z80.ConditionCode.PO, equalTo(instruction.getConditionCode()));
		assertThat("JP PO,04567H", 0X04567, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("EX (SP),HL", Z80.Register.SP, equalTo(instruction.getDoubleRegister1()));
		assertThat("EX (SP),HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister2()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("CALL PO,0005H",  Z80.ConditionCode.PO, equalTo(instruction.getConditionCode()));
		assertThat("CALL PO,0005H", 0X0005, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		instruction = new Instruction();
		assertThat("PUSH HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("AND  07H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("AND  07H", (byte)0X07, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 020H", (byte) 0X020, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("RET PE", Z80.ConditionCode.PE, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);

		instruction = new Instruction();
		assertThat("JP (HL)", Z80.Register.PC, equalTo(instruction.getDoubleRegister1()));
		assertThat("JP (HL)", Z80.Register.HL, equalTo(instruction.getDoubleRegister2()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP PE,0100H",  Z80.ConditionCode.PE, equalTo(instruction.getConditionCode()));
		assertThat("JP PE,0100H", 0X0100, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		
		instruction = new Instruction();
		assertThat("EX DE,HL", Z80.Register.DE, equalTo(instruction.getDoubleRegister1()));
		assertThat("EX DE,HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister2()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("CALL PE,02222H",  Z80.ConditionCode.PE, equalTo(instruction.getConditionCode()));
		assertThat("CALL PE,02222H", 0X02222, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		
		instruction = new Instruction();
		assertThat("XOR 00H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("XOR 00H", (byte)0X00, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 28H", (byte) 0X28, equalTo(instruction.getImmediateByte()));
			
	}// testInstructionsFromE0ToEF
	

	@Test
	public void testInstructionsFromF0ToFF() {

	/* @formatter:off 		*/
	/* 
0001: 0000         ; testCodesF0_FF.asm
0002: 0000                                             
0003: 0000 F0                     RET    P                    
0004: 0001 F1                     POP    AF                   
0005: 0002 F2 67 45               JP     P,04567H             
0006: 0005 F3                     DI                          
0007: 0006 F4 05 00               CALL   P,0005H              
0008: 0009 F5                     PUSH   AF                   
0009: 000A F6 07                  OR     07H                  
0010: 000C F7                     RST    30H                  
0011: 000D F8                     RET    M                    
0012: 000E F9                     LD     SP,HL                
0013: 000F FA 00 01               JP     M,0100H              
0014: 0012 FB                     EI                          
0015: 0013 FC 22 22               CALL   M,02222H             
0016: 0016         ; IY
0017: 0016 FE 00                  CP     00                   
0018: 0018 FF                     RST    38H                    
	 */
	
	int location = 0000;
	values = new byte[] {
			(byte) 0XF0,
			(byte) 0XF1,
			(byte) 0XF2, (byte) 0X67, (byte) 0X45,
			(byte) 0XF3, 
			(byte) 0XF4, (byte) 0X05, (byte) 0X00,
			(byte) 0XF5,
			(byte) 0XF6, (byte) 0X07,
			(byte) 0XF7,
			(byte) 0XF8,
			(byte) 0XF9,
			(byte) 0XFA, (byte) 0X00, (byte) 0X01,
			(byte) 0XFB,			
			(byte) 0XFC, (byte) 0X22, (byte) 0X22,

			(byte) 0XFE, (byte) 0X00,
			(byte) 0XFF};
	
	 /* @formatter:on	 */
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("RET P", Z80.ConditionCode.P, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("POP AF", Z80.Register.AF, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP P,04567H",  Z80.ConditionCode.P, equalTo(instruction.getConditionCode()));
		assertThat("JP P,04567H", 0X04567, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		// skip DI
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("CALL P,0005H",  Z80.ConditionCode.P, equalTo(instruction.getConditionCode()));
		assertThat("CALL P,0005H", 0X0005, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		instruction = new Instruction();
		assertThat("PUSH AF", Z80.Register.AF, equalTo(instruction.getDoubleRegister1()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("OR  07H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("OR  07H", (byte)0X07, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 030H", (byte) 0X030, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("RET M", Z80.ConditionCode.M, equalTo(instruction.getConditionCode()));
		wrs.incrementProgramCounter(1);

		instruction = new Instruction();
		assertThat("LD SP,HL", Z80.Register.SP, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD SP,HL", Z80.Register.HL, equalTo(instruction.getDoubleRegister2()));
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("JP M,0100H",  Z80.ConditionCode.M, equalTo(instruction.getConditionCode()));
		assertThat("JP M,0100H", 0X0100, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);
		
		
		//skip EI
		wrs.incrementProgramCounter(1);
		
		instruction = new Instruction();
		assertThat("CALL M,02222H",  Z80.ConditionCode.M, equalTo(instruction.getConditionCode()));
		assertThat("CALL M,02222H", 0X02222, equalTo(instruction.getImmediateWord()));
		wrs.incrementProgramCounter(3);

		
		instruction = new Instruction();
		assertThat("CP 00H", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("CP 00H", (byte)0X00, equalTo(instruction.getImmediateByte()));
		wrs.incrementProgramCounter(2);
		
		instruction = new Instruction();
		assertThat("RST 38H", (byte) 0X38, equalTo(instruction.getImmediateByte()));
			
	}// testInstructionsFromF0ToFF
	

	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory

}// TestMainInstructionsPage11
