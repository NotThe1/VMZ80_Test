package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AdderBits {
	Adder adder = Adder.getInstance();
	AdderTestUtility atu = AdderTestUtility.getInstance();
	byte arg;

	@Before
	public void setUp() throws Exception {
		assertThat("keep imports", 1, equalTo(1));
		adder = Adder.getInstance();
		// adder.clearSets();
	}// setUp

	@Test
	public void testBit() {
		byte mask;
		boolean ans;
		String msg;
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			for (int bit = 0; bit < 8; bit++) {
				mask = (byte) (1 << bit);
				ans = (arg & mask) == 0;
				adder.bitTest(arg, bit);
				msg = String.format("Bit - Arg: %02X, Bit %d", arg, bit);
				assertThat(msg, ans, equalTo(adder.isZero()));
			} // inner for - bits
		} // for - arg
	}// testRLCA

	@Test
	public void testSet() {
		byte mask;
		byte ans;
		String msg;
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			for (int bit = 0; bit < 8; bit++) {
				mask = (byte) (1 << bit);
				ans = (byte) (arg | mask) ;
				adder.bitSet(arg, bit);
				msg = String.format("Set -Arg: %02X, Bit %d", arg, bit);
				assertThat(msg, ans, equalTo(adder.bitSet(arg, bit)));
			} // inner for - bits
		} // for - arg
	}// testRLCA

	@Test
	public void testRes() {
		byte mask;
		byte ans;
		String msg;
		for (int i = 0; i < 0XFF; i++) {
			arg = (byte) i;
			for (int bit = 0; bit < 8; bit++) {
				mask = (byte) (1 << bit);
				ans = (byte) (arg & ~mask) ;
				adder.bitRes(arg, bit);
				msg = String.format("Res -Arg: %02X, Bit %d", arg, bit);
				assertThat(msg, ans, equalTo(adder.bitRes(arg, bit)));
			} // inner for - bits
		} // for - arg
	}// testRLCA

}// class AdderBits
