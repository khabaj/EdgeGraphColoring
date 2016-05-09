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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JPanel;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class GraphColoringController implements Initializable {

	private String[] possibleColors;
	private int numOfEdges = 0;
	private int numOfVertices = 0;
	private int highestVertexDegree;
	private Graph<Integer, String> graph;
	private String fileName;
	EdgeLabelTransformer edgeLabelTransformer;
	private String[] coloredEdges;
	boolean stopColoringThread = false;
	Thread thread;
	
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

	/*------------- parameters ----------------*/
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

	/*----------- Visualisation checkboxes-------------- */
	@FXML
	CheckBox showVisualisationCheckBox;

	@FXML
	CheckBox showEdgesLabelsCheckbox;

	VisualizationViewer<Integer, String> visualizationViewer;
	
	/*-------------- Generate graph-------------------*/
	@FXML
	TextField genNumberOfVertices;
	
	@FXML
	TextField genNumberOfEdges;
	
	@FXML
	Button generateGraphButton;

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

		populationDim.setText("30");
		crossoverProb.setText("85");
		randomSelectionChance.setText("10");
		maxGenerations.setText("1000000");
		numPrelimRuns.setText("20");
		maxPrelimGenerations.setText("20");
		mutationProb.setText("0.1");
		chromDecimalPoints.setText("0");

		crossoverType.getItems().add(CrossoverName.ctOnePoint);
		crossoverType.getItems().add(CrossoverName.ctTwoPoint);
		crossoverType.getItems().add(CrossoverName.ctRoulette);
		crossoverType.getItems().add(CrossoverName.ctUniform);
		crossoverType.setValue(CrossoverName.ctTwoPoint);

		edgeLabelTransformer = new EdgeLabelTransformer();
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
		Platform.runLater(() -> {
			openFileButton.setDisable(disabled);
			startButton.setText(disabled ? "Stop" : "Start");
			generateGraphButton.setDisable(disabled);
		});
	}

	@FXML
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text files (*.txt)", "*.txt"));

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			try {
				readFile(file);
				fileName = file.getName();
				fileNameLabel.setText(fileName);
				consoleTextArea.clear();
				startButton.setDisable(false);
				visualizationViewer = null;
				coloredEdges = null;
				edgeLabelTransformer.setGenes(null);
				printGraph();
				System.out.println("Loaded file: " + fileName);
				System.out.println("Vertices: " + numOfVertices);
				System.out.println("Edges: " + graph.getEdgeCount());
			} catch (Exception e) {
				startButton.setDisable(true);
				fileNameLabel.setText("");
				System.err.println("Error while loading file!");
				e.printStackTrace();
			}
		}
	}

	private void readParams(Map<String,Number> parameters) {
		try {
			parameters.put("populationDimParam", Integer.parseInt(populationDim.getText()));
			parameters.put("crossoverProbParam", Double.parseDouble(crossoverProb.getText()));
			parameters.put("randomSelectionChanceParam", Integer.parseInt(randomSelectionChance.getText()));
			parameters.put("maxGenerationsParam", Integer.parseInt(maxGenerations.getText()));
			parameters.put("numPrelimRunsParam", Integer.parseInt(numPrelimRuns.getText()));
			parameters.put("maxPrelimGenerationsParam", Integer.parseInt(maxPrelimGenerations.getText()));
			parameters.put("mutationProbParam", Double.parseDouble(mutationProb.getText()));
			parameters.put("chromDecimalPointsParam", Integer.parseInt(chromDecimalPoints.getText()));

			switch (crossoverType.getValue()) {
			case CrossoverName.ctOnePoint:
				parameters.put("crossoverTypeParam", Crossover.ctOnePoint);
				break;
			case CrossoverName.ctTwoPoint:
				parameters.put("crossoverTypeParam", Crossover.ctTwoPoint);
				break;
			case CrossoverName.ctUniform:
				parameters.put("crossoverTypeParam", Crossover.ctUniform);
				break;
			case CrossoverName.ctRoulette:
				parameters.put("crossoverTypeParam", Crossover.ctRoulette);
				break;
			default:
				parameters.put("crossoverTypeParam", Crossover.ctTwoPoint);
			}
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Incorrect parameters");
			alert.setHeaderText("Incorrect parameters");
			alert.setContentText("Please enter correct parameters.");
			alert.showAndWait();
			throw e;
		}
	}
	
	@FXML
	public void start() throws GAException {
		
		if (startButton.getText().equalsIgnoreCase("stop")) {
			thread.interrupt();
			return;
		}
		
		Map<String, Number> parameters = new HashMap<>();
		
		try {
			readParams(parameters);
		} catch (Exception e) {
			return;
		}	
		
		consoleTextArea.clear();
		
		Runnable graphRunnable = () -> {

			System.out.println("Running on file: " + fileName);			

			disableElements(true);

			long startTime = System.currentTimeMillis();
			
			GAStringsSeq gaGraphColoring;
			try {
				gaGraphColoring = new GAStringsSeq(numOfEdges, 
						parameters.get("populationDimParam").intValue(), 
						parameters.get("crossoverProbParam").doubleValue(),
						parameters.get("randomSelectionChanceParam").intValue(), 
						parameters.get("maxGenerationsParam").intValue(), 
						parameters.get("numPrelimRunsParam").intValue(),
						parameters.get("maxPrelimGenerationsParam").intValue(),
						parameters.get("mutationProbParam").doubleValue(), 
						parameters.get("chromDecimalPointsParam").intValue(), 
						possibleColors, 
						parameters.get("crossoverTypeParam").intValue(), true) {

					@Override
					protected double getFitness(int chromeIndex) {

						ChromStrings chromosome = getChromosome(chromeIndex);
						String genes[] = chromosome.getGenes();
						Set<String> usedColors = new HashSet<>(Arrays.asList(genes));
						
						int badColoring = 0;

						for (Integer vertex : graph.getVertices()) {
							Collection<String> incidentEdges = graph.getIncidentEdges(vertex);
							Set<String> incidentEdgesColors = new HashSet<>();
							for (String incidentEdge : incidentEdges) {
								String edgeColor = genes[Integer.parseInt(incidentEdge) - 1];
								if (incidentEdgesColors.contains(edgeColor)) {
									badColoring++; 
								} else {
									incidentEdgesColors.add(edgeColor);
								}
							}
						}	
						if(badColoring > 0) {
							return (genes.length - usedColors.size()) - numOfEdges - 2*badColoring;
						} else 
							return (genes.length - usedColors.size());
					}
				};
				gaGraphColoring.run();
				long duration = System.currentTimeMillis() - startTime;
				
				System.out.println("--------------------------------------");
				System.out.println("Vertices: " + numOfVertices);
				System.out.println("Edges: " + graph.getEdgeCount());
				if (gaGraphColoring.getFittestChromosomesFitness() >= 0) {					
					
					ChromStrings chromosome = (ChromStrings) gaGraphColoring.getFittestChromosome();
					coloredEdges = chromosome.getGenes();
					Set<String> usedColors = new HashSet<>(Arrays.asList(coloredEdges));
					int numOfUsedColors = usedColors.size();
					System.out.println("Number of colors used in solution: " + numOfUsedColors);
					edgeLabelTransformer.setGenes(coloredEdges);
					printGraph();
				} else {
					System.out.println("!!!!!!!!! COLORING NOT FOUND !!!!!!!!!!!");
				}				
				System.out.println("Expected optimal number of colors <= " + (highestVertexDegree + 1));
				System.out.println("Duration: " + duration/1000.0 + "s");
			} catch (GAException | InterruptedException e) {
				System.out.println("Thread interrupted");
				e.printStackTrace();
			} finally {
				disableElements(false);
			}
		};

		thread = new Thread(graphRunnable);
		thread.start();

	}

	private void readFile(File file) throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileReader(file));
		graph = new SparseGraph<Integer, String>();
		highestVertexDegree = 0;
		numOfEdges = scanner.nextInt();

		ArrayList<String> colors = new ArrayList<>();

		while (scanner.hasNextLine()) {
			int edgeNumber = scanner.nextInt();
			int verticeIndexStart = scanner.nextInt();
			int verticeIndexEnd = scanner.nextInt();

			graph.addEdge(String.valueOf(edgeNumber), verticeIndexStart, verticeIndexEnd);
		}

		for (Integer vertex : graph.getVertices()) {
			int degree = graph.degree(vertex);
			highestVertexDegree = highestVertexDegree+1 < degree ? degree : highestVertexDegree;
		}

		for(int i=1; i<=highestVertexDegree+1; i++) {
			colors.add(String.valueOf(i));
		}
		
		possibleColors = colors.toArray(new String[colors.size()]);		
		numOfVertices = graph.getVertexCount();
		scanner.close();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void printGraph() {
		if (showVisualisationCheckBox.isSelected()) {
			if (visualizationViewer == null) {
				Layout<Integer, String> layout = new FRLayout<Integer, String>(graph);
				layout.setSize(new Dimension(600, 500));
				visualizationViewer = new VisualizationViewer<Integer, String>(layout);

				DefaultModalGraphMouse<Object, Object> graphMouse = new DefaultModalGraphMouse<>();
				graphMouse.setMode(edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode.TRANSFORMING);
				visualizationViewer.setGraphMouse(graphMouse);

				visualizationViewer.setBackground(Color.lightGray);
				visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
				visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
				visualizationViewer.getRenderContext()
						.setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(2.0f)));
				visualizationPane.setContent(visualizationViewer);
			} 

			if (coloredEdges != null) {

				visualizationViewer.getRenderContext().setEdgeDrawPaintTransformer((index) -> {
					int value = Integer.parseInt(coloredEdges[Integer.parseInt(index) - 1]);
					return new Color(colors[value % 128]);
				});				
			}
			showEdgesLabels();
			visualizationViewer.repaint();
		} else if (visualizationViewer != null) {	
			visualizationViewer = null;
			visualizationPane.setContent(new JPanel());
		}
	}

	@FXML
	public void showEdgesLabels() {
		if (visualizationViewer != null) {
			if (showEdgesLabelsCheckbox.isSelected()) {
				visualizationViewer.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
			} else {
				visualizationViewer.getRenderContext().setEdgeLabelTransformer((index) -> null);
			}
			visualizationViewer.repaint();
		}
	}

	@FXML
	public void showVisualisation() {
		printGraph();
	}
	
	@FXML
	public void generateGraph() {
		
		try {
			int numVert = Integer.parseInt(genNumberOfVertices.getText());
			int numEdges = Integer.parseInt(genNumberOfEdges.getText());
			int maxPossibleEdges = numVert * (numVert-1) / 2;
			if(numEdges > maxPossibleEdges) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Incorrect graph generation parameters");
				alert.setHeaderText("Incorrect graph generation parameters");
				alert.setContentText("Max number of Edges for " + numVert 
						+ " vertices = " + maxPossibleEdges);
				alert.showAndWait();
				return;
			}
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save file");
			fileChooser.setInitialDirectory(new File("."));		
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Text files (*.txt)", "*.txt"));
			
			File file = fileChooser.showSaveDialog(stage);		
			if (file!=null) {
				GraphGenerator graphGenerator = new GraphGenerator();
				graphGenerator.generateRandomGraph(numVert, numEdges, file);
				
				readFile(file);
				fileName = file.getName();
				fileNameLabel.setText(fileName);
				consoleTextArea.clear();
				startButton.setDisable(false);
				visualizationViewer = null;
				coloredEdges = null;
				edgeLabelTransformer.setGenes(null);
				printGraph();
				System.out.println("Loaded file: " + fileName);
			}
		} catch (NumberFormatException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Incorrect graph generation parameters");
			alert.setHeaderText("Incorrect graph generation parameters");
			alert.setContentText("Please enter correct parameters.");
			alert.showAndWait();
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("File not found");
			alert.setHeaderText("File not found");
			alert.setContentText("File not Found.");
			alert.showAndWait();
		}
		
	}
	
	
	@FXML
	public void clearConsole() {
		consoleTextArea.clear();
	}

}
