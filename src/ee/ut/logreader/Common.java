package ee.ut.logreader;

import java.util.Scanner;

public class Common {
	 
	private static Scanner scan;

	public static String userInput(){
		scan = new Scanner(System.in);			 
		 String input = scan.nextLine();
		 return input;
		 
	}


}
