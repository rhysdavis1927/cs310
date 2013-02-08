package logic.proof.builder.gui;

import java.util.ArrayList;

import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Variable;
import logic.proof.builder.proof.ProofStep;
import logic.proof.builder.proof.RulesOfInference;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static logic.proof.builder.gui.ProofBuilderActivity.proof;

public class ChooseRuleActivity extends Activity {

    private static final int PREMISE = 0;
    private static final int AND_INTRODUCTION = 1;
    private static final int AND_ELIMINATION1 = 2;
    private static final int AND_ELIMINATION2 = 3;
    private static final int OR_INTRODUCTION1 = 4;
    private static final int OR_INTRODUCTION2 = 5;
    private static final int OR_ELIMINATION = 6;
    private static final int IMPLIES_INTRODUCTION = 7;
    private static final int MODUS_PONENS = 8;
    private static final int NEGATION_INTRODUCTION = 9;
    private static final int NEGATION_ELIMINATION = 10;
    private static final int DOUBLE_NEGATION_INTRODUCTION = 11;
    private static final int DOUBLE_NEGATION_ELIMINATION = 12;
    private static final int BOTTOM_ELIMINATION = 13;
    private static final int COPY = 14;
    protected static final int EQUALS_INTRODUCTION = 15;
    protected static final int EQUALS_ELIMINATION = 16;
    protected static final int FOR_ALL_INTRODUCTION = 17;
    protected static final int FOR_ALL_ELIMINATION = 18;
    protected static final int THERE_EXISTS_INTRODUCTION = 19;
    protected static final int THERE_EXISTS_ELIMINATION = 20;

    static public ArrayAdapter<String> lineJustificationListAdapter;
    static public ArrayAdapter<String> subproofJustificationListAdapter;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner chooseRuleSpinner;
    TextView label1;
    TextView label2;
    TextView label3;

    String string = new String("kd");
    int lineNumber;

    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_choose_rule);
	spinner1 = (Spinner) findViewById(R.id.spinner1);
	spinner2 = (Spinner) findViewById(R.id.spinner2);
	spinner3 = (Spinner) findViewById(R.id.spinner3);
	chooseRuleSpinner = (Spinner) findViewById(R.id.chooseRuleSpinner);
	lineNumber = getIntent().getIntExtra("line_number", 0);
	label1 = (TextView) findViewById(R.id.label1);
	label2 = (TextView) findViewById(R.id.label2);
	label3 = (TextView) findViewById(R.id.label3);

	ArrayAdapter<CharSequence> ruleAdapter = ArrayAdapter
		.createFromResource(this, R.array.rules_array,
			android.R.layout.simple_spinner_item);
	ruleAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	chooseRuleSpinner.setAdapter(ruleAdapter);

	lineJustificationListAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item);
	subproofJustificationListAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item);

	lineJustificationListAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	subproofJustificationListAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	ProofStep step = proof.lines.get(lineNumber - 1);
	while (step.parent.node.toString() != "void") {
	    lineJustificationListAdapter.insert(
		    step.parent.lineNumber.toString(), 0);
	    step = step.parent;
	}

	step = proof.lines.get(lineNumber - 1);
	int subproofRoot;
	int subproofStart;

	subproofRoot = step.lineNumber;
	while (step.parent != null) {
	    step = step.parent;
	    for (ProofStep s : step.subproofs) {
		subproofStart = s.lineNumber;
		if (subproofRoot > subproofStart) {
		    while (s.next != null) {
			s = s.next;
		    }

		    subproofJustificationListAdapter.add(subproofStart + " - "
			    + s.lineNumber);
		}
	    }
	    subproofRoot = step.lineNumber;
	}

	chooseRuleSpinner
		.setOnItemSelectedListener(new OnItemSelectedListener() {

		    public void onItemSelected(AdapterView<?> parent,
			    View view, int pos, long id) {

			switch (pos) {
			case PREMISE:

			    label1.setVisibility(View.GONE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);
			    spinner1.setVisibility(View.GONE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case COPY:

			    setAdapter(spinner1, lineJustificationListAdapter);
			    label1.setText("P:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;

			case AND_INTRODUCTION:

			    setAdapter(spinner1, lineJustificationListAdapter);
			    setAdapter(spinner2, lineJustificationListAdapter);
			    label1.setText("P:");
			    label2.setText("Q:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.VISIBLE);
			    label3.setVisibility(View.GONE);
			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.GONE);

			    break;

			case AND_ELIMINATION1:
			case AND_ELIMINATION2:

			    setAdapter(spinner1, lineJustificationListAdapter);
			    label1.setText("P:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case OR_INTRODUCTION1:
			case OR_INTRODUCTION2:
			    setAdapter(spinner1, lineJustificationListAdapter);
			    label1.setText("P:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;

			case OR_ELIMINATION:
			    setAdapter(spinner1, lineJustificationListAdapter);
			    setAdapter(spinner2,
				    subproofJustificationListAdapter);
			    setAdapter(spinner3,
				    subproofJustificationListAdapter);

			    label1.setText("P:");
			    label2.setText("Subproof1:");
			    label3.setText("Subproof2:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.VISIBLE);
			    label3.setVisibility(View.VISIBLE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.VISIBLE);

			    break;

			case IMPLIES_INTRODUCTION:
			    setAdapter(spinner1,
				    subproofJustificationListAdapter);

			    label1.setText("Subproof");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;

			case MODUS_PONENS:
			    setAdapter(spinner1, lineJustificationListAdapter);
			    setAdapter(spinner2, lineJustificationListAdapter);

			    label1.setText("P:");
			    label2.setText("Implication:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.VISIBLE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case NEGATION_INTRODUCTION:
			    setAdapter(spinner1,
				    subproofJustificationListAdapter);

			    label1.setText("Subproof");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case NEGATION_ELIMINATION:
			    setAdapter(spinner1, lineJustificationListAdapter);
			    setAdapter(spinner2, lineJustificationListAdapter);

			    label1.setText("P:");
			    label2.setText("not P:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.VISIBLE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.GONE);

			    break;

			case DOUBLE_NEGATION_INTRODUCTION:
			    setAdapter(spinner1, lineJustificationListAdapter);

			    label1.setText("P:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case DOUBLE_NEGATION_ELIMINATION:
			    setAdapter(spinner1, lineJustificationListAdapter);

			    label1.setText("P:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case BOTTOM_ELIMINATION:
			    setAdapter(spinner1, lineJustificationListAdapter);

			    label1.setText("Bottom:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case EQUALS_INTRODUCTION:

			    label1.setVisibility(View.GONE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.GONE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case EQUALS_ELIMINATION:
			    setAdapter(spinner1, lineJustificationListAdapter);
			    setAdapter(spinner2, lineJustificationListAdapter);

			    label1.setText("Bottom:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.VISIBLE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case FOR_ALL_INTRODUCTION:
			    setAdapter(spinner1, lineJustificationListAdapter);

			    label1.setText("Bottom:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case FOR_ALL_ELIMINATION:
			    setAdapter(spinner1, lineJustificationListAdapter);

			    label1.setText("Bottom:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case THERE_EXISTS_INTRODUCTION:
			    setAdapter(spinner1, lineJustificationListAdapter);

			    label1.setText("Bottom:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			case THERE_EXISTS_ELIMINATION:
			    setAdapter(spinner1, lineJustificationListAdapter);

			    label1.setText("Bottom:");

			    label1.setVisibility(View.VISIBLE);
			    label2.setVisibility(View.GONE);
			    label3.setVisibility(View.GONE);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;
			}

		    }

		    public void onNothingSelected(AdapterView<?> parent) {
		    }

		});

    }

    public void clickOkButton(View view) {
	SimpleNode conclusion = proof.lines.get(lineNumber - 1).node;
	StringBuilder strb = new StringBuilder(
		chooseRuleSpinner.getSelectedItem() + ": ");
	Intent intent = new Intent();
	int lineNumber1;
	int lineNumber2;
	int lineNumber3;
	boolean validRule = false;

	SimpleNode p;
	SimpleNode q;
	ArrayList<SimpleNode> subproof1 = new ArrayList<SimpleNode>();
	ArrayList<SimpleNode> subproof2 = new ArrayList<SimpleNode>();

	switch (chooseRuleSpinner.getSelectedItemPosition()) {

	case PREMISE:
	    String previousJustification = proof.lines.get(lineNumber - 1).parent.justification;
	    if (previousJustification.equals("Premise")
		    || previousJustification.equals("root node")) {
		validRule = true;
		strb.delete(strb.length() - 2, strb.length());
	    } else {
		CharSequence text = "Premises must be at the beginning of a proof";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    }
	    break;
	case COPY:
	    if (spinner1.getSelectedItem() == "No justification available") {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.copy(p, conclusion);
		    strb.append(lineNumber1);
		    validRule = true;
		} catch (Exception ex) {
		    CharSequence text = ex.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
		break;
	    }
	case AND_INTRODUCTION:
	    if ((spinner1.getSelectedItem() == "No justification available")
		    || (spinner2.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		lineNumber2 = getLineNumber(spinner2);
		p = proof.lines.get(lineNumber1 - 1).node;
		q = proof.lines.get(lineNumber2 - 1).node;

		if (RulesOfInference.andIntroduction(p, q, conclusion)) {
		    strb.append(lineNumber1 + ", " + lineNumber2);
		    validRule = true;

		} else {
		    CharSequence text = "Some explanation";
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }

	    break;

	case AND_ELIMINATION1:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.andElimination1(p, conclusion);
		    strb.append(lineNumber1);
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case AND_ELIMINATION2:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.andElimination2(p, conclusion);
		    strb.append(lineNumber1);
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case OR_INTRODUCTION1:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.orIntroduction1(p, conclusion);
		    strb.append(lineNumber1);
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case OR_INTRODUCTION2:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.orIntroduction2(p, conclusion);
		    strb.append(lineNumber1);
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case OR_ELIMINATION:
	    if ((spinner1.getSelectedItem() == "No justification available")
		    || (spinner2.getSelectedItem() == "No justification available")
		    || (spinner3.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		lineNumber2 = getLineNumber(spinner2);
		lineNumber3 = getLineNumber(spinner3);
		p = proof.lines.get(lineNumber1 - 1).node;
		subproof1.add(proof.lines.get(lineNumber2 - 1).node);
		subproof1
			.add(proof.lines.get(getFinalLineNumer(spinner2) - 1).node);
		subproof2.add(proof.lines.get(lineNumber3 - 1).node);
		subproof2
			.add(proof.lines.get(getFinalLineNumer(spinner3) - 1).node);

		try {
		    RulesOfInference.orElimination(subproof1, subproof2, p,
			    conclusion);
		    strb.append(lineNumber1 + ", " + spinner2.getSelectedItem()
			    + ", " + spinner3.getSelectedItem());
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case IMPLIES_INTRODUCTION:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		subproof1.add(proof.lines.get(lineNumber1 - 1).node);
		subproof1
			.add(proof.lines.get(getFinalLineNumer(spinner1) - 1).node);

		try {
		    RulesOfInference.impliesIntroduction(subproof1, conclusion);
		    strb.append(spinner1.getSelectedItem());
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case MODUS_PONENS:
	    if ((spinner1.getSelectedItem() == "No justification available")
		    || (spinner2.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		lineNumber2 = getLineNumber(spinner2);
		p = proof.lines.get(lineNumber1 - 1).node;
		q = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.modusPonens(p, q, conclusion);
		    strb.append(lineNumber1 + ", " + lineNumber2);
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }

	    break;

	case NEGATION_INTRODUCTION:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		subproof1.add(proof.lines.get(lineNumber1 - 1).node);
		subproof1
			.add(proof.lines.get(getFinalLineNumer(spinner1) - 1).node);

		try {
		    RulesOfInference
			    .negationIntroduction(subproof1, conclusion);
		    strb.append(spinner1.getSelectedItem());
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;

	case NEGATION_ELIMINATION:
	    if ((spinner1.getSelectedItem() == "No justification available")
		    || (spinner2.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		lineNumber2 = getLineNumber(spinner2);
		p = proof.lines.get(lineNumber1 - 1).node;
		q = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.negationElimination(p, q, conclusion);
		    strb.append(lineNumber1 + ", " + lineNumber2);
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case DOUBLE_NEGATION_INTRODUCTION:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.doubleNegationIntroduction(p, conclusion);
		    strb.append(spinner1.getSelectedItem());
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case DOUBLE_NEGATION_ELIMINATION:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.doubleNegationElimination(p, conclusion);
		    strb.append(spinner1.getSelectedItem());
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case BOTTOM_ELIMINATION:
	    if ((spinner1.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;

		try {
		    RulesOfInference.bottomElimination(p, conclusion);
		    strb.append(spinner1.getSelectedItem());
		    validRule = true;

		} catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;
	case EQUALS_INTRODUCTION:

	    try {
		RulesOfInference.equalsIntroduction(conclusion);
		validRule = true;

	    } catch (Exception e) {
		CharSequence text = e.getMessage();
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    }
	    break;
	case EQUALS_ELIMINATION:
	    if ((spinner1.getSelectedItem() == "No justification available")
		    && (spinner2.getSelectedItem() == "No justification available")) {
		CharSequence text = "Please choose a rule and provide the evidence";
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    } else {

		lineNumber1 = getLineNumber(spinner1);
		p = proof.lines.get(lineNumber1 - 1).node;
		lineNumber2 = getLineNumber(spinner2);
		q = proof.lines.get(lineNumber2 - 1).node;
		
		

		try {
		    Variable v = proof.lines.get(lineNumber2 - 1).freeVariables
			.get(((SimpleNode) (p.jjtGetChild(0))).jjtGetValue());
		    RulesOfInference.equalsElimination(p, q, v, conclusion);
		    strb.append(spinner1.getSelectedItem() + ", " + spinner2.getSelectedItem());
		    validRule = true;

		} catch (NullPointerException e) {
		    CharSequence text = "Justification must must be of the form t1 = t2";
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}catch (Exception e) {
		    CharSequence text = e.getMessage();
		    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		}
	    }
	    break;

	}

	if (validRule) {
	    intent.putExtra("justification", strb.toString());
	    setResult(RESULT_OK, intent);
	    finish();
	}
    }

    public void clickCancelButton(View view) {
	setResult(RESULT_CANCELED);
	finish();
    }

    private void setAdapter(Spinner spinner, ArrayAdapter<String> adapter) {
	if (adapter.isEmpty()) {
	    adapter.insert("No justification available", 0);
	    spinner.setAdapter(adapter);
	} else {
	    spinner.setAdapter(adapter);
	}
    }

    private int getLineNumber(Spinner spinner) {
	return Integer
		.parseInt(((String) spinner.getSelectedItem()).split(" ")[0]);
    }

    private int getFinalLineNumer(Spinner spinner) {
	return Integer.parseInt(((String) spinner.getSelectedItem())
		.split("- ")[1]);
    }
}
