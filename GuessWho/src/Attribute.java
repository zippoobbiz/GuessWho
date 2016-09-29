import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Attribute {

	private String attributeName;
	private String[] values;
	private List<String> persons;

	public Attribute(String attributeName, String[] values) {
		this.attributeName = attributeName;
		this.values = values;
		persons = new ArrayList<String>();
	}

	public void addPerson(String name) {
		if (persons != null) {
			persons.add(name);
		}
	}

	public String randomValue() {
		return values[ThreadLocalRandom.current().nextInt(0, values.length)];
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

}
