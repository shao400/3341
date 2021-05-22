import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {
	public static void main(String[] args) throws IOException {
		// Initialize the scanner with the input file
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter your file: ");
		 
		String input = reader.readLine();
		System.out.println("Your file is: " + input);
		
		ScannerX S = new ScannerX(input);
		//Scanner S = new Scanner(args[0]);
		//System.out.println(S.coreSize());
		//System.out.println(S.tokenSize());
		// Print the token stream
		//S.currentToken() != Core.EOF && S.currentToken() != Core.ERROR
		//int i = 0;
		while (S.currentToken() != Core.EOF && S.currentToken() != Core.ERROR) {
			// Pring the current token, with any extra data needed
			System.out.print(S.currentToken());
			if (S.currentToken() == Core.ID) {
				String value = S.getID();
				System.out.print("[" + value + "]");
			} else if (S.currentToken() == Core.CONST) {
				int value = S.getCONST();
				System.out.print("[" + value + "]");
			}
			System.out.print("\n");

			// Advance to the next token
			S.nextToken();
			//i++;
		}
	}
}