package graphcoloring;

class Edge {

	private final int edgeNumber;
	private final int vertexSrc;
	private final int vertexDst;
	private String color;

	public Edge(int edgeNumber, int vertexSrc, int vertexDst) {
		this.edgeNumber = edgeNumber;
		this.vertexSrc = vertexSrc;
		this.vertexDst = vertexDst;
	}

	public int getVertexSrc() {
		return vertexSrc;
	}

	public int getVertexDst() {
		return vertexDst;
	}

	public int getEdgeNumber() {
		return edgeNumber;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean checkIfNeighbourWithTheSameColor(Edge edge) {
		return ((this.vertexSrc == edge.getVertexSrc() || this.vertexSrc == edge.getVertexDst())
				|| (this.vertexDst == edge.getVertexSrc() || this.vertexDst == edge.getVertexDst()))
				&& this.color != null && this.color.equals(edge.getColor()) && this.edgeNumber != edge.getEdgeNumber();
	}

}