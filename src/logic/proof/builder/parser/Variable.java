package logic.proof.builder.parser;

public class Variable {
    String name;

    public Variable(String name) {
	this.name = name;

    }

    @Override
    public String toString() {
	return name;
    }
    
    public void setName(String name) {
	this.name=name;
    }

    public boolean equals(Object o) {
	if (name.equals(((Variable) o).getName())) {
	    return true;
	} else {
	    return false;
	}
    }

    private String getName() {
	return name;
    }

}
