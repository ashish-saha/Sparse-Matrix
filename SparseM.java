
public interface SparseM {
	int nrows();							//return number of rows 
	int ncols();							//return number of columns
	int numElements();						//return total number of nonzero elements in the matrix
	int getElement(int ridx, int cidx);	   	//return the element at a given entry (ridx, cidx), 
	void clearElement(int ridx, int cidx); 	//set the element at (ridx,cidx) to zero
	void setElement(int ridx, int cidx, int val); 			//set the element at (ridx,cidx) to val
	void getAllElements(int[] ridx, int[] cidx, int[] val);	//get all nonzero elements
	void addition(SparseM otherM);			// adding the matrix otherM into the current matrix
}
