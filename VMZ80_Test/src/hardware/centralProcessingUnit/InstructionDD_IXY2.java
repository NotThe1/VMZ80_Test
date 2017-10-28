package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionDD_IXY2 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	byte dd = (byte) 0XDD;
	byte fd = (byte) 0XFD;
	int instructionBase = 0X1000;
	
	Register regIX = Z80.Register.IX;
	Register regIY = Z80.Register.IY;

	String message;

	@Test
	public void testINC(){
		int result;
		for (int i = 0; i<0X10000;i++){
			loadInstructions((byte)0X23);
			result = (i+1)& Z80.WORD_MASK;
			//IX
			message = String.format("IX : i = %04X", i);
			wrs.setDoubleReg(regIX, i);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,result ,equalTo(wrs.getDoubleReg(regIX)));
			//IY
			message = String.format("IY : i = %04X", i);
			wrs.setDoubleReg(regIY, i);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,result,equalTo(wrs.getDoubleReg(regIY)));
		}//for
	}//testINC
	
	@Test
	public void testDEC(){
		
		loadInstructions((byte)0X2B);
		wrs.setDoubleReg(regIX, 0);
		wrs.setDoubleReg(regIY, 0);
		cpu.executeInstruction(wrs.getProgramCounter());
		assertThat("IX ffff",0XFFFF,equalTo(wrs.getDoubleReg(regIX)));
		cpu.executeInstruction(wrs.getProgramCounter());
		assertThat("IY ffff",0XFFFF,equalTo(wrs.getDoubleReg(regIY)));
		
		int result;
		for (int i = 1; i<0X10000;i++){
			loadInstructions((byte)0X2B);
			result = (i-1)& Z80.WORD_MASK;
			//IX
			message = String.format("IX : i = %04X", i);
			wrs.setDoubleReg(regIX, i);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,result ,equalTo(wrs.getDoubleReg(regIX)));
			//IY
			message = String.format("IY : i = %04X", i);
			wrs.setDoubleReg(regIY, i);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,result,equalTo(wrs.getDoubleReg(regIY)));
		}//for
	}//testDEC
	
	private void loadInstructions(byte opcode2){
		byte[] instructions = new byte[]{dd,opcode2,fd,opcode2};
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
	}//loadInstructions


}//InstructionDD_IXY2
