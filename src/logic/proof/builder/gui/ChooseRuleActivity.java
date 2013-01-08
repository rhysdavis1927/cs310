package logic.proof.builder.gui;

import logic.proof.builder.ROI.Proof.ProofStep;
import logic.proof.builder.ROI.RulesOfInference;
import logic.proof.builder.parser.SimpleNode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import static logic.proof.builder.gui.ProofBuilderActivity.proof;

public class ChooseRuleActivity extends Activity {

    private static final int orElimination = 5;
    private static final int orIntroduction2 = 4;
    private static final int orIntroduction1 = 3;
    private static final int andIntroduction = 0;
    private static final int andElimination = 1;
    static public ArrayAdapter<String> lineJustificationListAdapter;
    static public ArrayAdapter<String> subproofJustificationListAdapter;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner chooseRuleSpinner;
    int lineNumber;

    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_choose_rule);

	spinner1 = (Spinner) findViewById(R.id.spinner1);
	spinner2 = (Spinner) findViewById(R.id.spinner2);
	spinner3 = (Spinner) findViewById(R.id.spinner3);
	chooseRuleSpinner = (Spinner) findViewById(R.id.chooseRuleSpinner);
	lineNumber = getIntent().getIntExtra("line_number", 0);

	ArrayAdapter<CharSequence> ruleAdapter = ArrayAdapter
		.createFromResource(this, R.array.rules_array,
			android.R.layout.simple_spinner_item);
	ruleAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	chooseRuleSpinner.setAdapter(ruleAdapter);

	lineJustificationListAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_dropdown_item_1line);
	subproofJustificationListAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_expandable_list_item_1);

	ProofStep step = proof.lines.get(lineNumber - 1);
	while (step.parent.formula.toString() != "void") {
	    lineJustificationListAdapter.insert(
		    step.parent.lineNumber.toString(), 0);
	    step = step.parent;
	}

	step = proof.lines.get(lineNumber - 1);
	step = step.parent;
	int subproofEnd;
	for (ProofStep s : step.subproofs) {
	    while (step.next != null) {
		step = step.next;
	    }
	    subproofEnd = step.lineNumber;
	    subproofJustificationListAdapter.add(s.lineNumber + " - "
		    + subproofEnd);
	}

	chooseRuleSpinner
		.setOnItemSelectedListener(new OnItemSelectedListener() {

		    public void onItemSelected(AdapterView<?> parent,
			    View view, int pos, long id) {

			switch (pos) {
			case andIntroduction:

			    setAdapter(spinner1, lineJustificationListAdapter);
			    setAdapter(spinner2, lineJustificationListAdapter);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.GONE);

			    break;

			case andElimination:

			    setAdapter(spinner1, lineJustificationListAdapter);
			    setAdapter(spinner2,
				    subproofJustificationListAdapter);
			    setAdapter(spinner3,
				    subproofJustificationListAdapter);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.VISIBLE);

			    break;
			case orIntroduction1:
			case orIntroduction2:
			    spinner1.setAdapter(lineJustificationListAdapter);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.GONE);
			    spinner3.setVisibility(View.GONE);

			    break;

			case orElimination:
			    spinner1.setAdapter(subproofJustificationListAdapter);
			    spinner2.setAdapter(subproofJustificationListAdapter);
			    spinner3.setAdapter(lineJustificationListAdapter);

			    spinner1.setVisibility(View.VISIBLE);
			    spinner2.setVisibility(View.VISIBLE);
			    spinner3.setVisibility(View.VISIBLE);

			    break;

			}

		    }

		    public void onNothingSelected(AdapterView<?> parent) {
		    }

		});

    }

    public void clickOkButton(View view) {
	SimpleNode conclusion = proof.lines.get(lineNumber - 1).formula;
	StringBuilder strb = new StringBuilder("Lines: ");
	Intent intent = new Intent();
	int lineNumber1;
	int lineNumber2;
	int lineNumber3;

	SimpleNode p;
	SimpleNode q;

	switch (chooseRuleSpinner.getSelectedItemPosition()) {
	case andIntroduction:
	    if ((spinner1.getSelectedItem() == null) || (spinner2.getSelectedItem() == null)) {
		CharSequence text = "Choose evidence..";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();		
	    }
	    
	    lineNumber1 = getLineNumber(spinner1);
	    lineNumber2 = getLineNumber(spinner2);
	    p = proof.lines.get(lineNumber1 - 1).formula;
	    q = proof.lines.get(lineNumber2 - 1).formula;
	    
	    if (RulesOfInference.andIntroduction(p, q, conclusion)) {
		strb.append(lineNumber1 + ", " + lineNumber2);
		
	    }
	    else {
		CharSequence text = "Some explanation";
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	    }
	    
	    break;

	case andElimination:
	    //RulesOfInference.andElimination1(premise, conclusion);
	    //strb.append(line1);
	    break;
	// case 2:
	// strb.append(justification1 + ", " + spinner2.getSelectedItem()
	// + ", " + spinner3.getSelectedItemId());
	// break;

	}
	intent.putExtra("justification", strb.toString());
	setResult(RESULT_OK, intent);
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

}
