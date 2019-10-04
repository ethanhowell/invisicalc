import java.util.*;

public class UserInterface {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Calculator calc = new Calculator();
		String input;

		System.out.println("INVISICALC by Ethan J. Howell, version 1.1, June 2017.");
		System.out.println("Type \"help\" for some help.");

		programLoop: do {
			System.out.printf("%nλ> ");
			input = s.nextLine();
			switch (input) {
				case "q":
				case "quit":
					break programLoop;

				case "base":
					System.out.println("Sorry, this option is under construction.");
					break;

				case "ops":
					String[][] table = calc.getOperations();
					for (String[] arr : table)
						System.out.println(Arrays.toString(arr));
					break;

				case "help":
					System.out.println("Available options: \"quit\" or \"q\", \"base\" (change base), \"ops\" (show list of operations), or \"help\" (show this).");
					System.out.println("Use \"ANS\" as a placeholder for the previous answer (EX: \"5 + ANS\").");
					break;

				default:
					String readyToEval = calc.load(input);
					if (!readyToEval.equals(input)) {
						System.out.printf("λ> %s%n", readyToEval);
					}
					try {
						System.out.printf(" = %s%n", calc.evaluate());
					}
					catch (IllegalArgumentException ex) {
						System.out.printf("%s%n", "Syntax Error: " + ex.getMessage());
					}
			}

		} while (true);
	}

}
