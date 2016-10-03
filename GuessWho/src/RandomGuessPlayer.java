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
	private String chosenName;
	private List<Attribute> attributes;
	private List<String> reminingCandidates;
	private List<Pair> reminingPairs;

	public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException {
		this.chosenName = chosenName;
		System.out.println("choose " + chosenName);
		attributes = new ArrayList<Attribute>();
		reminingCandidates = new ArrayList<String>();
		String content = FileHelper.readFile(gameFilename, Charset.defaultCharset());
		String[] blocks = content.split("\n\n");
		setAttributes(blocks[0].split("\n"));
		for (int i = 1; i < blocks.length; i++) {
			addPeople(blocks[i].split("\n"));
		}
		System.out.println(blocks.length);
	} // end of RandomGuessPlayer()

	public void setAttributes(String[] lines) {
		for (String line : lines) {
			String name = line.substring(0, line.indexOf(" "));
			String[] values = line.substring(line.indexOf(" ") + 1, line.length()).split(" ");
			attributes.add(new Attribute(name, values));
		}
	}

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

	public void print() {
		attributes.forEach(a -> System.out.println(a));
	}

	public static void main(String[] args) {
		try {
			RandomGuessPlayer player = new RandomGuessPlayer("/Users/xiaoduo/Desktop/sampleGameFiles/game1.config",
					"/Users/xiaoduo/Desktop/sampleGameFiles/game1.chosen");
			player.print();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Guess guess() {
		if (reminingCandidates.size() == 1)
			return new Guess(Guess.GuessType.Person, "", reminingCandidates.get(0));

		reminingPairs = new ArrayList<Pair>();
		for (Attribute a : attributes) {
			reminingPairs.addAll(a.remainingPair(reminingCandidates));
		}
		Pair randomPair = reminingPairs.get(ThreadLocalRandom.current().nextInt(0, reminingPairs.size()));

		return new Guess(Guess.GuessType.Attribute, randomPair.getName(), randomPair.getValue());
	} // end
		// of
		// guess()

	public boolean answer(Guess currGuess) {

		// placeholder, replace

		if (currGuess.getType() == Guess.GuessType.Person) {
			if (currGuess.getValue().equals(chosenName)) {
				return true;
			} else {
				return false;
			}
		} else {
			for (Attribute a : attributes) {
				if (a.getAttributeName().equals(currGuess.getAttribute())) {
					return a.check(chosenName, currGuess.getValue());
				}
			}
		}

		return false;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {
		if (currGuess.getType() == Guess.GuessType.Person) {
			return answer;
		} else {
			Attribute guessedAttr = null;
			for (Attribute a : attributes) {
				if (currGuess.getAttribute().equals(a.getAttributeName()))
					guessedAttr = a;
			}

			if (guessedAttr != null) {
				ArrayList<String> eliminateList = guessedAttr.getEliminateList(currGuess.getValue(), answer);
				reminingCandidates.removeAll(eliminateList);
			}
			return false;
		}
	} // end of receiveAnswer()

} // end of class RandomGuessPlayer
