
public class Pair implements Comparable<Pair> {

	private String name;
	private String value;
	private double chanceToWin;

	public Pair(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double getChanceToWin() {
		return chanceToWin;
	}

	public void setChanceToWin(double chanceToWin) {
		this.chanceToWin = chanceToWin;
	}

	@Override
	public int compareTo(Pair o) {
		// TODO Auto-generated method stub
		return Math.abs(this.chanceToWin - 0.5) - Math.abs(o.chanceToWin - 0.5) > 0 ? 1 : -1;
	}
	
	@Override
	public String toString()
	{
		return name + "\t" + value + "\t" + chanceToWin;
	}

}
