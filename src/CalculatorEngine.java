// NOTES: Eventually this class will just calculate postfix. There will be other classes to balance the parantheses and
// take care of the conversion to postfix. In addition, it will be a class that is passed in it's constructor an array of
// operators and use templates for all methods. The methods will be passed a class (Integer, Double, etc.) that determines 
// what is evaluated.

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Map;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class CalculatorEngine {

	public CalculatorEngine(Map<String, Operator> ops) {
		operators = ops;
	}

	// TODO: change this:
	private static final int intBase = 10;
	private static final int MAX_BASE = 36;
	private static final char DECIMAL_SEPERATOR = '.';

	public int getAnswer() { return previousAnswer; }

	public String[][] getOperations() {
		String[][] ops = new String[operators.size()][3];
		int i = 0;
		for (String s : operators.keySet()) {
			Operator o = operators.get(s);
			ops[i][0] = s;
			ops[i][1] = o.getExample();
			ops[i][2] = o.getExplanation();
			i++;
		}

		return ops;
	}

	public int evaluate(String infixExpression) {
		buildPostfixQueue(infixExpression);
		previousAnswer = evaluatePostfix();
		hasPreviousAnswer = true;
		return getAnswer();
	}

	// Based on Shunting Yard Algorithm, see https://en.wikipedia.org/wiki/Shunting-yard_algorithm#The_algorithm_in_detail
	private void buildPostfixQueue(String infixExpression) {
		postfixQueue.clear();
		operatorStack.clear();

		boolean isNumber = false;
		boolean isRightParan = false;

		boolean needNumber = true;

		buildTokenDeque(infixExpression);
		while (!tokenDeque.isEmpty()) {
			// Read a token.
			String token = tokenDeque.remove();

			// detect if previous token was a number or right parantheses for implicit multiplication
			boolean lastWasNumber = isNumber;
			boolean lastWasRightParan = isRightParan;

			// clear flags
			isRightParan = false;
			isNumber = true;

			try {
				Integer.parseInt(token);
			}
			catch (NumberFormatException ex) {
				isNumber = false;
			}

			// use previous answer variable
			if (token.equals("ANS")) {
				if (hasPreviousAnswer) {
					isNumber = true;
					token = Integer.toString(previousAnswer);
				}
				else
					throw new IllegalArgumentException("ANS is not defined.");
			}

			if (isNumber) {
				// for implicit multiplication
				if (lastWasRightParan) {
					tokenDeque.addFirst(token);
					token = "*";
				}
				else {
					postfixQueue.add(token);
					needNumber = false;

					// next token to process
					continue;
				}
			}

			if (token.equals("(") || isFunction(token)) {
				// if number was previous, then do some implicit multiplication
				if (lastWasNumber || lastWasRightParan) {
					tokenDeque.addFirst(token);
					token = "*";
					// no continue b/c we want to go to next line
				}
				else {
					operatorStack.push(token);

					// next token to process
					continue;
				}
			}

			Operator op;
			if ((op = operators.get(token)) != null) {
				if (op.getArity() == Operator.Arity.BINARY) {
					if (needNumber)
						throw new IllegalArgumentException("Error on operator " + token + ".");
					needNumber = true;
				}
				else if (op.getArity() == Operator.Arity.UNARY) {
					if (op.getAssociativity() == Operator.Associativity.RIGHT)
						needNumber = true;

					else if (op.getAssociativity() == Operator.Associativity.LEFT && needNumber)
						throw new IllegalArgumentException("Error on operator " + token + ".");
				}

				Operator op2;
				while (!operatorStack.isEmpty() && (op2 = operators.get(operatorStack.element())) != null) {
					if (op.getAssociativity() == Operator.Associativity.LEFT && op.getPrecedence().compareTo(op2.getPrecedence()) <= 0 ||
						op.getAssociativity() == Operator.Associativity.RIGHT && op.getPrecedence().compareTo(op2.getPrecedence()) < 0) {
						postfixQueue.add(operatorStack.pop());
					}
					else break;
				}

				operatorStack.push(token);

				// next token to process
				continue;
			}

			if ((isRightParan = token.equals(")")) || token.equals(",")) {
				try {
					String tempOperator;
					while (!(tempOperator = operatorStack.pop()).equals("(")) {
						postfixQueue.add(tempOperator);
					}
				}
				catch (NoSuchElementException ex) {
					throw new IllegalArgumentException("Expression has too many right parentheses.");
				}

				// next token to process
				continue;
			}

			else
				throw new IllegalArgumentException("Unrecognized symbol \'" + token + "\'");
		}

		if (needNumber)
			throw new IllegalArgumentException("Mismatched operands and operators.");

		while (!operatorStack.isEmpty()) {
			if (operatorStack.element().equals("("))
				throw new IllegalArgumentException("Expression has too many left parentheses.");
			postfixQueue.add(operatorStack.pop());
		}
	}

	// TODO: make the following method implement some functions
	private int evaluatePostfix() {
		String tempString;
		resultStack.clear();

		while (!postfixQueue.isEmpty()) {
			tempString = postfixQueue.remove();
			Operator op = operators.get(tempString);
			if (op != null) {
				int[] operands;
				switch (op.getArity()) {
					case UNARY:
						if (resultStack.size() < 1) throw new IllegalArgumentException("Not enough operands.");
						operands = new int[1];
						operands[0] = resultStack.pop();
						break;

					case BINARY:
						if (resultStack.size() < 2) throw new IllegalArgumentException("Not enough operands.");
						operands = new int[2];
						operands[1] = resultStack.pop();
						operands[0] = resultStack.pop();
						break;

					default:
						throw new IllegalArgumentException("Operator is undefined.");
				}

				try {
					resultStack.push(doMath(operands, op));
				}
				catch (ArithmeticException ex) {
					throw new IllegalArgumentException("Division by zero not supported.");
				}
			}
			else {
				try {
					resultStack.push(Integer.valueOf(tempString));
				}
				catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Mismatched operands.");
				}
			}
		}

		if (resultStack.isEmpty() || resultStack.size() > 1)
			throw new IllegalArgumentException("Too many operands.");

		return resultStack.element();
	}


	private void buildTokenDeque(String expression) {
		tokenDeque.clear();
		StringBuilder currentToken = new StringBuilder();
		TokenType type = TokenType.NONE;
		boolean numberHasDecimal = false;

		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);

			if (isWhiteSpace(c)) {
				switch (type) {
					case NUMBER:
					case OPERATOR:
						tokenDeque.add(currentToken.toString());
						currentToken = new StringBuilder();
						numberHasDecimal = false;
						break;
					case FUNCTION:
						while (isWhiteSpace(expression.charAt(++i)));
						if (isFunction(currentToken.toString())) {
							tokenDeque.add(currentToken.toString());
							tokenDeque.add("(");
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
							tokenDeque.add(currentToken.toString());
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
							tokenDeque.add(currentToken.toString());
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
							tokenDeque.add(currentToken.toString());
							tokenDeque.add("(");
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
				tokenDeque.add(currentToken.toString());
				break;
			case FUNCTION:
				throw new IllegalArgumentException("Function \"" + currentToken.toString() + "\" must be followed by parantheses.");
		}
	}

	private enum TokenType { NUMBER, OPERATOR, FUNCTION, NONE }

	boolean isOperator(String token) {
		return token.equals("(") ||
		       token.equals(")") ||
		       operators.get(token) != null;
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


	// ============================== OBJECT FIELDS =============================== //
	private int previousAnswer;
	private boolean hasPreviousAnswer = false;

	private Deque<String> tokenDeque = new ArrayDeque<>();
	private Queue<String> postfixQueue = new ArrayDeque<>();
	private Deque<Integer> resultStack = new ArrayDeque<>();
	private Deque<String> operatorStack = new ArrayDeque<>();

	private final Map<String, Operator> operators;



	// ============================== STATIC METHODS ============================== //
	private static int doMath(int[] operands, Operator operator) {
		return operator.operate(operands);
	}
	
	// TODO: add function support
	private static boolean isFunction(String token) {
		return false;
	}


	// ============================== STATIC FIELDS =============================== //

}