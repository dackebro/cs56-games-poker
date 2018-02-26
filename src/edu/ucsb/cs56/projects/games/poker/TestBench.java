package edu.ucsb.cs56.projects.games.poker;
import java.util.HashMap;

public class TestBench {
	public static HashMap<String, Boolean> coverage;
	
	public static void SetupCoverageTracking() {
		coverage = new HashMap<String, Boolean>();
		coverage.put("compareHands1", false);
		coverage.put("compareHands2", false);
		coverage.put("compareHands3", false);
		coverage.put("compareHands4", false);
		coverage.put("compareHands5", false);
		coverage.put("compareHands6", false);
		coverage.put("compareHands7", false);
		coverage.put("compareHands8", false);
		coverage.put("compareHands9", false);
		coverage.put("compareHands10", false);
		coverage.put("compareHands11", false);
		coverage.put("straightTie1", false);
		coverage.put("straightTie2", false);
		coverage.put("straightTie3", false);
		coverage.put("straightTie4", false);
		coverage.put("straightTie5", false);
		coverage.put("straightTie6", false);
		coverage.put("straightTie7", false);
		coverage.put("straightTie8", false);
		coverage.put("straightTie9", false);
	}
}
