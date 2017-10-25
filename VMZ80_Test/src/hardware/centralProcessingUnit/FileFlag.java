package hardware.centralProcessingUnit;

class FileFlag {
	public boolean sign, zero, halfCarry, parity, nFlag, carry;
	private byte source, result;
	private int arg1,arg2,resultInt;

	public FileFlag(int arg1,int arg2, int resultInt, boolean sign, boolean zero, boolean halfCarry, boolean parity,
			boolean nFlag, boolean carry) {
		this.arg1 = arg1 & 0X0FFFF;
		this.arg2 = arg2 & 0X0FFFF;
		this.resultInt = resultInt;
		this.sign = sign;
		this.zero = zero;
		this.halfCarry = halfCarry;
		this.parity = parity;
		this.nFlag = nFlag;
		this.carry = carry;
	}// Constructor
	
	public FileFlag(byte source, byte result, boolean sign, boolean zero, boolean halfCarry, boolean parity,
			boolean nFlag, boolean carry) {
		this.source = source;
//		this.arg2 = null;
		this.result = result;
		this.sign = sign;
		this.zero = zero;
		this.halfCarry = halfCarry;
		this.parity = parity;
		this.nFlag = nFlag;
		this.carry = carry;
	}// Constructor

	public byte getSource() {
		return this.source;
	}// getSource
	
	public byte getResult() {
		return this.result;
	}// getResult
	
	public int getArg1(){
		return this.arg1;
	}//getArg1
	
	public int getArg2(){
		return this.arg2;
	}//getArg2

	public int getResultInt() {
		return this.resultInt;
	}// getResultInt
}// FileFlag