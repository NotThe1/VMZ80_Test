package misc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.IoBuss;

public class YY {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	Register[] registers = new Register[] { Z80.Register.BC, Z80.Register.DE, Z80.Register.SP };
	String message;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
	}// setUp

	@Test
	public void testRLD() {
		byte accBefore,memBefore,accAfter,memAfter;
		int limit = 0X0500;
		int memBase = 0x0100;
		Random random = new Random();
		byte[] accValues = new byte[limit];
		random.nextBytes(accValues);

		byte[] memValues = new byte[limit];
		random.nextBytes(memValues);
		setUpMemory(memBase, memValues);

		int location = 0X0000;
		byte[] opCode = new byte[] { (byte) 0XED, (byte) 0X6F };
		setUpMemory(location, opCode);
		
		byte n1,n2,n3,n4;
		for( int i = 0; i < limit;i++) {
			memBefore = ioBuss.read(i + memBase);
			accBefore = accValues[i];
			n1 = (byte) (accBefore & 0XF0);
			n2 = (byte) (accBefore & 0X0F);
			n3 = (byte) (memBefore & 0XF0);
			n4 = (byte) (memBefore & 0X0F);
			
			accAfter = (byte) (n1 | ((n3>>4) & 0X0F));
			memAfter = (byte) (((n4 <<4) & 0XF0) | n2);
			
			message = String.format("acc - mem : %02X, %02X, - %02X, %02X", accBefore,memBefore,accAfter,memAfter);
//			System.out.println(message);
			wrs.setProgramCounter(location);
			wrs.setDoubleReg(Z80.Register.HL, i + memBase);
			wrs.setAcc(accBefore);
			cpu.executeInstruction(wrs.getProgramCounter());
			
//			System.out.printf("Acc -> %02X :", wrs.getAcc());
//			System.out.printf("Mem -> %02X %n",ioBuss.read(i + memBase) );
			assertThat(message,accAfter,equalTo(wrs.getAcc()));
			assertThat(message,memAfter,equalTo(ioBuss.read(i + memBase)));
				
		}//for
		
	}// testRRD

	///////////////////////////////////////////////////////////////////////////////

	// private byte getValue(String value) {
	// int tempInt;
	// tempInt = Integer.valueOf(value, 16);
	// return (byte) tempInt;
	// }// getValue

	// private void setUpWordRegisters(int registerIndex, byte[] arg1, byte[] arg2, boolean carryState) {
	// wrs.setDoubleReg(Z80.Register.HL, arg1); // Set HL
	// wrs.setDoubleReg(registers[registerIndex], arg2);// Set rr
	// ccr.setCarryFlag(carryState);
	// cpu.executeInstruction(wrs.getProgramCounter());// do the ADCcy
	// }// setUpWordRegisters
	//
	private void setUpMemory(int location, byte[] newValues) {
		wrs.setProgramCounter(location);
		// int size = newValues.length;
		ioBuss.writeDMA(location, newValues);
	}// setUpMemory
		//
		// private byte[] getValueArray(String value) {
		// int workingValue = Integer.valueOf(value, 16);
		// byte msb = (byte) ((workingValue & 0XFF00) >> 8);
		// byte lsb = (byte) ((byte) workingValue & 0X00FF);
		// return new byte[] { lsb, msb };
		// }// getValueArray

}// class YY
