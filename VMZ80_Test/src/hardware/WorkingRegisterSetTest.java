package hardware;

import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import codeSupport.Z80;
import codeSupport.Z80.Register;
 
public class WorkingRegisterSetTest {
	WorkingRegisterSet wrs;
	Register[] byteRegisters = new Register[] { Register.A, Register.B, Register.C, Register.D, Register.E, Register.H,
			Register.H, Register.I, Register.R };
	Register[] wordRegisters = new Register[] { Register.BC, Register.DE, Register.HL, Register.SP, Register.PC,
			Register.IX, Register.IY };

	@Before
	public void setUp() throws Exception {
		wrs = WorkingRegisterSet.getInstance();
		assertThat("keep imports", 1, equalTo(1));
	}// setUp

	@Test
	public void testByteRegisters() {
		byte value;
		for (int i = 0; i < 256; i++) {
			value = (byte) i;

			for (Register r : byteRegisters) {
				wrs.setReg(r, value);
			} // for set values

			for (Register r : byteRegisters) {
				assertThat(r.toString() + " testByteRegisters", value, equalTo(wrs.getReg(r)));
			} // for test values
		} // outer for

		for (int i = 0; i < 256; i++) {
			value = (byte) i;
			wrs.setAcc(value);
			assertThat("setACC" + " testByteRegisters", value, equalTo(wrs.getAcc()));
		} // outer for
	}// testByteRegisters

	@Test
	public void testWordRegisters() {
		byte[] valueArray = new byte[] { (byte) 00, (byte) 00 };
		byte hi, lo;
		for (int value = 0; value < 65535; value++) {
			hi = (byte) ((value >> 8) & Z80.BYTE_MASK);
			lo = (byte) (value & Z80.BYTE_MASK);
			valueArray[0] = lo;
			valueArray[1] = hi;
			
//			System.out.printf("Value = %04X (%,d)%n", value,value);

			for (Register r : wordRegisters) {
				wrs.setDoubleReg(r, value);
			} // for set values

			for (Register wr : wordRegisters) {
				assertThat(wr.toString() + " testWordRegisters(int)", value, equalTo(wrs.getDoubleReg(wr)));
				assertThat(wr.toString() + " testWordRegisters(byte[]) ", valueArray,
						equalTo(wrs.getDoubleRegArray(wr)));
			} // for test values

			for (Register r : wordRegisters) {
				wrs.setDoubleReg(r, valueArray);
			} //// for set values\

			for (Register wr : wordRegisters) {
				assertThat(wr.toString() + " testWordRegisters(byte[],byte[])", value, equalTo(wrs.getDoubleReg(wr)));
			} // for test values

		} // outer for
	}// testWordRegisters

	@Test
	public void testWordRegistersByteByte() {
		byte hi, lo;
		byte[] valueArray = new byte[] { (byte) 00, (byte) 00 };

		for (int value = 0; value < 65536; value++) {
			hi = (byte) ((value >> 8) & Z80.BYTE_MASK);
			lo = (byte) (value & Z80.BYTE_MASK);
			valueArray[0] = lo;
			valueArray[1] = hi;
			
			wrs.setStackPointer(value);
			wrs.setProgramCounter(value);
			wrs.setIX(value);
			wrs.setIY(value);

			assertThat(value + " SP(value) ", value, equalTo(wrs.getStackPointer()));
			assertThat(value + " PC(value) ", value, equalTo(wrs.getProgramCounter()));
			assertThat(value + " IX(value) ", value, equalTo(wrs.getIX()));
			assertThat(value + " IY(value) ", value, equalTo(wrs.getIY()));

			assertThat(value + " SP(valueArray) ", valueArray, equalTo(wrs.getStackPointerArray()));
			assertThat(value + " PC(valueArray) ", valueArray, equalTo(wrs.getProgramCounterArray()));
			assertThat(value + " IX(valueArray) ", valueArray, equalTo(wrs.getIXarray()));
			assertThat(value + " IY(valueArray) ", valueArray, equalTo(wrs.getIYarray()));
			
			wrs.setStackPointer(hi,lo);
			wrs.setProgramCounter(hi,lo);
			wrs.setIX(hi,lo);
			wrs.setIY(hi,lo);
			
			assertThat(value + " SP(hi,lo) ", value, equalTo(wrs.getStackPointer()));
			assertThat(value + " PC(hi,lo) ", value, equalTo(wrs.getProgramCounter()));
			assertThat(value + " IX(hi,lo) ", value, equalTo(wrs.getIX()));
			assertThat(value + " IY(hi,lo) ", value, equalTo(wrs.getIY()));

			wrs.setStackPointer(hi,lo);
			wrs.setProgramCounter(hi,lo);
			wrs.setIX(valueArray);
			wrs.setIY(valueArray);
			
			assertThat(value + " SP(valueArray) ", value, equalTo(wrs.getStackPointer()));
			assertThat(value + " PC(valueArray) ", value, equalTo(wrs.getProgramCounter()));
			assertThat(value + " IX(valueArray) ", value, equalTo(wrs.getIX()));
			assertThat(value + " IY(valueArray) ", value, equalTo(wrs.getIY()));
			
		} // outer for
	}// testWordRegistersByteByte

	@Test
	public void testSetPCIncremet() {
		int nextValue = 0;
		int delta;
		Random random = new Random();
		wrs.setProgramCounter(nextValue);
		while (nextValue < 65000) {
			delta = random.nextInt(4);
			nextValue = nextValue + delta;
			wrs.incrementProgramCounter(delta);
			assertThat(nextValue + " incrementProgramCounter ", nextValue, equalTo(wrs.getProgramCounter()));
		} // while
	}// testSetPCIncremet

	@Test
	public void testIFF() {
		boolean state1 = true;
		boolean state2 = true;
		wrs.setIFF1(state1);
		wrs.setIFF2(state2);
		assertThat("IFF1 - 1", state1, equalTo(wrs.isIFF1Set()));
		assertThat("IFF2 - 1", state2, equalTo(wrs.isIFF2Set()));

		state1 = true;
		state2 = false;
		wrs.setIFF1(state1);
		wrs.setIFF2(state2);
		assertThat("IFF1 - 1", state1, equalTo(wrs.isIFF1Set()));
		assertThat("IFF2 - 1", state2, equalTo(wrs.isIFF2Set()));

		state1 = false;
		state2 = true;
		wrs.setIFF1(state1);
		wrs.setIFF2(state2);
		assertThat("IFF1 - 1", state1, equalTo(wrs.isIFF1Set()));
		assertThat("IFF2 - 1", state2, equalTo(wrs.isIFF2Set()));

		state1 = false;
		state2 = false;
		wrs.setIFF1(state1);
		wrs.setIFF2(state2);
		assertThat("IFF1 - 1", state1, equalTo(wrs.isIFF1Set()));
		assertThat("IFF2 - 1", state2, equalTo(wrs.isIFF2Set()));

	}// testIFF

	@Test
	public void testSwaps() {

		byte b = (byte) 0X00;
		byte bp = (byte) 0XFF;
		byte c = (byte) 0X12;
		byte cp = (byte) 0X34;
		byte d = (byte) 0X56;
		byte dp = (byte) 0X78;
		byte e = (byte) 0X9A;
		byte ep = (byte) 0XBC;
		byte h = (byte) 0XDE;
		byte hp = (byte) 0XF0;
		byte l = (byte) 0XAA;
		byte lp = (byte) 0X55;

		wrs.setReg(Register.B, bp);
		wrs.setReg(Register.C, cp);
		wrs.setReg(Register.D, dp);
		wrs.setReg(Register.E, ep);
		wrs.setReg(Register.H, hp);
		wrs.setReg(Register.L, lp);
		wrs.swapMainRegisters();
		wrs.setReg(Register.B, b);
		wrs.setReg(Register.C, c);
		wrs.setReg(Register.D, d);
		wrs.setReg(Register.E, e);
		wrs.setReg(Register.H, h);
		wrs.setReg(Register.L, l);

		assertThat("b swap All Registers", b, equalTo(wrs.getReg(Register.B)));
		assertThat("c swap All Registers", c, equalTo(wrs.getReg(Register.C)));
		assertThat("d swap All Registers", d, equalTo(wrs.getReg(Register.D)));
		assertThat("e swap All Registers", e, equalTo(wrs.getReg(Register.E)));
		assertThat("h swap All Registers", h, equalTo(wrs.getReg(Register.H)));
		assertThat("l swap All Registers", l, equalTo(wrs.getReg(Register.L)));

		wrs.swapMainRegisters();

		assertThat("bp swap All Registers", bp, equalTo(wrs.getReg(Register.B)));
		assertThat("cp swap All Registers", cp, equalTo(wrs.getReg(Register.C)));
		assertThat("dp swap All Registers", dp, equalTo(wrs.getReg(Register.D)));
		assertThat("ep swap All Registers", ep, equalTo(wrs.getReg(Register.E)));
		assertThat("hp swap All Registers", hp, equalTo(wrs.getReg(Register.H)));
		assertThat("lp swap All Registers", lp, equalTo(wrs.getReg(Register.L)));

		wrs.swapMainRegisters();

		assertThat("b1 swap All Registers", b, equalTo(wrs.getReg(Register.B)));
		assertThat("c1 swap All Registers", c, equalTo(wrs.getReg(Register.C)));
		assertThat("d1 swap All Registers", d, equalTo(wrs.getReg(Register.D)));
		assertThat("e1 swap All Registers", e, equalTo(wrs.getReg(Register.E)));
		assertThat("h1 swap All Registers", h, equalTo(wrs.getReg(Register.H)));
		assertThat("l1 swap All Registers", l, equalTo(wrs.getReg(Register.L)));

	}// testSwaps

}// class WorkingRegisterSetTest
