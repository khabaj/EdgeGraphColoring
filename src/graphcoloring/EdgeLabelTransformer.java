package graphcoloring;

import org.apache.commons.collections15.Transformer;

public class EdgeLabelTransformer implements Transformer<String, String>{

	String[] genes;
	
	public EdgeLabelTransformer() {
	}
	
	public EdgeLabelTransformer(String[] genes) {
		this.genes = genes;
	}
	
	@Override
	public String transform(String index) {
		if (genes != null && genes.length > 0)
			return "Edge " + index + ", color: " + genes[Integer.parseInt(index) - 1];
		else return index;
	}
	
	public void setGenes(String[] genes) {
		this.genes = genes;
	}
}
