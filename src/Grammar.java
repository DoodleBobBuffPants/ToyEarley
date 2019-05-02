import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
public class Grammar {
	HashSet<String> nonterminals;
	HashSet<String> alphabet;
	String start;
	HashSet<Production> productions;
	HashMap<String, ArrayList<String>> terminalset;
	public Grammar() {
		nonterminals = new HashSet<String>();
		nonterminals.add("START");
		nonterminals.add("NP");
		nonterminals.add("VP");
		nonterminals.add("PP");
		nonterminals.add("N");
		nonterminals.add("V");
		nonterminals.add("P");
		
		alphabet = new HashSet<String>();
		alphabet.add("they");
		alphabet.add("can");
		alphabet.add("fish");
		alphabet.add("in");
		alphabet.add("rivers");
		alphabet.add("december");
		
		start = "START";
		
		productions = new HashSet<Production>();
		
		HashSet<ArrayList<String>> startp = new HashSet<ArrayList<String>>();
		ArrayList<String> startp1 = new ArrayList<String>();
		startp1.add("NP");
		startp1.add("VP");
		startp.add(startp1);
		productions.add(new Production("START", startp));
		
		HashSet<ArrayList<String>> npp = new HashSet<ArrayList<String>>();
		ArrayList<String> npp1 = new ArrayList<String>();
		ArrayList<String> npp2 = new ArrayList<String>();
		npp1.add("N");
		npp1.add("PP");
		npp2.add("N");
		npp.add(npp1);
		npp.add(npp2);
		productions.add(new Production("NP", npp));
		
		HashSet<ArrayList<String>> vpp = new HashSet<ArrayList<String>>();
		ArrayList<String> vpp1 = new ArrayList<String>();
		ArrayList<String> vpp2 = new ArrayList<String>();
		ArrayList<String> vpp3 = new ArrayList<String>();
		ArrayList<String> vpp4 = new ArrayList<String>();
		vpp1.add("VP");
		vpp1.add("PP");
		vpp2.add("V");
		vpp2.add("VP");
		vpp3.add("V");
		vpp3.add("NP");
		vpp4.add("V");
		vpp.add(vpp1);
		vpp.add(vpp2);
		vpp.add(vpp3);
		vpp.add(vpp4);
		productions.add(new Production("VP", vpp));
		
		HashSet<ArrayList<String>> ppp = new HashSet<ArrayList<String>>();
		ArrayList<String> ppp1 = new ArrayList<String>();
		ppp1.add("P");
		ppp1.add("NP");
		ppp.add(ppp1);
		productions.add(new Production("PP", ppp));
		
		HashSet<ArrayList<String>> np = new HashSet<ArrayList<String>>();
		ArrayList<String> np1 = new ArrayList<String>();
		ArrayList<String> np2 = new ArrayList<String>();
		ArrayList<String> np3 = new ArrayList<String>();
		ArrayList<String> np4 = new ArrayList<String>();
		ArrayList<String> np5 = new ArrayList<String>();
		np1.add("they");
		np2.add("can");
		np3.add("fish");
		np4.add("rivers");
		np5.add("december");
		np.add(np1);
		np.add(np2);
		np.add(np3);
		np.add(np4);
		np.add(np5);
		productions.add(new Production("N", np));
		
		HashSet<ArrayList<String>> vp = new HashSet<ArrayList<String>>();
		ArrayList<String> vp1 = new ArrayList<String>();
		ArrayList<String> vp2 = new ArrayList<String>();
		vp1.add("can");
		vp2.add("fish");
		vp.add(vp1);
		vp.add(vp2);
		productions.add(new Production("V", vp));
		
		HashSet<ArrayList<String>> pp = new HashSet<ArrayList<String>>();
		ArrayList<String> pp1 = new ArrayList<String>();
		pp1.add("in");
		pp.add(pp1);
		productions.add(new Production("P", pp));
		
		terminalset = new HashMap<String, ArrayList<String>>();
		terminalset.put("P", pp1);
		vp1.addAll(vp2);
		terminalset.put("V", vp1);
		np1.addAll(np2);
		np1.addAll(np3);
		np1.addAll(np4);
		np1.addAll(np5);
		terminalset.put("N", np1);
	}
}