package logic.proof.builder.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import logic.proof.builder.ROI.Proof;
import logic.proof.builder.ROI.ProofStep;
import logic.proof.builder.parser.ParserConstants;
import logic.proof.builder.parser.ParserState;
import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Token;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class ProofBuilderActivity extends Activity {

    static ListView formulaList;
    static ListView proofList;
    static Intent intent;
    private static int lineNumber;
    static Proof proof;
    static final int NEW_LINE = 1;
    static final int NEW_SUBPROOF = 2;
    static final int END_SUBPROOF = 3;
    static final int CHANGE_JUSTIFICATION = 4;
    static ListViewAdapter adapter;
    static ArrayList<String> predicates;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_proof_builder);
	getActionBar().setDisplayHomeAsUpEnabled(true);
	proof = new Proof(this);

	intent = new Intent(this, InputSentenceActivity.class);

	proofList = (ListView) findViewById(R.id.proofList);

	proofList.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		position++;
		Intent chooseRule = new Intent(getBaseContext(),
			ChooseRuleActivity.class);
		chooseRule.putExtra("line_number", position);
		lineNumber = position;
		startActivityForResult(chooseRule, CHANGE_JUSTIFICATION);
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
	    Toast.makeText(this, "There are no  formulae to delete",
		    Toast.LENGTH_SHORT).show();
	} else {
	    proof.removeStep();
	    adapter.notifyDataSetChanged();
	}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == NEW_LINE) {
	    if (resultCode == RESULT_OK) {
		SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		proof.addStepAsNewLine(rootNode, data.getStringExtra("Formula"));
		adapter.notifyDataSetChanged();
	    } 
	} else if (requestCode == NEW_SUBPROOF) {
	    if (resultCode == RESULT_OK) {
		SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		proof.addStepAsStartOfSubproof(rootNode,
			data.getStringExtra("Formula"));
		adapter.notifyDataSetChanged();
	    } 

	} else if (requestCode == END_SUBPROOF) {
	    if (resultCode == RESULT_OK) {
		SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		proof.addStepAsEndOfSubproof(rootNode,
			data.getStringExtra("Formula"));
		adapter.notifyDataSetChanged();
	    } 
	} else if (requestCode == CHANGE_JUSTIFICATION) {
	    if (resultCode == RESULT_OK) {
		String justification = data.getStringExtra("justification");
		proof.lines.get(lineNumber - 1).justification = justification;
		adapter.notifyDataSetChanged();
	    } 
	}
    }

    public void onStart() {
	super.onStart();
	// formulaList.setAdapter(proof.formulae);
	// justificationList.setAdapter(proof.justifications);
    }
}
