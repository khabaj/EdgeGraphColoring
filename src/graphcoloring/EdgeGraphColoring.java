package graphcoloring;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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

import javax.swing.JFrame;

import com.softtechdesign.ga.ChromStrings;
import com.softtechdesign.ga.Crossover;
import com.softtechdesign.ga.GAException;
import com.softtechdesign.ga.GAStringsSeq;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class EdgeGraphColoring extends GAStringsSeq {
	private static final DataSet currentData = DataSet.MYCIEL6;
	final static String fileName = "data/Graph1.txt"; // currentData.getFilename();//
													// "Graph1.txt";
	static String[] possibleColors;
	static ArrayList<Edge> graphEdges;
	static int numOfEdges = 0;
	static int numOfVertices = 0;
	static int highestVertexDegree;

	/**/
	public static Graph<Integer, String> graph = new SparseGraph<Integer, String>();

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
			graphGenerator.generateRandomGraph(10, 15, "data/Graph1.txt");*/

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
				printGraph(genes);
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

	public static void printGraph(String[] genes) {
		Layout<Integer, String> layout = new FRLayout<Integer, String>(graph);
		layout.setSize(new Dimension(1000, 600));
		VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout);

		vv.setPreferredSize(new Dimension(1000, 600));
/*
		vv.getRenderContext().setVertexFillPaintTransformer((index) -> {
			int x = Character.getNumericValue(chromosome.charAt(index - 1));
			return new Color(x);
		});*/

		DefaultModalGraphMouse<Object, Object> graphMouse = new DefaultModalGraphMouse<>();
		graphMouse.setMode(edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);

		vv.setBackground(Color.gray);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		JFrame frame = new JFrame("Edges Graph Coloring");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
		frame.repaint();
	}

	public static void readFile(String fileName) throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileReader(fileName));
		numOfEdges = scanner.nextInt();
		graphEdges = new ArrayList<>(numOfEdges);

		Map<Integer, Integer> degreeMap = new HashMap<>();

		while (scanner.hasNextLine()) {
			int edgeNumber = scanner.nextInt();
			int verticeIndexStart = scanner.nextInt();
			int verticeIndexEnd = scanner.nextInt();

			graphEdges.add(new Edge(edgeNumber, verticeIndexStart, verticeIndexEnd));

			Integer startVertexDegree = degreeMap.get(verticeIndexStart);
			Integer destVertexDegree = degreeMap.get(verticeIndexEnd);

			degreeMap.put(verticeIndexStart, startVertexDegree != null ? startVertexDegree + 1 : 1);
			degreeMap.put(verticeIndexEnd, destVertexDegree != null ? destVertexDegree + 1 : 1);

		}

		highestVertexDegree = Collections.max(degreeMap.values());
		possibleColors = new String[numOfEdges];

		for (int i = 0; i < numOfEdges; i++) {
			possibleColors[i] = String.valueOf(i + 1);

			Edge edge = graphEdges.get(i);

			graph.addEdge("Edge " + edge.getEdgeNumber(), edge.getVertexSrc(), edge.getVertexDst());
		}

		numOfVertices = graph.getVertexCount();
		scanner.close();
	}

}
