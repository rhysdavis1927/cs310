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
    public String introducedVariable;
    
    /**
     * 
     * @param node
     * @param currentSize
     * @param level
     * @param formula
     */

    ProofStep(SimpleNode node, int currentSize, int level, String formula) {
	subproofs = new ArrayList<ProofStep>();
	this.node = node;
	this.lineNumber = currentSize+1;
	this.level = level;	
	this.formula = formula;
	this.justification = "No justification given";
	freeVariables= new HashMap<String, Variable>();
	introducedVariable=null;
    }
    
    /**
     * 
     * @param name
     * @param currentSize
     * @param level
     */
    ProofStep(String name, int currentSize, int level) {
	subproofs = new ArrayList<ProofStep>();
	this.node = new SimpleNode(0);
	this.lineNumber = currentSize+1;
	this.level = level;	
	this.formula = "";
	this.justification = "";
	freeVariables= new HashMap<String, Variable>();
	introducedVariable=name;
    }
    
    /**
     * 
     * @param node
     * @param name
     * @param currentSize
     * @param level
     * @param formula
     */
    ProofStep(SimpleNode node,String name, int currentSize, int level,String formula) {
	subproofs = new ArrayList<ProofStep>();
	this.node = node;
	this.lineNumber = currentSize+1;
	this.level = level;	
	this.formula = formula;
	this.justification = "Assumption";
	freeVariables= new HashMap<String, Variable>();
	introducedVariable=name;
    }

}
