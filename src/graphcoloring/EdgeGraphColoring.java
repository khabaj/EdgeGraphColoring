package graphcoloring;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.apache.commons.collections15.functors.ConstantTransformer;

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

//			GraphGenerator graphGenerator = new GraphGenerator();
//			graphGenerator.generateRandomGraph(10, 15, "data/Graph1.txt");

			readFile(fileName);
			System.out.println("Running on file: " + fileName);
			System.out.println("Vertices: " + numOfVertices);
			System.out.println("Edges: " + graph.getEdgeCount());
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

		for (Integer vertex : graph.getVertices()) {

			Collection<String> incidentEdges = graph.getIncidentEdges(vertex);
			Set<String> incidentEdgesColors = new HashSet<>();

			for (String incidentEdge : incidentEdges) {

				String edgeColor = genes[Integer.parseInt(incidentEdge) - 1];

				if (incidentEdgesColors.contains(edgeColor)) {
					return -1;
				} else {
					incidentEdgesColors.add(edgeColor);
				}
			}
		}

		return (numOfEdges + 1 - usedColors.size());
	}

	public static void printGraph(String[] genes) {
		Layout<Integer, String> layout = new FRLayout<Integer, String>(graph);
		layout.setSize(new Dimension(1000, 600));
		VisualizationViewer<Integer, String> visualizationViewer = new VisualizationViewer<Integer, String>(layout);

		final int[] colors = new int[] { 0x000000, 0xFFFF00, 0x1CE6FF, 0xFF34FF, 0xFF4A46, 0x008941, 0x006FA6,
				0xA30059, 0xFFDBE5, 0x7A4900, 0x0000A6, 0x63FFAC, 0xB79762, 0x004D43, 0x8FB0FF, 0x997D87, 0x5A0007,
				0x809693, 0xFEFFE6, 0x1B4400, 0x4FC601, 0x3B5DFF, 0x4A3B53, 0xFF2F80, 0x61615A, 0xBA0900, 0x6B7900,
				0x00C2A0, 0xFFAA92, 0xFF90C9, 0xB903AA, 0xD16100, 0xDDEFFF, 0x000035, 0x7B4F4B, 0xA1C299, 0x300018,
				0x0AA6D8, 0x013349, 0x00846F, 0x372101, 0xFFB500, 0xC2FFED, 0xA079BF, 0xCC0744, 0xC0B9B2, 0xC2FF99,
				0x001E09, 0x00489C, 0x6F0062, 0x0CBD66, 0xEEC3FF, 0x456D75, 0xB77B68, 0x7A87A1, 0x788D66, 0x885578,
				0xFAD09F, 0xFF8A9A, 0xD157A0, 0xBEC459, 0x456648, 0x0086ED, 0x886F4C,

				0x34362D, 0xB4A8BD, 0x00A6AA, 0x452C2C, 0x636375, 0xA3C8C9, 0xFF913F, 0x938A81, 0x575329, 0x00FECF,
				0xB05B6F, 0x8CD0FF, 0x3B9700, 0x04F757, 0xC8A1A1, 0x1E6E00, 0x7900D7, 0xA77500, 0x6367A9, 0xA05837,
				0x6B002C, 0x772600, 0xD790FF, 0x9B9700, 0x549E79, 0xFFF69F, 0x201625, 0x72418F, 0xBC23FF, 0x99ADC0,
				0x3A2465, 0x922329, 0x5B4534, 0xFDE8DC, 0x404E55, 0x0089A3, 0xCB7E98, 0xA4E804, 0x324E72, 0x6A3A4C,
				0x83AB58, 0x001C1E, 0xD1F7CE, 0x004B28, 0xC8D0F6, 0xA3A489, 0x806C66, 0x222800, 0xBF5650, 0xE83000,
				0x66796D, 0xDA007C, 0xFF1A59, 0x8ADBB4, 0x1E0200, 0x5B4E51, 0xC895C5, 0x320033, 0xFF6832, 0x66E1D3,
				0xCFCDAC, 0xD0AC94, 0x7ED379, 0x012C58 };

		visualizationViewer.setPreferredSize(new Dimension(1000, 600));
		visualizationViewer.getRenderContext().setEdgeDrawPaintTransformer((index) -> {
			int value = Integer.parseInt(genes[Integer.parseInt(index) - 1]);
			return new Color(colors[value % 128]);
		});

		DefaultModalGraphMouse<Object, Object> graphMouse = new DefaultModalGraphMouse<>();
		graphMouse.setMode(edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode.PICKING);
		visualizationViewer.setGraphMouse(graphMouse);

		visualizationViewer.setBackground(Color.lightGray);
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
		visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		visualizationViewer.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(2.0f)));
		
		visualizationViewer.getRenderContext().setEdgeLabelTransformer((index) -> {
			return "Edge " + index + ", color: " + genes[Integer.parseInt(index) - 1];
		});

		JFrame frame = new JFrame("Edges Graph Coloring");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(visualizationViewer);
		frame.pack();
		frame.setVisible(true);
		frame.repaint();
	}

	public static void readFile(String fileName) throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileReader(fileName));
		numOfEdges = scanner.nextInt();

		ArrayList<String> colors = new ArrayList<>();

		while (scanner.hasNextLine()) {
			int edgeNumber = scanner.nextInt();
			int verticeIndexStart = scanner.nextInt();
			int verticeIndexEnd = scanner.nextInt();

			graph.addEdge(String.valueOf(edgeNumber), verticeIndexStart, verticeIndexEnd);
			colors.add(String.valueOf(edgeNumber));
		}

		for (Integer vertex : graph.getVertices()) {
			int degree = graph.degree(vertex);
			highestVertexDegree = highestVertexDegree < degree ? degree : highestVertexDegree;
		}

		possibleColors = colors.toArray(new String[colors.size()]);
		numOfVertices = graph.getVertexCount();
		scanner.close();
	}

}
