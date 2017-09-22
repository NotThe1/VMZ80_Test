import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import hardware.Instruction;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class TestMainInstructionsPage01 {
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
	public void testInstructionsFrom40To7F() {
		int start = 0X40;
		int end = 0X7F;
		int source, destination;

		for (int i = start; i < end; i++) {
			ioBuss.write(i, (byte) (i & 0XFF));
		} // for

		wrs.setProgramCounter(start);

		for (int i = start; i < end; i++) {
			instruction = new Instruction();
			source = i & 0B0111;
			destination = (i >> 3) & 0B0111;
			assertThat(" opCode: " + i, Z80.singleRegisters[destination], equalTo(instruction.getSingleRegister1()));
			assertThat(" opCode: " + i, Z80.singleRegisters[source], equalTo(instruction.getSingleRegister2()));
			wrs.incrementProgramCounter(1);
		} // for

	}// testInstructionsFrom40To4F

}// TestMainInstructionsPage01
