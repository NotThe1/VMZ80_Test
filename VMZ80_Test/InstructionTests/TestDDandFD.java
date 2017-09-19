import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import hardware.Instruction;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class TestDDandFD {
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
	public void testPage3() {// DD (E1,E5,E9 and E3,F9)
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0XE1,
							(byte) 0XDD, (byte) 0XE5,
							(byte) 0XDD, (byte) 0XE9,
							(byte) 0XDD, (byte) 0XE3,
							(byte) 0XDD, (byte) 0XF9,
							(byte) 0XFD, (byte) 0XE1,
							(byte) 0XFD, (byte) 0XE5,
							(byte) 0XFD, (byte) 0XE9,
							(byte) 0XFD, (byte) 0XE3,
							(byte) 0XFD, (byte) 0XF9
				  };
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("POP IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("PUSH IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("JP (IX)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("EX (SP),IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("EX (SP),IX", Z80.Register.SP, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("LD SP,IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD SP,IX", Z80.Register.SP, equalTo(instruction.getDoubleRegister2()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("POP IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		
		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("PUSH IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("JP (IY)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("EX (SP),IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("EX (SP),IY", Z80.Register.SP, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("LD SP,IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD SP,IX", Z80.Register.SP, equalTo(instruction.getDoubleRegister2()));

	}//testPage3
	
	@Test
	public void testLogical() {// DD 36
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0X86, (byte) 0X01,
							(byte) 0XDD, (byte) 0X8E, (byte) 0X23,
							(byte) 0XDD, (byte) 0X96, (byte) 0X45,
							(byte) 0XDD, (byte) 0X9E, (byte) 0X67,
							(byte) 0XDD, (byte) 0XA6, (byte) 0X89,
							(byte) 0XDD, (byte) 0XAE, (byte) 0XAB,
							(byte) 0XDD, (byte) 0XB6, (byte) 0XCD,
							(byte) 0XDD, (byte) 0XBE, (byte) 0XEF
				  };
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("ADD A,(IX+001H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD A,(IX+001H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("ADD A,(IX+001H)", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("ADC A,(IX+023H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADC A,(IX+023H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("ADC A,(IX+023H)", (byte) 0X23, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("SUB A,(IX+045H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("SUB A,(IX+045H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("SUB A,(IX+045H)", (byte) 0X45, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("SBC A,(IX+067H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("SBC A,(IX+067H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("SBC A,(IX+067H)", (byte) 0X67, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("AND(IX+089H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("AND(IX+089H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("AND(IX+089H)", (byte) 0X89, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("XOR(IX+0ABH)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("XOR(IX+0ABH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("XOR(IX+0ABH)", (byte) 0XAB, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("OR(IX+0CDH)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("OR(IX+0CDH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("OR(IX+0CDH)", (byte) 0XCD, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("CP(IX+0ABH)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("CP(IX+0ABH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("CP(IX+0ABH)", (byte) 0XEF, equalTo(instruction.getIndexDisplacement()));


		// now do it for IY
		for (int i = 0; i < 8; i++) {
			ioBuss.write(i * 3, (byte) 0XFD);
		} // for

		wrs.setProgramCounter(00);

		instruction = new Instruction();
		assertThat("ADD A,(IY+001H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD A,(IY+001H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("ADD A,(IY+001H)", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("ADC A,(IY+023H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADC A,(IY+023H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("ADC A,(IY+023H)", (byte) 0X23, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("SUB A,(IY+045H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("SUB A,(IY+045H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("SUB A,(IY+045H)", (byte) 0X45, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("SBC A,(IY+067H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("SBC A,(IY+067H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("SBC A,(IY+067H)", (byte) 0X67, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("AND(IY+089H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("AND(IY+089H)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("AND(IY+089H)", (byte) 0X89, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("XOR(IY+0ABH)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("XOR(IY+0ABH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("XOR(IY+0ABH)", (byte) 0XAB, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("OR(IY+0CDH)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("OR(IY+0CDH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("OR(IY+0CDH)", (byte) 0XCD, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("CP(IY+0ABH)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("CP(IY+0ABH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		assertThat("CP(IY+0ABH)", (byte) 0XEF, equalTo(instruction.getIndexDisplacement()));


	}//testLogical
	
	@Test
	public void testDisplacementAndSingleRegister() {// DD 36
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0X46, (byte) 0X01,
				(byte) 0XDD, (byte) 0X4E, (byte) 0X23,
				(byte) 0XDD, (byte) 0X56, (byte) 0X45,
				(byte) 0XDD, (byte) 0X5E, (byte) 0X67,
				(byte) 0XDD, (byte) 0X66, (byte) 0X89,
				(byte) 0XDD, (byte) 0X6E, (byte) 0XAB,
				(byte) 0XDD, (byte) 0X7E, (byte) 0XCD,
				 (byte) 0XDD, (byte) 0X70, (byte) 0X01,
					(byte) 0XDD, (byte) 0X71, (byte) 0X23,
					(byte) 0XDD, (byte) 0X72, (byte) 0X45,
					(byte) 0XDD, (byte) 0X73, (byte) 0X67,
					(byte) 0XDD, (byte) 0X74, (byte) 0X89,
					(byte) 0XDD, (byte) 0X75, (byte) 0XAB,
					(byte) 0XDD, (byte) 0X77, (byte) 0XCD
				  };
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("LD B,(IX+001H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD B,(IX+001H)", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD B,(IX+001H)", Z80.Register.B, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD C,(IX+023H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD C,(IX+023H)", (byte) 0X23, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD C,(IX+023H)", Z80.Register.C, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD D,(IX+045H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD D,(IX+045H)", (byte) 0X45, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD D,(IX+045H)", Z80.Register.D, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD E,(IX+067H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD E,(IX+067H)", (byte) 0X67, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD E,(IX+067H)", Z80.Register.E, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD H,(IX+089H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD H,(IX+089H)", (byte) 0X89, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD H,(IX+089H)", Z80.Register.H, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD L,(IX+0ABH)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD L,(IX+0ABH)", (byte) 0XAB, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD L,(IX+0ABH)", Z80.Register.L, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD A,(IX+0CDH)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD A,(IX+0CDH)", (byte) 0XCD, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD A,(IX+0CDH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("LD (IX+001H),B", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IX+001H),B", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IX+001H),B", Z80.Register.B, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IX+001H),C", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IX+001H),C", (byte) 0X23, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IX+001H),C", Z80.Register.C, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IX+045H),D", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IX+045H),D", (byte) 0X45, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IX+045H),D", Z80.Register.D, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IX+067H),E", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IX+067H),E", (byte) 0X67, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IX+067H),E", Z80.Register.E, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IX+089H),H", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IX+089H),H", (byte) 0X89, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IX+089H),H", Z80.Register.H, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IX+0ABH),L", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IX+0ABH),L", (byte) 0XAB, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IX+0ABH),L", Z80.Register.L, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IX+0CDH),A", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IX+0CDH),A", (byte) 0XCD, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IX+0CDH),A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));

		// now do it for IY
		for (int i = 0; i < 14; i++) {
			ioBuss.write(i * 3, (byte) 0XFD);
		} // for

		wrs.setProgramCounter(00);
		
		instruction = new Instruction();
		assertThat("LD B,(IY+001H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD B,(IY+001H)", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD B,(IY+001H)", Z80.Register.B, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD C,(IY+023H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD C,(IY+023H)", (byte) 0X23, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD C,(IY+023H)", Z80.Register.C, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD D,(IY+045H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD D,(IY+045H)", (byte) 0X45, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD D,(IY+045H)", Z80.Register.D, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD E,(IY+067H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD E,(IY+067H)", (byte) 0X67, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD E,(IY+067H)", Z80.Register.E, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD H,(IY+089H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD H,(IY+089H)", (byte) 0X89, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD H,(IY+089H)", Z80.Register.H, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD L,(IY+0ABH)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD L,(IY+0ABH)", (byte) 0XAB, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD L,(IY+0ABH)", Z80.Register.L, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD A,(IY+0CDH)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD A,(IY+0CDH)", (byte) 0XCD, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD A,(IY+0CDH)", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		
		instruction = new Instruction();
		assertThat("LD (IY+001H),B", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IY+001H),B", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IY+001H),B", Z80.Register.B, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IY+001H),C", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IY+001H),C", (byte) 0X23, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IY+001H),C", Z80.Register.C, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IY+045H),D", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IY+045H),D", (byte) 0X45, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IY+045H),D", Z80.Register.D, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IY+067H),E", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IY+067H),E", (byte) 0X67, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IY+067H),E", Z80.Register.E, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IY+089H),H", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IY+089H),H", (byte) 0X89, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IY+089H),H", Z80.Register.H, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IY+0ABH),L", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IY+0ABH),L", (byte) 0XAB, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IY+0ABH),L", Z80.Register.L, equalTo(instruction.getSingleRegister1()));
		
		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("LD (IY+0CDH),A", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (IY+0CDH),A", (byte) 0XCD, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD (IY+0CDH),A", Z80.Register.A, equalTo(instruction.getSingleRegister1()));
		
		
		
	}//testDisplacementAndSingleRegister
	
	@Test
	public void testDisplacementAndByte() {// DD 36
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0X36, (byte) 0X12, (byte) 0X34,
				 (byte) 0XFD, (byte) 0X36, (byte) 0XCD, (byte) 0XEF};
		setUpMemory(location, values);
		
		instruction = new Instruction();
		assertThat("LD(IX+012H),034H", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("INC (IX+012H),034H", (byte) 0X12, equalTo(instruction.getIndexDisplacement()));
		assertThat("INC (IX+012H),034H", (byte) 0X34, equalTo(instruction.getImmediateByte()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD(IY+0CDH),0EFH", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD(IY+0CDH),0EFH", (byte) 0XCD, equalTo(instruction.getIndexDisplacement()));
		assertThat("LD(IY+0CDH),0EFH", (byte) 0XEF, equalTo(instruction.getImmediateByte()));


	}//testDisplacementAndByte

	@Test
	public void testIncDec() {// DD (34,35) nn and DD 23,2B
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0X34, (byte) 0X12, (byte) 0XDD, (byte) 0X35, (byte) 0XAA, (byte) 0XFD,
				(byte) 0X34, (byte) 0X34, (byte) 0XFD, (byte) 0X35, (byte) 0X55, (byte) 0XDD, (byte) 0X23, (byte) 0XDD,
				(byte) 0X2B, (byte) 0XFD, (byte) 0X23, (byte) 0XFD, (byte) 0X2B, };
		setUpMemory(location, values);

		instruction = new Instruction();
		assertThat("INC (IX+012H)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("INC (IX+012H)", (byte) 0X12, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("DEC (IX+0AAH)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("DEC (IX+0AAH)", (byte) 0XAA, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("INC (IY+034H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("INC (IY+034H)", (byte) 0X34, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("DEC (IY+055H)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("DEC (IY+055H)", (byte) 0X55, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(3);
		instruction = new Instruction();
		assertThat("INC IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("DEC IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("INC IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("DEC IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
	}// testIncDec

	@Test
	public void testImmediateWord() {// DD (21,22,2A) nn
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0X21, (byte) 0X34, (byte) 0X12, (byte) 0XDD, (byte) 0X22, (byte) 0XAA,
				(byte) 0X55, (byte) 0XDD, (byte) 0X2A, (byte) 0XEF, (byte) 0XCD };
		setUpMemory(location, values);

		instruction = new Instruction();
		assertThat("LD IX,01234H", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD IX,01234H", 0X1234, equalTo(instruction.getImmediateWord()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD (055AAH),IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (055AAH),IX", 0X55AA, equalTo(instruction.getImmediateWord()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD IX,(0CDEFH)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD IX,(0CDEFH)", 0XCDEF, equalTo(instruction.getImmediateWord()));

		// now do it for IY
		for (int i = 0; i < 3; i++) {
			ioBuss.write(i * 4, (byte) 0XFD);
		} // for

		wrs.setProgramCounter(00);

		instruction = new Instruction();
		assertThat("LD IY,01234H", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD IY,01234H", 0X1234, equalTo(instruction.getImmediateWord()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD (055AAH),IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD (055AAH),IY", 0X55AA, equalTo(instruction.getImmediateWord()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("LD IY,(0CDEFH)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("LD IY,(0CDEFH)", 0XCDEF, equalTo(instruction.getImmediateWord()));

	}// testImmediateWord

	@Test
	public void testADD() {// DD (09,19,29,39)
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0X09, (byte) 0XDD, (byte) 0X19, (byte) 0XDD, (byte) 0X29, (byte) 0XDD,
				(byte) 0X39, };
		setUpMemory(location, values);

		instruction = new Instruction();
		assertThat("ADD IX,BC", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IX,BC", Z80.Register.BC, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADD IX,DE", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IX,DE", Z80.Register.DE, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADD IX,IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IX,IX", Z80.Register.IX, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADD IX,SP", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IX,SP", Z80.Register.SP, equalTo(instruction.getDoubleRegister2()));

		// now do it for IY
		for (int i = 0; i < 4; i++) {
			ioBuss.write(i * 2, (byte) 0XFD);
		} // for

		wrs.setProgramCounter(00);
		instruction = new Instruction();
		assertThat("ADD IY,BC", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IY,BC", Z80.Register.BC, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADD IY,DE", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IY,DE", Z80.Register.DE, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADD IY,IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IY,IY", Z80.Register.IY, equalTo(instruction.getDoubleRegister2()));

		wrs.incrementProgramCounter(2);
		instruction = new Instruction();
		assertThat("ADD IY,SP", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("ADD IY,SP", Z80.Register.SP, equalTo(instruction.getDoubleRegister2()));

	}// testADD

	@Test
	public void testBitsShifts() {// DD CB xx (06,0E,16,1E,26,2E,3E)
		int location = 0000;
		values = new byte[] { (byte) 0XDD, (byte) 0XCB, (byte) 0X00, (byte) 0X06, (byte) 0XDD, (byte) 0XCB, (byte) 0X01,
				(byte) 0X0E, (byte) 0XDD, (byte) 0XCB, (byte) 0X10, (byte) 0X16, (byte) 0XDD, (byte) 0XCB, (byte) 0X08,
				(byte) 0X1E, (byte) 0XDD, (byte) 0XCB, (byte) 0X80, (byte) 0X26, (byte) 0XDD, (byte) 0XCB, (byte) 0X55,
				(byte) 0X2E, (byte) 0XDD, (byte) 0XCB, (byte) 0XAA, (byte) 0X3E };
		setUpMemory(location, values);

		instruction = new Instruction();
		assertThat("RLC (IX+d)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("RLC (IX+d)", (byte) 0X00, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("RRC (IX+d)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("RRC (IX+d)", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("RL (IX+d)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("RL (IX+d)", (byte) 0X10, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("RR (IX+d)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("RR (IX+d)", (byte) 0X08, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("SLA (IX+d)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("SLA (IX+d)", (byte) 0X80, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("SRA (IX+d)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("SRA (IX+d)", (byte) 0X55, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("SRL (IX+d)", Z80.Register.IX, equalTo(instruction.getDoubleRegister1()));
		assertThat("SRL (IX+d)", (byte) 0XAA, equalTo(instruction.getIndexDisplacement()));

		// now do it for IY
		for (int i = 0; i < 7; i++) {
			ioBuss.write(i * 4, (byte) 0XFD);
		} // for

		wrs.setProgramCounter(00);

		instruction = new Instruction();
		assertThat("RLC (IY+d)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("RLC (IY+d)", (byte) 0X00, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("RRC (IY+d)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("RRC (IY+d)", (byte) 0X01, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("RL (IY+d)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("RL (IY+d)", (byte) 0X10, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("RR (IY+d)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("RR (IY+d)", (byte) 0X08, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("SLA (IY+d)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("SLA (IY+d)", (byte) 0X80, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("SRA (IY+d)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("SRA (IY+d)", (byte) 0X55, equalTo(instruction.getIndexDisplacement()));

		wrs.incrementProgramCounter(4);
		instruction = new Instruction();
		assertThat("SRL (IY+d)", Z80.Register.IY, equalTo(instruction.getDoubleRegister1()));
		assertThat("SRL (IY+d)", (byte) 0XAA, equalTo(instruction.getIndexDisplacement()));

	}// testBitsShifts

	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory

}// class TestDDandFD
