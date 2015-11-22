package examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class checkSum {
	private HashSet<Long> Series; 

	public checkSum(String FileName) throws Exception {
		BufferedReader Reader = new BufferedReader(new FileReader(FileName));
		String Row;
		this.Series = new HashSet<Long>();
		while ( (Row = Reader.readLine()) != null) {
			long i = Long.parseLong(Row.trim());
			addElement(i);
		}
		Reader.close();
	}
	
	public checkSum() {
		this.Series = new HashSet<Long>();
	}
	
	public void addElements(int[] Elements) {
		Arrays.stream(Elements).forEach(item -> addElement(item));
	}
	
	public void addElement (long i) {
		this.Series.add(i);
	}
	
	private boolean check(int Destination) {
		Iterator<Long> itr = this.Series.iterator();
		while (itr.hasNext()) {
			long el = itr.next();
			if (this.Series.contains(Destination-el) && el != (Destination-el)) { 
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
		
	}
	
	public int count2sums(int RangeFrom, int RangeTo) {
		int res = 0;
		for (int i = RangeFrom; i <= RangeTo; i++) {
			if (check(i)) 
				res += 1;
		}
		return res;
	}

}
