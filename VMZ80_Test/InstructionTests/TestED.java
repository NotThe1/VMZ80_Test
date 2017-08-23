import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import hardware.Instruction;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class TestED {
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
	public void testPage01_XX_00() {//Page=1, YYY = ???, ZZZ = 000
		int location = 0000;
		values = new byte[] {(byte) 0XED,(byte) 0X40,
				(byte) 0XED,(byte) 0X48,
				(byte) 0XED,(byte) 0X50,
				(byte) 0XED,(byte) 0X58,
				(byte) 0XED,(byte) 0X60,
				(byte) 0XED,(byte) 0X68,
				(byte) 0XED,(byte) 0X70,
				(byte) 0XED,(byte) 0X78};
		setUpMemory(location,values);
		
		instruction = new Instruction();
		assertThat("IN B,(C)",Z80.Register.B,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("IN C,(C)",Z80.Register.C,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("IN D,(C)",Z80.Register.D,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("IN E,(C)",Z80.Register.E,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("IN H,(C)",Z80.Register.H,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("IN L,(C)",Z80.Register.L,equalTo(instruction.getSingleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("IN M,(C)",Z80.Register.M,equalTo(instruction.getSingleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("IN A,(C)",Z80.Register.A,equalTo(instruction.getSingleRegister1()));
	
	}//testPage01_XX_00
	
	@Test
	public void testPage01_XX_01() {//Page=1, YYY = ???, ZZZ = 001
		int location = 0000;
		values = new byte[] {(byte) 0XED,(byte) 0X41,
				(byte) 0XED,(byte) 0X49,
				(byte) 0XED,(byte) 0X51,
				(byte) 0XED,(byte) 0X59,
				(byte) 0XED,(byte) 0X61,
				(byte) 0XED,(byte) 0X69,
				(byte) 0XED,(byte) 0X71,
				(byte) 0XED,(byte) 0X79};
		setUpMemory(location,values);
		
		instruction = new Instruction();
		assertThat("OUT B,(C)",Z80.Register.B,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("OUT C,(C)",Z80.Register.C,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("OUT D,(C)",Z80.Register.D,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("OUT E,(C)",Z80.Register.E,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("OUT H,(C)",Z80.Register.H,equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("OUT L,(C)",Z80.Register.L,equalTo(instruction.getSingleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("OUT M,(C)",Z80.Register.M,equalTo(instruction.getSingleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("OUT A,(C)",Z80.Register.A,equalTo(instruction.getSingleRegister1()));

	}//testPage01_XX_01
	
	@Test
	public void testPage01_XX_02() {//Page=1, YYY = ???, ZZZ = 002
		int location = 0000;
		values = new byte[] {(byte) 0XED,(byte) 0X42,
				(byte) 0XED,(byte) 0X4A,
				(byte) 0XED,(byte) 0X52,
				(byte) 0XED,(byte) 0X5A,
				(byte) 0XED,(byte) 0X62,
				(byte) 0XED,(byte) 0X6A,
				(byte) 0XED,(byte) 0X72,
				(byte) 0XED,(byte) 0X7A};
		setUpMemory(location,values);
		
		instruction = new Instruction();
		assertThat("SBC HL,BC",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		assertThat("SBC HL,BC",Z80.Register.BC,equalTo(instruction.getDoubleRegister2()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADC HL,BC",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		assertThat("ADC HL,BC",Z80.Register.BC,equalTo(instruction.getDoubleRegister2()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("SBC HL,DE",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		assertThat("SBC HL,DE",Z80.Register.DE,equalTo(instruction.getDoubleRegister2()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADC HL,DE",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		assertThat("ADC HL,DE",Z80.Register.DE,equalTo(instruction.getDoubleRegister2()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("SBC HL,HL",Z80.Register.HL,equalTo(instruction.getDoubleRegister2()));
		assertThat("SBC HL,HL",Z80.Register.HL,equalTo(instruction.getDoubleRegister2()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADC HL,HL",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		assertThat("ADC HL,HL",Z80.Register.HL,equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("SBC HL,SP",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		assertThat("SBC HL,SP",Z80.Register.SP,equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADC HL,SP",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		assertThat("ADC HL,SP",Z80.Register.SP,equalTo(instruction.getDoubleRegister2()));

	}//testPage01_XX_02
	
	@Test
	public void testPage01_XX_03() {//Page=1, YYY = ???, ZZZ = 003
		int location = 0000;
		values = new byte[] {(byte) 0XED,(byte) 0X43,(byte) 0X23,(byte) 0X01,
				(byte) 0XED,(byte) 0X4B,(byte) 0X56,(byte) 0X34,
				(byte) 0XED,(byte) 0X53,(byte) 0X9A,(byte) 0X78,
				(byte) 0XED,(byte) 0X5B,(byte) 0XDE,(byte) 0XBC,
				(byte) 0XED,(byte) 0X63,(byte) 0X12,(byte) 0XF0,
				(byte) 0XED,(byte) 0X6B,(byte) 0X56,(byte) 0X34,
				(byte) 0XED,(byte) 0X73,(byte) 0X9A,(byte) 0X78,
				(byte) 0XED,(byte) 0X7B,(byte) 0XAA,(byte) 0X55};
		setUpMemory(location,values);
		
		instruction = new Instruction();
		assertThat("LD (nn),BC",0X0123,equalTo(instruction.getImmediateWord()));
		assertThat("LD (nn),BC ",Z80.Register.BC,equalTo(instruction.getDoubleRegister1()));
		
		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD BC,(nn)",0X3456,equalTo(instruction.getImmediateWord()));
		assertThat("LD BC,(nn)",Z80.Register.BC,equalTo(instruction.getDoubleRegister1()));
		
		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD (nn),DE",0X789A,equalTo(instruction.getImmediateWord()));
		assertThat("LD (nn),DE",Z80.Register.DE,equalTo(instruction.getDoubleRegister1()));
		
		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD DE,(nn)",0XBCDE,equalTo(instruction.getImmediateWord()));
		assertThat("LD DE,(nn)",Z80.Register.DE,equalTo(instruction.getDoubleRegister1()));
		
		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD (nn),HL",0XF012,equalTo(instruction.getImmediateWord()));
		assertThat("LD (nn),HL",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));
		
		wrs.incrementProgramCounter(4);
		instruction = new Instruction();////
		assertThat("LD HL,(nn)",0X3456,equalTo(instruction.getImmediateWord()));
		assertThat("LD HL,(nn)",Z80.Register.HL,equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD (nn),SP",0X789A,equalTo(instruction.getImmediateWord()));
		assertThat("LD (nn),SP",Z80.Register.SP,equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD SP,(nn)",0X55AA,equalTo(instruction.getImmediateWord()));
		assertThat("LD SP,(nn)",Z80.Register.SP,equalTo(instruction.getDoubleRegister1()));

	}//testPage01_XX_03
	
	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}//loadMemory
	
}//class TestED
