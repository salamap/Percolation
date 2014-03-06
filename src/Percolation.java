
public class Percolation {
	private int bottomSite;  		// invisible bottom site 
	private int topSite;	  		// invisible top site
	private int [][] grid;    		// 2-D Array of sites
	private WeightedQuickUnionUF QFUF;  		// Quick Find Union Find Object
	private int N;					// N X N total number of sites
	private static final int OPEN = 1;		// open site shall have value 1
	private static final int BLOCKED = 0;	// blocked site shall have value 0
	
	
	// construct a Percolation object to model a percolation system.
	public Percolation(int N) {
		if (N <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.N = N;
		bottomSite = N*N + 1;
		topSite = 0;
		QFUF = new WeightedQuickUnionUF(N*N + 2);
		grid = new int [N+1][N+1];
		this.createGrid();
	}

	
	public boolean isFull(int i, int j) {
		if (!isValidSite(i, j)) {
			throw new IndexOutOfBoundsException();
		}
        if (!isOpen(i, j)) {
            return false;
        }
		int currSite = N*(i-1)+j;
		return QFUF.connected(currSite, topSite);
	}
	
	
	public boolean isOpen(int i, int j) {
		if (!isValidSite(i,j)) {
			throw new IndexOutOfBoundsException();
		}
		return grid[i][j] == OPEN ? true : false;
	}
	
	
	public void open(int i, int j) {
		if (!isValidSite(i,j)) {
			throw new IndexOutOfBoundsException();
		}
		if (isOpen(i, j)) {
			return;
		}
		
		grid[i][j] = OPEN;
		int currSite = N*(i-1) + j;
		int neighborSite;
		// Union the current site to a neighbor only if
		// the neighbor is open. 
		if(isValidSite(i - 1, j) && isOpen(i - 1, j)) {
			neighborSite = N*(i-2) + j;
			QFUF.union(currSite, neighborSite);
		}
		if(isValidSite(i, j + 1) && isOpen(i, j + 1)) {
			neighborSite = N*(i-1) + (j+1);
			QFUF.union(currSite, neighborSite);
		}
		if(isValidSite(i + 1, j) && isOpen(i + 1, j)) {
			neighborSite = N*(i) + (j);
			QFUF.union(neighborSite, currSite);
		}
		if(isValidSite(i, j - 1) && isOpen(i, j - 1)) {
			neighborSite = N*(i-1) + (j-1);
			QFUF.union(currSite, neighborSite);
		}
		return;
	}

	
	public boolean percolates() {
		return QFUF.connected(topSite, bottomSite);
	}
	
	
	// construct an N X N grid of sites
	private void createGrid() {
		int id;
		for(int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				grid[i][j] = BLOCKED;
				if(i == 1) {			// union top row of grid with invisible top site
					id = N*(i-1)+j;
					QFUF.union(id, topSite);
				}
				if (i == N) {			// union bottom row of grid with invisible bottom site
					id = N*(i-1)+j;
					QFUF.union(id, bottomSite);	
				}
			}
		}
	}
	
	
	private boolean isValidSite (int i, int j) {
        if (i <= 0 || i > N) 
        	return false;    	// invalid row
        else if (j <= 0 || j > N) 
        	return false; 		// invalid column
        else
        	return true;		// valid indices
	}
	
	
//	public static void main(String[] args) {
//		int N = 100;
//		Percolation system = new Percolation(N);
//		double count = 0;
//		Stopwatch percTime = new Stopwatch();
//		do {
//			int i = (int)(Math.random()*N) + 1;
//			int j = (int)(Math.random()*N) + 1;
//			if (!system.isOpen(i, j)) {
//				system.open(i, j);
//				count++;
//			}
//		}while(!system.percolates());
//		double time = percTime.elapsedTime();
//		System.out.println(count);
//		System.out.println(N*N);
//		System.out.println("Time = " + time);
//		System.out.printf("Percentage = " + "%.7f", count/(N*N));
//	}
}