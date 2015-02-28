import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Class: CIS 3360 Security in Computing
 * Due date: 11/15/2013 11:59 PM
 * Programming Assignment Two : CRC-16 calculator
 * 
 * @author Justin Holzman
 */
public class Holzman_Justin_CRC {

	static String fileName = "testinput.txt";	// Default to the input file name but this will not be a problem if your file has a different name.
	private static File usersFile;
	private final static String CRC_POLYNOMIAL = "10000100110001101";
	static Scanner in = new Scanner(System.in);

	/** Main Method */
	public static void main(String[] args) {
		// Start the crc calculation program. Calls CRC method.
		CRC();
	}
	
	/** CRC Method which displays the menu and performs action based on input from user*/
	public static void CRC(){
		String userChoice = "";

		do {
			// Menu
			printMenu();
			userChoice = in.next().toLowerCase();	// Read in input. Convert to lowerCase

			// (C)alculate CRC
			if (userChoice.equals("c"))
				CRCCalculation();

			// (V)erify CRC
			else if (userChoice.equals("v"))
				CRCVerification();

			// System Exit, user did not enter a C or V
			else {
				System.err.println("\nSorry you entered an invalid input. Program Terminated");
				System.exit(0);
			}
		} while (userChoice.equals("c") || userChoice.equals("v"));	// Loop while user enters a valid input
		in.close();
	}

	/** Displays menu to the user */
	public static void printMenu(){
		System.out.println("\nEnter 'C' for Calculate CRC or 'V' for Verify CRC.");
		System.out.println("(C)alculate CRC");
		System.out.println("(V)erify CRC");
	}
	
	/** Prints the header containing information on the HEX and CRC poly */
	public static void printHeader() {
		// Print out the input file
		System.out.println("\nThe input file (hex): " + getInputHexString());

		// Print out a binary input of the hex input.
		System.out.println("The input file (bin):\n");
		printBinaryRepresentation(hexToBinary(getInputHexString()));
		System.out.println("");

		// print out polynomial used
		System.out.print("\nThe polynomial that was used " + "(bin): ");
		printBinaryRepresentation(CRC_POLYNOMIAL);
		System.out.println("");
	}

	/** Returns a string representing the input
	 * @param getInputHexString
	 * @return <code>inputHexString</code> a string representing the input
	 */
	public static String getInputHexString() {
		String inputHexString = "";
		
		try {
			Scanner fileScan = new Scanner(usersFile);

			// Loop while the scanner can find strings in the input.
			while (fileScan.hasNext())
				inputHexString = inputHexString + fileScan.next();

			fileScan.close();

			// FileNotFoundException.
		} catch (FileNotFoundException e) {
			System.err.println("File was not Found.");
		}
		return inputHexString;
	}

	/** Returns a string representing the input
	 * @param binaryNumber
	 * 
	 */
	public static void printBinaryRepresentation(String binaryNumber) {
		// Replaces each substring of this string that matches the given regular expression with the given replacement. 
		binaryNumber = binaryNumber.replaceAll(".{32}", "$0");

		// Twice reverse to get proper output on bits. Space each four bits.
		binaryNumber = reverse(binaryNumber);
		binaryNumber = binaryNumber.replaceAll(".{4}", "$0 ");
		binaryNumber = reverse(binaryNumber);

		System.out.print(binaryNumber);

		// New line when binaryNumber.length() < 32
		if (binaryNumber.length() < 32)
			System.out.print("");
	}

	/** Returns the string reversed.
	 * @return <code>str</code> if binary string str.length() <= 1
	 * @return <code>reverse</code> (str.substring(1, str.length())) + str.charAt(0)
	 */
	public static String reverse(String str) {
		if (str.length() <= 1) {
			return str;
		}
		
		// Return the string reversed.
		return reverse(str.substring(1, str.length())) + str.charAt(0);
	}

	/** Convert hexadecimal to binary. Was using the big int method but was loosing leading zero's on the binary conversion.
	 * 
	 * @return <code>binary</code> the binary representation of the hex passed in
	 */
	public static String hexToBinary(String hex) {
		String binary = "";
	    String binaryStringF = "";
	    
	    int intHex;
	    hex = hex.trim();
	    hex = hex.replaceFirst("0x", "");

	    for(int i = 0; i < hex.length(); i++){
	    	// Parses the string argument as a signed integer in the radix specified by the second argument hexChar.
	        intHex = Integer.parseInt("" + hex.charAt(i),16);
	        binaryStringF = Integer.toBinaryString(intHex);	// Place into binaryString

	        // Loss of zero's on conversion. Add them if needed. 
	        while(binaryStringF.length() < 4){
	            binaryStringF = "0" + binaryStringF;
	        }
	        binary += binaryStringF;
	    }
	    return binary;
	}
	
	/** Convert binary string to hexadecimal using big int.
	 * @return <code>bigInt</code> the bigInt representation of the binaryNumber passed in. Returns the hex value 
	 */
	public static String binaryToHex(String binaryNum) {
		BigInteger bigInt = new BigInteger(binaryNum, 2);
		return bigInt.toString(16).toUpperCase();
	}

	/** File validation class
	 * 
	 * @return <code>true</code> if all test cases pass. The file is only hex
	 * @return <code>false</code> if number does not fall within the askii values of 0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,A,B,C,D,E,F 
	 */
	public static boolean fileValidation(File input) {
		try {
			Scanner hexScan = new Scanner(input);

			// Sets this scanner's delimiting pattern to a pattern constructed from the specified String. 
			// This insures we are returning one character at a time.
			hexScan.useDelimiter("");

			while (hexScan.hasNext() == true) {
				// Convert the next string to a char. Make all Upper case so we don't have to deal with both lower and Upper case characters.
				char currentHexChar = hexScan.next().toUpperCase().toCharArray()[0];
				// Check if the char is within the valid ascii range of Hex characters
				if (currentHexChar < '0') {
					hexScan.close();
					return false;
				} else if (currentHexChar > 'F') {
					hexScan.close();
					return false;
				} else if (currentHexChar > '9' && currentHexChar < 'A') {
					hexScan.close();
					return false;
				}
			}

			hexScan.close();

			// Return true is every char is within Hex validation.
			return true;

			// FileNotFoundException.
		} catch (FileNotFoundException e) {
			System.out.println("The file was not Found. ");
			return false;
		}
	}

	/** Does an XOR on two binary strings
	 * 
	 * @param binaryStringOne
	 * @param binaryStringTwo
	 * @return <code>output</code> this is the String after XOR takes place
	 */
	public static String XOR(String binaryStringOne, String binaryStringTwo) {
		// Start based on the smallest number
		int minimumSize = Math.min(binaryStringOne.length(), binaryStringTwo.length());
		String output = "";

		// For each character in the string, do a bitwise XOR
		for (int i = 0; i < minimumSize; i++){
			output = output + (binaryStringOne.charAt(i) ^ binaryStringTwo.charAt(i));
		}

		return output;
	}
	
	/** Get the file name from the user */
	public static void getFileName(){
		System.out.println("\nPlease enter the name for the file to be examined. (Example. testinput.txt)");
		fileName = in.next();
		//pathname = "testinput.txt";
		File tempFile = new File(fileName);

		// Check to see if file is there
		if (tempFile.exists() == false) {
			System.err.printf("%s was not found. Invalid name for the file. Try testinput.txt next time. ", fileName);
			System.exit(0);
		} else {
			// Found file. Set to userFile
			usersFile = tempFile;

			// validateFile to check for hex characters ONLY.
			if (fileValidation(usersFile) == false){
				System.out.println("Input is not in hexadecimal format.");
			}
		}
	}
	
	/** CRC calculation method */
	public static void CRCCalculation() {
		getFileName();	// Call method getFileName to read in the hex

		printHeader();
		
		System.out.println("We will append sixteen zeros at the end of the binary input.\n");
		String inputString = hexToBinary(getInputHexString());	// convert Hex to Binary
		inputString = inputString + "0000000000000000";	// Add the zero's to the inputString
		System.out.println("The binary string answer at each XOR step of CRC calculation:\n");
		printBinaryRepresentation(inputString);	// Print the grid to the screen
		String printString = inputString; // Print

		// print while i is less than size of inPutString
		for (int i = 0; i < inputString.length(); i++) {
			if (printString.charAt(i) == '0')
				continue;
			if ((CRC_POLYNOMIAL.length() + i + 1) > printString.length()) {
				System.out.println();
				
				Scanner hexScan;
				
				try {
					hexScan = new Scanner(usersFile);
					char number = hexScan.next().toUpperCase().toCharArray()[0];	// Place each character read into a char value. Read in as string.
					
					// Equations which hex value's most significant bit being a zero (with 0-6), their remainders end in 0 so do not print. 
					// Otherwise the remainder ends with 1 with values of 8-F where their most significant bit is a one.
					if(number != '0' && number != '1' && number != '2' && number != '3' && number != '4' && number != '5' && number != '6'){
						printString = printString.substring(0, i)+ XOR(printString.substring(i), CRC_POLYNOMIAL);
					}
					hexScan.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				printBinaryRepresentation(printString);
				break;
			}

			System.out.println();
			
			// PrintString what we've already done and add what was xor'd
			printString = printString.substring(0, i) + XOR(printString.substring(i,(CRC_POLYNOMIAL.length() + i)), CRC_POLYNOMIAL) + inputString.substring((CRC_POLYNOMIAL.length() + i));
			printBinaryRepresentation(printString);
		}

		// Twice reverse the string to rid of the checksum attached.
		printString = reverse(printString);
		printString = printString.substring(0, 16);
		printString = reverse(printString);

		System.out.print("\n\nThe computed CRC for this file is ");
		printBinaryRepresentation(printString);
		System.out.println("(bin) = " + binaryToHex(printString) + " (hex)");
		String input = getInputHexString();

		// Write the CRC remainder to the file.
		try {
			BufferedWriter userFileWrite = new BufferedWriter(new FileWriter(usersFile));
			userFileWrite.write(input + binaryToHex(printString));
			userFileWrite.close();
		} catch (IOException e) {	// Catch IOException
			System.err.println("IOException Error");
		}
		return;
	}
	
	/** CRC verification method */
	public static void CRCVerification() {
		
		getFileName();
		
		// Save the input to a string input.
		String input = getInputHexString();

		printHeader();

		if (input.length() < 4) {
			System.out.println("Error: input too short. Program Terminated. ");
			System.exit(1);
		}

		// Twice reverse to get the CRC
		String crc = reverse(input);
		crc = crc.substring(0, 4);
		crc = reverse(crc);

		// Print out the CRC
		System.out.print("The 16-bit CRC observed at the end of the file: " + crc + " (hex) = ");
		printBinaryRepresentation(hexToBinary(crc));

		System.out.println("\n\nThe binary string answer at each XOR step of CRC verification:\n");
		
		// Print out the input in binary
		printBinaryRepresentation(hexToBinary(input));

		String binaryInput = hexToBinary(input);
		String printString = binaryInput;

		// Print the chart showing each step
		for (int i = 0; i < binaryInput.length(); i++) {
			// Do not work on substrings that start with Zero
			if (printString.charAt(i) == '0')
				continue;

			// Prevents the error StringOutOfBounds
			if ((CRC_POLYNOMIAL.length() + i) > printString.length()) {
				System.out.println();
				// PrintString what we've already done and add what was xor'd
				printString = printString.substring(0, i) + XOR(printString.substring(i), CRC_POLYNOMIAL);
				printBinaryRepresentation(printString);

				break;
			}
			System.out.println();
			// PrintString what we've already done and add what was xor'd
			printString = printString.substring(0, i) + XOR(printString.substring(i,(CRC_POLYNOMIAL.length() + i)), CRC_POLYNOMIAL) + binaryInput.substring((CRC_POLYNOMIAL.length() + i));
			printBinaryRepresentation(printString);
		}

		// True == Pass ... False == Fail
		boolean checkCRCPassOrFail = true;

		// If there is a one within the final line of string, the CRC check did not pass the test.
		for (int i = 0; i < printString.length(); i++) {
			if (printString.charAt(i) == '1')
				checkCRCPassOrFail = false;	// Found a one in the last line. CRC Fail.
		}

		// Print if the CRC check Passed or Failed
		System.out.print("\n\nDid the CRC check pass? (Yes or No): ");
		if (checkCRCPassOrFail == true)
			System.out.println("Yes");	// Passed
		else
			System.out.println("No");	// Failed
	}
}