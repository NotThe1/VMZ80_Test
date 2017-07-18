package misc;

public class DAATestResult {
	public int arg1,arg2,addSum,daaSum;
	public boolean CY,HC,CY1,HC1;
	
	public DAATestResult(int arg1,int arg2,int rawSum,boolean CY,boolean HC,int daaSum,boolean CY1,boolean HC1){
		this.arg1 = arg1;
		this.arg2 =arg2;
		this.addSum =rawSum;
		this.daaSum = daaSum;
		
		this.CY=CY;
		this.HC =HC;
		this.CY1 =CY1;
		this.HC1 =HC1;
	}//Constructor
	
	public int getIntSum(){
		return this.arg1 + this.arg2;
	}//getIntSum
	
	public int getIntDiff(){
		return this.arg1 - this.arg2;
	}//getIntSum

}//DAATestResult
