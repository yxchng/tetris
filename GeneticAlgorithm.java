
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;


public class GeneticAlgorithm {
	final static int ITERATIONS = 10;
	final static int GENERATIONS = 1000;
	final static int BEST_N = 40;
	final static int NUM_FEATURES = 10;
	
	final static double MUTATION_CHANCE = 0.25;
	final static double DISASTER_CHANCE = 0.10;
	final static double SHAKEUP_CHANCE = 0.05;

	final static String WHITESPACE = " ";
	final static String NEW_LINE = "\n";
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		for (int z = 0; z < GENERATIONS; z++) {
			System.out.print("GENERATION ");
			System.out.println(z);
			ArrayList<ArrayList<Double>> genes = findBestGenes(args[0]);
			generateNewGenes(genes, args[0]);
			System.out.println("------");
		}
	}

	private static ArrayList<ArrayList<Double>> findBestGenes(String inputFile) throws IOException {
		File file = new File(inputFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedReader bfr = new BufferedReader(new FileReader(file));
		String line;
		
		ArrayList<IntegerPair> averageNumPairs = new ArrayList<IntegerPair>();
		ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();
		int j=0;

		while ((line = bfr.readLine()) != null) {
			String[] wString = line.split(WHITESPACE);
			ArrayList<Double> wDouble = new ArrayList<Double>();
			for (int i = 0; i < NUM_FEATURES; i++)
				wDouble.add(Double.parseDouble(wString[i]));
			weights.add(wDouble);

			PlayerSkeleton.setWeight(wDouble.get(0), wDouble.get(1),wDouble.get(2),wDouble.get(3),wDouble.get(4),wDouble.get(5), wDouble.get(6), wDouble.get(7), wDouble.get(8), wDouble.get(9));

			int i = ITERATIONS;
			int sum = 0;
			while (i-- != 0) {
				sum += PlayerSkeleton.play();
			}
			int average = sum/ITERATIONS;
			System.out.println(average);
			
			averageNumPairs.add(new IntegerPair(-average, j++));
		}
		Collections.sort(averageNumPairs);
		ArrayList<ArrayList<Double>> bestGenes = new ArrayList<ArrayList<Double>>();
		for(int i=0; i<BEST_N; i++)
			bestGenes.add(weights.get(averageNumPairs.get(i).second()));
		
		bfr.close();
		return bestGenes;
	}
	
	private static void generateNewGenes(ArrayList<ArrayList<Double>> genes, String inputFile) throws IOException {
		ArrayList<ArrayList<Double>> newGenes = new ArrayList<ArrayList<Double>>();
		
		for(int i=0; i<BEST_N-1; i++) {
			for(int j=i+1; j<BEST_N; j++) {
				newGenes.add(generateNewGene(genes.get(i), genes.get(j)));
			}
		}
		
		writeToFile(genes, newGenes, inputFile);
	}
	
	private static ArrayList<Double> generateNewGene(ArrayList<Double> gene1, ArrayList<Double> gene2) {
		ArrayList<Double> newGene = new ArrayList<Double>();

		for(int i=0; i<NUM_FEATURES; i++) {
			double chromosone;
			if(Math.random()>0.5)
				chromosone = gene1.get(i);
			else
				chromosone = gene2.get(i);
			
			if(Math.random()<MUTATION_CHANCE) {
				if (Math.random() < 0.5) {
					chromosone = chromosone * 1.05;
				} else {
					chromosone = chromosone * 0.95;
				}
			}

			if(Math.random()< DISASTER_CHANCE) {
				if (Math.random() < 0.5) {
					chromosone += Math.random();
				} else {
					chromosone -= Math.random();
				}
			}

			if(Math.random()< SHAKEUP_CHANCE) {
				if (Math.random() < 0.5) {
					chromosone = 2 * chromosone + Math.random();
				} else {
					chromosone = 2 * chromosone - Math.random();
				}
			}
			
			newGene.add(chromosone);
		}
		
		return newGene;
	}

	private static void writeToFile(ArrayList<ArrayList<Double>> genes, ArrayList<ArrayList<Double>> newGenes, String inputFile) throws IOException {
		PrintWriter pw = new PrintWriter(inputFile);
		pw.close();
		
		PrintWriter fw = new PrintWriter(inputFile);
		
		for(int i=0; i<BEST_N; i++) {
			ArrayList<Double> list = genes.get(i);
			for(int j=0; j<NUM_FEATURES; j++) {
				fw.append(String.valueOf(list.get(j)) + WHITESPACE);
			}
			fw.append(NEW_LINE);
		}
		
		for(int i=0; i<BEST_N*(BEST_N-1)/2; i++) {
			ArrayList<Double> list = newGenes.get(i);
			for(int j=0; j<NUM_FEATURES; j++) {
				fw.append(String.valueOf(list.get(j)) + WHITESPACE);
			}
			fw.append(NEW_LINE);
		}
		
		fw.close();
	}
	
}

class IntegerPair implements Comparable<Object> {
	Integer _first, _second;

	public IntegerPair(Integer f, Integer s) {
		_first = f;
		_second = s;
	}

	public String toString() {
		return _first + " " + _second;
	}

	public int compareTo(Object o) {
		if (!this.first().equals(((IntegerPair) o).first()))
			return this.first() - ((IntegerPair) o).first();
		else
			return this.second() - ((IntegerPair) o).second();
	}

	Integer first() {
		return _first;
	}

	Integer second() {
		return _second;
	}
}