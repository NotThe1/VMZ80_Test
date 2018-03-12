package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;
 
public class InstructionDD_IXY3 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	Register[] regSetRR = new Register[] { Register.BC, Register.DE, Register.SP, Register.BC, Register.DE,
			Register.SP };
	Register[] regSetIXorIY = new Register[] { Register.IX, Register.IX, Register.IX, Register.IY, Register.IY,
			Register.IY };
	HashMap<Integer, FileFlag> mapINC = new HashMap<>();
	HashMap<Integer, FileFlag> mapDEC = new HashMap<>();
	boolean sign, zero, halfCarry, parity, nFlag, carry;

	String message;
	int instructionBase = 0X1000;
	int valueBase = 0X2000;

	@Before
	public void setUp() throws Exception { // used for testADD
		mapINC = setUp("/IncOriginal.txt");
		mapDEC = setUp("/DecOrignal.txt");
	}// setUp
	
	@Test
	public void testINC() {
		int valueLocation = valueBase;
		FileFlag ff;
		byte displacement;
		// IX
		loadInstructions((byte) 0XDD,(byte) 0X34);
		for (int i = 0; i < 0X100; i++) {
			ff = mapINC.get(i);
			wrs.setIX(valueLocation);
			displacement = (byte) i;
			message = String.format("loc -> %04d, displ -> %d, i ->  %d, netLoc = %04d %n",
					valueLocation,displacement, i,valueLocation + displacement );
			cpuBuss.write(valueLocation + displacement, ff.getSource());
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,ff.getResult(),equalTo(cpuBuss.read(valueLocation + displacement)));
		} // for ff IX valuesADD
		
		// IX
		loadInstructions((byte) 0XFD,(byte) 0X34);
		for (int i = 0; i < 0X100; i++) {
			ff = mapINC.get(i);
			wrs.setIY(valueLocation);
			displacement = (byte) i;
			message = String.format("loc -> %04d, displ -> %d, i ->  %d, netLoc = %04d %n",
					valueLocation,displacement, i,valueLocation + displacement );
			cpuBuss.write(valueLocation + displacement, ff.getSource());
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,ff.getResult(),equalTo(cpuBuss.read(valueLocation + displacement)));
		} // for ff IX valuesADD
		
	}// testINC
	
	
	@Test
	public void testDEC() {
		int valueLocation = valueBase;
		FileFlag ff;
		byte displacement;
		// IX
		loadInstructions((byte) 0XDD,(byte) 0X35);
		for (int i = 0; i < 0X100; i++) {
			ff = mapDEC.get(i);
			wrs.setIX(valueLocation);
			displacement = (byte) i;
			message = String.format("loc -> %04d, displ -> %d, i ->  %d, netLoc = %04d %n",
					valueLocation,displacement, i,valueLocation + displacement );
			cpuBuss.write(valueLocation + displacement, ff.getSource());
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,ff.getResult(),equalTo(cpuBuss.read(valueLocation + displacement)));
		} // for ff IX valuesADD
		
		// IX
		loadInstructions((byte) 0XFD,(byte) 0X35);
		for (int i = 0; i < 0X100; i++) {
			ff = mapDEC.get(i);
			wrs.setIY(valueLocation);
			displacement = (byte) i;
			message = String.format("loc -> %04d, displ -> %d, i ->  %d, netLoc = %04d %n",
					valueLocation,displacement, i,valueLocation + displacement );
			cpuBuss.write(valueLocation + displacement, ff.getSource());
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,ff.getResult(),equalTo(cpuBuss.read(valueLocation + displacement)));
		} // for ff IX valuesADD
		
	}// testDEC
	
		//////////////////////////////////////////////////////////////


	private HashMap<Integer, FileFlag> setUp(String resourceFileName) {
		HashMap<Integer, FileFlag> map = new HashMap<>();
		String sArg1, flags;
		byte arg1, result;

		Integer key = 0;

		try {
			InputStream inputStream = this.getClass().getResourceAsStream(resourceFileName);
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArg1 = scanner.next();
				if (sArg1.startsWith(";")) {
					scanner.nextLine();
					continue;
				} // if skip line
					// System.out.printf("sArg1 -> %S%n", sArg1);
				arg1 = getValue(sArg1);
				result = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;

				map.put(key++, new FileFlag(arg1, result, sign, zero, halfCarry, parity, nFlag, carry));

				// scanner.nextLine();
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

		return map;
	}// setUp

	private void loadInstructions(byte oc1,byte oc2) {
		int instructionLocation = instructionBase;
		for (int i = 0; i < 0X100; i++) {
			cpuBuss.write(instructionLocation++, oc1);
			cpuBuss.write(instructionLocation++, oc2);
			cpuBuss.write(instructionLocation++, (byte) i);
		} // for
		wrs.setProgramCounter(instructionBase);
	}// loadInstructions

	private byte getValue(String value) {
		int valueInt = Integer.valueOf(value, 16);
		return (byte) valueInt;
	}// getValue

}// class InstructionDD_IXY3
