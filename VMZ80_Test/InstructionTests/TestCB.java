import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import hardware.Instruction;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class TestCB {
	IoBuss ioBuss = IoBuss.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	Instruction instruction;
	byte values[];
	int location;
//	Register[] registers = new Register[] {

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));

	}// setUp

	@Test
	public void testSingleRegister() {// all
		String message;
		location = 0;
		int end = 256;
		int index;
		byte value = (byte) 00;
		for (int i = location; i < end; i++) {
			ioBuss.write(2*i, (byte)0XCB);
			ioBuss.write((2*i) + 1, value++);
		}// load load memory
		
		wrs.setProgramCounter(location);
		for (int i = location; i < end; i++) {
			instruction = new Instruction();
			index = i % 8;
			message = String.format("i = %02x, index = %02X", i,index);
			assertThat(message,Z80.singleRegisters[index],equalTo(instruction.getSingleRegister1()));
			wrs.incrementProgramCounter(2);
		}// for - tests
		
	}// testSingleRegister

	@Test
	public void testBits() {// BIT RES SET
		String message;
		location = 00;
		int end = 256;
		int bit;
		byte value = (byte) 00;
		for (int i = location; i < end; i++) {
			ioBuss.write(2*i, (byte)0XCB);
			ioBuss.write((2*i) + 1, value++);
		}// load load memory
		
		wrs.setProgramCounter(128);
		for (int i = 0x40; i < end; i++) {
			instruction = new Instruction();
			bit = ((i / 8) - 8) & 0B0111;
			message = String.format("i = %02x, bit = %02X", i,bit);
			assertThat(message,bit,equalTo(instruction.getBit()));
			wrs.incrementProgramCounter(2);
		}// for - tests
		
	}// testSingleRegister

}// TestCB
