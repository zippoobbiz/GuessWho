import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random guessing player. This player is for task B.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {

	/**
	 * Loads the game configuration from gameFilename, and also store the chosen
	 * person.
	 *
	 * @param gameFilename
	 *            Filename of game configuration.
	 * @param chosenName
	 *            Name of the chosen person for this player.
	 * @throws IOException
	 *             If there are IO issues with loading of gameFilename. Note you
	 *             can handle IOException within the constructor and remove the
	 *             "throws IOException" method specification, but make sure your
	 *             implementation exits gracefully if an IOException is thrown.
	 */

	// store the chosen person name
	private String chosenName;
	// store a list of attributes, each attribute contains a map<possible values
	// of this attribute, List<person has such attribute value>>
	private List<Attribute> attributes;
	// remained candidates
	private List<String> reminingCandidates;
	// remained possible guessing pairs
	private List<Pair> reminingPairs;

	// constructor
	public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException {
		this.chosenName = chosenName;
		// System.out.println("choose: " + chosenName);
		// initialize lists
		attributes = new ArrayList<Attribute>();
		reminingCandidates = new ArrayList<String>();
		// read file content from a certain path by FileHelper
		String content = FileHelper.readFile(gameFilename, Charset.defaultCharset());
		// split content into blocks
		String[] blocks = content.split("\n\n");
		// the first block has attributes list
		setAttributes(blocks[0].split("\n"));
		// split the rest of blocks
		for (int i = 1; i < blocks.length; i++) {
			addPeople(blocks[i].split("\n"));
		}
		// System.out.println(blocks.length);
	} // end of RandomGuessPlayer()

	// split the first block and store the attributes information
	public void setAttributes(String[] lines) {
		for (String line : lines) {
			String name = line.substring(0, line.indexOf(" "));
			String[] values = line.substring(line.indexOf(" ") + 1, line.length()).split(" ");
			attributes.add(new Attribute(name, values));
		}
	}

	// split the rest of blocks, add people to remaining candidates list
	// and set the valuePersonMap in each Attribute
	public void addPeople(String[] lines) {
		String name = lines[0].trim();
		reminingCandidates.add(name);
		for (int i = 1; i < lines.length; i++) {
			String[] words = lines[i].split(" ");
			for (Attribute a : attributes) {
				if (a.getAttributeName().equals(words[0])) {
					a.addPerson(name, words[1]);
					break;
				}
			}
		}
	}

	// print each attribute
	public void print() {
		attributes.forEach(a -> System.out.println(a));
	}

//	public static void main(String[] args) {
//		try {
//			RandomGuessPlayer player = new RandomGuessPlayer("/Users/xiaoduo/Desktop/sampleGameFiles/game1.config",
//					"/Users/xiaoduo/Desktop/sampleGameFiles/game1.chosen");
//			player.print();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public Guess guess() {
		// if there was only one candidates in the list then guess him/her
		if (reminingCandidates.size() == 1)
			return new Guess(Guess.GuessType.Person, "", reminingCandidates.get(0));

		// if there were more than one candidates, get the remaining pairs
		reminingPairs = new ArrayList<Pair>();
		for (Attribute a : attributes) {
			reminingPairs.addAll(a.remainingPair(reminingCandidates));
		}

		// pick a pair randomly
		Pair randomPair = reminingPairs.get(ThreadLocalRandom.current().nextInt(0, reminingPairs.size()));

		return new Guess(Guess.GuessType.Attribute, randomPair.getName(), randomPair.getValue());
	} // end of guess()

	public boolean answer(Guess currGuess) {

		// if the opponent guess a person
		if (currGuess.getType() == Guess.GuessType.Person) {
			// if the name equals to the chosenName return true, otherwise false
			if (currGuess.getValue().equals(chosenName)) {
				return true;
			} else {
				return false;
			}
		} else {
			// guess a attribute, traverse to find this attribute,
			// then using the check method in Class Attribute to decide if the
			// guessing was correct
			for (Attribute a : attributes) {
				if (a.getAttributeName().equals(currGuess.getAttribute())) {
					return a.check(chosenName, currGuess.getValue());
				}
			}
		}

		return false;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {
		// return the answer received
		if (currGuess.getType() == Guess.GuessType.Person) {
			return answer;
		} else {
			// get the guessed attribute
			Attribute guessedAttr = null;
			for (Attribute a : attributes) {
				if (currGuess.getAttribute().equals(a.getAttributeName()))
					guessedAttr = a;
			}

			// eliminate all the failed candidates
			if (guessedAttr != null) {
				ArrayList<String> eliminateList = guessedAttr.getEliminateList(currGuess.getValue(), answer);
				reminingCandidates.removeAll(eliminateList);
			}
			return false;
		}
	} // end of receiveAnswer()

} // end of class RandomGuessPlayer
