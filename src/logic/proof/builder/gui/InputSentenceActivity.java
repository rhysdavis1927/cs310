package logic.proof.builder.gui;

import java.util.ArrayList;
import java.util.LinkedList;

import logic.proof.builder.parser.ParseException;
import logic.proof.builder.parser.Parser;
import logic.proof.builder.parser.ParserConstants;
import logic.proof.builder.parser.ParserState;
import logic.proof.builder.parser.Token;
import logic.proof.builder.parser.TokenCollector;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class InputSentenceActivity extends Activity {

    private static final String OR = Html.fromHtml("&or;").toString();
    private static final String IMPLIES = Html.fromHtml("&rArr;").toString();
    private static final String AND = Html.fromHtml("&and;").toString();
    private static final String NOT = Html.fromHtml("&not;").toString();
    private static final String EQUIVALENT = Html.fromHtml("&hArr;").toString();

    EditText formulaTextView;
    TokenCollector tc = new TokenCollector();
    Parser parser  = new Parser(tc);
    ArrayAdapter<String> listAdapter;
    ListView predicateList;
    ArrayList<String> predicates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_input_sentence);

	formulaTextView = (EditText) findViewById(R.id.formulaTextView);
	formulaTextView.setOnClickListener(new OnClickListener() {
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

	    public void onClick(View v) {
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	    }
	});

	Button andButton = (Button) findViewById(R.id.andButton);
	andButton.setText(Html.fromHtml("&and;"));

	Button orButton = (Button) findViewById(R.id.orButton);
	orButton.setText(Html.fromHtml("&or;"));

	Button impliesButton = (Button) findViewById(R.id.impliesButton);
	impliesButton.setText(Html.fromHtml("&rArr;"));

	Button equivalentButton = (Button) findViewById(R.id.equivalentButton);
	equivalentButton.setText(Html.fromHtml("&hArr"));

	Button notButton = (Button) findViewById(R.id.notButton);
	notButton.setText(Html.fromHtml("&not;"));

	Button backspaceButton = (Button) findViewById(R.id.backspaceButton);
	backspaceButton.setText(Html.fromHtml("&larr;"));

	predicates = new ArrayList<String>();
	predicateList = (ListView) findViewById(R.id.list);
	predicateList.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		String item = (String) predicateList
			.getItemAtPosition(position);
		Token predicate = new Token();
		predicate.kind = ParserConstants.PREDICATE;
		predicate.image = item;
		insertOperator(view, predicate);
	    }
	});
	listAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, predicates);
	listAdapter.add("P");
	predicateList.setAdapter(listAdapter);

    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.activity_input_sentence, menu);
	return true;
    }

    public void submitFormula(View view) {
	// Calling parser.Formula() consumes all the tokens in the linked list
	// if parsing fails then backup is used to recover the tokens so
	// the user may edit the sentence and try again.

	LinkedList<Token> backup = (LinkedList<Token>) tc.list.clone();
	parser.ReInit(tc);
	try {
	    parser.Formula();
	    ParserState.saveTree(parser);
	    Intent intent = new Intent();
	    intent.putExtra("Formula", formulaTextView.getText().toString());
	    setResult(RESULT_OK,intent);
	    finish();  	    

	} catch (ParseException e) {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		    this);

	    // set title
	    alertDialogBuilder.setTitle("Your Title");

	    // set dialog message
	    alertDialogBuilder.setMessage("Formula is not well formed.")
		    .setPositiveButton("OK",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    // if this button is clicked, close
				    // current activity
				    dialog.dismiss();
				}
			    });

	    // create alert dialog
	    AlertDialog alertDialog = alertDialogBuilder.create();

	    // show it
	    alertDialog.show();

	    tc.list = backup;
	    e.printStackTrace();

	}
    }

    public void clickAddPredicateButton(final View view) {
	AlertDialog.Builder alert = new AlertDialog.Builder(this);

	alert.setTitle("Title");
	alert.setMessage("Message");

	// Set an EditText view to get user input
	final EditText input = new EditText(this);
	alert.setView(input);

	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int whichButton) {
		String value = input.getText().toString();
		listAdapter.add(value);
		predicateList.setAdapter(listAdapter);
		// Token pred = new Token();
		// pred.kind = ParserConstants.PREDICATE;
		// pred.image = value;
		// insertOperator(view, value, pred);
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

    public void clickAndButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.AND;
	token.image = AND;
	insertOperator(view, token);

    }

    public void clickOrButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.OR;
	token.image = OR;
	insertOperator(view, token);
    }

    public void clickImpliesButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.IMPLIES;
	token.image = IMPLIES;
	insertOperator(view, token);
    }

    public void clickEquivalentButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.EQUIVALENT;
	token.image = EQUIVALENT;
	insertOperator(view, token);
    }

    public void clickLeftBracketButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.LBRACKET;
	token.image = "(";
	insertOperator(view, token);
    }

    public void clickRightBracketButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.RBRACKET;
	token.image = ")";
	insertOperator(view, token);
    }

    public void clickNotButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.NOT;
	token.image = NOT;
	insertOperator(view, token);
    }

    public void clickBackspaceButton(View view) {
	int cursorIndex = formulaTextView.getSelectionStart();
	int tokenIndex = getTokenIndex(cursorIndex);
	if (tokenIndex > 0) {
	    Token token = tc.list.remove(tokenIndex - 1);
	    StringBuilder str = new StringBuilder();
	    for (int i = 0; i < tc.list.size(); i++) {
		str.append(tc.list.get(i));
	    }
	    formulaTextView.setText(str);
	    formulaTextView.setSelection(cursorIndex - token.image.length());
	}

    }

    public void insertOperator(View view, Token token) {
	int cursorIndex = formulaTextView.getSelectionStart();
	int tokenIndex = getTokenIndex(cursorIndex);
	tc.list.add(tokenIndex, token);
	StringBuilder str = new StringBuilder();
	for (int i = 0; i < tc.list.size(); i++) {
	    str.append(tc.list.get(i));
	}
	formulaTextView.setText(str);
	formulaTextView.setSelection(cursorIndex + token.image.length());
    }

    private int getTokenIndex(int cursorIndex) {
	int tokenIndex = 0;
	if (cursorIndex > 0) {
	    int i = 0;
	    while (i < cursorIndex) {
		i = i + tc.list.get(tokenIndex).image.length();
		tokenIndex++;
	    }
	}
	return tokenIndex;
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

}
