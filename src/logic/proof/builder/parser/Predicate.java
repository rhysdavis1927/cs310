package logic.proof.builder.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Predicate {
    String name;
    public ArrayList<Variable> parameters = new ArrayList<Variable>();
    
    public String getName() {
	return this.name;
    }

    public Predicate(String str, HashMap<String, Variable> variables) {
	str.replaceAll("\\s", "");
	String[] s = str.split("\\(|\\,|\\)", 0);
	this.name = s[0].toUpperCase();
	for (int i = 1; i < s.length; i++) {
	    s[i].toLowerCase();
	    if (variables.containsKey(s[i])) {
		parameters.add(variables.get(s[i]));
	    } else {
		Variable var = new Variable(s[i],variables);
		parameters.add(var);
	    }
	}
    }

    public String toString() {
	String str = name;
	if (parameters.size() > 0) {
	    str = str + "(";
	    for (int i = 0; i < parameters.size() - 1; i++) {
		str = str + parameters.get(i) + ",";
	    }

	    str = str + parameters.get(parameters.size() - 1) + ")";
	}
	return str;
    }

    public void bind(Variable x) {
	for (int index = 0; index < parameters.size(); index++) {
	    if (x.equals(parameters.get(index))) {
		parameters.set(index, x);

	    }
	}
    }
}
