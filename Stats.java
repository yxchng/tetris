import java.util.*;

public class Stats {

	public static void main(String[] args) {
		int n = 100;
		int min = Integer.MAX_VALUE;
		int max = -1;
		int average = 0;
		ArrayList<Integer> results = new ArrayList<Integer>();
		int sum = 0;

		for (int i = 0; i < n; i++) {
			int score = PlayerSkeleton.play();
			results.add(score);
			sum += score;
			average = sum / n;
			System.out.println(score);
			if (min > score) {
				min = score;
			}

			if (score > max) {
				max = score;
			}
		}

		Collections.sort(results);
		int median = results.get(n/2);
		double stdDev = 0;

		for (int i = 0; i < n; i++) {
			int score = results.get(i);
			stdDev += Math.pow((score - average), 2.0);
		}

		stdDev = Math.sqrt(stdDev / n);

		System.out.println("MIN: " + min);
		System.out.println("MAX: " + max);
		System.out.println("AVERAGE: " + average);
		System.out.println("MEDIAN: " + median);
		System.out.println("STDDEV: " + stdDev);
	}







}