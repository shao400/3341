import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;

class ScannerX {
	
	private static final String SYMBOLS = ";(),=!<+-*"; //Set of symbols
	private static final String SEPARATORS = " \t\n\r"; //Set of separators
	private static final int CONST_ID = 31;
	private static final int IDENT_ID = 32;
	private static final ArrayList<String> Token_ID = new ArrayList<String>(Arrays.asList("program", 
			"begin","end", "new", "define", "extends", "class", "endclass", "int", "endfunc",
            "if", "then", "else", "while", "endwhile", "endif", ";", "(", ")", ",", "=", "!",
            "||", "==","<", "<=", "+", "-", "*", "input", "output", "const", "id", "EOF", "ERROR"));
	
	/*Tokens Arr List which we store all  the tokens*/
	private static ArrayList<String> tokens = new ArrayList<String>();
	/*Index of current token*/
	private int currentPos;
	/*Formal Core Arr List*/
	private static ArrayList<Core> coreTokens = new ArrayList<Core>();
	/*Map store constant*/
	private static Map<Integer, Integer> constMap = new HashMap<>();
	/*Map store id*/
	private static Map<Integer, String> idMap = new HashMap<>();
	
	// Constructor should open the file and find the first token
	ScannerX(String filename) throws IOException {
		Scanner br = new Scanner(new File(filename));	
		   generateTokens(br);      
			br.close();
			
			generateCores();
	       currentPos = 0;
	}
	
	public int coreSize() {
		return coreTokens.size();
	}
	
	public int tokenSize() {
			return tokens.size();
		}
	
	
	// generateToken should generate a ArrList of Tokens
		private static void generateTokens(Scanner in) throws IOException {
			/*Str Tokens*/
			while (in.hasNextLine()) {
	            String line = in.nextLine();
	            int pos = 0;
	            while (pos < line.length()) {
	                String token = nextWordOrSeparator(line, pos);
	                pos += token.length();
	                if (!SEPARATORS.contains(token.subSequence(0, 1))) {
	                	if(containsSymbol(token)) {
	                		tokenOperation(token);
	                	}
	                	else{tokens.add(token);}
	                }
	            }
	        }
	        tokens.add("EOF");
	        
		}
		
		
		
		/*generateCores should create core Arr list*/
		private static void generateCores() {
			  /*Core Tokens*/
	        int k = 0;
	        while(k<tokens.size()) {
	        	int pos = Token_ID.indexOf(tokens.get(k));
	        	if(isConst(tokens.get(k))) {//Is Const
	        		coreTokens.add(Core.values()[CONST_ID]);
	        		constMap.put(k,Integer.valueOf(tokens.get(k)));
	        	}else if (isIdent(tokens.get(k))) {//Is Ident
	        		coreTokens.add(Core.values()[IDENT_ID]);
	        		idMap.put(k,tokens.get(k));
	        	}else if(pos<Core.values().length && pos>=0){
	        	coreTokens.add(Core.values()[pos]);
	        	}else {
	        		//ERROR
	        		coreTokens.add(Core.values()[34]);
	        	}
	        	k++;
	        }
		}
		
		
		/*Judge whether a token string is digit*/
		private static boolean isConst(String token) {
			boolean result = true;
			
				for(int i =0;i<token.length();i++) {
			if(!Character.isDigit(token.charAt(i)))result = false;
				}
			
			return result;
		}	
		
		/*Judge whether a token string is ident*/
		private static boolean isIdent(String token) {
			boolean result = true;
			if(Token_ID.contains(token)) {
				result = false;
			}else {
				for(int i =0;i<token.length();i++) {
			if(!Character.isLetterOrDigit(token.charAt(i)))result = false;
				}
			}
			return result;
		}	
	

	/*Judge whether a token string contains symbols*/
	private static boolean containsSymbol(String op) {
		for(int i=0; i<op.length();i++) {
			if (!SYMBOLS.contains(op.subSequence(i, i+1))) {
				return true;
			}
		}
		return false;     
	}
	
	/* Token a String containing symbol operations to legal tokens and insert to tokens * 
	 * in order, *
	 * Requirement: the String contains no whitespace */
	private static void tokenOperation(String op) {
		int i = 0;
		while(i<op.length()) {
        StringBuilder next = new StringBuilder("");
        if (SYMBOLS.contains(String.valueOf(op.charAt(i)))) { //It's a symbol
            if(i<op.length()-1) { //Has opportunity that two char symbols be the next token
            	next.append(op.charAt(i));
                i++;
            	if(SYMBOLS.contains(String.valueOf(op.charAt(i)))) { //Being Greedy
            		next.append(op.charAt(i));
	                i++;
            	}
            }else {
            	next.append(op.charAt(i));
                i++;
            }
        } else {											  //It's not a symbol
            while (i < op.length()
                    && !SYMBOLS.contains(String.valueOf(op.charAt(i)))) {
                next.append(op.charAt(i));
                i++;
            }
        }
        tokens.add(next.toString());
		}
	}
	
	

	// nextToken should advance the scanner to the next token
	public void nextToken() {
		if(currentPos<coreTokens.size())currentPos++;
	}

	// currentToken should return the current token
	public Core currentToken() {
		return coreTokens.get(currentPos);
	}

	// If the current token is ID, return the string value of the identifier
	// Otherwise, return value does not matter
	public String getID() {
		return idMap.get(currentPos);
	}

	// If the current token is CONST, return the numerical value of the constant
	// Otherwise, return value does not matter
	public int getCONST() {
		return constMap.get(currentPos);
	}
	
	/*Return the index of next legal token in String*/
	private static String nextWordOrSeparator(String s, int position) {
        assert s != null : "Violation of: text is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < s.length() : "Violation of: position < |text|";

        int i = position;
        StringBuilder next = new StringBuilder("");
        if (SEPARATORS.contains(String.valueOf(s.charAt(i)))) {//Is Seperator
            while (i < s.length()
                    && SEPARATORS.contains(String.valueOf(s.charAt(i)))) {
                next.append(s.charAt(i));
                i++;
            }
        } else if(SYMBOLS.contains(String.valueOf(s.charAt(i)))) { //Is Op
            while (i < s.length()
                    && !SEPARATORS.contains(String.valueOf(s.charAt(i)))
                    && SYMBOLS.contains(String.valueOf(s.charAt(i)))) {
                next.append(s.charAt(i));
                i++;
            }
        } else { //Is const or id or others
        	 while (i < s.length()
                     && !SEPARATORS.contains(String.valueOf(s.charAt(i)))
                     && !SYMBOLS.contains(String.valueOf(s.charAt(i)))) {
                 next.append(s.charAt(i));
                 i++;
             }
        }
        return next.toString();

    }
	

}