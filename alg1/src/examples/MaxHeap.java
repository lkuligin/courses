package examples;

import java.util.ArrayList;

public class MaxHeap {
	public ArrayList<Integer> Heap;
	protected int size = 0;

	public MaxHeap() {
		this.Heap = new ArrayList<Integer>();
	}
	
	protected int parent(int i) {
		return i/2;
	}
	
	protected void swap(int i, int j) {
		int el1 = this.Heap.get(i);
		this.Heap.remove(i);
		this.Heap.add(i, this.Heap.get(j));
		this.Heap.remove(j);
		this.Heap.add(j, el1);
	}
	
	public void print() {
		System.out.println("size: " + this.Heap.size());
		for (int i = 0; i < this.Heap.size(); i++) {
			System.out.print(this.Heap.get(i));
			if (LeftChild(i) != null)
				System.out.print(" left: " + this.Heap.get(LeftChild(i)));
			if (RightChild(i) != null)
				System.out.print(" right: " + this.Heap.get(RightChild(i)));
			System.out.println("");

		}
	}
	
	public void add(int el) {
		this.Heap.add(el);
		int i = this.size;
		this.size++;
		while (this.Heap.get(i) > this.Heap.get(parent(i))) {
			swap(i, parent(i));
			i = parent(i);
		}
	}
	
	public Integer getTop() {
		if (this.size > 0)
			return this.Heap.get(0);
		return null;
	}
	
	public int size() {
		return this.size;
	}
	
	protected boolean HasDescendants (int i) {
		if (this.size == 0)
				return Boolean.FALSE;
		if (i >= this.size/2 && i < this.size)
			return Boolean.FALSE;
		return Boolean.TRUE;
	}
	
	protected Integer LeftChild (int i){
		if (2*(i+1)-1 < this.size)
			return 2*(i+1)-1;
		return null;
	};
	
	protected Integer RightChild (int i){
		if (2*(i+1) < this.size)
			return 2*(i+1);
		return null;
	};
	
	
	public int extractTop() {
		int res = this.Heap.get(0);
		this.Heap.remove(0);
		this.size--;
		if (this.size == 0)
			return res;
		int i = this.Heap.get(this.size-1);
		this.Heap.remove(this.size-1);
		this.Heap.add(0, i);
		i = 0;
		while (HasDescendants(i)) {
			int lc = LeftChild(i);
			int lcv = this.Heap.get(lc);
			int rcv = Integer.MIN_VALUE;
			int rc = -1;
			if (RightChild(i) != null){
				rc = RightChild(i);
				rcv = this.Heap.get(rc);
			}
			//System.out.[rint]
			if (lcv > rcv) {
				if (this.Heap.get(i) < lcv) {
					swap(lc, i);
					i = lc;	
				}
				else
					return res;
			}
			else {
				if (this.Heap.get(i) < rcv) {
					swap(rc, i);
					i = rc;
				}
				else
					return res;
			}
		}
		return res;
	}
	

}
