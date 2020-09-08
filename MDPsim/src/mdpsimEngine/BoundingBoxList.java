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
		//System.out.println(bbpointer.value());
		//this.printall();
		return;
	}
	
	public int binSearchBBPointer(BoundingBoxPointer bbpointer) {
		int left = 0;
		int right = bblist.size() - 1;
		int middle = 0;
		while (left <= right) {
			middle = (left + right) / 2;
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
}
