import java.util.ArrayList;
import java.util.HashSet;
public class Parse {
	HashSet<ChartEntry> chart;
	HashSet<ChartEntry> predicted;
	HashSet<ChartEntry> scanned;
	HashSet<ChartEntry> completed;
	HashSet<ChartEntry> allcomp;
	HashSet<String> privileged;
	public Parse() {
		chart = new HashSet<ChartEntry>();
		predicted = new HashSet<ChartEntry>();
		scanned = new HashSet<ChartEntry>();
		completed = new HashSet<ChartEntry>();
		allcomp = new HashSet<ChartEntry>();
		privileged = new HashSet<String>();
	}
	
	public void begin(Grammar grammar, String[] sentence) {
		ArrayList<String> initunseen = new ArrayList<String>();
		initunseen.add("NP");
		initunseen.add("VP");
		ChartEntry initial = new ChartEntry("START", new ArrayList<String>(), initunseen, 0, 0, new HashSet<ArrayList<ChartEntry>>());
		completed.add(initial);
		
		privileged.add("N");
		privileged.add("V");
		privileged.add("P");
		
		for(int i = 0; i < sentence.length; i++) {
			String word = sentence[i];
			
			for(ChartEntry edge: completed) {
				if(edge.unseen.size()>0 && !privileged.contains(edge.unseen.get(0))) {
					for(Production p: grammar.productions) {
						if (p.from.equals(edge.unseen.get(0))) {
							for(ArrayList<String> unseen: p.to) {
								predicted.add(new ChartEntry(edge.unseen.get(0), new ArrayList<String>(), unseen, edge.end, edge.end, new HashSet<ArrayList<ChartEntry>>()));
							}
						}
					}
				}
			}
			
			chart.addAll(completed);
			allcomp.addAll(completed);
			completed.clear();
			
			HashSet<ChartEntry> predictswap = new HashSet<ChartEntry>();
			HashSet<ChartEntry> predicttotal = new HashSet<ChartEntry>();
			while(predicted.size()>0) {
				predicttotal.addAll(predicted);
				for(ChartEntry predictededge: predicted) {
					if(!privileged.contains(predictededge.unseen.get(0))) {
						boolean recexpand = true;
						for(ChartEntry otheredge: predicttotal) {
							if(otheredge.head.equals(predictededge.unseen.get(0)) && privileged.contains(otheredge.unseen.get(0))) recexpand = false;
						}
						if(recexpand) predictswap.add(predictededge);
					}
				}
				predicted.clear();
				for(ChartEntry edge: predictswap) {
					for(Production p: grammar.productions) {
						if (p.from.equals(edge.unseen.get(0))) {
							for(ArrayList<String> unseen: p.to) {
								predicted.add(new ChartEntry(edge.unseen.get(0), new ArrayList<String>(), unseen, edge.end, edge.end, new HashSet<ArrayList<ChartEntry>>()));
							}
						}
					}
				}
				predictswap.clear();
			}
			
			chart.addAll(predicttotal);
			predicted.addAll(predicttotal);
			
			for(ChartEntry predictededge: predicted) {
				String nonterm = predictededge.unseen.get(0);
				if(privileged.contains(nonterm) && grammar.terminalset.get(nonterm).contains(word)) {
					boolean expand = true;
					for(ChartEntry scanedge: scanned) {
						if(scanedge.head.equals(nonterm) && scanedge.start==predictededge.start && scanedge.end==predictededge.end+1) expand = false;
					}
					
					if(expand) {
						if (i==predictededge.end) {
							ArrayList<String> seen = new ArrayList<String>();
							seen.add(word);
							scanned.add(new ChartEntry(nonterm, seen, new ArrayList<String>(), predictededge.start, predictededge.end+1, new HashSet<ArrayList<ChartEntry>>()));
						}
					}
				}
			}
			
			chart.addAll(scanned);
			System.out.println(chart.size());
			
			HashSet<ChartEntry> scanswap = new HashSet<ChartEntry>();
			predicted.addAll(allcomp);
			while(scanned.size()>0) {
				for(ChartEntry scanedge: scanned) {
					for(ChartEntry predictededge: predicted) {
						if(predictededge.unseen.size()>0 && predictededge.unseen.get(0).equals(scanedge.head) && predictededge.end==scanedge.start) {
							String usnonterm = predictededge.unseen.get(0);
							ArrayList<String> newseen = new ArrayList<String>(predictededge.seen);
							newseen.add(usnonterm);
							ArrayList<String> newunseen = new ArrayList<String>(predictededge.unseen);
							newunseen.remove(0);
							HashSet<ArrayList<ChartEntry>> newhist = new HashSet<ArrayList<ChartEntry>>(predictededge.histories);
							if(newhist.size()==0) newhist.add(new ArrayList<ChartEntry>());
							newhist.forEach(history->history.add(scanedge));
							ChartEntry newEntry = new ChartEntry(predictededge.head, newseen, newunseen, predictededge.start, scanedge.end, newhist);
							
							boolean uniqueedge = true;
							for(ChartEntry compedge: completed) {
								if(newEntry.head.equals(compedge.head) && newEntry.seen.equals(compedge.seen) && newEntry.unseen.equals(compedge.unseen) && newEntry.start==compedge.start && newEntry.end==compedge.end) {
									uniqueedge = false;
								}
							}
							if(uniqueedge) completed.add(newEntry);
							if(newunseen.size()==0 && !newEntry.head.equals("START")) scanswap.add(newEntry);
						}
					}
				}
				scanned.clear();
				scanned.addAll(scanswap);
				scanswap.clear();
			}
			predicted.clear();
			scanned.clear();
		}
		
		chart.addAll(completed);
		System.out.println(chart.size());
		for(ChartEntry edge: chart) {
			if(edge.head.equals("START") && edge.start==0 && edge.end==sentence.length) {
				printtree(edge, 1);
				break;
			}
		}
	}
	
	public void printtree(ChartEntry edge, int depth) {
		System.out.println(edge);
		for(ArrayList<ChartEntry> history: edge.histories) {
			for(ChartEntry histedge: history) {
				for(int i = 0; i < depth; i++) System.out.print("       ");
				printtree(histedge, depth+1);
			}
		}
	}
	
	public static void main(String[] args) {
		Parse parser = new Parse();
		Grammar grammar = new Grammar();
		String[] sentence = {"they", "can", "fish", "in", "rivers"};
		parser.begin(grammar, sentence);
	}
}