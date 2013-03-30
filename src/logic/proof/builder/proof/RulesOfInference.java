package logic.proof.builder.proof;

import java.util.ArrayList;
import java.util.List;

import logic.proof.builder.gui.ProofBuilderActivity;
import logic.proof.builder.parser.ParserTreeConstants;
import logic.proof.builder.parser.Predicate;
import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Variable;
import android.text.Html;

public class RulesOfInference {

    private static final String OR = Html.fromHtml("&or;").toString();
    private static final String IMPLIES = Html.fromHtml("&rArr;").toString();
    private static final String AND = Html.fromHtml("&and;").toString();
    private static final String NOT = Html.fromHtml("&not;").toString();
    private static final String EQUIVALENT = Html.fromHtml("&hArr;").toString();
    private static final String BOTTOM = Html.fromHtml("&bot;").toString();
    private static final String FOR_ALL = Html.fromHtml("&forall;").toString();
    private static final String THERE_EXISTS = Html.fromHtml("&exist;")
	    .toString();

    private static final String PHI = Html.fromHtml("&Phi;").toString();
    private static final String PSI = Html.fromHtml("&Psi;").toString();
    private static final String CHI = Html.fromHtml("&Chi;").toString();

    public static boolean andIntroduction(SimpleNode p, SimpleNode q,
	    SimpleNode conclusion) {
	SimpleNode conjunction = andIntroduction(p, q);
	return compareAST(conjunction, conclusion);
    }

    public static SimpleNode andIntroduction(SimpleNode p, SimpleNode q) {
	SimpleNode conjunction = new SimpleNode(ParserTreeConstants.JJTAND);
	conjunction.jjtAddChild(p, 0);
	conjunction.jjtAddChild(q, 1);
	return conjunction;
    }

    public static void andElimination1(SimpleNode premise, SimpleNode conclusion)
	    throws Exception {
	SimpleNode leftChild = andElimination1(premise);
	if (!compareAST(leftChild, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be the same as " + PHI);
	}
    }

    public static SimpleNode andElimination1(SimpleNode premise)
	    throws Exception {
	SimpleNode leftChild = null;
	if (premise.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTAND])) {
	    leftChild = (SimpleNode) premise.jjtGetChild(0);
	} else {
	    throw new Exception("Justification must be a formula of the form "
		    + PHI + AND + PSI);
	}
	return leftChild;
    }

    public static void andElimination2(SimpleNode premise, SimpleNode conclusion)
	    throws Exception {
	SimpleNode rightChild = andElimination2(premise);
	if (!compareAST(rightChild, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be the same as " + PSI);
	}
    }

    public static SimpleNode andElimination2(SimpleNode premise)
	    throws Exception {
	SimpleNode rightChild = null;
	if (premise.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTAND])) {
	    rightChild = (SimpleNode) premise.jjtGetChild(1);
	} else {
	    throw new Exception("Justification must be a formula of the form "
		    + PHI + AND + PSI);
	}
	return rightChild;
    }

    public static void orIntroduction1(SimpleNode premise, SimpleNode conclusion)
	    throws Exception {
	if (!conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTOR])) {
	    throw new Exception(
		    "The line you are trying to justify should have the form "
			    + PHI + OR + PSI);
	}
	if (!compareAST(premise, (SimpleNode) conclusion.jjtGetChild(0))) {
	    throw new Exception("The justification should be the same as "
		    + PHI);
	}
    }

    public static void orIntroduction2(SimpleNode premise, SimpleNode conclusion)
	    throws Exception {
	if (!conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTOR])) {
	    throw new Exception(
		    "The line you are trying to justify should have the form "
			    + PHI + OR + PSI);
	}
	if (!compareAST(premise, (SimpleNode) conclusion.jjtGetChild(1))) {
	    throw new Exception("The justification should have the form " + PSI);
	}
    }

    public static void orElimination(List<SimpleNode> subproof1,
	    List<SimpleNode> subproof2, SimpleNode disjunction,
	    SimpleNode conclusion) throws Exception {
	SimpleNode implied = orElimination(subproof1, subproof2, disjunction);
	if (!compareAST(implied, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be the same as "
			    + CHI);
	}
    }

    public static SimpleNode orElimination(List<SimpleNode> subproof1,
	    List<SimpleNode> subproof2, SimpleNode disjunction)
	    throws Exception {
	SimpleNode chi = null;

	if (disjunction.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTOR])) {
	    String phi = disjunction.jjtGetChild(0).toString();
	    String psi = disjunction.jjtGetChild(1).toString();

	    if (subproof1.get(0).toString().equals(phi)
		    && subproof2.get(0).toString().equals(psi)) {
		if (subproof1.get(subproof1.size() - 1).equals(
			subproof1.get(subproof1.size() - 1))) {
		    chi = subproof1.get(subproof1.size() - 1);
		} else {
		    throw new Exception("Subproof " + PHI
			    + ProofBuilderActivity.TURNSTILE + CHI
			    + " and subproof " + PSI
			    + ProofBuilderActivity.TURNSTILE + CHI
			    + " must end with the same formula " + CHI);
		}
	    } else {
		throw new Exception("Subproof " + PHI
			+ ProofBuilderActivity.TURNSTILE + CHI
			+ " must begin with " + PHI + " and subproof " + PSI
			+ ProofBuilderActivity.TURNSTILE + CHI
			+ " must begin with " + PSI);
	    }
	} else {
	    throw new Exception("Justification " + PHI + OR + PSI
		    + " must be a disjunction");
	}
	return chi;
    }

    public static void impliesIntroduction(List<SimpleNode> subproof,
	    SimpleNode conclusion) throws Exception {
	SimpleNode implication = impliesIntroduction(subproof);
	if (!compareAST(implication, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be of the form "
			    + PHI + IMPLIES + PSI);
	}

    }

    public static SimpleNode impliesIntroduction(List<SimpleNode> subproof) {
	SimpleNode implication = new SimpleNode(ParserTreeConstants.JJTIMPLIES);
	implication.jjtAddChild(subproof.get(0), 0);
	implication.jjtAddChild(subproof.get(subproof.size() - 1), 1);
	return implication;
    }

    public static void modusPonens(SimpleNode p, SimpleNode implication,
	    SimpleNode conclusion) throws Exception {
	SimpleNode q = modusPonens(p, implication);
	if (!compareAST(q, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be the same as "
			    + PSI);
	}
    }

    public static SimpleNode modusPonens(SimpleNode p, SimpleNode implication)
	    throws Exception {
	SimpleNode conclusion = null;
	if (!(implication.toString()
		.equals(ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTIMPLIES])
		&& compareAST((SimpleNode) implication.jjtGetChild(0),p))) {
	    throw new Exception("Justification must be of the form " + PHI
		    + "; " + PHI + IMPLIES + PSI);
	}
	conclusion = (SimpleNode) implication.jjtGetChild(1);
	return conclusion;
    }

    public static void negationIntroduction(List<SimpleNode> subproof,
	    SimpleNode conclusion) throws Exception {
	SimpleNode notPremise = negationIntroduction(subproof);
	if (!compareAST(notPremise, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be "+ NOT + 
			    PHI + " where " + PHI + " is the first line of the subproof");
	}
    }

    public static SimpleNode negationIntroduction(List<SimpleNode> subproof)
	    throws Exception {
	SimpleNode notPremise = new SimpleNode(ParserTreeConstants.JJTNOT);
	if (!subproof.get(subproof.size() - 1).toString().equals(BOTTOM)) {
	    throw new Exception("The last line of the subproof must be "
		    + BOTTOM);
	}
	notPremise.jjtAddChild(subproof.get(0), 0);
	return notPremise;
    }

    public static void negationElimination(SimpleNode p, SimpleNode notP,
	    SimpleNode conclusion) throws Exception {
	SimpleNode bottom = negationElimination(p, notP);
	if (!compareAST(bottom, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be only "
			    + BOTTOM);
	}
    }

    public static SimpleNode negationElimination(SimpleNode p, SimpleNode notP)
	    throws Exception {
	SimpleNode bottom = new SimpleNode(ParserTreeConstants.JJTPREDICATE);
	bottom.jjtSetValue(BOTTOM);
	if (!notP.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTNOT])
		|| !compareAST((SimpleNode) notP.jjtGetChild(0), p)) {
	    throw new Exception("Justifications must be of the form " + PHI
		    + "; " + NOT + PHI);
	}
	return bottom;
    }

    public static void doubleNegationIntroduction(SimpleNode p,
	    SimpleNode conclusion) throws Exception {
	SimpleNode doubleNegation = doubleNegationIntroduction(p);
	if (!compareAST(doubleNegation, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be a double negation of the chosen justification "
			    + PHI);
	}

    }

    public static SimpleNode doubleNegationIntroduction(SimpleNode p) {
	SimpleNode not1 = new SimpleNode(ParserTreeConstants.JJTNOT);
	SimpleNode not2 = new SimpleNode(ParserTreeConstants.JJTNOT);
	not2.jjtAddChild(not1, 0);
	not1.jjtAddChild(p, 0);
	return not2;

    }

    public static void doubleNegationElimination(SimpleNode p,
	    SimpleNode conclusion) throws Exception {
	if (!compareAST(doubleNegationElimination(p), conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be the same as "
			    + PHI);

	}

    }

    public static SimpleNode doubleNegationElimination(SimpleNode p)
	    throws Exception {
	if (p.jjtGetNumChildren() > 0) {
	    p = (SimpleNode) p.jjtGetChild(0);
	} else {
	    throw new Exception("Justification must begin with two " + NOT
		    + " symbols");

	}
	if (p.jjtGetNumChildren() > 0) {
	    p = (SimpleNode) p.jjtGetChild(0);
	} else {
	    throw new Exception("Justification must begin with two " + NOT
		    + " symbols");

	}
	return p;

    }

    public static void bottomElimination(SimpleNode p, SimpleNode conclusion)
	    throws Exception {
	if (!p.toString().equals(BOTTOM)) {
	    throw new Exception("Justification for Bottom Elimination must be "
		    + BOTTOM);
	}

    }

    public static boolean copy(SimpleNode p, SimpleNode conclusion)
	    throws Exception {
	if (compareAST(p, conclusion)) {
	    return true;
	} else {
	    throw new Exception(
		    "Justification must be same as selected formula");
	}
    }

    public static void equalsIntroduction(SimpleNode conclusion)
	    throws Exception {
	SimpleNode t1 = (SimpleNode) conclusion.jjtGetChild(0);
	SimpleNode t2 = (SimpleNode) conclusion.jjtGetChild(1);
	if (!conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTEQUALS])) {
	    throw new Exception(
		    "The line you are trying to justify must be of the form t = t");
	}
	if (!compareAST(t1, t2)) {
	    throw new Exception(
		    "The line you are trying to justify must be of the form t = t");
	}

    }

    public static void equalsElimination(SimpleNode equals,
	    SimpleNode statement, Variable variable, SimpleNode conclusion)
	    throws Exception {
	if (!equals.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTEQUALS])) {
	    throw new Exception(
		    "Justification must must be of the form t1 = t2");
	}
	String newName = (String) ((SimpleNode) equals.jjtGetChild(1))
		.jjtGetValue().toString();
	if (!compareEqualsElim(statement, conclusion, variable, newName)) {
	    throw new Exception("The line you are trying to justify should have the form "
		    + PHI + "[" + newName + "/" + variable + "]");
	}

    }

    // Compares tree a and b, allowing subVariable to be replaced with newName
    public static boolean compareEqualsElim(SimpleNode a, SimpleNode b,
	    Variable subVariable, String newName) {
	if ((a.jjtGetValue() instanceof Predicate)
		&& (b.jjtGetValue() instanceof Predicate)) {
	    Predicate p = ((Predicate) a.jjtGetValue());
	    Predicate q = ((Predicate) b.jjtGetValue());
	    if (!p.getName().equals(q.getName())) {
		return false;
	    }
	    for (int i = 0; i < p.parameters.size(); i++) {
		if (!q.parameters.get(i).equals(p.parameters.get(i))) {
		    if (!(p.parameters.get(i) == subVariable && q.parameters
			    .get(i).getName().equals(newName))) {
			return false;
		    }
		}
	    }
	} else if (a.jjtGetValue() instanceof Variable
		&& b.jjtGetValue() instanceof Variable) {
	    Variable p = ((Variable) a.jjtGetValue());
	    Variable q = ((Variable) b.jjtGetValue());
	    if (!q.equals(p)) {
		if (!(p.equals(subVariable) && q.getName().equals(newName))) {
		    return false;
		}
	    }
	} else if (!a.toString().equals(b.toString())) {
	    return false;
	} else if (a.jjtGetNumChildren() != b.jjtGetNumChildren()) {
	    return false;
	}
	int numberOfChildren = a.jjtGetNumChildren();
	for (int i = 0; i < numberOfChildren; i++) {
	    if (!compareEqualsElim((SimpleNode) a.jjtGetChild(i),
		    (SimpleNode) b.jjtGetChild(i), subVariable, newName))
		return false;
	}
	return true;
    }

    public static void forAllIntroduction(SimpleNode p, Variable variable,
	    SimpleNode conclusion) throws Exception {
	// Intro of new variable, this variable in proof
	String origVarName = variable.getName();
	String quantifiedVarName = ((Variable) conclusion.jjtGetValue())
		.getName();
	variable.setName(quantifiedVarName);
	if (!compareAST(p, (SimpleNode) conclusion.jjtGetChild(0))) {
	    throw new Exception(
		    "The line you are trying to prove should have the form: "
			    + FOR_ALL + quantifiedVarName + p);
	}
	variable.setName(origVarName);
    }

    public static void forAllElimination(SimpleNode forAll,
	    SimpleNode conclusion) throws Exception {
	if (!forAll.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTFORALL])) {
	    throw new Exception("Justification must start with " + FOR_ALL);
	}
	Variable quantifiedVariable = ((Variable) forAll.jjtGetValue());
	SimpleNode quantifiedFormula = (SimpleNode) forAll.jjtGetChild(0);
	if (!compareForAllElim(quantifiedFormula, conclusion,
		quantifiedVariable, null)) {
	    throw new Exception("Only the quantified variable may be changed");
	}
    }

    // Compares trees a and b allowing subVariable to be replace
    // with a possibly unspecified newName
    public static boolean compareForAllElim(SimpleNode a, SimpleNode b,
	    Variable subVariable, Variable newName) {
	if ((a.jjtGetValue() instanceof Predicate)
		&& (b.jjtGetValue() instanceof Predicate)) {
	    Predicate p = ((Predicate) a.jjtGetValue());
	    Predicate q = ((Predicate) b.jjtGetValue());
	    if (!p.getName().equals(q.getName())) {
		return false;
	    }
	    for (int i = 0; i < p.parameters.size(); i++) {
		if (!q.parameters.get(i).equals(p.parameters.get(i))) {
		    if (p.parameters.get(i) == subVariable) {
			if (newName == null) {
			    newName = q.parameters.get(i);
			} else if (!(q.parameters.get(i).equals(newName))) {
			    return false;
			}

		    } else {
			return false;
		    }
		}
	    }
	} else if (!a.toString().equals(b.toString())) {
	    return false;
	} else if (a.jjtGetNumChildren() != b.jjtGetNumChildren()) {
	    return false;
	}
	int numberOfChildren = a.jjtGetNumChildren();
	for (int i = 0; i < numberOfChildren; i++) {
	    if (!compareForAllElim((SimpleNode) a.jjtGetChild(i),
		    (SimpleNode) b.jjtGetChild(i), subVariable, newName))
		return false;
	}
	return true;
    }

    public static void thereExistsIntroduction(SimpleNode p,
	    SimpleNode conclusion) throws Exception {

	if (!conclusion
		.toString()
		.equals(ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTTHEREEXISTS])) {
	    throw new Exception(
		    "The line you are trying to prove should be of the form: "
			    + THERE_EXISTS + "x P(x)");
	}
	Variable quantifiedVariable = (Variable) conclusion.jjtGetValue();
	if (!compareThereExistsIntro(p, (SimpleNode) conclusion.jjtGetChild(0),
		null, quantifiedVariable)) {
	    throw new Exception(
		    "The line you are trying prove should have the form: "
			    + THERE_EXISTS + quantifiedVariable + p + "["
			    + quantifiedVariable + "/t]");
	}
    }

    // Compares trees a and b allowing an unspecified subVariable to be replaced
    // by quantifiedVariable
    public static boolean compareThereExistsIntro(SimpleNode a, SimpleNode b,
	    Variable subVariable, Variable quantifiedVariable) {
	if ((a.jjtGetValue() instanceof Predicate)
		&& (b.jjtGetValue() instanceof Predicate)) {
	    Predicate p = ((Predicate) a.jjtGetValue());
	    Predicate q = ((Predicate) b.jjtGetValue());
	    if (!p.getName().equals(q.getName())) {
		return false;
	    }
	    for (int i = 0; i < p.parameters.size(); i++) {
		if (!q.parameters.get(i).equals(p.parameters.get(i))) {
		    if (q.parameters.get(i) == quantifiedVariable) {
			if (subVariable == null) {
			    subVariable = p.parameters.get(i);
			} else if (!(q.parameters.get(i).equals(subVariable))) {
			    return false;
			}
		    } else {
			return false;
		    }
		}
	    }
	} else if (!a.toString().equals(b.toString())) {
	    return false;
	} else if (a.jjtGetNumChildren() != b.jjtGetNumChildren()) {
	    return false;
	}
	int numberOfChildren = a.jjtGetNumChildren();
	for (int i = 0; i < numberOfChildren; i++) {
	    if (!compareForAllElim((SimpleNode) a.jjtGetChild(i),
		    (SimpleNode) b.jjtGetChild(i), subVariable,
		    quantifiedVariable))
		return false;
	}
	return true;
    }

    public static void thereExistsElimination(SimpleNode p,
	    ArrayList<SimpleNode> subproof, String variableName,
	    SimpleNode conclusion) throws Exception {
	if (!p.toString()
		.equals(ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTTHEREEXISTS])) {
	    throw new Exception("The jusitification should be of the form: "
		    + THERE_EXISTS + "x" + PHI);
	}

	Variable var = (Variable) p.jjtGetValue();
	String quantifiedName = var.toString();
	if (!compareThereExistsElim((SimpleNode) p.jjtGetChild(0),
		subproof.get(0), var, variableName)) {
	    throw new Exception(
		    "The first line of the subproof should be of the form "
			    + PHI + "[" + variableName + "/" + quantifiedName
			    + "]");
	}
	if (!compareAST(subproof.get(subproof.size() - 1), conclusion)) {
	    throw new Exception(
		    "The line you are justifying should be the same as the last line of the subproof.");
	}
    }

    // Compares tree a and b, allowing subVariable to be replaced with newName
    public static boolean compareThereExistsElim(SimpleNode a, SimpleNode b,
	    Variable subVariable, String newName) {
	if ((a.jjtGetValue() instanceof Predicate)
		&& (b.jjtGetValue() instanceof Predicate)) {
	    Predicate p = ((Predicate) a.jjtGetValue());
	    Predicate q = ((Predicate) b.jjtGetValue());
	    if (!p.getName().equals(q.getName())) {
		return false;
	    }
	    for (int i = 0; i < p.parameters.size(); i++) {
		if (p.parameters.get(i).equals(subVariable)) {
		    if (!q.parameters.get(i).getName().equals(newName)) {
			return false;
		    }
		} else if (!q.parameters.get(i).equals(p.parameters.get(i))) {
		    return false;
		}
	    }
	} else if (!a.toString().equals(b.toString())) {
	    return false;
	} else if (a.jjtGetNumChildren() != b.jjtGetNumChildren()) {
	    return false;
	}
	int numberOfChildren = a.jjtGetNumChildren();
	for (int i = 0; i < numberOfChildren; i++) {
	    if (!compareThereExistsElim((SimpleNode) a.jjtGetChild(i),
		    (SimpleNode) b.jjtGetChild(i), subVariable, newName))
		return false;
	}
	return true;
    }

    public static boolean compareAST(SimpleNode a, SimpleNode b) {
	if (!a.toString().equals(b.toString())) {
	    return false;
	} else if (a.jjtGetNumChildren() != b.jjtGetNumChildren()) {
	    return false;
	}
	int numberOfChildren = a.jjtGetNumChildren();
	for (int i = 0; i < numberOfChildren; i++) {
	    if (!compareAST((SimpleNode) a.jjtGetChild(i),
		    (SimpleNode) b.jjtGetChild(i)))
		return false;
	}
	return true;
    }
}
