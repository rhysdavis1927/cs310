package logic.proof.builder.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import logic.proof.builder.ROI.Proof;
import logic.proof.builder.parser.ParserConstants;
import logic.proof.builder.parser.ParserState;
import logic.proof.builder.parser.SimpleNode;
import logic.proof.builder.parser.Token;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class ProofBuilderActivity extends Activity {
    
    static ListView formulaList;
    static ListView justificationList;
    static Intent intent;
    private static int lineNumber;  
    static Proof proof;
    static final int NEW_LINE = 1;
    static final int NEW_SUBPROOF = 2;
    static final int END_SUBPROOF = 3;
    static final int CHANGE_JUSTIFICATION= 4;    
    ArrayAdapter<String> formulaAdapter;
    ArrayAdapter<String> justificationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_builder);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        proof = new Proof(this);
        
        intent = new Intent(this,InputSentenceActivity.class);
        
        formulaList = (ListView) findViewById(R.id.formulaList);
        
	formulaList.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		String item = (String) formulaList
			.getItemAtPosition(position);
	    }
	});
	
	formulaAdapter = new ArrayAdapter<String>(getBaseContext(),
		android.R.layout.simple_list_item_1,proof.formulae);
	formulaList.setAdapter(formulaAdapter);
	
	
        justificationList = (ListView) findViewById(R.id.justificationList);
        
	justificationList.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		position++;
		Intent chooseRule = new Intent(getBaseContext(),ChooseRuleActivity.class);
		chooseRule.putExtra("line_number", position);
		lineNumber = position;
		startActivityForResult(chooseRule, CHANGE_JUSTIFICATION);		
	    }
	});
	

	justificationAdapter = new ArrayAdapter<String>(getBaseContext(),
		android.R.layout.simple_list_item_1,proof.justifications);
	justificationList.setAdapter(justificationAdapter);
        
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
	startActivityForResult(intent, END_SUBPROOF);
    }

    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
	int level;
	
        if (requestCode ==NEW_LINE) {
            if (resultCode == RESULT_OK) {
        	SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		level = proof.addStepAsNewLine(rootNode);
        	proof.add(data.getStringExtra("Formula"),level);
        	proof.justifications.add("No justification chosen");
        	formulaAdapter.notifyDataSetChanged();
        	justificationAdapter.notifyDataSetChanged();
            }
            else if (resultCode == RESULT_CANCELED) {
                System.out.println("err");
            }
        }
        else if (requestCode ==NEW_SUBPROOF) {
            if (resultCode == RESULT_OK) {
        	SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		level = proof.addStepAsStartOfSubproof(rootNode);
        	proof.add(data.getStringExtra("Formula"),level);
        	proof.justifications.add("Assumption");
        	formulaAdapter.notifyDataSetChanged();
        	justificationAdapter.notifyDataSetChanged();
            }
            else if (resultCode == RESULT_CANCELED) {
                System.out.println("err");
            }
            
        }
        else if (requestCode ==END_SUBPROOF) {
            if (resultCode == RESULT_OK) {
        	SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		level = proof.addStepAsEndOfSubproof(rootNode);
        	proof.add(data.getStringExtra("Formula"),level+1);
        	proof.justifications.add("Assumption");
        	formulaAdapter.notifyDataSetChanged();
        	justificationAdapter.notifyDataSetChanged();
            }
            else if (resultCode == RESULT_CANCELED) {
                System.out.println("err");
            }
        }
        else if (requestCode == CHANGE_JUSTIFICATION) {
            if (resultCode == RESULT_OK) {
        	String justification = data.getStringExtra("justification");
        	proof.justifications.set(lineNumber-1, justification);
        	justificationAdapter.notifyDataSetChanged();
            }
            else if (resultCode == RESULT_CANCELED) {
                System.out.println("err");
            }
        }
    }

    public void onStart() {
	super.onStart();
	//formulaList.setAdapter(proof.formulae);
	//justificationList.setAdapter(proof.justifications);
    }
}
