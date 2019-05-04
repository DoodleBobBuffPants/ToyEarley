import java.util.ArrayList;
import java.util.HashSet;
public class ChartEntry {
	String head;
	ArrayList<String> seen;
	ArrayList<String> unseen;
	int start;
	int end;
	HashSet<ArrayList<ChartEntry>> histories;
	public ChartEntry(String h, ArrayList<String> s, ArrayList<String> u, int b, int e, HashSet<ArrayList<ChartEntry>> hist) {
		head = h;
		seen = new ArrayList<String>(s);
		unseen = new ArrayList<String>(u);
		start = b;
		end = e;
		histories = new HashSet<ArrayList<ChartEntry>>(hist);
	}
	@Override
	public String toString() {
		return head + " " + seen + " " + unseen + " " + start + " " + end;
	}
}