package examples;

public class MinHeap extends MaxHeap {
	@Override
	public void add(int el) {
		this.Heap.add(el);
		int i = this.Heap.size()-1;
		while (this.Heap.get(i) < this.Heap.get(parent(i))) {
			swap(i, parent(i));
			i = parent(i);
		}
		this.size++;
	}
	
	@Override
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
			if (lcv < rcv || rc == -1) {
				if (this.Heap.get(i) > lcv) {
					swap(lc, i);
					i = lc;	
				}
				else
					return res;
			}
			else {
				if (this.Heap.get(i) > rcv) {
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
