import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
public class Calculator {

	public String load(String expression) {
		return expressionToEvaluate = ExpressionManager.balance(expression);
	}

	public int evaluate() {
		return intCalc.evaluate(expressionToEvaluate);
	}

	public String[][] getOperations() {
		return intCalc.getOperations();
	}

	public static int getBalance(String expression) {
		return ExpressionManager.getBalance(expression);
	}

	private CalculatorEngine intCalc = new CalculatorEngine(intOperators);
	private String expressionToEvaluate;




	public static enum Notation { PREFIX, INFIX, POSTFIX }

	protected static final Map<String, Operator> intOperators;

	// initialize operator maps
	static {
		Map<String, Operator> tempIntMap = new HashMap<>();

		// operators and what they do below
		tempIntMap.put("!", new Operator(Operator.Associativity.RIGHT, Operator.Precedence.UNARY, Operator.Arity.UNARY,
			      (args) -> { return args[0] == 0 ? 1 : 0; },
		            "! NUM", "Takes the boolean NOT of NUM. Anything besides 0 is treated as TRUE (1), FALSE (0) otherwise."));
		tempIntMap.put("~", new Operator(Operator.Associativity.RIGHT, Operator.Precedence.UNARY, Operator.Arity.UNARY,
			      (args) -> { return ~args[0]; },
		            "~ NUM", "Takes the bitwise compliment of NUM."));
		tempIntMap.put("*", new Operator(Operator.Associativity.LEFT, Operator.Precedence.MULTIPLICATION, Operator.Arity.BINARY,
			      (args) -> { return args[0] * args[1]; },
		            "NUM1 * NUM2", "Multiplies NUM1 and NUM2."));
		tempIntMap.put("/", new Operator(Operator.Associativity.LEFT, Operator.Precedence.MULTIPLICATION, Operator.Arity.BINARY,
			      (args) -> { return args[0] / args[1]; },
		            "NUM1 / NUM2", "Integer divides NUM1 by NUM2 (i.e., drops the part after the decimal point)."));
		tempIntMap.put("%", new Operator(Operator.Associativity.LEFT, Operator.Precedence.MULTIPLICATION, Operator.Arity.BINARY,
			      (args) -> { return args[0] % args[1]; },
		            "NUM1 % NUM2", "Returns the remainder of NUM1 divided by NUM2."));
		tempIntMap.put("+", new Operator(Operator.Associativity.LEFT, Operator.Precedence.ADDITION, Operator.Arity.BINARY,
			      (args) -> { return args[0] + args[1]; },
		            "NUM1 + NUM2", "Adds NUM1 and NUM2."));
		tempIntMap.put("-", new Operator(Operator.Associativity.LEFT, Operator.Precedence.ADDITION, Operator.Arity.BINARY,
			      (args) -> { return args[0] - args[1]; },
		            "NUM1 - NUM2", "Subtracts NUM2 from NUM1."));
		tempIntMap.put("<<", new Operator(Operator.Associativity.LEFT, Operator.Precedence.BIT_SHIFT, Operator.Arity.BINARY,
			      (args) -> { return args[0] << args[1]; },
		            "NUM1 << NUM2", "Shifts NUM1 left NUM2 times (i.e., returns NUM1 * (2 ^ NUM2))."));
		tempIntMap.put(">>", new Operator(Operator.Associativity.LEFT, Operator.Precedence.BIT_SHIFT, Operator.Arity.BINARY,
			      (args) -> { return args[0] >> args[1]; },
		            "NUM1 >> NUM2", "Shifts NUM1 right NUM2 times (i.e., returns the integer part of NUM1 / (2 ^ NUM2))."));
		tempIntMap.put("<", new Operator(Operator.Associativity.LEFT, Operator.Precedence.COMPARISON, Operator.Arity.BINARY,
			      (args) -> { return args[0] < args[1] ? 1 : 0; },
		            "NUM1 < NUM2", "Returns 1 if NUM1 is less than NUM2, false otherwise."));
		tempIntMap.put(">", new Operator(Operator.Associativity.LEFT, Operator.Precedence.COMPARISON, Operator.Arity.BINARY,
			      (args) -> { return args[0] > args[1] ? 1 : 0; },
		            "NUM1 > NUM2", "Returns 1 if NUM1 is greater than NUM2, false otherwise."));
		tempIntMap.put("&", new Operator(Operator.Associativity.LEFT, Operator.Precedence.BIT_AND, Operator.Arity.BINARY,
			      (args) -> { return args[0] & args[1]; },
		            "NUM1 & NUM2", "Performs bitwise AND between NUM1 and NUM2."));
		tempIntMap.put("^", new Operator(Operator.Associativity.LEFT, Operator.Precedence.XOR, Operator.Arity.BINARY,
			      (args) -> { return args[0] ^ args[1]; },
		            "NUM1 ^ NUM2", "Performs bitwise XOR (exclusive OR) between NUM1 and NUM2."));
		tempIntMap.put("|", new Operator(Operator.Associativity.LEFT, Operator.Precedence.BIT_OR, Operator.Arity.BINARY,
			      (args) -> { return args[0] | args[1]; },
		            "NUM1 | NUM2", "Performs bitwise OR between NUM1 and NUM2."));
		tempIntMap.put("&&", new Operator(Operator.Associativity.LEFT, Operator.Precedence.LOGIC_AND, Operator.Arity.BINARY,
			      (args) -> { return (args[0] & args[1]) != 0 ? 1 : 0; },
		            "NUM1 && NUM2", "Performs boolean AND between NUM1 and NUM2. Anything besides 0 is treated as TRUE (1), FALSE (0) otherwise."));
		tempIntMap.put("||", new Operator(Operator.Associativity.LEFT, Operator.Precedence.LOGIC_OR, Operator.Arity.BINARY,
			      (args) -> { return (args[0] | args[1]) != 0 ? 1 : 0; },
		            "NUM1 || NUM2", "Performs boolean OR between NUM1 and NUM2. Anything besides 0 is treated as TRUE (1), FALSE (0) otherwise."));


		// set the static operator map
		intOperators = Collections.unmodifiableMap(tempIntMap);
	}
}