package hardware.centralProcessingUnit;

class FileFlag {
	public boolean sign, zero, halfCarry, parity, nFlag, carry;
	private byte source, result;

	public FileFlag(byte source, byte result) {
		this.source = source;
		this.result = result;
	}// Constructor

	public FileFlag(byte source, byte result, boolean sign, boolean zero, boolean halfCarry, boolean parity,
			boolean nFlag, boolean carry) {
		this.source = source;
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
	}// getSource
}// FileFlag