
public class PercolationStats {
	private final double g = 1.96;
	private int N;					// Size of lattice
	private int T;					// Number of experiments to run
	private double thresholdSums = 0;
	private double mean = 0;
	private double [] results;
	
	public PercolationStats(int N, int T) {
		this.N = N;
		this.T = T;
		this.results = new double [T + 1];
		this.startExperiments(); 	// Kickoff the experiments
	}
	
	
	public double mean () {
		return mean;
	}
	
	
	public double stddev() {
		double sum = 0;
		for(int k = 1; k <= T; k++) {
			sum += ((results[k] - mean) * (results[k] - mean));
		}
		return Math.sqrt(sum/(T-1));
	}
	
	
	public double confidenceLo() {
		return (mean - (g * this.stddev())/Math.sqrt(T));
	}
	
	
	public double confidenceHi() {
		return (mean + (g * this.stddev())/Math.sqrt(T));
	}
	
	
	private void startExperiments() {
		for (int k = 1; k <= T; k++) {
			Percolation perc = new Percolation(N);
			double openSiteCount = 0;
			do {
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);
				if(!perc.isOpen(i, j)) {
					perc.open(i, j);
					openSiteCount++;
				}
			}while(!perc.percolates());
			results[k] = openSiteCount/(N*N);
			thresholdSums += openSiteCount/(N*N);
		}
		this.mean = (thresholdSums/T);
	}
	
	
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);

		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException();
		}
		
		Stopwatch timer = new Stopwatch();
		
		PercolationStats experiments = new PercolationStats(N,T);
		
		double time = timer.elapsedTime();
		
		double mean = experiments.mean();
		double sigma = experiments.stddev();
		double loConfidence = experiments.confidenceLo();
		double highConfidence = experiments.confidenceHi();
		
        System.out.println("time                    = " + time);
        System.out.println("mean                    = " +  mean);
        System.out.println("stddev                  = " + sigma);
        System.out.println("95% confidence interval = [" + loConfidence + ", " +  highConfidence + "]");
	}
}
