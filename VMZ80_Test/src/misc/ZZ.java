package misc;

import hardware.ArithmeticUnit;

public class ZZ {
		static ArithmeticUnit au = ArithmeticUnit.getInstance();

	public static void main(String[] args) {

		byte arg1 = (byte)0X7F;
		byte arg2 = (byte)0X00;
		
		doIt(arg1,arg2);
		doIt(arg1,(byte)0X7F);
		doIt(arg1,(byte)0XFF);
		
	}// main
	
	private static void doIt(byte arg1,byte arg2){
		au.compare(arg1, arg2);
		
		System.out.printf("%nArg1 = %02X, Arg2 = %02X%n",arg1,arg2);
		System.out.printf("%s\tSign%n", au.hasSign());
		System.out.printf("%s\tZero%n", au.isZero());
		System.out.printf("%s\tHalf Carry%n", au.hasHalfCarry());

	}//doIt

}
