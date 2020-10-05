package mdpsimEngine;
import java.util.ArrayList;
import java.lang.Double;

public class BoundingBoxList {
	private ArrayList<BoundingBoxPointer> bblist;
	
	public BoundingBoxList () {
		this.bblist = new ArrayList<BoundingBoxPointer>();
	}
	
	public void insert(BoundingBoxPointer bbpointer) {
		this.bblist.add(binSearchBBPointer(bbpointer),bbpointer);
		return;
	}
	
	public int binSearchBBPointer(BoundingBoxPointer bbpointer) {
		int left = 0;
		int right = bblist.size() - 1;
		int middle = 0;
		while (left <= right) {
			middle = Math.floorDiv((left + right),2);
			int cmp = Double.compare(bbpointer.value(), bblist.get(middle).value());
			if (cmp < 0) {
				left = middle + 1;
			} else if (cmp > 0) {
				right = middle - 1;
			} else {
				return middle;
			}
		}
		return middle;
	}
	
	public ArrayList<Object2D> between(BoundingBoxPointer bbbottom, BoundingBoxPointer bbtop) {
		ArrayList<Object2D> objects = new ArrayList<Object2D>(0);
		int bottom = binSearchBBPointer(bbtop);
		int top = binSearchBBPointer(bbbottom);
		for (int a = bottom; a < top; a++) {
			objects.add(this.bblist.get(a).bb().object());	
		}
		return objects;
	}
	
	public void printall() {
		for (int a = 0; a < bblist.size(); a++) {
			System.out.print(bblist.get(a).value());
			System.out.print(" ");
		}
		System.out.println();
	}
	
	public void sort() {
		boolean sorted = false;
		while(!sorted) {
			sorted = true;
			for (int a = 0; a < this.bblist.size()-1; a++) {
				if (bblist.get(a).value()<bblist.get(a+1).value()) {
					BoundingBoxPointer temp = bblist.get(a);
					bblist.set(a, bblist.get(a+1));
					bblist.set(a+1, temp);
					sorted = false;
				}
			}
		}
	}
}
