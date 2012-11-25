package logic.proof.builder.gui;

import java.util.ArrayList;

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
    
    ArrayList<SimpleNode> rootNodes;
    ArrayList<String> formulae;
    ListView formulaList;
    public ArrayAdapter<String> listAdapter;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_builder);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        intent = new Intent(this, InputSentenceActivity.class);
        formulae = new ArrayList<String>();
        rootNodes = new ArrayList<SimpleNode>();
        formulaList = (ListView) findViewById(R.id.list);
	formulaList.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		String item = (String) formulaList
			.getItemAtPosition(position);
	    }
	});
	listAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, formulae);
	formulaList.setAdapter(listAdapter);
        
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
  	startActivityForResult(intent, 1);
    }

    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode ==1) {
            if (resultCode == RESULT_OK) {
        	SimpleNode rootNode = (SimpleNode) ParserState.getTree();
		rootNodes.add(rootNode);
        	listAdapter.add(data.getStringExtra("Formula"));
            }
            else if (resultCode == RESULT_CANCELED) {
                System.out.println("err");
            }
        }
    }

    public void onStart() {
	super.onStart();
	formulaList.setAdapter(listAdapter);
    }
}
