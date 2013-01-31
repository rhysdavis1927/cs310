package logic.proof.builder.proof;

import java.util.ArrayList;
import java.util.List;
import logic.proof.builder.parser.SimpleNode;

public class ProofStep {

    public ProofStep parent = null;
    public List<ProofStep> subproofs;
    public ProofStep next = null;
    public SimpleNode node;
    public Integer lineNumber;
    public int level;
    public String formula;
    public String justification;
    public boolean endOfSubproof;

    ProofStep(SimpleNode node, int currentSize, int level, String formula) {
	subproofs = new ArrayList<ProofStep>();
	this.node = node;
	this.lineNumber = currentSize+1;
	this.level = level;
	StringBuffer s = new StringBuffer();
	/*
	for (int i = 0; i < level; i++) {
	    s.append("   ");
	}*/
	s.append(formula);
	
	this.formula = (s.toString());
	this.justification = "No justification given";
    }

}
