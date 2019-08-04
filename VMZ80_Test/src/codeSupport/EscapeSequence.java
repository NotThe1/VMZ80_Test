package codeSupport;

public class EscapeSequence {
	public byte[] sequence;
	public String description;
	public  EscapeSequence(byte[] sequence,String description) {
		this.sequence=sequence;
		this.description=description;
//		byte[] a = new  byte[]{(byte)0,(byte)1};
	}//Constructor
	
	public String toString() {
		return description;
	}//toString
}// class EscapeSequence
