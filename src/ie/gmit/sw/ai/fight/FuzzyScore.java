package ie.gmit.sw.ai.fight;

import net.sourceforge.jFuzzyLogic.*;
import net.sourceforge.jFuzzyLogic.rule.*;

/**
 * Uses the JFuzzyLogic library to generate fight scores.
 * Takes statistics as input and generates an output.
 * The output score is used as damage to the opposing character.
 * 
 * @author Ronan
 */
public class FuzzyScore {
	
	/**
	 * Test class to check that different values make sense.
	 */
	public void test() {
		// low
		getCharScore(0, 0, 0);
		getCharScore(0, 5, 5);
		getCharScore(5, 0, 5);
		getCharScore(5, 5, 0);

		// medium
		getCharScore(5, 10, 15);

		// high
		getCharScore(10, 20, 20);
		getCharScore(20, 20, 20);
	}

	public int getCharScore(int charLuckScore, int charHealthScore, int charWeaponScore) {
		// Load from 'FCL' file
		String fileName = "fcl/scorer.fcl";
		FIS fis = FIS.load(fileName, true);

		// Error while loading?
		if (fis == null) {
			System.err.println("Can't load file: '" + fileName + "'");
		}

		// fuzzy inference system (can't program it in this library, jfuzzylite
		// lets you program/specify it). It's not that important anyway.
		fis.evaluate();
		FunctionBlock functionBlock = fis.getFunctionBlock("scorer");

		// Show
		// JFuzzyChart.get().chart(functionBlock);

		// Set inputs
		fis.setVariable("luck", charLuckScore);
		fis.setVariable("health", charHealthScore);
		fis.setVariable("weapon", charWeaponScore);

		// Evaluate
		fis.evaluate(); // gets input, fuzzifies it, evaluates, spits out crisp
						// value

		// Show output variable's chart
		Variable score = functionBlock.getVariable("score");
		// System.out.println("score getValue: " + score.getValue());

		// print chart
//		JFuzzyChart.get().chart(score, score.getDefuzzifier(), true);

		// Print ruleSet
		// System.out.println(fis);

//		System.out.println("fuzzy score value: " + score.getValue());

		return (int) Math.round(score.getValue());
	}
}
