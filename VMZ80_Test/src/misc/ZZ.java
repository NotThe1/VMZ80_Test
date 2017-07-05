package misc;
import codeSupport.Z80;

public class ZZ {

	public static void main(String[] args) {

		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_SIGN, Z80.MASK_SIGN,(Z80.BIT_SIGN^ Z80.MASK_SIGN));
		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_ZERO, Z80.MASK_ZERO, Z80.BIT_ZERO^ Z80.MASK_ZERO);
		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_BIT5, Z80.MASK_BIT5, Z80.BIT_BIT5^ Z80.MASK_BIT5);
		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_AUX, Z80.MASK_AUX, Z80.BIT_AUX^ Z80.MASK_AUX);
		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_BIT3, Z80.MASK_BIT3, Z80.BIT_BIT3^ Z80.MASK_BIT3);
		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_PV, Z80.MASK_PV, Z80.BIT_PV^ Z80.MASK_PV);
		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_N, Z80.MASK_N, Z80.BIT_N^ Z80.MASK_N);
		System.out.printf("%02X <-> %02X : %02X%n", Z80.BIT_CARRY, Z80.MASK_CARRY, Z80.BIT_CARRY^ Z80.MASK_CARRY);

	}// main

}
