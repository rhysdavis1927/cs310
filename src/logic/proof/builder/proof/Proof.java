package logic.proof.builder.proof;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.widget.ArrayAdapter;

import logic.proof.builder.parser.Predicate;
import logic.proof.builder.parser.SimpleNode;


public class Proof {

    Stack<ProofStep> parents;
    public ArrayList<ProofStep> lines;
    public int currentLevel;
    public ArrayList<String> predicates;

    public Proof(Context context) {
	predicates = new ArrayList<String>();
	predicates.add("P");
	predicates.add("P(x)");
	predicates.add("P(y)");
	predicates.add("P(x,y)");
	predicates.add("Q");
	predicates.add("Q(x)");
	predicates.add("Q(x,y)");
	lines = new ArrayList<ProofStep>();
	parents = new Stack<ProofStep>();
	ProofStep rootNode = new ProofStep(new SimpleNode(0),0,0,"");
	parents.add(rootNode);
	currentLevel =0;
	rootNode.justification= "root node";
    }

    public ProofStep addStepAsNewLine(SimpleNode node,String formula) {
	ProofStep parentNode = parents.pop();
	ProofStep l = new ProofStep(node,lines.size(),currentLevel,formula);
	lines.add(l);	
	l.parent = parentNode;
	parentNode.next = l;
	parents.push(l);
	return l;
    }

    public ProofStep addStepAsStartOfSubproof(SimpleNode node,String formula) {
	ProofStep parentNode = parents.peek();
	ProofStep l = new ProofStep(node,lines.size(),
		++currentLevel, formula);
	lines.add(l);	
	l.parent = parentNode;
	parentNode.subproofs.add(l);
	parents.push(l);
	l.justification = "Assumption";
	return l;
    }

    public ProofStep addStepAsEndOfSubproof(SimpleNode node,String formula) {
	ProofStep parentNode = parents.pop();
	ProofStep l = new ProofStep(node,lines.size(),currentLevel--,formula);
	lines.add(l);	
	parentNode.next = l;
	l.parent = parentNode;
	l.endOfSubproof=true;
	return l;
    }
    
    public void removeStep() {
	ProofStep step =lines.remove(lines.size()-1);
	if(step.endOfSubproof) {
	    currentLevel++;
	    parents.push(step.parent);
	    step.parent.next =null;
	}
	else {
	    parents.pop();
	    if (step.parent.level < step.level) {
		currentLevel--;
		step.parent.subproofs.remove(step);
	    }
	    else{
		parents.push(step.parent);
		step.parent.next = null;
	    }
	}
    }
    
}
