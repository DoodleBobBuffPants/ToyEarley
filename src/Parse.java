import java.util.ArrayList;
import java.util.HashSet;
public class Parse {
	HashSet<ChartEntry> chart;
	HashSet<ChartEntry> predicted;
	HashSet<ChartEntry> scan;
	HashSet<ChartEntry> completed;
	HashSet<ChartEntry> allcomp;
	HashSet<String> privileged;
	public Parse() {
		chart = new HashSet<ChartEntry>();
		predicted = new HashSet<ChartEntry>();
		scan = new HashSet<ChartEntry>();
		completed = new HashSet<ChartEntry>();
		allcomp = new HashSet<ChartEntry>();
		privileged = new HashSet<String>();
	}
	
	public void begin(Grammar grammar, String[] sentence) {
		ArrayList<String> initunseen = new ArrayList<String>();
		initunseen.add("NP");
		initunseen.add("VP");
		ChartEntry initial = new ChartEntry("START", new ArrayList<String>(), initunseen, 0, 0, 1);
		completed.add(initial);
		
		privileged.add("N");
		privileged.add("V");
		privileged.add("P");
		
		int length = sentence.length;
		for(int i = 0; i < length; i++) {
			String word = sentence[i];
			
			for(ChartEntry edge: completed) {
				if(edge.unseen.size()>0 && !privileged.contains(edge.unseen.get(0))) {
					for(Production p: grammar.productions) {
						if (p.from.equals(edge.unseen.get(0))) {
							for(ArrayList<String> unseen: p.to) {
								predicted.add(new ChartEntry(edge.unseen.get(0), new ArrayList<String>(), unseen, edge.end, edge.end, edge.parseCount));
							}
						}
					}
				}
			}
			
			chart.addAll(completed);
			allcomp.addAll(completed);
			completed.clear();
			chart.addAll(predicted);
			
			for(ChartEntry predictededge: predicted) {
				String nonterm = predictededge.unseen.get(0);
				if(privileged.contains(nonterm) && grammar.terminalset.get(nonterm).contains(word)) {
					boolean expand = true;
					for(ChartEntry scanedge: scan) {
						if(scanedge.head.equals(nonterm) && scanedge.start==predictededge.start && scanedge.end==i+1) expand = false;
					}
					
					if(expand) {
						if (i+1 == predictededge.end+1) {
							ArrayList<String> seen = new ArrayList<String>();
							seen.add(word);
							scan.add(new ChartEntry(nonterm, seen, new ArrayList<String>(), predictededge.start, i+1, 1));
						}
					}
				}
			}
			
			chart.addAll(scan);
			System.out.println(chart.size());
			
			HashSet<ChartEntry> newScan = new HashSet<ChartEntry>();
			HashSet<ChartEntry> scanhist = new HashSet<ChartEntry>();
			scanhist.addAll(scan);
			predicted.addAll(allcomp);
			while(scan.size()>0) {
				for(ChartEntry scanedge: scan) {
					for(ChartEntry predictededge: predicted) {
						if(predictededge.unseen.size()>0 && predictededge.unseen.get(0).equals(scanedge.head) && predictededge.end==scanedge.start) {
							String usnonterm = predictededge.unseen.get(0);
							ArrayList<String> newseen = new ArrayList<String>(predictededge.seen);
							newseen.add(usnonterm);
							ArrayList<String> newunseen = new ArrayList<String>(predictededge.unseen);
							newunseen.remove(0);
							ChartEntry newEntry = new ChartEntry(predictededge.head, newseen, newunseen, predictededge.start, scanedge.end, predictededge.parseCount);
							
							boolean uniqueedge = true;
							for(ChartEntry compedge: completed) {
								if(newEntry.head.equals(compedge.head) && newEntry.seen.equals(compedge.seen) && newEntry.unseen.equals(compedge.unseen) &&
								   newEntry.start==compedge.start && newEntry.end==compedge.end) {compedge.parseCount+=newEntry.parseCount; uniqueedge = false;}
							}
							if(uniqueedge) completed.add(newEntry);
							
							uniqueedge = true;
							for(ChartEntry scannededge: scanhist) {
								if(newEntry.head.equals(scannededge.head) && newEntry.seen.equals(scannededge.seen) && 
								   newEntry.start==scannededge.start && newEntry.end==scannededge.end) uniqueedge = false;
							}
							if(uniqueedge && newunseen.size()==0 && !newEntry.head.equals("START")) newScan.add(newEntry);
						}
					}
				}
				scan.clear();
				scan.addAll(newScan);
				scanhist.addAll(scan);
				newScan.clear();
			}
			predicted.clear();
			scan.clear();
		}
		
		chart.addAll(completed);
		System.out.println(chart.size());
		for(ChartEntry edge: chart) {
			if(edge.head.equals("START") && edge.start==0 && edge.end==7) {
				System.out.println(edge.head + " " + edge.seen + " " + edge.unseen + " " + edge.start + " " + edge.end + " " + edge.parseCount);
			}
		}
	}
	
	public static void main(String[] args) {
		Parse parser = new Parse();
		Grammar grammar = new Grammar();
		String[] sentence = {"they", "can", "fish", "in", "rivers", "in", "december"};
		parser.begin(grammar, sentence);
	}
}