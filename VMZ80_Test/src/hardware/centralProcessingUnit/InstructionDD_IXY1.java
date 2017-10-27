package hardware.centralProcessingUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
import hardware.CentralProcessingUnit;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import memory.CpuBuss;
import memory.IoBuss;

public class InstructionDD_IXY1 {
	CentralProcessingUnit cpu = CentralProcessingUnit.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	IoBuss ioBuss = IoBuss.getInstance();
	CpuBuss cpuBuss = CpuBuss.getInstance();

	Register[] regSetRR = new Register[] { Register.BC, Register.DE, Register.SP, Register.BC,
			Register.DE,  Register.SP };
	Register[] regSetIXorIY = new Register[] { Register.IX, Register.IX, Register.IX, Register.IY, Register.IY,
			Register.IY};
	HashMap<Integer, FileFlag> mapADD = new HashMap<>();
	boolean sign, zero, halfCarry, parity, nFlag, carry;
	int instructionBase = 0X1000;
	
	int testSize = 0X01000;
    int ixValueBase =	instructionBase+ 0X0100;	
    int iyValueBase =	ixValueBase + (2 * testSize) + 0X0100;;	

	// int hlRegisterValue = 0X0100; // (HL) - m

	@Before
	public void setUp() throws Exception { // used for testADD
		assertThat("keep imports", 1, equalTo(1));
		String sArgIXY, flags;
		int argIXY, argRR, resultInt;

		Integer key = 0;

		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/AddAdcWordOriginal.txt");
			// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
			Scanner scanner = new Scanner(inputStream);
			scanner.nextLine(); // skip header
			while (scanner.hasNextLine()) {
				sArgIXY = scanner.next();
				if (sArgIXY.startsWith(";")) {
					scanner.nextLine();
					continue;
				} // if skip line

				argIXY = getValue(sArgIXY);
				argRR = getValue(scanner.next());
				resultInt = getValue(scanner.next());
				flags = scanner.next();

				sign = flags.subSequence(0, 1).equals("1") ? true : false;
				zero = flags.subSequence(1, 2).equals("1") ? true : false;
				halfCarry = flags.subSequence(2, 3).equals("1") ? true : false;
				parity = flags.subSequence(3, 4).equals("1") ? true : false;
				nFlag = flags.subSequence(4, 5).equals("1") ? true : false;
				carry = flags.subSequence(5, 6).equals("1") ? true : false;
				mapADD.put(key++, new FileFlag(argIXY, argRR, resultInt, sign, zero, halfCarry, parity, nFlag, carry));

				scanner.nextLine();
			} // while
			scanner.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());
		} // try

	}// setUp
	
	@Test
	public void testLDIXYaa(){	// LD IXY,(nn)
		String message;
		Register regIX = Z80.Register.IX;
		Register regIY = Z80.Register.IY;
		IntStream intStream = new Random().ints((long)testSize,0X0000,0X10000);
		int[] ixValues= intStream.toArray();
		 intStream = new Random((long)testSize).ints((long)testSize,0X0000,0X10000);
		loadMemory(ixValueBase,ixValues);
		int[] iyValues= intStream.toArray();
		loadMemory(iyValueBase,iyValues);
		loadInstructions1(testSize);
		
		int xMem,yMem;
		for ( int i = 0; i < ixValues.length;i ++){
			//IX
			xMem = ixValueBase + (2* i);
			message = String.format("ixValues[%d] = %04X \t: mem[%04x] = %04X",
					i,ixValues[i],xMem,cpuBuss.readWordReversed(xMem));
//			System.out.printf(message);
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,ixValues[i],equalTo(wrs.getDoubleReg(regIX)));
//			System.out.printf("\tReg IX = %04X%n",wrs.getDoubleReg(regIX));
			//IY
			yMem = iyValueBase + (2* i);
			message = String.format("iyValues[%d] = %04X \t: mem[%04x] = %04X",
					i,iyValues[i],yMem,cpuBuss.readWordReversed(yMem));
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat(message,iyValues[i],equalTo(wrs.getDoubleReg(regIY)));
		
		}//for
		
	}//testLDIXYaa
	
	private void loadMemory(int base, int[] intValues){
		byte[] values;
		int location;
		for (int i = 0; i < intValues.length;i++){
			location = base + (2 * i);
			values = intToByteArray(intValues[i]);
			cpuBuss.writeWord(location, values[0], values[1]);
		}//for i
	}//loadMemory
	
	@Test
	public void testLDnnIXY(){ // LD (nn),IXY
		Register regIX = Z80.Register.IX;
		Register regIY = Z80.Register.IY;
		IntStream intStream = new Random().ints((long)testSize,0X0000,0X10000);
		int[] ixValues= intStream.toArray();
		 intStream = new Random((long)testSize).ints((long)testSize,0X0000,0X10000);
		int[] iyValues= intStream.toArray();
		for (int i = 0; i < testSize; i++){
			
			loadInstructions(ixValueBase+(2 *i),iyValueBase + (2 *i));
			wrs.setDoubleReg(regIX, ixValues[i]);
			wrs.setDoubleReg(regIY, iyValues[i]);
			cpu.executeInstruction(wrs.getProgramCounter());
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("PC value",instructionBase + 8,equalTo(wrs.getProgramCounter()));
		}//for i - testSize
		String message;
		for (int i = 0; i < testSize; i++){
			message = String.format("  read: i<-> %04X", i);

//			System.out.printf("ixValues[%02X] = %04X, Mem @ %04X = %02X%n",
//					i,ixValues[i],ixValueBase + (2*i),cpuBuss.readWordReversed(ixValueBase + (2*i)));
			
			assertThat("IX" + message,ixValues[i],equalTo( cpuBuss.readWordReversed(ixValueBase + (2*i))));
			assertThat("IY" + message,iyValues[i],equalTo( cpuBuss.readWordReversed(iyValueBase + (2*i))));
		}//for i - testSize
		int a = 0;
		a = a+0;
	}//testLDnnIXY
	
	@Test
	public void testLDIXYnn(){
		for ( int i = 0;i <0X10000;i++){
			loadInstructions(i);
			
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IX -> " + i,i,equalTo(wrs.getDoubleReg(Register.IX)));
			
			cpu.executeInstruction(wrs.getProgramCounter());
			assertThat("IY -> " + i,i,equalTo(wrs.getDoubleReg(Register.IY)));
		}//for
		
	}//testLDIXYnn


	@Test
	public void testADD() {
		String message;
		Collection<FileFlag> valuesADD = mapADD.values();
		int arg1, arg2, resultInt;
			loadInstructions();		
		for (FileFlag ff : valuesADD) {
			wrs.setProgramCounter(instructionBase);
			arg1 = ff.getArg1();
			arg2 = ff.getArg2();
			resultInt = ff.getResultInt();
			message = String.format("arg1 = %04X, arg2 = %04X, result = %04X", arg1,arg2,resultInt);
			for (int reg = 0; reg < regSetRR.length; reg++) {
//				System.out.println(message);
				wrs.setDoubleReg(regSetIXorIY[reg], arg1);
				wrs.setDoubleReg(regSetRR[reg], arg2);
				cpu.executeInstruction(wrs.getProgramCounter());
				assertThat(message,resultInt,equalTo(wrs.getDoubleReg(regSetIXorIY[reg])));
				assertThat("Hflag: " + message,ff.halfCarry,equalTo(ccr.isHFlagSet()));
				assertThat("Nflag: " + message,ff.nFlag,equalTo(ccr.isNFlagSet()));
				assertThat("carry: " + message,ff.carry,equalTo(ccr.isCarryFlagSet()));
			} // for reg

		} // for ff - each line of input
	}// ADD
	
	//////////////////////////////////////////////////////////////
	private int getValue(String value) {
		int tempInt;
		tempInt = Integer.valueOf(value, 16);
		return tempInt;
	}// getValue

	private void loadInstructions() {
		byte[] instructions = new byte[] { (byte) 0XDD, (byte) 0X009, (byte) 0XDD, (byte) 0X019, 
				 (byte) 0XDD, (byte) 0X039, (byte) 0XFD, (byte) 0X009, (byte) 0XFD, (byte) 0X019,
				 (byte) 0XFD, (byte) 0X039 };
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
		
	}// loadInstructions
	
	private void loadInstructions(int value){
		byte[] values = intToByteArray(value);
		
		byte[] instructions = new byte[] { (byte) 0XDD, (byte) 0X021,values[0],values[1],
			  	(byte) 0XFD, (byte) 0X021,values[0],values[1]};
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
	}//loadInstructions
	
	private void loadInstructions(int value1,int value2){
		byte[] values1 = intToByteArray(value1);
		byte[] values2 = intToByteArray(value2);

		byte[] instructions = new byte[] { (byte) 0XDD, (byte) 0X022,values1[0],values1[1],
			  	(byte) 0XFD, (byte) 0X022,values2[0],values2[1],};
		ioBuss.writeDMA(instructionBase, instructions);
		wrs.setProgramCounter(instructionBase);
	}//loadInstructions
	
	
	private void loadInstructions1(int testSize){
		byte dd = (byte) 0XDD;
		byte fd = (byte) 0XFD;
		byte code =(byte) 0X02A;
		byte[] values;
		int locationX,locationY,locationInstruction;
		int opcodeBase = iyValueBase + (2 * testSize) + 0X0100;
		for (int i = 0; i < testSize;i ++){
			locationInstruction = opcodeBase +( 8 * i);
			locationX = ixValueBase + ( 2*i);
			locationY = iyValueBase + ( 2*i);
			//IX
			ioBuss.write(locationInstruction++, dd);
			ioBuss.write(locationInstruction++, code);
			values = intToByteArray(locationX);
			cpuBuss.writeWord(locationInstruction++, values[0], values[1]);
			locationInstruction++;
			//IY
			ioBuss.write(locationInstruction++, fd);
			ioBuss.write(locationInstruction++, code);
			values = intToByteArray(locationY);
			cpuBuss.writeWord(locationInstruction++, values[0], values[1]);
			
			
			

		}//for
//		byte[] values = intToByteArray(value);
//		
//		byte[] instructions = new byte[] { (byte) 0XDD, (byte) 0X02A,values[0],values[1],
//			  	(byte) 0XFD, (byte) 0X02A,values[0],values[1]};

		wrs.setProgramCounter(opcodeBase);
	}//loadInstructions1
	

	
	private byte[] intToByteArray(int value){//[0]-> lsb, [1] -> msb
		byte lsb = (byte) (value & Z80.BYTE_MASK);
		byte msb = (byte) ((value >> 8) & Z80.BYTE_MASK);
		return new byte[]{lsb,msb};
	}//intToByteArray
	
	
	

}// class InstructionDD1
