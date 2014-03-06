
public class Percolation {
	private Site bottomSite;  		// invisible bottom site 
	private Site topSite;	  		// invisible top site
	private Site[][] grid;    		// 2-D Array of Site Objects
	private WeightedQuickUnionUF QFUF;  		// Quick Find Union Find Object
	private int N;					// N X N total number of sites
	private final int OPEN = 1;		// open site shall have value 1
	private final int BLOCKED = 0;	// blocked site shall have value 0

	
	// Site class abstracts the behavior of 
	// each site residing in the composite system
	private class Site {
		private int id;					// site id
		private int state = BLOCKED;	// site state, either open or blocked
		private boolean isFull = false;	// site is either full or empty
		
		// construct a site from an integer, set the site id
		private Site (int id) {
			if (id < 0) {
				throw new IllegalArgumentException();
			}
			this.id = id;
		}
	}
	
	
	// construct a Percolation object to model a percolation system.
	// Create an N X N grid of blocked Site objects. Construct the quick find 
	// union find object as well as the bottom and top Site objects.
	public Percolation(int N) {
		if (N <= 0) {
			throw new IllegalArgumentException();
		}
		this.N = N;
		bottomSite = new Site(N*N + 1);
		topSite = new Site(0);
		QFUF = new WeightedQuickUnionUF(N*N + 2);
		grid = new Site[N+1][N+1];
		this.createGrid();
	}
	
	
	// construct an N X N grid of sites
	private void createGrid() {
		int id = 1;
		for(int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				//Site curr = new Site(id);
				grid[i][j] = new Site(id);
                if (i == 1) {
                    QFUF.union(grid[i][j].id, topSite.id);
                    grid[i][j].isFull = true;
                }
                if (i == N) {
                    QFUF.union(grid[i][j].id, bottomSite.id);
                }
				id++;
			}
		}
	}
	
	
	public boolean isFull(int i, int j) {
		if (!isValidSite(i, j)) {
			throw new IndexOutOfBoundsException();
		}
		// This check is necessary since the top sites are 
		// initialized as being full. In the event a blocked 
		// top site is queried the method will return false.
		if (grid[i][j].state == BLOCKED) {
			return false;
		}
		return grid[i][j].isFull;
	}
	
	
	public boolean isOpen(int i, int j) {
		if (!isValidSite(i,j)) {
			throw new IndexOutOfBoundsException();
		}
		return grid[i][j].state == OPEN ? true : false;
	}
	
	
	public void open(int i, int j) {
		if (!isValidSite(i,j)) {
			throw new IndexOutOfBoundsException();
		}
		if (grid[i][j].state == OPEN) {
			return;
		}
		
		// A flag to signify that this portion of the grid
		// has been filled
		boolean fullComponent = false;
		// An array of the neighboring sites
		Site [] neighbors = new Site[5];
		// Set current site to open
		grid[i][j].state = OPEN;
		// Add current site to array
		neighbors[0] = grid[i][j];
		
		if(neighbors[0].isFull) { 		// This is only true for sites in the top row
			fullComponent = true;
		}
		// Union the current site to a neighbor only if
		// the neighbor is open. 
		// Add the neighbor to the array.
		// Flag variable will be set to true here only 
		// if the neighbor is full and the current site
		// is not in the top row.
		if(isValidSite(i - 1, j) && isOpen(i - 1, j)) {
			neighbors[1] = grid[i - 1][j];
			QFUF.union(grid[i][j].id, neighbors[1].id);
			if (!fullComponent && neighbors[1].isFull) {
				fullComponent = true;
			}
		}
		if(isValidSite(i, j + 1) && isOpen(i, j + 1)) {
			neighbors[2] = grid[i][j + 1];
			QFUF.union(grid[i][j].id, neighbors[2].id);
			if (!fullComponent && neighbors[2].isFull) {
				fullComponent = true;
			}
		}
		if(isValidSite(i + 1, j) && isOpen(i + 1, j)) {
			neighbors[3] = grid[i + 1][j];
			QFUF.union(neighbors[3].id, grid[i][j].id);
			if (!fullComponent && neighbors[3].isFull) {
				fullComponent = true;
			}
		}
		if(isValidSite(i, j - 1) && isOpen(i, j - 1)) {
			neighbors[4] = grid[i][j-1];
			QFUF.union(neighbors[4].id, grid[i][j].id);
			if (!fullComponent && neighbors[4].isFull) {
				fullComponent = true;
			}
		}
		// Fill the current and neighboring sites if the 
		// fullComponent flag is true
		if(fullComponent) {
			for (int k = 0; k < neighbors.length; k++) {
				if(neighbors[k] != null)
					neighbors[k].isFull = true;
			}
		}
		return;
	}
	
	private boolean isValidSite (int i, int j) {
        if (i <= 0 || i > N) 
        	return false;    	// invalid row
        else if (j <= 0 || j > N) 
        	return false; 		// invalid column
        else
        	return true;		// valid indices
	}
	
	public boolean percolates() {
		return QFUF.connected(topSite.id, bottomSite.id);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int N = 100;
		Percolation system = new Percolation(N);
		double count = 0;
		Stopwatch percTime = new Stopwatch();
		do {
			int i = (int)(Math.random()*N) + 1;
			int j = (int)(Math.random()*N) + 1;
			if (!system.isOpen(i, j)) {
				system.open(i, j);
				count++;
			}
		}while(!system.percolates());
		double time = percTime.elapsedTime();
		System.out.println(count);
		System.out.println(N*N);
		System.out.println("Time = " + time);
		System.out.printf("Percentage = " + "%.7f", count/(N*N));
	}
}