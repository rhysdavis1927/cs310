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
	String[] s = str.split("\\(|\\,|\\)", 0);
	this.name = s[0];
	System.out.println(name);
	for (int i = 1; i < s.length; i++) {
	    if (variables.containsKey(s[i])) {
		parameters.add(variables.get(s[i]));
	    } else {
		Variable var = new Variable(s[i]);
		parameters.add(var);
		variables.put(s[i], var);
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
	    if (x.getName().equals(parameters.get(index))) {
		parameters.set(index, x);

	    }
	}
    }
}
