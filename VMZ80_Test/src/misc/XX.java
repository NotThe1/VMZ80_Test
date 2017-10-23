package misc;

import java.io.InputStream;
import java.util.Scanner;

public class XX {

	public static void main(String[] args) {
		new XX().checkFile();

	}//main
	
	public  void checkFile(){
		String source,result1,flags1,result2,flags2;
	
		InputStream inputStream = this.getClass().getResourceAsStream("/ShiftOriginal.txt");
		// InputStream inputStream = this.getClass().getResourceAsStream("/daaTemp.txt");
		Scanner scanner = new Scanner(inputStream);
		scanner.nextLine(); // skip header
		while (scanner.hasNextLine()) {
			source = scanner.next();
			if (source.startsWith(";")) {
				scanner.nextLine();
				continue;
			} // if skip line
			
			System.out.printf("source = %s%n", source);
			//SLA
			result1 = scanner.next();
			flags1 = scanner.next();
			result2 = scanner.next();
			flags2 = scanner.next();
			if(!result1.equals(result2)){
				System.out.printf("SLA: source = %s, result1 = %s, result2 = %s%n",source,result1,result2);
			}//if
			if (!flags1.equals(flags2)){
				System.out.printf("SLA: source = %s, flags1 = %s, flags2 = %s%n",source,flags1,flags2);
			}//if
			
			//SRA
			result1 = scanner.next();
			flags1 = scanner.next();
			result2 = scanner.next();
			flags2 = scanner.next();
			if(!result1.equals(result2)){
				System.out.printf("SRA: source = %s, result1 = %s, result2 = %s%n",source,result1,result2);
			}//if
			if (!flags1.equals(flags2)){
				System.out.printf("SRA: source = %s, flags1 = %s, flags2 = %s%n",source,flags1,flags2);
			}//if
			
			//SRL
			result1 = scanner.next();
			flags1 = scanner.next();
			result2 = scanner.next();
			flags2 = scanner.next();
			if(!result1.equals(result2)){
				System.out.printf("SRL: source = %s, result1 = %s, result2 = %s%n",source,result1,result2);
			}//if
			if (!flags1.equals(flags2)){
				System.out.printf("SRL: source = %s, flags1 = %s, flags2 = %s%n",source,flags1,flags2);
			}//if
			
			
		} // while
		scanner.close();
	}//checkFile

}//class XX
