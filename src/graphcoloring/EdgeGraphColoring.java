package graphcoloring;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

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
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EdgeGraphColoring implements Initializable {

	private String[] possibleColors;
	private int numOfEdges = 0;
	private int numOfVertices = 0;
	private int highestVertexDegree;
	private Graph<Integer, String> graph;;
	private String fileName;

	/***/
	Stage stage;

	@FXML
	SwingNode visualizationPane;

	@FXML
	private TextArea consoleTextArea;

	@FXML
	private Label fileNameLabel;

	@FXML
	private Button startButton;	

	@FXML
	private Button openFileButton;
	
	/* parameters */
	@FXML
	private TextField populationDim;

	@FXML
	private TextField crossoverProb;

	@FXML
	private TextField randomSelectionChance;

	@FXML
	private TextField maxGenerations;

	@FXML
	private TextField numPrelimRuns;

	@FXML
	private TextField maxPrelimGenerations;

	@FXML
	private TextField mutationProb;

	@FXML
	private TextField chromDecimalPoints;

	@FXML
	private ComboBox<String> crossoverType = new ComboBox<>();

	final int[] colors = new int[] { 0x000000, 0xFFFF00, 0x1CE6FF, 0xFF34FF, 0xFF4A46, 0x008941, 0x006FA6, 0xA30059,
			0xFFDBE5, 0x7A4900, 0x0000A6, 0x63FFAC, 0xB79762, 0x004D43, 0x8FB0FF, 0x997D87, 0x5A0007, 0x809693,
			0xFEFFE6, 0x1B4400, 0x4FC601, 0x3B5DFF, 0x4A3B53, 0xFF2F80, 0x61615A, 0xBA0900, 0x6B7900, 0x00C2A0,
			0xFFAA92, 0xFF90C9, 0xB903AA, 0xD16100, 0xDDEFFF, 0x000035, 0x7B4F4B, 0xA1C299, 0x300018, 0x0AA6D8,
			0x013349, 0x00846F, 0x372101, 0xFFB500, 0xC2FFED, 0xA079BF, 0xCC0744, 0xC0B9B2, 0xC2FF99, 0x001E09,
			0x00489C, 0x6F0062, 0x0CBD66, 0xEEC3FF, 0x456D75, 0xB77B68, 0x7A87A1, 0x788D66, 0x885578, 0xFAD09F,
			0xFF8A9A, 0xD157A0, 0xBEC459, 0x456648, 0x0086ED, 0x886F4C,

			0x34362D, 0xB4A8BD, 0x00A6AA, 0x452C2C, 0x636375, 0xA3C8C9, 0xFF913F, 0x938A81, 0x575329, 0x00FECF,
			0xB05B6F, 0x8CD0FF, 0x3B9700, 0x04F757, 0xC8A1A1, 0x1E6E00, 0x7900D7, 0xA77500, 0x6367A9, 0xA05837,
			0x6B002C, 0x772600, 0xD790FF, 0x9B9700, 0x549E79, 0xFFF69F, 0x201625, 0x72418F, 0xBC23FF, 0x99ADC0,
			0x3A2465, 0x922329, 0x5B4534, 0xFDE8DC, 0x404E55, 0x0089A3, 0xCB7E98, 0xA4E804, 0x324E72, 0x6A3A4C,
			0x83AB58, 0x001C1E, 0xD1F7CE, 0x004B28, 0xC8D0F6, 0xA3A489, 0x806C66, 0x222800, 0xBF5650, 0xE83000,
			0x66796D, 0xDA007C, 0xFF1A59, 0x8ADBB4, 0x1E0200, 0x5B4E51, 0xC895C5, 0x320033, 0xFF6832, 0x66E1D3,
			0xCFCDAC, 0xD0AC94, 0x7ED379, 0x012C58 };

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		OutputStream out = new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				appendText(String.valueOf((char) b));
			}
		};
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));

		populationDim.setText("1000");
		crossoverProb.setText("0.4");
		randomSelectionChance.setText("10");
		maxGenerations.setText("1000");
		numPrelimRuns.setText("20");
		maxPrelimGenerations.setText("20");
		mutationProb.setText("0.2");
		chromDecimalPoints.setText("0");

		crossoverType.getItems().add(CrossoverName.ctOnePoint);
		crossoverType.getItems().add(CrossoverName.ctTwoPoint);
		crossoverType.getItems().add(CrossoverName.ctRoulette);
		crossoverType.getItems().add(CrossoverName.ctUniform);		
		crossoverType.setValue(CrossoverName.ctTwoPoint);
	}

	public void appendText(String valueOf) {
		Platform.runLater(() -> {
			consoleTextArea.appendText(valueOf);
		});
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	private void disableElements(boolean disabled) {
		openFileButton.setDisable(disabled);
	}
	

	@FXML
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");

		fileChooser.setInitialDirectory(new File("."));

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			try {
				readFile(file);
				fileName = file.getName();
				fileNameLabel.setText(fileName);
				startButton.setDisable(false);
				System.out.println("Loaded file: " + fileName);
			} catch (Exception e) {
				startButton.setDisable(true);
				fileNameLabel.setText("");
				System.err.println("Error while loading file!");
				// e.printStackTrace();
			}
		}
	}

	@FXML
	public void start() throws GAException {

		// GraphGenerator graphGenerator = new GraphGenerator();
		// graphGenerator.generateRandomGraph(40, 45, "data/Graph1.txt");		
		consoleTextArea.clear();

		int populationDimParam;
		double crossoverProbParam;
		int randomSelectionChanceParam;
		int maxGenerationsParam;
		int numPrelimRunsParam;
		int maxPrelimGenerationsParam;
		double mutationProbParam;
		int chromDecimalPointsParam;
		int crossoverTypeParam;

		try {
			populationDimParam = Integer.parseInt(populationDim.getText());
			crossoverProbParam = Double.parseDouble(crossoverProb.getText());
			randomSelectionChanceParam = Integer.parseInt(randomSelectionChance.getText());
			maxGenerationsParam = Integer.parseInt(maxGenerations.getText());
			numPrelimRunsParam = Integer.parseInt(numPrelimRuns.getText());
			maxPrelimGenerationsParam = Integer.parseInt(maxPrelimGenerations.getText());
			mutationProbParam = Double.parseDouble(mutationProb.getText());
			chromDecimalPointsParam = Integer.parseInt(chromDecimalPoints.getText());

			switch (crossoverType.getValue()) {
			case CrossoverName.ctOnePoint:
				crossoverTypeParam = Crossover.ctOnePoint;
				break;
			case CrossoverName.ctTwoPoint:
				crossoverTypeParam = Crossover.ctTwoPoint;
				break;
			case CrossoverName.ctUniform:
				crossoverTypeParam = Crossover.ctUniform;
				break;
			case CrossoverName.ctRoulette:
				crossoverTypeParam = Crossover.ctRoulette;
				break;
			default:
				crossoverTypeParam = Crossover.ctTwoPoint;
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Incorrect parameters");
			alert.setHeaderText("Incorrect parameters");
			alert.setContentText("Please enter correct parameters.");
			alert.showAndWait();
			return;
		}

		Runnable graphRunnable = () -> {
			
			System.out.println("Running on file: " + fileName);
			System.out.println("Vertices: " + numOfVertices);
			System.out.println("Edges: " + graph.getEdgeCount());
			
			disableElements(true);

			GAStringsSeq gaGraphColoring;
			try {
				gaGraphColoring = new GAStringsSeq(numOfEdges, populationDimParam, crossoverProbParam,
						randomSelectionChanceParam, maxGenerationsParam, numPrelimRunsParam, maxPrelimGenerationsParam,
						mutationProbParam, chromDecimalPointsParam, possibleColors, crossoverTypeParam, true) {

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
						return (genes.length - usedColors.size());
					}
				};

				Thread threadGraph = new Thread(gaGraphColoring);
				threadGraph.start();
				threadGraph.join();

				if (!threadGraph.isAlive()) {
					System.out.println("--------------------------------------");
					if (gaGraphColoring.getFittestChromosomesFitness() >= 0) {

						ChromStrings chromosome = (ChromStrings) gaGraphColoring.getFittestChromosome();
						String genes[] = chromosome.getGenes();
						Set<String> usedColors = new HashSet<>(Arrays.asList(genes));
						int numOfUsedColors = usedColors.size();
						System.out.println("Number of colors used in solution: " + numOfUsedColors);
						printGraph(genes);
					} else {
						System.out.println("!!!!!!!!! COLORING NOT FOUND !!!!!!!!!!!");
					}
					System.out.println("Expected optimal number of colors <= " + (highestVertexDegree + 1));
				}

			} catch (GAException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				disableElements(false);
			}
		};

		Thread thread = new Thread(graphRunnable);
		thread.start();

	}

	public void readFile(File file) throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileReader(file));
		graph = new SparseGraph<Integer, String>();
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

	public void printGraph(String[] genes) {
		Layout<Integer, String> layout = new FRLayout<Integer, String>(graph);
		layout.setSize(new Dimension(600, 500));
		VisualizationViewer<Integer, String> visualizationViewer = new VisualizationViewer<Integer, String>(layout);

		visualizationViewer.setPreferredSize(new Dimension(600, 500));

		DefaultModalGraphMouse<Object, Object> graphMouse = new DefaultModalGraphMouse<>();
		graphMouse.setMode(edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode.TRANSFORMING);
		visualizationViewer.setGraphMouse(graphMouse);

		visualizationViewer.setBackground(Color.lightGray);
		visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
		visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		visualizationViewer.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(2.0f)));

		if (genes != null) {
			visualizationViewer.getRenderContext().setEdgeDrawPaintTransformer((index) -> {
				int value = Integer.parseInt(genes[Integer.parseInt(index) - 1]);
				return new Color(colors[value % 128]);
			});

			visualizationViewer.getRenderContext().setEdgeLabelTransformer((index) -> {
				return "Edge " + index + ", color: " + genes[Integer.parseInt(index) - 1];
			});
		}

		visualizationPane.setContent(visualizationViewer);
	}

}
