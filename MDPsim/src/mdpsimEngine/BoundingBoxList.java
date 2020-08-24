package mdpsimEngine;
import java.util.ArrayList;

public class BoundingBoxList {
	private ArrayList<BoundingBoxPointer> bblist;
	
	public BoundingBoxList () {
		this.bblist = new ArrayList<BoundingBoxPointer>();
	}
	
	public void insert(BoundingBoxPointer bbpointer) {
		int front = 0;
		int end = bblist.size();
		int mid = (front + end)/2;
		boolean found = false;	
		while(bblist.get(mid) != null && bblist.get(mid).value() > bbpointer.value() && end-front > 1 && found == false) {
			front = mid;
			mid = (front+end)/2;
			if (bblist.get(mid).value() == bbpointer.value() && bblist.get(mid).bb() == bbpointer.bb()) {
				found = true;
			}
		}
		if (found == true) {
			return;
		} else {
			bblist.add(mid,bbpointer);
		}
	}
}
