package graphcoloring;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.softtechdesign.ga.ChromStrings;
import com.softtechdesign.ga.Crossover;
import com.softtechdesign.ga.GAException;
import com.softtechdesign.ga.GAStringsSeq;

public class EdgeGraphColoring extends GAStringsSeq {
	private static final DataSet currentData = DataSet.GRAF_CYKLICZNY_9;
	final static String fileName = currentData.getFilename();// "Graph1.txt";
	static String[] possibleColors;
	static ArrayList<Edge> graphEdges;
	static int numOfEdges = 0;
	static int numOfVertices = 0;
	static int highestVertexDegree;

	public EdgeGraphColoring() throws GAException {
		super(numOfEdges, //// liczba genów w chromosomie
				1000, // liczebność populacji
				0.4, // prawdopodobieństwo krzyżowania
				10, // szansa na losową selekcję (w %) bez względu na
					// dopasowanie.
				1000, // maksymalna liczba generacji
				10, // liczba powtórzeń generacji populacji początkowej (0
					// wyłącza opcje)
				0, // max generations per prelim run
				0.1, // prawdopodobieństwo wystąpienia mutacji
				0, // liczba miejsc po przecinku dla wartości genu
				possibleColors, // możliwe wartości genu 
				Crossover.ctTwoPoint, // typ krzyżowania 
				true); // compute statisitics
	}

	public static void main(String[] args) throws GAException {
		try {

			/*GraphGenerator graphGenerator = new GraphGenerator();
			graphGenerator.generateRandomGraph(10, 15, "Graph1.txt");*/

			readFile(fileName);
			System.out.println("Running on file: " + fileName);
			System.out.println("Vertices: " + numOfVertices);
			System.out.println("Edges: " + graphEdges.size());
			EdgeGraphColoring edgeGrapColoring = new EdgeGraphColoring();
			edgeGrapColoring.run();
			System.out.println("--------------------------------------");
			if (edgeGrapColoring.getFittestChromosomesFitness() > 0) {

				ChromStrings chromosome = (ChromStrings) edgeGrapColoring.getFittestChromosome();
				String genes[] = chromosome.getGenes();
				Set<String> usedColors = new HashSet<>(Arrays.asList(genes));
				int numOfUsedColors = usedColors.size();
				System.out.println("Number of colors used in solution: " + numOfUsedColors);
			} else {
				System.out.println("!!!!!!!!! COLORING NOT FOUND !!!!!!!!!!!");
			}
			System.out.println("Expected optimal number of colors <= " + (highestVertexDegree + 1));

		} catch (FileNotFoundException | GAException ex) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "an exception was thrown:", ex);
		}
	}

	@Override
	protected double getFitness(int chromeIndex) {
		ChromStrings chromosome = getChromosome(chromeIndex);
		String genes[] = chromosome.getGenes();
		Set<String> usedColors = new HashSet<>(Arrays.asList(genes));

		for (Edge edge : graphEdges) {
			String edgeColor = genes[edge.getEdgeNumber() - 1];

			edge.setColor(genes[edge.getEdgeNumber() - 1]);

			for (Edge e : graphEdges) {

				if (e.checkIfNeighbourWithTheSameColor(edge)) {
					return -1;
				} else
					usedColors.add(edgeColor);
			}

		}
		return (numOfEdges + 1 - usedColors.size());
	}

	public static void readFile(String fileName) throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileReader(fileName));
		Set<Integer> vertices = new HashSet<>();

		numOfEdges = scanner.nextInt();
		graphEdges = new ArrayList<>(numOfEdges);
		
		Map<Integer, Integer> degreeMap = new HashMap<>();

		while (scanner.hasNextLine()) {
			int edgeNumber = scanner.nextInt();
			int verticeIndexStart = scanner.nextInt();
			int verticeIndexEnd = scanner.nextInt();
			vertices.add(verticeIndexStart);
			vertices.add(verticeIndexEnd);
			graphEdges.add(new Edge(edgeNumber, verticeIndexStart, verticeIndexEnd));

			Integer startVertexDegree = degreeMap.get(verticeIndexStart);
			Integer destVertexDegree = degreeMap.get(verticeIndexEnd);

			degreeMap.put(verticeIndexStart, startVertexDegree != null ? startVertexDegree + 1 : 1);
			degreeMap.put(verticeIndexEnd, destVertexDegree != null ? destVertexDegree + 1 : 1);

		}

		highestVertexDegree = Collections.max(degreeMap.values());
		numOfVertices = vertices.size();
		possibleColors = new String[numOfEdges];
		
		for (int i = 0; i < numOfEdges; i++) {
			possibleColors[i] = String.valueOf(i + 1);
		}
		scanner.close();
	}

}
