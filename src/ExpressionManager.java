import java.util.HashMap;
import java.util.Map;

class ExpressionManager {
	public static String convertNotation(String expression, Map<String, Operator> operators, Calculator.Notation notation) {
		switch (notation) {
			case PREFIX:
				return prefixToPostfix(expression, operators);
			case INFIX:
				return infixToPostfix(expression, operators);
			case POSTFIX:
				return expression;
		}
		throw new IllegalArgumentException("Invalid notation.");
	}

	private static String prefixToPostfix(String prefixExpression, Map<String, Operator> operators) {
		// TODO
		return "";
	}

	private static String infixToPostfix(String infixExpression, Map<String, Operator> operators) {
		// TODO
		return "";
	}

	public static String balance(String infixExpression) {
		int balance = getBalance(infixExpression);
		if (balance == 0) {
			return infixExpression;
		}
		else if (balance > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < balance; i++)
				sb.append("( ");
			sb.append(infixExpression);
			return sb.toString();
		}
		else {
			StringBuilder sb = new StringBuilder(infixExpression);
			for (int i = 0; i > balance; i--)
				sb.append(" )");
			return sb.toString();
		}
	}

	public static int getBalance(String infixExpression) {
		int balance = 0;

		for (int i = 0, length = infixExpression.length(); i < length; i++) {
			char c = infixExpression.charAt(i);
			if (c == '(') {
				balance--;
			}
			else if (c == ')') {
				balance++;
			}
			// else: skips over any non grouping characters
		}
		return balance;
	}
}