//Debashish Saha
public class SLLSparseM implements SparseM {
	
	public static class elementNode {
		private int element;
		private int row, col;
		private elementNode next;
		public elementNode (int e, int r, int c, elementNode n) {
			element = e;
			row = r;
			col = c;
			next = n;
		}
		public elementNode (int e, int r, int c) {
			element = e;
			row = r;
			col = c;
			next = null;
		}
	}
		
	public static class rowHeadNode {
		private int rowHeadidx;
		private int rowElements;
		private elementNode first;
		private rowHeadNode nextRow;
		public rowHeadNode (int r, int n, elementNode e, rowHeadNode h) {
			rowHeadidx = r;
			rowElements++;// = n;
			first = e;
			nextRow = h;
		}	
		public rowHeadNode (int r, int n, elementNode e) {
			rowHeadidx = r;
			rowElements++;// = n;
			first = e;
			nextRow = null;
		}		
	}
	
	private rowHeadNode header;
	private int nrows, ncols;
	private int nelements;
	
	public SLLSparseM(int nr, int nc){
		if (nr <= 0) nr =  1;
		if (nc <= 0) nc  = 1;
		nrows = nr;
		ncols = nc;
		header = null;
		nelements = 0;
	}
	private boolean outOfBounds (int ridx, int cidx){
		return (ridx < 0) ||  (ridx >= nrows) || (cidx < 0) || (cidx >= ncols);
	}
	
	public int nrows() {
		return nrows;
	}
	public int ncols() {
		return ncols;
	}
	public int numElements() {
		return nelements;
	}
	public int getElement(int ridx, int cidx) {
		if (outOfBounds(ridx, cidx))
			return -1;
		rowHeadNode curr = header;
		elementNode ele;
		while (curr != null)	{
			ele = curr.first;
			while (ele != null)	{
				if ((ele.row == ridx) && (ele.col == cidx))
					return ele.element;
				else ele = ele.next;
			}
			curr = curr.nextRow;
		}
		return 0;
	}

	public void clearElement(int ridx, int cidx) {
		if (outOfBounds(ridx, cidx))
			return;
		rowHeadNode currHead = header;
		rowHeadNode prevHead = header;
		elementNode ele;
		elementNode prev;
		
		while (currHead != null)	{
			ele = currHead.first;
			prev = ele;
			while (ele != null)	{
				
				//if the elements is the first in that row
				if ((ele.row == ridx) && (ele.col == cidx) && (ele == currHead.first))	{
					currHead.first = ele.next;	
					//delete the rowHead entirely if there is only one element
					if (currHead.first == null)			{					
						if (currHead == header)			header = header.nextRow;
						prevHead.nextRow = currHead.nextRow;
					}
					currHead.rowElements--;
					nelements--;					
					return;			
				}
				
				//if  the element is in between or at last place of the row
				if ((ele.row == ridx) && (ele.col == cidx))	{
					prev.next=ele.next;	
					currHead.rowElements--;
					nelements--;
					return;
				}	 
				prev = ele;
				ele = ele.next;
			}
			prevHead = currHead;
			currHead = currHead.nextRow;
		}
		return;
	}

	public void setElement(int ridx, int cidx, int val) {
		
		rowHeadNode currHead = header;
		elementNode e;
		
		if (outOfBounds(ridx, cidx))
			return;
		if (val == 0){
			clearElement (ridx, cidx);
			return;
		}
		nelements++;
		
		// if the list is empty create an element then create a rowHeadNode
		if (currHead== null)	{
			e = new elementNode (val, ridx, cidx);
			header = new rowHeadNode (ridx, numElements(), e);
			return;
		}		
		
		while ( currHead!= null)	{
			//add rowHeadNode in fort of the list
			if(ridx < currHead.rowHeadidx)		{
				e = new elementNode (val, ridx, cidx);
				header = new rowHeadNode (ridx, numElements(), e, header);
				return;
			}
		
			//add rowHeadNode at last
			if ((ridx > currHead.rowHeadidx) && (currHead.nextRow == null))	{
				e = new elementNode (val, ridx, cidx);
				currHead.nextRow = new rowHeadNode (ridx, numElements(), e);
				return;
			}
			
			//add rowHeadNode in between or end of the list 
			if ((ridx > currHead.rowHeadidx) && (ridx <currHead.nextRow.rowHeadidx))	{
				e = new elementNode (val, ridx, cidx);
				currHead.nextRow = new rowHeadNode (ridx, numElements(), e, currHead.nextRow );
				return;
			}
						
			//add an element
			if (ridx == currHead.rowHeadidx)	{
				elementNode currEle = currHead.first;
				while (currEle != null)	{
				
					//add at the front
					if (cidx < currHead.first.col) 	{
						currHead.first = new elementNode (val, ridx, cidx, currHead.first);			
						currHead.rowElements++;
						return;
						}
					//add at last
					if ((cidx > currEle.col) && (currEle.next == null)){
						currEle.next = new elementNode (val, ridx, cidx); //, currEle.next);	
						currHead.rowElements++;
						return;
					}
					//add in between
					if ( (cidx > currEle.col) && (cidx<currEle.next.col) )		{						
						currEle.next = new elementNode (val, ridx, cidx, currEle.next);												
						currHead.rowElements++;
						return;
						}					
					//edit the current element
					if (cidx == currEle.col)	{
						nelements--;
						currEle.element = val;
						return;
					}				
					currEle = currEle.next;
				}
			}			
			currHead = currHead.nextRow;
		}
	}

	public void getAllElements(int[] ridx, int[] cidx, int[] val) {
		rowHeadNode curr = header;
		elementNode ele;
		int counter = 0;
		while (curr != null)	{
			ele =curr.first;
			while ( ele!= null )	{
				ridx[counter] = ele.row;
				cidx[counter] = ele.col;
				val[counter] = ele.element;
				counter++;
				ele = ele.next;
 			}
			curr = curr.nextRow;
		}
		return;
	}

	@Override
	public void addition(SparseM otherM) {
		if((otherM.nrows() != nrows) || (otherM.ncols() != ncols))
			return;
		
		rowHeadNode currHead = header;
		rowHeadNode otherHead = ((SLLSparseM)otherM).header;
		elementNode currEle;
		elementNode otherEle;
		
		//check if the current file is empty
		if (currHead == null) {
			
			otherEle = otherHead.first;
			elementNode e = new elementNode (otherEle.element, otherEle.row, otherEle.col);
			nelements++;
			otherEle = otherEle.next;
			
			header = new rowHeadNode (otherHead.rowHeadidx, otherHead.rowElements, e, header);
			rowHeadNode prevNode = header;
			rowHeadNode nextNode ;

			while(otherEle != null){
				e.next = new elementNode (otherEle.element, otherEle.row, otherEle.col);
				otherEle=otherEle.next;
				e=e.next;
				nelements++;
				header.rowElements++;
			}
			otherHead = otherHead.nextRow;
			
			while (otherHead != null)	{	
				otherEle = otherHead.first;
				elementNode e2 = new elementNode (otherEle.element, otherEle.row, otherEle.col);
				nelements++;
				otherEle = otherEle.next;
				nextNode = new rowHeadNode (otherHead.rowHeadidx, otherHead.rowElements, e2); //, header);
				while(otherEle != null){
					e2.next = new elementNode (otherEle.element, otherEle.row, otherEle.col);
					otherEle=otherEle.next;
					e2=e2.next;
					nelements++;
					nextNode.rowElements++;
				}
				otherHead = otherHead.nextRow;
				prevNode.nextRow = nextNode;
				prevNode = nextNode;
			}
			return;
		}
		
		//if the file is not empty
		while((currHead != null) && (otherHead != null)){
			currEle = currHead.first;
			otherEle = otherHead.first;
			
			//insert a RowHead and its elements at first
			if ((otherHead.rowHeadidx < currHead.rowHeadidx) && (currHead == header)){
				elementNode e = new elementNode (otherEle.element, otherEle.row, otherEle.col);
				nelements++;
				otherEle = otherEle.next;
				rowHeadNode r = new rowHeadNode (otherHead.rowHeadidx, otherHead.rowElements, e, header);
				while(otherEle != null){
					e.next = new elementNode (otherEle.element, otherEle.row, otherEle.col);
					otherEle=otherEle.next;
					e=e.next;
					nelements++;
					r.rowElements++;
				}
				header = r;	
				currHead = header;
				otherHead = otherHead.nextRow;
				continue;
			}
			
			//insert a rorHead in between
			if ((otherHead.rowHeadidx > currHead.rowHeadidx) && (currHead.nextRow != null) && (otherHead.rowHeadidx < currHead.nextRow.rowHeadidx) ){
				elementNode e = new elementNode (otherEle.element, otherEle.row, otherEle.col);
				nelements++;
				otherEle = otherEle.next;
				rowHeadNode r = new rowHeadNode (otherHead.rowHeadidx, otherHead.rowElements, e, currHead.nextRow);
				while(otherEle != null){
					e.next = new elementNode (otherEle.element, otherEle.row, otherEle.col);
					otherEle=otherEle.next;
					e=e.next;
					nelements++;
					r.rowElements++;

				}
				currHead.nextRow = r;
				otherHead = otherHead.nextRow;
				continue;	
			}
			
			//insert a rowHead at last
			if ((otherHead.rowHeadidx > currHead.rowHeadidx) && (currHead.nextRow == null) ){
				elementNode e = new elementNode (otherEle.element, otherEle.row, otherEle.col);
				nelements++;
				otherEle = otherEle.next;
				rowHeadNode r = new rowHeadNode (otherHead.rowHeadidx, otherHead.rowElements, e);
				while(otherEle != null){
					e.next = new elementNode (otherEle.element, otherEle.row, otherEle.col);
					otherEle=otherEle.next;
					e=e.next;
					nelements++;
					r.rowElements++;
				}
				currHead.nextRow = r;
				otherHead = otherHead.nextRow;
				continue;
			}
			
			// if the rowHeads are equal to each other
			if (currHead.rowHeadidx == otherHead.rowHeadidx)	{
				while ((currEle != null) && (otherEle!=null)){
					
					//insert element at first
					if ((otherEle.col < currEle.col) && (currEle == currHead.first)){
						currHead.first = new elementNode (otherEle.element, otherEle.row, otherEle.col, currEle);
						currEle = currHead.first;
						otherEle = otherEle.next;
						nelements++;
						currHead.rowElements++;
						continue;
					}				
					//insert element in between
					if ((otherEle.col > currEle.col) && (currEle.next != null) && (otherEle.col < currEle.next.col)){
						currEle.next = new elementNode (otherEle.element, otherEle.row, otherEle.col,currEle.next);	
						otherEle = otherEle.next;
						nelements++;
						currHead.rowElements++;
						continue;
					}
					//insert elements at last
					if ((otherEle.col > currEle.col) && (currEle.next == null)) {
						currEle.next = new elementNode (otherEle.element, otherEle.row, otherEle.col);
						otherEle = otherEle.next;
						nelements++;
						currHead.rowElements++;
						continue;
					}
					//add the elements
					if (otherEle.col == currEle.col) { 
						currEle.element = (currEle.element + otherEle.element);
						otherEle = otherEle.next;
			//			if (currEle.next != null)	currEle = currEle.next;
						continue;
					}
					currEle = currEle.next;				
				}
				otherHead = otherHead.nextRow;
				continue;
			}
			currHead = currHead.nextRow;
		}
	}
	
	public void print (){
		rowHeadNode h = header;
		elementNode e;
		while (h != null)	{
			e =h.first;
			System.out.print(h.rowHeadidx +"|" + h.rowElements + "----------");;
			while ( e!= null )	{
				System.out.print("|" + e.row + "|" + e.col + "|" + e.element + "|" + "--");
				e = e.next;
				if (e == null) 		System.out.println ("@");
 			}
			h = h.nextRow;
		}
		return;
	}

}
