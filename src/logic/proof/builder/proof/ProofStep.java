package logic.proof.builder.proof;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Variable;

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
    public HashMap<String, Variable> freeVariables;

    ProofStep(SimpleNode node, int currentSize, int level, String formula) {
	subproofs = new ArrayList<ProofStep>();
	this.node = node;
	this.lineNumber = currentSize+1;
	this.level = level;	
	this.formula = formula;
	this.justification = "No justification given";
	freeVariables= new HashMap<String, Variable>();
    }

}
