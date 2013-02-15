package logic.proof.builder.parser;

public class ParserState {

    static SimpleNode rootNode;
    static Parser parser;

    public static void saveTree(Parser p) {
	rootNode = (SimpleNode) p.jjtree.rootNode();
	parser= p;
    }
    
    public static SimpleNode getTree(Parser p) {
	return (SimpleNode) p.jjtree.rootNode();
    }

    public static SimpleNode getTree() {
	return rootNode;
    }

    public static String getFormula(SimpleNode n) {

	StringBuilder str = new StringBuilder();
	if (n.jjtGetNumChildren() > 0) {
	    for (Node m : n.children) {
		str = str.append(getFormula((SimpleNode) m));
	    }
	    
	}
	return str.append(n.toString()).toString();
    }
    
    public static void bindTree(SimpleNode n, Variable x) {
	if (n.id == ParserTreeConstants.JJTPREDICATE) {
	    ((Predicate) n.value).bind(x);
	}
	else {
	    for(int i=0; i< n.jjtGetNumChildren(); i++) {
		bindTree((SimpleNode) n.jjtGetChild(i),x);
	    }
	}
    }
    
    public static void findQuantifiers(SimpleNode n) {
	if ((n.id == ParserTreeConstants.JJTFORALL)  || (n.id ==ParserTreeConstants.JJTTHEREEXISTS)) {
	    bindTree((SimpleNode) n.jjtGetChild(0),(Variable)n.value);
	}
	else{
	    for(int i=0; i< n.jjtGetNumChildren(); i++) {
		findQuantifiers((SimpleNode) n.jjtGetChild(i));
	    }
	}
    }

}
