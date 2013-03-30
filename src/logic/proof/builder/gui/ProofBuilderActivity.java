package logic.proof.builder.gui;

import logic.proof.builder.parser.Parser;
import logic.proof.builder.parser.ParserConstants;
import logic.proof.builder.parser.ParserState;
import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Token;
import logic.proof.builder.proof.Proof;
import logic.proof.builder.proof.ProofStep;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProofBuilderActivity extends Activity {

    static ListView formulaList;
    static ListView proofList;
    static Intent intent;
    private static int lineNumber;
    static Proof proof;
    private static final int NEW_LINE = 1;
    private static final int NEW_SUBPROOF = 2;
    private static final int END_SUBPROOF = 3;
    private static final int CHANGE_JUSTIFICATION = 4;
    public static final String TURNSTILE = "  |—  ";// Html.fromHtml("&#8872;").toString();
    protected static final int INTRODUCE_VARIABLE = 0;
    private static ListViewAdapter adapter;
    private ProofStep proofStep;
    static AlertDialog.Builder alert;
    private static String introducedVariable;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_proof_builder);
	alert = new AlertDialog.Builder(this);
	proof = new Proof(this);

	intent = new Intent(this, InputSentenceActivity.class);

	proofList = (ListView) findViewById(R.id.proofList);

	proofList.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		if (!(proof.lines.get(position).justification.equals("")
			|| proof.lines.get(position).justification.equals("Assumption"))) {

		    position++;
		    Intent chooseRule = new Intent(getBaseContext(),
			    ChooseRuleActivity.class);
		    chooseRule.putExtra("line_number", position);
		    lineNumber = position;
		    startActivityForResult(chooseRule, CHANGE_JUSTIFICATION);
		}
	    }
	});

	adapter = new ListViewAdapter(this, proof.lines);
	proofList.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_proof_builder, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    NavUtils.navigateUpFromSameTask(this);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    public void newFormula(View view) {
	startActivityForResult(intent, NEW_LINE);
    }

    public void newSubproof(View view) {
	startActivityForResult(intent, NEW_SUBPROOF);
    }

    public void endSubproof(View view) {
	if (proof.currentLevel > 0) {
	    startActivityForResult(intent, END_SUBPROOF);
	} else {
	    Toast.makeText(this, "There are no open subproofs to close",
		    Toast.LENGTH_SHORT).show();
	}
    }

    public void deleteLine(View view) {
	if (proof.lines.size() == 0) {
	    Toast.makeText(this, "There is nothing to delete",
		    Toast.LENGTH_SHORT).show();
	} else {
	    proof.removeStep();
	    adapter.notifyDataSetChanged();
	}
    }

    public void introduceVar(View view) {
	alert.setView(null);
	alert.setTitle("Variable Introduction");
	alert.setMessage("Type the variable name to be introduced");
	// Set an EditText view to get user input

	LayoutInflater inflater = this.getLayoutInflater();
	final LinearLayout layout = (LinearLayout) inflater.inflate(
		R.layout.variable_input, null);
	alert.setView(layout);

	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int whichButton) {
		introducedVariable = ((EditText) layout.getChildAt(0))
			.getText().toString();
		if (((CheckBox) layout.getChildAt(1)).isChecked()) {
		    startActivityForResult(intent, INTRODUCE_VARIABLE);
		} else {
		    proof.addVar(introducedVariable);
		}
		adapter.notifyDataSetChanged();
	    }
	});

	alert.setNegativeButton("Cancel",
		new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Canceled.
		    }
		});
	alert.show();
    }

    public void checkProof(View view) {
	boolean correct = true;
	int premises = 0;
	StringBuilder strb;

	alert.setView(null);
	// set title
	alert.setTitle("Proof checker");

	if (proof.lines.size() == 0) {
	    alert.setMessage("Nothing to check.");
	    correct = false;
	} else {

	    if (proof.lines.get(proof.lines.size() - 1).level != 0) {
		alert.setMessage("All subproofs must be closed to check proof.");
		correct = false;
	    } else {
		for (ProofStep s : proof.lines) {
		    if (s.justification == "No justification given") {
			alert.setMessage("Line " + s.lineNumber
				+ " has not been justified.");
			correct = false;
			break;
		    } else if (s.justification.equals("Premise")) {
			premises++;
		    }
		}
	    }
	}
	if (premises == proof.lines.size()) {
	    alert.setMessage("Nothing to check.");
	    correct = false;
	}

	if (correct == true) {
	    strb = new StringBuilder(
		    "Congratulations, you have succesfully proved: ");
	    if (premises > 0) {
		strb.append(proof.lines.get(0).formula);
	    }
	    for (int i = 1; i < premises; i++) {
		strb.append(", " + proof.lines.get(i).formula);
	    }
	    strb.append(TURNSTILE);
	    strb.append(proof.lines.get(proof.lines.size() - 1).formula);

	    alert.setMessage(strb.toString());
	}

	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();

	    }
	});

	// show it
	alert.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == NEW_LINE) {
	    if (resultCode == RESULT_OK) {
		SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		proofStep = proof.addStepAsNewLine(rootNode,
			data.getStringExtra("Formula"));
		proofStep.freeVariables = Parser.variables;
		adapter.notifyDataSetChanged();
	    }
	} else if (requestCode == NEW_SUBPROOF) {
	    if (resultCode == RESULT_OK) {
		SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		proofStep = proof.addStepAsStartOfSubproof(rootNode,
			data.getStringExtra("Formula"));

		proofStep.freeVariables = Parser.variables;
		adapter.notifyDataSetChanged();
	    }

	} else if (requestCode == END_SUBPROOF) {
	    if (resultCode == RESULT_OK) {
		SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		proofStep = proof.addStepAsEndOfSubproof(rootNode,
			data.getStringExtra("Formula"));

		proofStep.freeVariables = Parser.variables;
		adapter.notifyDataSetChanged();
	    }
	} else if (requestCode == CHANGE_JUSTIFICATION) {
	    if (resultCode == RESULT_OK) {
		String justification = data.getStringExtra("justification");
		proof.lines.get(lineNumber - 1).justification = justification;
		adapter.notifyDataSetChanged();
	    }
	} else if (requestCode == INTRODUCE_VARIABLE) {
	    if (resultCode == RESULT_OK) {
		SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		proofStep = proof.addVar(introducedVariable, rootNode,
			data.getStringExtra("Formula"));

		proofStep.freeVariables = Parser.variables;
		adapter.notifyDataSetChanged();
	    }
	}
    }

    public void onStart() {
	super.onStart();
    }
}
