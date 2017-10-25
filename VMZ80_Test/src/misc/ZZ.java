package misc;

import java.util.Random;
import java.util.stream.IntStream;

import hardware.ArithmeticUnit;

public class ZZ {
		static ArithmeticUnit au = ArithmeticUnit.getInstance();

	public static void main(String[] args) {

		byte arg1 = (byte)0X7F;
		byte arg2 = (byte)0X00;
		
//		compare(arg1,arg2);
//		compare(arg1,(byte)0X7F);
//		compare(arg1,(byte)0XFF);
		
//		arg1 = (byte) 0X00;
//		rotateLeftThruCarry(arg1,false);
//		arg1 = (byte) 0XFF;
//		rotateLeftThruCarry(arg1,false);
		
		test2RandomArrays();
		
	}// main
	
	private static void test2RandomArrays(){
		int testSize = 0x10;
	
		IntStream intStream = new Random().ints((long)testSize,0X0000,0X10000);
		int[] ixValues= intStream.toArray();
		 intStream = new Random().ints((long)testSize,0X0000,0X10000);//(long)testSize
		int[] iyValues= intStream.toArray();
		for (int i = 0; i < testSize; i++){
			System.out.printf("i = %02X, IX = %04X, IY = %04X%n", i,ixValues[i],iyValues[i]);
		}
	}//test2RandomArrays
	
	private static void rotateLeftThruCarry(byte arg1,boolean carry) {
		au.rotateLeftThru(arg1, carry);
		
		System.out.printf("%nArg1 = %02X, carry = %s%n",arg1,carry);
		System.out.printf("%s\tSign%n", au.hasSign());
		System.out.printf("%s\tZero%n", au.isZero());
		System.out.printf("%s\tHalf Carry%n", au.hasHalfCarry());
		System.out.printf("%s\tParity%n", au.hasParity());

	}
	
	private static void compare(byte arg1,byte arg2){
		au.compare(arg1, arg2);
		
		System.out.printf("%nArg1 = %02X, Arg2 = %02X%n",arg1,arg2);
		System.out.printf("%s\tSign%n", au.hasSign());
		System.out.printf("%s\tZero%n", au.isZero());
		System.out.printf("%s\tHalf Carry%n", au.hasHalfCarry());

	}//compare

}
