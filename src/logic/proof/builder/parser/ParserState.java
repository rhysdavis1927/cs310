package logic.proof.builder.parser;

public class ParserState {

    static SimpleNode rootNode;

    public static void saveTree(Parser p) {
	rootNode = (SimpleNode) p.jjtree.rootNode();
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

}
