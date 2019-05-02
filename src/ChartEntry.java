import java.util.ArrayList;
public class ChartEntry {
	static int nextID=0;
	int ID;
	String head;
	ArrayList<String> seen;
	ArrayList<String> unseen;
	int start;
	int end;
	int parseCount;
	public ChartEntry(String h, ArrayList<String> s, ArrayList<String> u, int b, int e, int pc) {
		ID = nextID; nextID++;
		head = h;
		seen = new ArrayList<String>(s);
		unseen = new ArrayList<String>(u);
		start = b;
		end = e;
		parseCount = pc;
	}
}