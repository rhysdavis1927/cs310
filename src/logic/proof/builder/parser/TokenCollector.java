package logic.proof.builder.parser;

import java.util.LinkedList;

import logic.proof.builder.parser.*;

public class TokenCollector implements TokenManager {

    public LinkedList<Token> list;

    public TokenCollector() {
	list = new LinkedList<Token>();
    }


    public Token getNextToken() {

	if (list.isEmpty()) {
	    Token eof = new Token();
	    eof.kind = ParserConstants.EOF;
	    return eof;
	} else {
	    return list.removeFirst();
	}
    }


}
