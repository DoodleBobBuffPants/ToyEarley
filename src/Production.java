import java.util.ArrayList;
import java.util.HashSet;
public class Production {
	String from;
	HashSet<ArrayList<String>> to;
	public Production(String f, HashSet<ArrayList<String>> t) {
		from = f;
		to = new HashSet<ArrayList<String>>(t);
	}
}