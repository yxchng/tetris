
public class PlayerSkeleton {
	private static double WEIGHT_NUM_HOLES = -8.243127886575365;
	private static double WEIGHT_ROWS_CLEARED = -1.7583119498289528;
	private static double WEIGHT_ROUGHNESS = -1.4676599897952063;
	private static double WEIGHT_VARIATION = -0.016422168328705355;
	private static double WEIGHT_DEPTH_HOLE = -0.053109507243626584;
	private static double WEIGHT_WALLS = -2.116683686177752;
	private static double WEIGHT_BLOCKADES = -3.817861127108991;
	private static double WEIGHT_MAX_MIN_DIFF = -0.14942054926012258;
	private static double WEIGHT_COL_STD_DEV = -1.47622593303053;
	private static double WEIGHT_DIFF_AVG_HEIGHT = -0.47823672367492953;

	private double evaluateState(State tmpState, State currentState) {
		double num_holes = tmpState.getHoles();
		double rows_cleared = tmpState.getRowsCleared();
		double roughness = tmpState.getRoughness();
		double variation = tmpState.getVariation();
		double depth_hole = tmpState.getHoleDepth();
		double walls = tmpState.getWalls();
		double blockades = tmpState.getBlockades();
		double maxmindiff = tmpState.getMaxMinDiff();
		double colStdDev = tmpState.getColStdDev();
		double averageHeightDiff = tmpState.getAverageHeight() - currentState.getAverageHeight();

		double score = 0;
		score += WEIGHT_NUM_HOLES * num_holes;
		score += WEIGHT_ROWS_CLEARED * rows_cleared;
		score += WEIGHT_ROUGHNESS * roughness;
		score += WEIGHT_VARIATION * variation;
		score += WEIGHT_DEPTH_HOLE * depth_hole;
		score += WEIGHT_WALLS * walls;
		score += WEIGHT_BLOCKADES * blockades;
		score += WEIGHT_MAX_MIN_DIFF * maxmindiff;
		score += WEIGHT_COL_STD_DEV * colStdDev;
		score += WEIGHT_DIFF_AVG_HEIGHT * averageHeightDiff;

		return score;
	}

	public static void setWeight(double w1, double w2, double w3, double w4, double w5, double w6, double w7, double w8, double w9, double w10){
		WEIGHT_NUM_HOLES = w1;
		WEIGHT_ROWS_CLEARED = w2;
		WEIGHT_ROUGHNESS = w3;
		WEIGHT_VARIATION = w4;
		WEIGHT_DEPTH_HOLE = w5;
		WEIGHT_WALLS = w6;	
		WEIGHT_BLOCKADES = w7;
		WEIGHT_MAX_MIN_DIFF = w8;
		WEIGHT_COL_STD_DEV = w9;
		WEIGHT_DIFF_AVG_HEIGHT = w10;
	}


	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
        int numOfChoice = legalMoves.length;
        double maxValue = 0;
        int currentChoice = -1;//-1 means have not choosed
        for (int i = 0; i < numOfChoice; i++){
            State tmpState = new State(s.getNextPiece(),s.getTurnNumber(),s.getField(),s.getTop());
            boolean canContinue = tmpState.makeMove(legalMoves[i][State.ORIENT], legalMoves[i][State.SLOT]);
            if (!canContinue) continue;
            double tmpValue = evaluateState(tmpState, s);
            if (tmpValue > maxValue || currentChoice == -1){
                currentChoice = i;
                maxValue = tmpValue;
            }
        }
        if (currentChoice == -1) return 0;
        return currentChoice;
    }

    public static int play() {
		State s = new State();
		// new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		//uncomment this part to enable setting weight
		//p.setWeight();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			// s.draw();
			// s.drawNext(0,0);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return s.getRowsCleared();
	}
	
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
