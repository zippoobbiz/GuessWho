import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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

	public RandomGuessPlayer(String gameFilename, String chosenName)
			throws IOException {
		this.chosenName = chosenName;
		attributes = new ArrayList<Attribute>();
		String content = FileHelper.readFile(gameFilename,
				Charset.defaultCharset());
		String[] blocks = content.split("\n\n");
		setAttributes(blocks[0].split("\n"));
//		System.out.println(blocks.length);
	} // end of RandomGuessPlayer()

	public void setAttributes(String[] lines) {
		for (String line : lines) {
			String name = line.substring(0, line.indexOf(" "));
			String[] values = line.substring(line.indexOf(" ") + 1,
					line.length()).split(" ");
			attributes.add(new Attribute(name, values));
		}
	}

	public static void main(String[] args) {
		try {
			RandomGuessPlayer player = new RandomGuessPlayer(
					"/Users/xiaoduo/Desktop/sampleGameFiles/game1.config",
					"/Users/xiaoduo/Desktop/sampleGameFiles/game1.chosen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Guess guess() {

		// placeholder, replace
		return new Guess(Guess.GuessType.Person, "", "Placeholder");
	} // end of guess()

	public boolean answer(Guess currGuess) {

		// placeholder, replace

		if (currGuess.getType() == Guess.GuessType.Person) {
			if (currGuess.getValue().equals(chosenName))
				return true;
		}

		return false;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {

		// placeholder, replace
		return true;
	} // end of receiveAnswer()

} // end of class RandomGuessPlayer
