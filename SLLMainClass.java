//Debashish Saha
import java.util.Scanner;
import java.io.File;

public class SLLMainClass {

	public static SparseM ParseMatrix(String file_name){
	    Scanner sc = null;
	    String tmps;
	    SparseM M = null;
	    try {
	        sc = new Scanner(new File(file_name));
		    while (sc.hasNext()) {
		    	tmps = sc.next();
		    	if(tmps.equals("MATRIX")){
		    		// initialize the matrix
		    		int nr = sc.nextInt();
		    		int nc = sc.nextInt();
		    		M = new SLLSparseM(nr,nc);	
		    	}else if(tmps.equals("END")){
		    		// finished, return the matrix
		    		sc.close();
		    		return M;
		    	}else if(tmps.equals("SET")){
		    		// set an element
		    		int ridx = sc.nextInt(); // row index
		    		int cidx = sc.nextInt(); // col index
		    		int val = sc.nextInt();  // value
		    		M.setElement(ridx, cidx, val);
		    	}else if(tmps.equals("CLEAR")){
		    		// clear an element
		    		int ridx = sc.nextInt(); // row index
		    		int cidx = sc.nextInt(); // col index
		    		M.clearElement(ridx, cidx);		    		
		    	}		    	
		    }
		    sc.close();
		    return M;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	public static void main(String[] args) {
		int n = args.length;
		if(n <= 0)
			return;

		String fname =  args[0];
		SparseM M = ParseMatrix(fname);	// read M from the first file
		
		if(M == null){	
			// if invalid input file, print NULL and exit
			System.out.println("NULL");
			return;
		}
		
		SparseM tmpM;
		for(int i = 1; i < n; i++){
			fname = args[i];
			tmpM = ParseMatrix(fname);	// read tmpM from the next file
			if(tmpM == null)			// if the file is invalid, skip
				continue;
			M.addition(tmpM); 			// add tmpM to M
		}
		
		// output the final matrix
		int nelem = M.numElements();
		int[] ridx = new int[nelem];
		int[] cidx = new int[nelem]; 
		int[] val = new int[nelem];
		M.getAllElements(ridx, cidx, val);
		int nr, nc, ne;
		nr = M.nrows();			// number of rows
		nc = M.ncols();			// number of columns
		ne = M.numElements();	// number of elements
		System.out.println(nr + " " + nc + " " + ne);
		for(int i = 0; i < ne; i++)
			System.out.println(ridx[i] + " " + cidx[i] + " " + val[i]);
		System.out.println("END");
	}
}
