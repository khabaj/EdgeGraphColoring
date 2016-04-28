package graphcoloring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class GraphGenerator {

	public void generateRandomGraph(int numberOfVertices, int numberOfEdges, File file) {

		PrintWriter writer = null;

		boolean[][] adjArray = new boolean[numberOfVertices][numberOfVertices];
		try {
			writer = new PrintWriter(file, "UTF-8");
			writer.print(numberOfEdges);

			int j = 1;

			for (int i = 1; i <= numberOfEdges; i++) {
				if (i < numberOfVertices) {
					adjArray[i - 1][j] = true;
					adjArray[j][i - 1] = true;

					writer.print("\n" + i + " " + j + " " + ++j);
				} else {
					Random random = new Random();
					Integer startNode;
					Integer destinationNode;

					do {
						startNode = random.nextInt(numberOfVertices) + 1;
						destinationNode = random.nextInt(numberOfVertices) + 1;
					} while (startNode == destinationNode || adjArray[startNode - 1][destinationNode - 1] == true
							|| adjArray[destinationNode - 1][startNode - 1] == true);

					adjArray[startNode - 1][destinationNode - 1] = true;
					adjArray[destinationNode - 1][startNode - 1] = true;

					writer.print("\n" + i + " " + startNode + " " + destinationNode);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}

	}

}
