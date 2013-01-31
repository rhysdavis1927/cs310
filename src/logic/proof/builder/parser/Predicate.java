package logic.proof.builder.parser;

import java.util.ArrayList;

public class Predicate {
    String name;
    ArrayList<Variable> parameters = new ArrayList<Variable>();

    public Predicate(String str) {
	String[] s = str.split("\\(|\\,|\\)",0);
	this.name = s[0];
	System.out.println(name);
	for (int i = 1; i < s.length; i++) {
	    parameters.add(new Variable(s[i]));
	}
    }

    public String toString() {
	String str = name;
	if (parameters.size() > 0) {
	    str = str + "(";
	    for (int i = 0; i<parameters.size() -1;i++) {
		str = str + parameters.get(i) + ",";
	    }
	    
	    str = str + parameters.get(parameters.size()-1)+ ")";
	}
	return str;
    }
    
    public void bind(Variable x) { 
	int index;
	while((index = parameters.indexOf(x)) !=-1){
	    parameters.set(index, x);
	}
    }
}
