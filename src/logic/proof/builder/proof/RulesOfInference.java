package logic.proof.builder.proof;

import java.util.List;

import android.text.Html;

import logic.proof.builder.parser.JJTParserState;
import logic.proof.builder.parser.Node;
import logic.proof.builder.parser.ParserConstants;
import logic.proof.builder.parser.ParserTreeConstants;
import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Variable;

public class RulesOfInference {

    private static final String OR = Html.fromHtml("&or;").toString();
    private static final String IMPLIES = Html.fromHtml("&rArr;").toString();
    private static final String AND = Html.fromHtml("&and;").toString();
    private static final String NOT = Html.fromHtml("&not;").toString();
    private static final String EQUIVALENT = Html.fromHtml("&hArr;").toString();
    private static final String BOTTOM = Html.fromHtml("&bot;").toString();

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

    public static boolean andElimination1(SimpleNode premise,
	    SimpleNode conclusion) throws Exception {
	SimpleNode leftChild = andElimination1(premise);
	return compareAST(leftChild, conclusion);
    }

    public static SimpleNode andElimination1(SimpleNode premise)
	    throws Exception {
	SimpleNode leftChild = null;
	if (premise.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTAND])) {
	    leftChild = (SimpleNode) premise.jjtGetChild(0);
	} else {
	    throw new Exception(
		    "Justification must be a formula of the form P " + AND
			    + " Q");
	}
	return leftChild;
    }

    public static boolean andElimination2(SimpleNode premise,
	    SimpleNode conclusion) throws Exception {
	SimpleNode rightChild = andElimination1(premise);
	return compareAST(rightChild, conclusion);
    }

    public static SimpleNode andElimination2(SimpleNode premise)
	    throws Exception {
	SimpleNode rightChild = null;
	if (premise.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTAND])) {
	    rightChild = (SimpleNode) premise.jjtGetChild(1);
	} else {
	    throw new Exception(
		    "Justification must be a formula of the form P " + AND
			    + " Q");
	}
	return rightChild;
    }

    public static boolean orIntroduction1(SimpleNode premise,
	    SimpleNode conclusion) throws Exception {
	boolean valid = false;
	if (conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTOR])) {
	    valid = compareAST(premise, (SimpleNode) conclusion.jjtGetChild(0));
	} else {
	    throw new Exception(
		    "Use of this rule results in a formula of the form P " + OR
			    + " Q");
	}
	return valid;
    }

    public static boolean orIntroduction2(SimpleNode premise,
	    SimpleNode conclusion) throws Exception {
	boolean valid = false;
	if (conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTOR])) {
	    valid = compareAST(premise, (SimpleNode) conclusion.jjtGetChild(1));
	} else {
	    throw new Exception(
		    "Use of this rule results in a formula of the form P " + OR
			    + " Q");
	}
	return valid;
    }

    public static boolean orElimination(List<SimpleNode> subproof1,
	    List<SimpleNode> subproof2, SimpleNode disjunction,
	    SimpleNode conclusion) throws Exception {
	SimpleNode implied = orElimination(subproof1, subproof2, disjunction);
	return compareAST(implied, conclusion);
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
		    throw new Exception(
			    "Both subproof1 and subproof2 must end with the same formula");
		}
	    } else {
		throw new Exception(
			"Subproof1 must begin with P and subproof2 must begin with Q");
	    }
	} else {
	    throw new Exception("Justification P V Q must be a disjunction");
	}
	return chi;
    }

    public static void impliesIntroduction(List<SimpleNode> subproof,
	    SimpleNode conclusion) throws Exception {
	SimpleNode implication = impliesIntroduction(subproof);
	if (!compareAST(implication, conclusion)) {
	    throw new Exception("explain");
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
	    throw new Exception("some explanation");
	}
    }

    public static SimpleNode modusPonens(SimpleNode p, SimpleNode implication) {
	SimpleNode conclusion = null;
	if (implication
		.toString()
		.equals(ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTIMPLIES])) {
	    if (implication.jjtGetChild(0).toString().equals(p.toString())) {
		conclusion = (SimpleNode) implication.jjtGetChild(1);
	    }
	}
	return conclusion;
    }

    public static boolean negationIntroduction(List<SimpleNode> subproof,
	    SimpleNode conclusion) {
	SimpleNode notPremise = negationIntroduction(subproof);
	return compareAST(notPremise, conclusion);
    }

    public static SimpleNode negationIntroduction(List<SimpleNode> subproof) {
	SimpleNode notPremise = new SimpleNode(ParserTreeConstants.JJTNOT);
	if (subproof.get(subproof.size() - 1).toString().equals(BOTTOM)) {
	    notPremise.jjtAddChild(subproof.get(0), 0);
	}
	return notPremise;
    }

    public static boolean negationElimination(SimpleNode p, SimpleNode notP,
	    SimpleNode conclusion) throws Exception {
	SimpleNode bottom = negationElimination(p, notP);
	return compareAST(bottom, conclusion);
    }

    public static SimpleNode negationElimination(SimpleNode p, SimpleNode notP)
	    throws Exception {
	SimpleNode bottom = new SimpleNode(ParserTreeConstants.JJTPREDICATE);
	bottom.jjtSetValue(BOTTOM);
	if (!notP.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTNOT])
		|| !compareAST((SimpleNode) notP.jjtGetChild(0), p)) {
	    throw new Exception("Justifications must be of the form P, not P");
	}
	return bottom;
    }

    public static void doubleNegationIntroduction(SimpleNode p,
	    SimpleNode conclusion) throws Exception {
	SimpleNode doubleNegation = doubleNegationIntroduction(p);
	if (!compareAST(doubleNegation, conclusion)) {
	    throw new Exception(
		    "The line you are trying to justify should be a double negation of the chosen justification");
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
		    "Justification must be the chosen line preceeded by two "
			    + NOT + " symbols");

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
	    throw new Exception("Justification for Bottom Elimination must be"
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

    public static SimpleNode forAllElimination(SimpleNode forAll,String variableName) {
	Variable v = ((Variable) forAll.jjtGetValue());
	v.setName(variableName);
    // while (int index = predicate.parameters.indexOf(variable) !=1) {
    // predicate.parameters.set(replacementVariable,index);
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
