import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
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
			//If first token is const, set it to error
			//if(coreTokens.get(0).equals(Core.values()[CONST_ID]))coreTokens.set(0, Core.values()[34]);
	       currentPos = 0;
	}
	
	public int coreSize() {
		return coreTokens.size();
	}
	
	public int tokenSize() {
			return tokens.size();
		}
	
	public void printTokens() {
		for(int i =0;i<tokens.size();i++) {
			System.out.println(tokens.get(i));
		}
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
	                //System.out.println(token);
	                if(token.length()==0) {
						System.out.println("Exit, Empty Token!");
						System.exit(0);
					}
	                //First Check not seperators
	                if(!SEPARATORS.contains(token.subSequence(0, 1))) {
	                //Start with symbols, might contain multiple symbols
	                if (SYMBOLS.contains(token.subSequence(0, 1))) {
	                	
	                	if(allSymbol(token)) {
	                		
	                		int pos2 = 0;
	                		while(pos2<token.length()) {
	                			String token2 = tokenOperation(token, pos2);
	                			tokens.add(token2);
	                			pos2+=token2.length();
	                		}
	                		
	                	}
	                	
	                }else {//Others
	                	tokens.add(token);
	                }
	            }
	                
	            }
	        }
	        tokens.add("EOF");
	        
		}
		
		/* Token a String containing all symbol operations to legal tokens and insert to tokens * 
		 * in order, *
		 * Requirement: the String contains only symbols */
		private static String tokenOperation(String op, int pos) {
			for(int k=0;k<op.length();k++) {
				if(!SYMBOLS.contains(op.subSequence(k, k+1)) ||op.length()<=0) {
					System.out.println("Exit, since op str does not meet func pre");
					System.exit(0);
				}
			}
			
			StringBuilder next = new StringBuilder("");
			
			int i = pos;
			if(i<op.length()) {
	       
	            if(i<op.length()-1) { //Has opportunity that two char symbols be the next token
	            	next.append(op.charAt(i));
	                i++;               
	                next.append(op.charAt(i));
	            	if(!Token_ID.contains(next.toString())) {//Delete last char
	            		next.deleteCharAt(next.length()-1);
	            	}else {////Being Greedy
	            		i++;
	            	}
	            }else if(i==op.length()-1) { //Left a single char symbol
	            	next.append(op.charAt(i));
	                //i++;
	            }     
	        //tokens.add(next.toString());
			}
			return next.toString();
		}
		
		
		
		/*generateCores should create core Arr list*/
		private static void generateCores() {
			  /*Core Tokens*/
	        int k = 0;
	        while(k<tokens.size()) {
	        	int pos = Token_ID.indexOf(tokens.get(k));
	        	if(isConst(tokens.get(k))) {//Is Const
	        		coreTokens.add(Core.values()[CONST_ID]);
	        		BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
	        		BigInteger value = new BigInteger(tokens.get(k));

	        		if (value.compareTo(maxInt) < 0)
	        		{
	        			//Smaller than MAX.INT
	        			constMap.put(k,Integer.valueOf(tokens.get(k)));
	        		}else {
	        			constMap.put(k,Integer.MAX_VALUE);
	        			System.out.println("Error: Const Out of Int Bound");
	        		}
	        		
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
	

	/*Judge whether a token string is all symbols*/
	private static boolean allSymbol(String op) {
		boolean re = true;
		for(int i=0; i<op.length();i++) {
			if (!SYMBOLS.contains(op.subSequence(i, i+1))) {
				re =  false;
			}
		}
		return re;     
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
            //System.out.println(next.toString());
            return next.toString();
        } else if(SYMBOLS.contains(String.valueOf(s.charAt(i)))) { //Is Op
            while (i < s.length()
                    && !SEPARATORS.contains(String.valueOf(s.charAt(i)))
                    && SYMBOLS.contains(String.valueOf(s.charAt(i)))) {
                next.append(s.charAt(i));
                i++;
            }
            //System.out.println(next.toString());
            return next.toString();
        } else { //Is const or id or others
        	 while (i < s.length()
                     && !SEPARATORS.contains(String.valueOf(s.charAt(i)))
                     && !SYMBOLS.contains(String.valueOf(s.charAt(i)))) {
                 next.append(s.charAt(i));
                 i++;
             }
        	 //System.out.println(next.toString());
        	 return next.toString();
        }

    }
	

}