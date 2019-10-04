public class Operator {

	Operator(Associativity as, Precedence pr, Arity ar, Operation op, String ex, String exp) {
		associativity = as;
		precedence = pr;
		arity = ar;

		operation = op;

		example = ex;
		explanation = exp;
	}

	public Associativity getAssociativity() { return associativity; }
	public Precedence getPrecedence() { return precedence; }
	public Arity getArity() { return arity; }
	public String getExample() { return example; }
	public String getExplanation() { return explanation; }

	public int operate(int[] args) { return operation.run(args); }

	private final Operation operation;
	private final Associativity associativity;
	private final Precedence precedence;
	private final Arity arity;
	private final String example;
	private final String explanation;


	public static enum Associativity { LEFT, RIGHT }

	// note: for this enum, order matters (is compared by ordinality)
	// Based on C89 precedence chart
	public static enum Precedence { LOGIC_OR,
									LOGIC_AND,
									BIT_OR,
									XOR,
									BIT_AND,
									COMPARISON,
									BIT_SHIFT,
									ADDITION,
									MULTIPLICATION,
									UNARY }

	public static enum Arity { UNARY, BINARY }
}
