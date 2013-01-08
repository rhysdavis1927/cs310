package logic.proof.builder.ROI;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.widget.ArrayAdapter;

import logic.proof.builder.parser.SimpleNode;


public class Proof {

    int proofLength;
    Stack<ProofStep> parents;
    public ArrayList<String> justifications;
    public ArrayList<String> formulae;
    public ArrayList<ProofStep> lines;

    public Proof(Context context) {
	lines = new ArrayList<ProofStep>();
	parents = new Stack<ProofStep>();
	ProofStep rootNode = new ProofStep(new SimpleNode(0),0,0);
	proofLength =0;
	parents.add(rootNode);
	
	justifications = new ArrayList<String>();	
	formulae = new ArrayList<String>();
    }

    public int addStepAsNewLine(SimpleNode formula) {
	proofLength++;
	ProofStep parentNode = parents.pop();
	ProofStep l = new ProofStep(formula,proofLength,parentNode.level);
	lines.add(l);	
	l.parent = parentNode;
	parentNode.next = l;
	parents.push(l);
	return l.level;
    }

    public int addStepAsStartOfSubproof(SimpleNode formula) {
	proofLength++;
	ProofStep parentNode = parents.peek();
	ProofStep l = new ProofStep(formula,proofLength,
		parentNode.level+1);
	lines.add(l);
	
	l.parent = parentNode;
	parentNode.subproofs.add(l);
	parents.push(l);
	return l.level;
    }

    public int addStepAsEndOfSubproof(SimpleNode formula) {
	proofLength++;
	ProofStep parentNode = parents.pop();
	ProofStep l = new ProofStep(formula,proofLength,parentNode.level -1);
	lines.add(l);
	
	parentNode.next = l;
	l.parent = parentNode;
	return l.level;
    }
    
    public void add(String formula,int level) {
	StringBuffer s = new StringBuffer();
	for(int i =0; i <level;i++) {
	    s.append("  ");
	}
	s.append(formula);
	formulae.add(s.toString());
    }

    public class ProofStep {

	public ProofStep parent = null;
	public List<ProofStep> subproofs;
	public ProofStep next = null;
	public SimpleNode formula;
	public Integer lineNumber;
	public int level;

	ProofStep(SimpleNode formula, int lineNumber, int level) {
	    subproofs = new ArrayList<ProofStep>();
	    this.formula = formula;
	    this.lineNumber=lineNumber;
	    this.level = level;
	}

    }
}
