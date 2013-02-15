package logic.proof.builder.parser;

import java.util.HashMap;

public class Variable {
    String name;

    public Variable(String name) {
	this.name = name;
    }

    public Variable(String name, HashMap<String, Variable> variables) {
	this.name = name;
	variables.put(name, this);

    }

    @Override
    public String toString() {
	return name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public boolean equals(Object o) {
	if (name.equals(((Variable) o).getName())) {
	    return true;
	} else {
	    return false;
	}
    }

}
