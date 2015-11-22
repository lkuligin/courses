package examples;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;

public class medianMaintenance {
	private MinHeap MinHeap;
	private MaxHeap MaxHeap;
	
	public medianMaintenance() {
		this.MinHeap = new MinHeap();
		this.MaxHeap = new MaxHeap();
	}

	public int processFile(String FileName) throws Exception {
		BufferedReader Reader = new BufferedReader(new FileReader(FileName));
		String Row;
		int res = 0;
		while ( (Row = Reader.readLine()) != null) {
			int i = Integer.parseInt(Row.trim());
			res += add(i);
		}
		Reader.close();
		return res;
	}
	
	public int median() {
		return this.MaxHeap.getTop();
	}
	
	public int add(int el) {
		if (this.MaxHeap.size() == 0)
			this.MaxHeap.add(el);
		else if (this.MinHeap.size() == 0)
			if (el >= this.MaxHeap.getTop())
				this.MinHeap.add(el);
			else {
				int left_el = this.MaxHeap.extractTop();
				this.MaxHeap.add(el);
				this.MinHeap.add(left_el);
			}
		else if (this.MaxHeap.size() <= this.MinHeap.size()) {
			//System.out.println("size: " + this.MaxHeap.size());
			//System.out.println("test: " + this.MaxHeap.extractTop());
			int right_el = this.MinHeap.getTop();
			if (right_el >= el)
				this.MaxHeap.add(el);
			else {
				right_el = MinHeap.extractTop();
				this.MinHeap.add(el);
				this.MaxHeap.add(right_el);
			}
		}
		else {
			int left_el = this.MaxHeap.getTop();
			if (left_el <= el)
				this.MinHeap.add(el);
			else {
				left_el = MaxHeap.extractTop();
				this.MaxHeap.add(el);
				this.MinHeap.add(left_el);
			}
		}
		return median();
	}

}
