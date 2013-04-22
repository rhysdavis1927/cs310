package logic.proof.builder.proof;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import logic.proof.builder.parser.SimpleNode;

/**
 * Stores all data necessary to construct a proof. Simple Methods are provided to manipulate
 * the proof such as adding or deleting lines.
 * @author rhys
 */
public class Proof {

    private Stack<ProofStep> parents;
    /**
     * The ordered list of proofsteps
     */
    private ArrayList<ProofStep> lines;
    /**
     * The number of subproofs currently open
     */
    private int currentLevel;

    /**
     * Returns the ordered list of proofsteps
     * 
     * @return the ordered list of proofsteps
     */
    public ArrayList<ProofStep> getLines() {
	return lines;
    }

    /**
     * Returns the number of subproofs currently open
     * 
     * @return the number of subproofs currently open
     */
    public int getCurrentLevel() {
	return currentLevel;
    }

    /**
     * A list of all the named predicates in the proof. Used to populate the
     * predicate list.
     */
    public List<String> predicates;

    /**
     * Default constructor. Constructs an empty proof
     * 
     */

    public Proof() {
	predicates = new ArrayList<String>();

	// commonly used predicates
	predicates.add("P");
	predicates.add("P(x)");
	predicates.add("P(y)");
	predicates.add("P(x,y)");
	predicates.add("Q");
	predicates.add("Q(x)");
	predicates.add("Q(x,y)");
	lines = new ArrayList<ProofStep>();
	parents = new Stack<ProofStep>();
	ProofStep rootNode = new ProofStep(new SimpleNode(0), 0, 0, "");
	parents.add(rootNode);
	currentLevel = 0;
	rootNode.justification = "root node";
    }

    /**
     * The default method to add a new proofstep to the proof
     * 
     * @param node
     *            Root node of the sentence of the proofstep
     * @param formula
     *            String representation of the sentence
     * @return Returns the proofstep that has been added
     */
    public ProofStep addStepAsNewLine(SimpleNode node, String formula) {
	ProofStep parentNode = parents.pop();
	ProofStep l = new ProofStep(node, lines.size(), currentLevel, formula);
	lines.add(l);
	l.parent = parentNode;
	parentNode.next = l;
	parents.push(l);
	return l;
    }

    /**
     * Adds a new proofstep that is the start of a subproof
     * 
     * @param node
     *            Root node of the sentence of the proofstep
     * @param formula
     *            String representation of the sentence
     * @return Returns the proofstep that has been added
     */
    public ProofStep addStepAsStartOfSubproof(SimpleNode node, String formula) {
	ProofStep parentNode = parents.peek();
	ProofStep l = new ProofStep(node, lines.size(), ++currentLevel, formula);
	lines.add(l);
	l.parent = parentNode;
	parentNode.subproofs.add(l);
	parents.push(l);
	l.justification = "Assumption";
	return l;
    }

    /**
     * Adds a new proofstep that is the last line of a subproof
     * 
     * @param node
     *            Root node of the sentence of the proofstep
     * @param formula
     *            String representation of the sentence
     * @return Returns the proofstep that has been added
     */

    public ProofStep addStepAsEndOfSubproof(SimpleNode node, String formula) {
	ProofStep parentNode = parents.pop();
	ProofStep l = new ProofStep(node, lines.size(), currentLevel--, formula);
	lines.add(l);
	parentNode.next = l;
	l.parent = parentNode;
	l.endOfSubproof = true;
	return l;
    }

    /**
     * Add a new proofstep which introduces a boxed variable
     * 
     * @param var
     *            The name of the variable being introduced
     * @return Returns the proofstep that has been added
     */

    public ProofStep addVar(String var) {
	ProofStep parentNode = parents.peek();
	ProofStep l = new ProofStep(var, lines.size(), ++currentLevel);
	lines.add(l);
	l.parent = parentNode;
	parentNode.subproofs.add(l);
	parents.push(l);
	return l;
    }

    /**
     * Add a new proofstep which introduces a boxed variable alongside an
     * assumption
     * 
     * @param introducedVariable
     *            The name of the variable being introduced
     * @param node
     *            Root node of the sentence
     * @param formula
     *            String representation of the sentence
     * @return Returns the proofstep that has been added
     */
    public ProofStep addVar(String introducedVariable, SimpleNode rootNode,
	    String formula) {
	ProofStep parentNode = parents.peek();
	ProofStep l = new ProofStep(rootNode, introducedVariable, lines.size(),
		++currentLevel, formula);
	lines.add(l);
	l.parent = parentNode;
	parentNode.subproofs.add(l);
	parents.push(l);
	return l;
    }

    /**
     * Removes the most recent line from the proof
     */
    public void removeStep() {
	ProofStep step = lines.remove(lines.size() - 1);
	if (step.endOfSubproof) {
	    currentLevel++;
	    parents.push(step.parent);
	    step.parent.next = null;
	} else {
	    parents.pop();
	    if (step.parent.level < step.level) {
		currentLevel--;
		step.parent.subproofs.remove(step);
	    } else {
		parents.push(step.parent);
		step.parent.next = null;
	    }
	}
    }

}
