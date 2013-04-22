package logic.proof.builder.proof;

import java.util.ArrayList;
import java.util.List;

import logic.proof.builder.exceptions.ConclusionException;
import logic.proof.builder.exceptions.PremiseException;
import logic.proof.builder.gui.ProofBuilderActivity;
import logic.proof.builder.parser.ParserTreeConstants;
import logic.proof.builder.parser.Predicate;
import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Variable;
import android.text.Html;

/**
 * Contains methods for the rules of inference of first-order logic
 * 
 * @author Rhys Davis
 * 
 */
public final class RulesOfInference {

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

    /**
     * 
     * @param p
     *            The root node of a sentence of FOL
     * @param q
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */

    public static void andIntroduction(SimpleNode p, SimpleNode q,
	    SimpleNode conclusion) throws Exception {
	SimpleNode conjunction = andIntroduction(p, q);
	if (compareAST(conjunction, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should have the form " + PHI
			    + AND + PSI);
	}

    }

    /**
     * @param p
     *            The root node of a sentence of FOL
     * @param q
     *            The root node of a sentence of FOL
     * @return the root node of a conjunction of the given
     *         parameters
     */
    public static SimpleNode andIntroduction(SimpleNode p, SimpleNode q) {
	SimpleNode conjunction = new SimpleNode(ParserTreeConstants.JJTAND);
	conjunction.jjtAddChild(p, 0);
	conjunction.jjtAddChild(q, 1);
	return conjunction;
    }

    /**
     * @param premise
     *            The root node of a conjunction
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void andElimination1(SimpleNode premise, SimpleNode conclusion)
	    throws ConclusionException, PremiseException {
	SimpleNode leftChild = andElimination1(premise);
	if (!compareAST(leftChild, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be the same as " + PHI);
	}
    }

    /**
     * @param premise
     *            The root node of a conjunctive sentence of FOL
     * @return the left child node of the given conjunction
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static SimpleNode andElimination1(SimpleNode premise)
	    throws PremiseException {
	SimpleNode leftChild = null;
	if (premise.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTAND])) {
	    leftChild = (SimpleNode) premise.jjtGetChild(0);
	} else {
	    throw new PremiseException("Premise must be a formula of the form "
		    + PHI + AND + PSI);
	}
	return leftChild;
    }

    /**
     * @param premise
     *            The root node of a conjunction
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void andElimination2(SimpleNode premise, SimpleNode conclusion)
	    throws ConclusionException, PremiseException {
	SimpleNode rightChild = andElimination2(premise);
	if (!compareAST(rightChild, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be the same as " + PSI);
	}
    }

    /**
     * @param premise
     *            The root node of a conjunction
     * @return the right child node of the given conjunction
     * @throws PremiseException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static SimpleNode andElimination2(SimpleNode premise)
	    throws PremiseException {
	SimpleNode rightChild = null;
	if (premise.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTAND])) {
	    rightChild = (SimpleNode) premise.jjtGetChild(1);
	} else {
	    throw new PremiseException("Premise must be a formula of the form "
		    + PHI + AND + PSI);
	}
	return rightChild;
    }

    /**
     * @param premise
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified, must be a
     *            disjunctive sentence
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void orIntroduction1(SimpleNode premise, SimpleNode conclusion)
	    throws PremiseException, ConclusionException {
	if (!conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTOR])) {
	    throw new ConclusionException(
		    "The conclusion of this rule should have the form " + PHI
			    + OR + PSI);
	}
	if (!compareAST(premise, (SimpleNode) conclusion.jjtGetChild(0))) {
	    throw new PremiseException("The premise should be the same as "
		    + PHI);
	}
    }

    /**
     * @param premise
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified, must be a
     *            disjunctive sentence
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void orIntroduction2(SimpleNode premise, SimpleNode conclusion)
	    throws PremiseException, ConclusionException {
	if (!conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTOR])) {
	    throw new ConclusionException(
		    "The conclusion of this rule should have the form " + PHI
			    + OR + PSI);
	}
	if (!compareAST(premise, (SimpleNode) conclusion.jjtGetChild(1))) {
	    throw new PremiseException("The premise should have the form "
		    + PSI);
	}
    }

    /**
     * @param subproof1
     *            A list of
     * @param subproof2
     *            The root node of the sentence being justified
     * @param disjunction
     *            The root node of a disjunction of sentences
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void orElimination(List<SimpleNode> subproof1,
	    List<SimpleNode> subproof2, SimpleNode disjunction,
	    SimpleNode conclusion) throws ConclusionException, PremiseException {
	SimpleNode implied = orElimination(subproof1, subproof2, disjunction);
	if (!compareAST(implied, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be the same as " + CHI);
	}
    }

    /**
     * @param subproof1
     *            A subproof starting with the LHS of the disjunction
     * @param subproof2
     *            A subproof starting with the RHS of the disjunction and ending with the same sentence as the previous subproof
     * @param disjunction
     *            The root node of a disjunction of sentences
     * @return the root node of the sentence that both subprrofs end with
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static SimpleNode orElimination(List<SimpleNode> subproof1,
	    List<SimpleNode> subproof2, SimpleNode disjunction)
	    throws PremiseException {
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
		    throw new PremiseException("Subproof " + PHI
			    + ProofBuilderActivity.TURNSTILE + CHI
			    + " and subproof " + PSI
			    + ProofBuilderActivity.TURNSTILE + CHI
			    + " must end with the same formula " + CHI);
		}
	    } else {
		throw new PremiseException("Subproof " + PHI
			+ ProofBuilderActivity.TURNSTILE + CHI
			+ " must begin with " + PHI + " and subproof " + PSI
			+ ProofBuilderActivity.TURNSTILE + CHI
			+ " must begin with " + PSI);
	    }
	} else {
	    throw new PremiseException("Premise " + PHI + OR + PSI
		    + " must be a disjunction");
	}
	return chi;
    }

    /**
     * @param premise
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static void impliesIntroduction(List<SimpleNode> subproof,
	    SimpleNode conclusion) throws ConclusionException {
	SimpleNode implication = impliesIntroduction(subproof);
	if (!compareAST(implication, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be of the form " + PHI
			    + IMPLIES + PSI);
	}

    }

    /**
     * 
     * @param subproof Any subproof
     * @return an implication where the LHS is the first line of the given subproof and the RHS is the last line
     */

    public static SimpleNode impliesIntroduction(List<SimpleNode> subproof) {
	SimpleNode implication = new SimpleNode(ParserTreeConstants.JJTIMPLIES);
	implication.jjtAddChild(subproof.get(0), 0);
	implication.jjtAddChild(subproof.get(subproof.size() - 1), 1);
	return implication;
    }

    /**
     * @param p
     *            The root node of a sentence of FOL
     * @param implication
     *            The root node of a material implication sentence. The LHS
     *            should be the previous argument.
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void modusPonens(SimpleNode p, SimpleNode implication,
	    SimpleNode conclusion) throws ConclusionException, PremiseException {
	SimpleNode q = modusPonens(p, implication);
	if (!compareAST(q, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be the same as " + PSI);
	}
    }

    /**
     * 
     * @param p
     *            The root node of a sentence of FOL
     * @param implication
     *            The root node of a material implication sentence. The LHS
     *            should be the previous argument.
     * @return the RHS of the implication
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */

    public static SimpleNode modusPonens(SimpleNode p, SimpleNode implication)
	    throws PremiseException {
	SimpleNode conclusion = null;
	if (!(implication
		.toString()
		.equals(ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTIMPLIES]) && compareAST(
		(SimpleNode) implication.jjtGetChild(0), p))) {
	    throw new PremiseException("Premise must be of the form " + PHI
		    + "; " + PHI + IMPLIES + PSI);
	}
	conclusion = (SimpleNode) implication.jjtGetChild(1);
	return conclusion;
    }

    /**
     * @param premise
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified, should start
     *            with a negation
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static void negationIntroduction(List<SimpleNode> subproof,
	    SimpleNode conclusion) throws ConclusionException, PremiseException {
	SimpleNode notPremise = negationIntroduction(subproof);
	if (!compareAST(notPremise, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be " + NOT + PHI
			    + " where " + PHI
			    + " is the first line of the subproof");
	}
    }

    /**
     * 
     * @param subproof
     *            A list of proofsteps ending with bottom
     * @return the negation of the first line of the subproof
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static SimpleNode negationIntroduction(List<SimpleNode> subproof)
	    throws PremiseException {
	SimpleNode notPremise = new SimpleNode(ParserTreeConstants.JJTNOT);
	if (!subproof.get(subproof.size() - 1).toString().equals(BOTTOM)) {
	    throw new PremiseException("The last line of the subproof must be "
		    + BOTTOM);
	}
	notPremise.jjtAddChild(subproof.get(0), 0);
	return notPremise;
    }

    /**
     * @param p
     *            The root node of a sentence of FOL
     * @param notP
     *            The negation of the previous argument
     * @param conclusion
     *            The root node of the sentence being justified, should only be
     *            bottom
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static void negationElimination(SimpleNode p, SimpleNode notP,
	    SimpleNode conclusion) throws PremiseException, ConclusionException {
	SimpleNode bottom = negationElimination(p, notP);
	if (!compareAST(bottom, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be only " + BOTTOM);
	}
    }

    /**
     * 
     * @param p
     *            The root node of a sentence of FOL
     * @param notP
     *            The negation of the previous argument
     * @return bottom
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */

    public static SimpleNode negationElimination(SimpleNode p, SimpleNode notP)
	    throws PremiseException {
	SimpleNode bottom = new SimpleNode(ParserTreeConstants.JJTPREDICATE);
	bottom.jjtSetValue(BOTTOM);
	if (!notP.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTNOT])
		|| !compareAST((SimpleNode) notP.jjtGetChild(0), p)) {
	    throw new PremiseException("Premises must be of the form " + PHI
		    + "; " + NOT + PHI);
	}
	return bottom;
    }

    /**
     * @param p
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified, should start
     *            with double negation
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static void doubleNegationIntroduction(SimpleNode p,
	    SimpleNode conclusion) throws ConclusionException {
	SimpleNode doubleNegation = doubleNegationIntroduction(p);
	if (!compareAST(doubleNegation, conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be a double negation of the chosen justification "
			    + PHI);
	}

    }

    /**
     * 
     * @param p
     *            The root node of a sentence of FOL
     * @return the premise with two negations appended to the start
     */

    public static SimpleNode doubleNegationIntroduction(SimpleNode p) {
	SimpleNode not1 = new SimpleNode(ParserTreeConstants.JJTNOT);
	SimpleNode not2 = new SimpleNode(ParserTreeConstants.JJTNOT);
	not2.jjtAddChild(not1, 0);
	not1.jjtAddChild(p, 0);
	return not2;

    }

    /**
     * @param premise
     *            The root node of a sentence of FOL starting with two negations
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void doubleNegationElimination(SimpleNode p,
	    SimpleNode conclusion) throws ConclusionException, PremiseException {
	if (!compareAST(doubleNegationElimination(p), conclusion)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should be the same as " + PHI);

	}

    }

    /**
     * 
     * @param p
     *            The root node of a sentence of FOL starting with two negations
     * @return The premise without the first two negations
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static SimpleNode doubleNegationElimination(SimpleNode p)
	    throws PremiseException {
	if (p.jjtGetNumChildren() > 0) {
	    p = (SimpleNode) p.jjtGetChild(0);
	} else {
	    throw new PremiseException("Premise must begin with two " + NOT
		    + " symbols");

	}
	if (p.jjtGetNumChildren() > 0) {
	    p = (SimpleNode) p.jjtGetChild(0);
	} else {
	    throw new PremiseException("Premise must begin with two " + NOT
		    + " symbols");

	}
	return p;

    }

    /**
     * @param premise
     *            Bottom only
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void bottomElimination(SimpleNode p, SimpleNode conclusion)
	    throws PremiseException {
	if (!p.toString().equals(BOTTOM)) {
	    throw new PremiseException(
		    "Premise for Bottom Elimination must be " + BOTTOM);
	}

    }

    /**
     * @param premise
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static boolean copy(SimpleNode p, SimpleNode conclusion)
	    throws PremiseException {
	if (compareAST(p, conclusion)) {
	    return true;
	} else {
	    throw new PremiseException("Premise must be same as conclusion");
	}
    }

    /**
     * 
     * @param conclusion
     *            The root node of the sentence being justified, must have the
     *            form t = t
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static void equalsIntroduction(SimpleNode conclusion)
	    throws ConclusionException {
	SimpleNode t1 = (SimpleNode) conclusion.jjtGetChild(0);
	SimpleNode t2 = (SimpleNode) conclusion.jjtGetChild(1);
	if (!conclusion.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTEQUALS])) {
	    throw new ConclusionException(
		    "The conclusion of this rule must be of the form t = t");
	}
	if (!compareAST(t1, t2)) {
	    throw new ConclusionException(
		    "The conclusion of this rule must be of the form t = t");
	}

    }

    /**
     * 
     * @param equals
     *            A sentence of the form t1 = t2
     * @param statement
     *            A sentence of FOL, should contain t1
     * @param variable
     *            The free variable t1
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     */
    public static void equalsElimination(SimpleNode equals,
	    SimpleNode statement, Variable variable, SimpleNode conclusion)
	    throws ConclusionException, PremiseException {
	if (!equals.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTEQUALS])) {
	    throw new PremiseException(
		    "Premise must must be of the form t1 = t2");
	}
	String newName = (String) ((SimpleNode) equals.jjtGetChild(1))
		.jjtGetValue().toString();
	if (!compareEqualsElim(statement, conclusion, variable, newName)) {
	    throw new ConclusionException(
		    "The conclusion of this rule should have the form " + PHI
			    + "[" + newName + "/" + variable + "]");
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

    /**
     * 
     * @param p
     *            The final line of the subproof, should contain the introduced
     *            variable
     * @param variable
     *            The introduced variable
     * @param conclusion
     *            The universally quantified version of the premise
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */

    public static void forAllIntroduction(SimpleNode p, Variable variable,
	    SimpleNode conclusion) throws ConclusionException {
	// Intro of new variable, this variable in proof
	String origVarName = variable.getName();
	String quantifiedVarName = ((Variable) conclusion.jjtGetValue())
		.getName();
	variable.setName(quantifiedVarName);
	if (!compareAST(p, (SimpleNode) conclusion.jjtGetChild(0))) {
	    throw new ConclusionException(
		    "The conclusion should have the form: " + FOR_ALL
			    + quantifiedVarName + p);
	}
	variable.setName(origVarName);
    }

    /**
     * @param forAll
     *            The root node of universally quantified sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified, should be the
     *            unquantified version of the premise
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */

    public static void forAllElimination(SimpleNode forAll,
	    SimpleNode conclusion) throws PremiseException, ConclusionException {
	if (!forAll.toString().equals(
		ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTFORALL])) {
	    throw new PremiseException("Premise must start with " + FOR_ALL);
	}
	Variable quantifiedVariable = ((Variable) forAll.jjtGetValue());
	SimpleNode quantifiedFormula = (SimpleNode) forAll.jjtGetChild(0);
	if (!compareForAllElim(quantifiedFormula, conclusion,
		quantifiedVariable, null)) {
	    throw new ConclusionException(
		    "Only the quantified variable may be changed");
	}
    }

    // Compares trees a and b allowing subVariable to be replace
    // with a possibly unspecified newName
    private static boolean compareForAllElim(SimpleNode a, SimpleNode b,
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

    /**
     * @param premise
     *            The root node of a sentence of FOL
     * @param conclusion
     *            The root node of the sentence being justified, should begin
     *            with an existential quantifer
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static void existsIntroduction(SimpleNode p, SimpleNode conclusion)
	    throws ConclusionException {

	if (!conclusion
		.toString()
		.equals(ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTTHEREEXISTS])) {
	    throw new ConclusionException(
		    "The conclusion should be of the form: " + THERE_EXISTS
			    + "x P(x)");
	}
	Variable quantifiedVariable = (Variable) conclusion.jjtGetValue();
	if (!compareThereExistsIntro(p, (SimpleNode) conclusion.jjtGetChild(0),
		null, quantifiedVariable)) {
	    throw new ConclusionException(
		    "The conclusion should have the form: " + THERE_EXISTS
			    + quantifiedVariable + p + "[" + quantifiedVariable
			    + "/t]");
	}
    }

    // Compares trees a and b allowing an unspecified subVariable to be replaced
    // by quantifiedVariable
    private static boolean compareThereExistsIntro(SimpleNode a, SimpleNode b,
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

    /**
     * 
     * @param p
     *            The root node of a sentence of FOL, should be existentially
     *            quantified
     * @param subproof a Subproof starting with a sentence in which the existentially quantified variable of the premise is named and ends in any sentence of FOL
     * @param variableName The name of the variable that is introduced
     * @param conclusion
     *            The root node of the sentence being justified
     * @throws PremiseException
     *             If premises are not of the correct form for this rule
     * @throws ConclusionException
     *             If conclusion does not follow from using rule on given
     *             arguments
     */
    public static void existsElimination(SimpleNode p,
	    ArrayList<SimpleNode> subproof, String variableName,
	    SimpleNode conclusion) throws PremiseException, ConclusionException {
	if (!p.toString()
		.equals(ParserTreeConstants.jjtNodeName[ParserTreeConstants.JJTTHEREEXISTS])) {
	    throw new PremiseException("The premise should be of the form: "
		    + THERE_EXISTS + "x" + PHI);
	}

	Variable var = (Variable) p.jjtGetValue();
	String quantifiedName = var.toString();
	if (!compareExistsElim((SimpleNode) p.jjtGetChild(0), subproof.get(0),
		var, variableName)) {
	    throw new PremiseException(
		    "The first line of the subproof should be of the form "
			    + PHI + "[" + variableName + "/" + quantifiedName
			    + "]");
	}
	if (!compareAST(subproof.get(subproof.size() - 1), conclusion)) {
	    throw new ConclusionException(
		    "The conclusion should be the same as the last line of the subproof.");
	}
    }

    // Compares tree a and b, allowing subVariable to be replaced with newName
    private static boolean compareExistsElim(SimpleNode a, SimpleNode b,
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
	    if (!compareExistsElim((SimpleNode) a.jjtGetChild(i),
		    (SimpleNode) b.jjtGetChild(i), subVariable, newName))
		return false;
	}
	return true;
    }

    private static boolean compareAST(SimpleNode a, SimpleNode b) {
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
