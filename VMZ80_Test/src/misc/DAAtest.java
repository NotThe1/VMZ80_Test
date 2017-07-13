package misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DAAtest {
	public static void main(String[] args) {
		new DAAtest().doit();
	}// main

	private void doit() {
		// ------------------------------------
		try {
			FileWriter fw = new FileWriter(new File("c:\\temp\\daa.txt"));
			PrintWriter pw = new PrintWriter(fw);

			int iMax = 100;
			int jMax = 100;
			byte binaryA, binaryB, binarySum;
			int intSum;
			Integer aTens = null;
			Integer aUnits = null;
			Integer bTens = null;
			Integer bUnits = null;
			String aLine = null;
			for (int i = 0; i < iMax; i++) {
				aTens = Integer.valueOf(i / 10);
				aUnits = Integer.valueOf(i % 10);
				binaryA = makeBinaryComposite(aTens, aUnits);

				for (int j = 0; j < jMax; j++) {
					bTens = Integer.valueOf(j / 10);
					bUnits = Integer.valueOf(j % 10);
					binaryB = makeBinaryComposite(bTens, bUnits);
					binarySum = (byte) (binaryA + binaryB);
					intSum = i + j;
//					aLine = String.format("binaryA = %02X, binaryB = %02X, binarySum = %02X, int sum =%d", binaryA,
//							binaryB, binarySum, intSum);
					aLine = String.format("%02X\t%02X\t%02X\t%d", binaryA,
							binaryB, binarySum, intSum);
					pw.println(aLine);
					// System.out.printf("i = %02d, tens = %d, units = %d ", i, aTens, aUnits);
					// System.out.printf("j = %02d, tens = %d, units = %d%n", j, bTens, bUnits);
				} // for
			} // for
			pw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ------------------------------------

	}// doit1

	private byte[] hiValues = new byte[] { (byte) 0X00, (byte) 0X10, (byte) 0X20, (byte) 0X30, (byte) 0X40, (byte) 0X50,
			(byte) 0X60, (byte) 0X70, (byte) 0X80, (byte) 0X90 };
	private byte[] loValues = new byte[] { (byte) 0X00, (byte) 0X01, (byte) 0X02, (byte) 0X03, (byte) 0X04, (byte) 0X05,
			(byte) 0X06, (byte) 0X07, (byte) 0X08, (byte) 0X09 };

	private byte makeBinaryComposite(int tens, int units) {
		return (byte) (hiValues[tens] + loValues[units]);
	}// makeBinaryComposit

	private void doit0() {
		for (int hi = 0; hi < 10; hi++) {
			for (int lo = 0; lo < 10; lo++) {
				System.out.printf("hi = %d, lo = %d%n", hi, lo);
			} // for

		} // for

	}// doit0

}//
