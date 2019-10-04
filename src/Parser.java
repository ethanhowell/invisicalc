import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;

class Parser {
	static int intBase = 10;
	static final int MAX_BASE = 36;
	static final char DECIMAL_SEPERATOR = '.';

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			String line = sc.nextLine();
			if (line.equals("base")) {
				System.out.print("Choose a base: ");
				intBase = Integer.valueOf(sc.nextLine());
			}
			else {
				System.out.println(parse(line).toString());
			}
		}
	}

	// NOTE: all digits > 9 (i.e., a, b, c ...) must be UPPERCASE
	// NOTE: all functions may only contain lowercase letters or an _
	static Deque<String> parse(String expression) {
		Deque<String> tokens = new ArrayDeque<>();
		StringBuilder currentToken = new StringBuilder();
		TokenType type = TokenType.NONE;
		boolean numberHasDecimal = false;

		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);

			if (isWhiteSpace(c)) {
				switch (type) {
					case NUMBER:
					case OPERATOR:
						tokens.add(currentToken.toString());
						currentToken = new StringBuilder();
						numberHasDecimal = false;
						break;
					case FUNCTION:
						while (isWhiteSpace(expression.charAt(++i)));
						if (isFunction(currentToken.toString())) {
							tokens.add(currentToken.toString());
							tokens.add("(");
							type = TokenType.NONE;
							currentToken = new StringBuilder();
						}
						else
							throw new IllegalArgumentException("Function \"" + currentToken.toString() + "\" must be followed by parantheses.");
				}
				type = TokenType.NONE;
			}
			else {
				switch (type) {
					case NUMBER:
						if (isDigit(c))
							currentToken.append(c);
						else if (c == DECIMAL_SEPERATOR) {
							if (numberHasDecimal) {
								throw new IllegalArgumentException("Illegal number \"" + currentToken.toString() + ".\".");
							}
							else {
								numberHasDecimal = true;
								currentToken.append(c);
							}
						}
						else {
							tokens.add(currentToken.toString());
							currentToken = new StringBuilder(Character.toString(c));
							numberHasDecimal = false;
							if (isDigit(c))
								type = TokenType.NUMBER;
							else if (c == DECIMAL_SEPERATOR) {
								type = TokenType.NUMBER;
								numberHasDecimal = true;
							}
							else if (isOperator(Character.toString(c)))
								type = TokenType.OPERATOR;
							else if (isFunctionCharacter(c))
								type = TokenType.FUNCTION;
							else
								throw new IllegalArgumentException("Unrecognized token '" + c + "'.");
						}
						break;

					case OPERATOR:
						if (isOperator(currentToken.toString() + c))
							currentToken.append(c);
						else {
							tokens.add(currentToken.toString());
							currentToken = new StringBuilder(Character.toString(c));
							if (isDigit(c))
								type = TokenType.NUMBER;
							else if (c == DECIMAL_SEPERATOR) {
								type = TokenType.NUMBER;
								numberHasDecimal = true;
							}
							else if (isOperator(Character.toString(c)))
								type = TokenType.OPERATOR;
							else if (isFunctionCharacter(c))
								type = TokenType.FUNCTION;
							else
								throw new IllegalArgumentException("Unrecognized token '" + c + "'.");
						}
						break;

					case FUNCTION:
						if (c != '(') {
							if (isFunctionCharacter(c))
								currentToken.append(c);
							else
								throw new IllegalArgumentException("Illegal token \'" + c + "\' in function name.");
						}
						else if (isFunction(currentToken.toString())) {
							tokens.add(currentToken.toString());
							tokens.add("(");
							type = TokenType.NONE;
							currentToken = new StringBuilder();
						}
						else
							throw new IllegalArgumentException("Unrecognized token \"" + currentToken.toString() + "\".");
						break;

					default:
						currentToken = new StringBuilder(Character.toString(c));
						if (isDigit(c)) {
							type = TokenType.NUMBER;
						}
						else if (c == DECIMAL_SEPERATOR) {
							type = TokenType.NUMBER;
							numberHasDecimal = true;
						}
						else if (isOperator(Character.toString(c))) {
							type = TokenType.OPERATOR;
						}
						else if (isFunctionCharacter(c)) {
							type = TokenType.FUNCTION;
						}
						else
							throw new IllegalArgumentException("Unrecognized token '" + c + "'.");
				}
			}
		}
		switch (type) {
			case NUMBER:
			case OPERATOR:
				tokens.add(currentToken.toString());
				break;
			case FUNCTION:
				throw new IllegalArgumentException("Function \"" + currentToken.toString() + "\" must be followed by parantheses.");
		}
		return tokens;
	}

	private enum TokenType { NUMBER, OPERATOR, FUNCTION, NONE }

	static boolean isFunction(String s) {
		return s.equals("sin");
	}

	static boolean isOperator(String s) {
		return s.equals("(") ||
		       s.equals(")") ||
		       s.equals("+") ||
		       s.equals("-") ||
		       s.equals("*") ||
		       s.equals("/");
	}

	static boolean isWhiteSpace(char c) {
		return c == ' ' || c == '\t';
	}

	static boolean isFunctionCharacter(char c) {
		return (c >= 'a' && c <= 'z' || c == '_');
	}

	static boolean isDigit(char c) {
		if (c >= '0' && c < '0' + ((intBase < 10) ? intBase : 10)) {
			return true;
		}
		else if (intBase > 10 && intBase <= MAX_BASE) {
			if (c >= 'A' && c < 'A' + intBase - 10)
				return true;
			else
				return false;
		}
		else
			return false;
	}
}