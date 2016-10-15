import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Attribute {

	/*
	 * This Class store a specific attribute, for example: the hair color, etc
	 * and a map, that set key as possible values of the attribute, for example:
	 * red, black, blue, etc set the value of the map as a list of people that
	 * have such value of the attribute
	 * 
	 * This Class fulfill the requirement in Task A, with a good data structure
	 * that not only store the information properly, but also can
	 */

	/** attribute name */
	private String attributeName;
	/**  the relation between each possible attribute value and person name */
	private Map<String, ArrayList<String>> valuesPersonMap;

	/**  constructor */
	public Attribute(String attributeName, String[] values) {
		this.attributeName = attributeName;
		valuesPersonMap = new HashMap<String, ArrayList<String>>();
		for (String s : values) {
			valuesPersonMap.put(s, new ArrayList<String>());
		}
	}

	/**  add one person to the map */
	public void addPerson(String name, String value) {
		if (valuesPersonMap.containsKey(value)) {
			valuesPersonMap.get(value).add(name);
		}
	}

	/**  giving a list of candidates, return the remaining pair that one can guess */
	public List<Pair> remainingPair(List<String> remainingCandidates) {
		List<Pair> remainingPairs = new ArrayList<Pair>();
		for (String s : valuesPersonMap.keySet()) {
			for (String v : valuesPersonMap.get(s)) {
				if (remainingCandidates.contains(v)) {
					remainingPairs.add(new Pair(attributeName, s));
					break;
				}
			}
		}
		return remainingPairs;
	}

	/**
	 * Giving a list of candidates, return the remaining pair that one can guess
	 * and calculate the chanceToWIn for each pair
	 * @formula: number of person has specific pair / number of remaining candidates
	 * @return List of Pair
	 */
	public List<Pair> remainingPairWeighted(List<String> remainingCandidates) {
		Map<String, ArrayList<String>> remainingValuesPersonMap = new HashMap<>();
		List<Pair> remainingPairs = new ArrayList<Pair>();
		for (String s : valuesPersonMap.keySet()) {
			for (String v : valuesPersonMap.get(s)) {
				if (remainingCandidates.contains(v)) {
					remainingPairs.add(new Pair(attributeName, s));
					break;
				}
			}
		}
		for (Pair p : remainingPairs) {
			int count = 0;
			for (String s : valuesPersonMap.get(p.getValue())) {
				if (remainingCandidates.contains(s)) {
					// remainingCandidates.forEach(a -> System.out.print(a));
					// System.out.println("===="+s);
					count++;
				}
			}
			// System.out.println(count + "----" + remainingCandidates.size());
			p.setChanceToWin((double) count / remainingCandidates.size());
		}
		return remainingPairs;
	}

	/** 
	 * search in the map, check if the pair of name and value exist 
	 * @return if the pair exist
	 */
	public boolean check(String name, String value) {
		if (valuesPersonMap.containsKey(value)) {
			return valuesPersonMap.get(value).contains(name);
		}

		return false;
	}

	/** get a random value from current attribute */
	public String randomValue() {
		List<String> keys = new ArrayList<String>(valuesPersonMap.keySet());
		return keys.get(ThreadLocalRandom.current().nextInt(0, keys.size()));
	}

	/** getter */
	public String getAttributeName() {
		return attributeName;
	}

	/** setter */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(attributeName);
		sb.append("\n");
		for (String s : valuesPersonMap.keySet()) {
			sb.append(s);
			sb.append("\n");
			for (String p : valuesPersonMap.get(s)) {
				sb.append(p);
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/** 
	 * When the opponent answered, if our guessing was correct, then we should
	 * keep all people that has the same attribute and same value.
	 * If the answer was wrong, then we should eliminate all people that has the
	 * same attribute and the same value.
	 * @parameter keep: decides whether we keep the remaining people or eliminate them.
	 * @parameter value: is the specific value that we guessed.
	 */
	public ArrayList<String> getEliminateList(String value, boolean keep) {
		ArrayList<String> eliminateList = new ArrayList<String>();
		if (keep) {
			for (String s : valuesPersonMap.keySet()) {
				if (!s.equals(value)) {
					for (String p : valuesPersonMap.get(s)) {
						eliminateList.add(p);
					}
				}
			}
		} else {
			for (String p : valuesPersonMap.get(value)) {
				eliminateList.add(p);
			}
		}
		return eliminateList;
	}

}
