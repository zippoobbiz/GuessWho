
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DecisionTree {

	public static void main(String[] args) throws Exception {
		String[] attrNames = new String[] { "hairLength", "glasses", "facialHair", "eyeColor", "pimples", "hat",
				"hairColor", "noseShape", "faceShape" };

		// read samples
		Map<Object, List<Sample>> samples = readSamples(attrNames);

		// generate decision tree
		Object decisionTree = generateDecisionTree(samples, attrNames);

		// print
		outputDecisionTree(decisionTree, 0, null);
	}

	/**
	 * read sample, return: classes -> List<samples>
	 */
	static Map<Object, List<Sample>> readSamples(String[] attrNames) {

		Object[][] rawData = new Object[][] {
				{ "blue", "green", "white", "brown", "brown", "blue", "white", "yellow", "white", "P1" },
				{ "blue", "green", "yellow", "brown", "blue", "white", "brown", "white", "red", "P2" },
				{ "white", "green", "blue", "blue", "blue", "red", "red", "green", "white", "P3" },
				{ "white", "green", "white", "blue", "brown", "brown", "red", "green", "white", "P4" },
				{ "yellow", "green", "white", "blue", "blue", "brown", "brown", "white", "red", "P5" },
				{ "white", "green", "blue", "blue", "white", "green", "brown", "yellow", "white", "P6" },
				{ "brown", "green", "yellow", "brown", "red", "green", "brown", "white", "blue", "P7" },
				{ "brown", "green", "blue", "blue", "blue", "yellow", "brown", "white", "blue", "P8" },
				{ "green", "green", "blue", "blue", "white", "yellow", "brown", "white", "black", "P9" },
				{ "yellow", "green", "yellow", "brown", "brown", "brown", "brown", "green", "white", "P10" } };

		Map<Object, List<Sample>> ret = new HashMap<Object, List<Sample>>();
		for (Object[] row : rawData) {
			Sample sample = new Sample();
			int i = 0;
			for (int n = row.length - 1; i < n; i++)
				sample.setAttribute(attrNames[i], row[i]);
			sample.setCategory(row[i]);
			List<Sample> samples = ret.get(row[i]);
			if (samples == null) {
				samples = new LinkedList<Sample>();
				ret.put(row[i], samples);
			}
			samples.add(sample);
		}

		return ret;
	}

	/**
	 * generate decision tree recursively
	 */
	static Object generateDecisionTree(Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {

		// if samples size is one, then return
		if (categoryToSamples.size() == 1)
			return categoryToSamples.keySet().iterator().next();

		// if doesn't provide with decision attribtue,
		// then take the sample with most attribute as the new classifier
		if (attrNames.length == 0) {
			int max = 0;
			Object maxCategory = null;
			for (Entry<Object, List<Sample>> entry : categoryToSamples.entrySet()) {
				int cur = entry.getValue().size();
				if (cur > max) {
					max = cur;
					maxCategory = entry.getKey();
				}
			}
			return maxCategory;
		}

		// get the test attributes
		Object[] rst = chooseBestTestAttribute(categoryToSamples, attrNames);

		// root
		Tree tree = new Tree(attrNames[(Integer) rst[0]]);

		// eliminate root
		String[] subA = new String[attrNames.length - 1];
		for (int i = 0, j = 0; i < attrNames.length; i++)
			if (i != (Integer) rst[0])
				subA[j++] = attrNames[i];

		// generate branch
		@SuppressWarnings("unchecked")
		Map<Object, Map<Object, List<Sample>>> splits = /* NEW LINE */(Map<Object, Map<Object, List<Sample>>>) rst[2];
		for (Entry<Object, Map<Object, List<Sample>>> entry : splits.entrySet()) {
			Object attrValue = entry.getKey();
			Map<Object, List<Sample>> split = entry.getValue();
			Object child = generateDecisionTree(split, subA);
			tree.setChild(attrValue, child);
		}

		return tree;
	}

	/**
	 * best classifier use the least info to classify samples return array:
	 * selectedIndex, sum of minimun info, branch
	 */
	static Object[] chooseBestTestAttribute(Map<Object, List<Sample>> categoryToSamples, String[] attrNames) {

		int minIndex = -1; // bestIndex
		double minValue = Double.MAX_VALUE; // minimum info
		Map<Object, Map<Object, List<Sample>>> minSplits = null; // branch

		for (int attrIndex = 0; attrIndex < attrNames.length; attrIndex++) {
			int allCount = 0;

			// create map：attribute->(class->List<samples>)
			Map<Object, Map<Object, List<Sample>>> curSplits = /* NEW LINE */new HashMap<Object, Map<Object, List<Sample>>>();
			for (Entry<Object, List<Sample>> entry : categoryToSamples.entrySet()) {
				Object category = entry.getKey();
				List<Sample> samples = entry.getValue();
				for (Sample sample : samples) {
					Object attrValue = sample.getAttribute(attrNames[attrIndex]);
					Map<Object, List<Sample>> split = curSplits.get(attrValue);
					if (split == null) {
						split = new HashMap<Object, List<Sample>>();
						curSplits.put(attrValue, split);
					}
					List<Sample> splitSamples = split.get(category);
					if (splitSamples == null) {
						splitSamples = new LinkedList<Sample>();
						split.put(category, splitSamples);
					}
					splitSamples.add(sample);
				}
				allCount += samples.size();
			}

			// calculate entropy
			double curValue = 0.0; 
			for (Map<Object, List<Sample>> splits : curSplits.values()) {
				double perSplitCount = 0;
				for (List<Sample> list : splits.values())
					perSplitCount += list.size(); 
				double perSplitValue = 0.0; 
				for (List<Sample> list : splits.values()) {
					double p = list.size() / perSplitCount;
					perSplitValue -= p * (Math.log(p) / Math.log(2));
				}
				curValue += (perSplitCount / allCount) * perSplitValue;
			}

			// select the minimum value
			if (minValue > curValue) {
				minIndex = attrIndex;
				minValue = curValue;
				minSplits = curSplits;
			}
		}

		return new Object[] { minIndex, minValue, minSplits };
	}

	/**
	 * print tree
	 */
	static void outputDecisionTree(Object obj, int level, Object from) {
		for (int i = 0; i < level; i++)
			System.out.print("|-----");
		if (from != null)
			System.out.printf("(%s):", from);
		if (obj instanceof Tree) {
			Tree tree = (Tree) obj;
			String attrName = tree.getAttribute();
			System.out.printf("[%s = ?]\n", attrName);
			for (Object attrValue : tree.getAttributeValues()) {
				Object child = tree.getChild(attrValue);
				outputDecisionTree(child, level + 1, attrName + " = " + attrValue);
			}
		} else {
			System.out.printf("[CATEGORY = %s]\n", obj);
		}
	}

	/**
	 * sample
	 */
	static class Sample {

		private Map<String, Object> attributes = new HashMap<String, Object>();

		private Object category;

		public Object getAttribute(String name) {
			return attributes.get(name);
		}

		public void setAttribute(String name, Object value) {
			attributes.put(name, value);
		}

		public Object getCategory() {
			return category;
		}

		public void setCategory(Object category) {
			this.category = category;
		}

		public String toString() {
			return attributes.toString();
		}

	}

	/**
	 * decision tree
	 * each leave has a classifier and many branches,
	 */
	static class Tree {

		private String attribute;

		private Map<Object, Object> children = new HashMap<Object, Object>();

		public Tree(String attribute) {
			this.attribute = attribute;
		}

		public String getAttribute() {
			return attribute;
		}

		public Object getChild(Object attrValue) {
			return children.get(attrValue);
		}

		public void setChild(Object attrValue, Object child) {
			children.put(attrValue, child);
		}

		public Set<Object> getAttributeValues() {
			return children.keySet();
		}

	}

}