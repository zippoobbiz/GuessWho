import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Attribute {

	private String attributeName;
	private Map<String, ArrayList<String>> valuesPersonMap;

	public Attribute(String attributeName, String[] values) {
		this.attributeName = attributeName;
		valuesPersonMap = new HashMap<String, ArrayList<String>>();
		for (String s : values) {
			valuesPersonMap.put(s, new ArrayList());
		}
	}

	public void addPerson(String name, String value) {
		if (valuesPersonMap.containsKey(value)) {
			valuesPersonMap.get(value).add(name);
		}
	}

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
//					remainingCandidates.forEach(a -> System.out.print(a));
//					System.out.println("===="+s);
					count++;
				}
			}
//			System.out.println(count + "----" + remainingCandidates.size());
			p.setChanceToWin((double)count / remainingCandidates.size());
		}

		// Collections.sort(remainingPairs, new Comparator<Pair>() {
		// @Override
		// public int compare(Pair o1, Pair o2) {
		// return 0;
		// }
		// });
		
		return remainingPairs;
	}

	public boolean check(String name, String value) {
		if (valuesPersonMap.containsKey(value)) {
			return valuesPersonMap.get(value).contains(name);
		}

		return false;
	}

	public String randomValue() {
		List<String> keys = new ArrayList<String>(valuesPersonMap.keySet());
		return keys.get(ThreadLocalRandom.current().nextInt(0, keys.size()));
	}

	public String getAttributeName() {
		return attributeName;
	}

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
