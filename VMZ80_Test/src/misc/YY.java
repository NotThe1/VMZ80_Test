package misc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Scanner;

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
	public void testNEGfile() {
		int location = 0X0000;
		byte arg1, result;
		String flags, sArg1;
		boolean sign, zero, halfCarry, parity, nFlag, carry;
		byte[] instruction = new byte[] { (byte) 0XED, (byte) 0X44 };
		for (int i = 0; i < 0X110; i++) {
			ioBuss.writeDMA(i * 2, instruction);
		} // for

		wrs.setProgramCounter(location);

		try {
			 InputStream inputStream = this.getClass().getResourceAsStream("/LogicOriginal1.txt");
//			InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.startsWith(";")) {
					scanner.nextLine();
					continue;
				} // check for comment line
				arg1 = getValue(sArg1);

				scanner.next();// Skip CPL result
				scanner.next();// Skip CPL flags

				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				// System.out.printf("%02X %02X %s ",arg1,arg2,flags);
				// System.out.printf(" %s %s %s %s %s %s %n", sign,zero,halfCarry,parity,nFlag,carry);

				message = String.format("file NEG -> %02X NEG -> %02X", arg1, result);

				wrs.setReg(Z80.Register.A, arg1);
				cpu.executeInstruction(wrs.getProgramCounter());
				
				
				assertThat("result: " + message, result, equalTo(wrs.getAcc()));
				 assertThat("sign: " + message,sign,equalTo(ccr.isSignFlagSet()));
				 assertThat("zero: " + message,zero,equalTo(ccr.isZeroFlagSet()));
				 assertThat("halfCarry: " + message,halfCarry,equalTo(ccr.isHFlagSet()));
				 assertThat("parity: " + message,parity,equalTo(ccr.isPvFlagSet()));
				 assertThat("nFlag: " + message,nFlag,equalTo(ccr.isNFlagSet()));
				 assertThat("carry: " + message,carry,equalTo(ccr.isCarryFlagSet()));
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try
	}// testNEGfile

	private byte getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return (byte) tempInt;
	}// getValue

	// private void setUpWordRegisters(int registerIndex, byte[] arg1, byte[] arg2, boolean carryState) {
	// wrs.setDoubleReg(Z80.Register.HL, arg1); // Set HL
	// wrs.setDoubleReg(registers[registerIndex], arg2);// Set rr
	// ccr.setCarryFlag(carryState);
	// cpu.executeInstruction(wrs.getProgramCounter());// do the ADCcy
	// }// setUpWordRegisters
	//
	// private void setUpMemory(int location, byte[] newValues) {
	// wrs.setProgramCounter(location);
	// // int size = newValues.length;
	// ioBuss.writeDMA(location, newValues);
	// }// setUpMemory
	//
	// private byte[] getValueArray(String value) {
	// int workingValue = Integer.valueOf(value, 16);
	// byte msb = (byte) ((workingValue & 0XFF00) >> 8);
	// byte lsb = (byte) ((byte) workingValue & 0X00FF);
	// return new byte[] { lsb, msb };
	// }// getValueArray

}// class YY
