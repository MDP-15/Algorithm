package mdpsimEngine;
import java.util.ArrayList;
import java.util.HashMap;

public class BoundingBoxList {
	private ArrayList<BoundingBoxPointer> bblist;
	
	public BoundingBoxList () {
		this.bblist = new ArrayList<BoundingBoxPointer>();
	}
	
	public void insert(BoundingBoxPointer bbpointer) {
		int value = binSearchBBPointer(bbpointer);
		this.bblist.add(value,bbpointer);
		return;
	}
	
	public int binSearchBBPointer(BoundingBoxPointer bbpointer) {
		int front = 0;
		int end = bblist.size();
		int mid = (front + end)/2;
		while(end - front > 0) {
			mid = (front + end)/2;
			if (bblist.get(mid).value() == bbpointer.value()) {
				return mid;
			}else if (bblist.get(mid).value() < bbpointer.value()) {
				end = mid;
			} else {
				front = mid;
			}
		} 
		return mid;
	}
	
	public ArrayList<Object2D> between(BoundingBoxPointer bbbottom, BoundingBoxPointer bbtop) {
		ArrayList<Object2D> objects = new ArrayList<Object2D>(0);
		int bottom = binSearchBBPointer(bbbottom);
		int top = binSearchBBPointer(bbtop);
		if (bottom < top) {
			for (int a = bottom; a < top; a++) {
				objects.add(this.bblist.get(a).bb().object());
			}
		}
		return objects;
	}
	
	public void printall() {
		
	}
}
