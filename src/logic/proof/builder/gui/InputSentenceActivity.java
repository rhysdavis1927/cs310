package logic.proof.builder.gui;

import java.util.LinkedList;

import logic.proof.builder.parser.ParseException;
import logic.proof.builder.parser.Parser;
import logic.proof.builder.parser.ParserConstants;
import logic.proof.builder.parser.ParserState;
import logic.proof.builder.parser.SimpleNode;
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
import android.text.InputType;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import static logic.proof.builder.gui.ProofBuilderActivity.proof;

public class InputSentenceActivity extends Activity {

    static final String OR = Html.fromHtml("&or;").toString();
    static final String IMPLIES = Html.fromHtml("&rArr;").toString();
    static final String AND = Html.fromHtml("&and;").toString();
    static final String NOT = Html.fromHtml("&not;").toString();
    static final String EQUIVALENT = Html.fromHtml("&hArr;").toString();
    static final String BOTTOM = Html.fromHtml("&bot;").toString();
    static final String FORALL = Html.fromHtml("&forall;").toString();
    static final String THEREEXISTS = Html.fromHtml("&exist;").toString();
    static final String EQUALS = "=";

    static final int ADD_NEW_PREDICATE = 1;
    static final int UNIVERSAL_QUANTIFICATION = 2;
    static final int EXISTENTIAL_QUANITIFICATION = 3;
    static final int EQUALS_ASSIGNMENT = 4;

    static EditText formulaTextView;
    static TokenCollector tc = new TokenCollector();
    static Parser parser = new Parser(tc);
    static ArrayAdapter<String> listAdapter;
    static ListView predicateList;
    static AlertDialog.Builder alert;
    Intent dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_input_sentence);
	dialog = new Intent(this, MyDialogActivity.class);

	

	alert = new AlertDialog.Builder(this);

	formulaTextView = (EditText) findViewById(R.id.formulaTextView);
	
	// Stops the soft keyboard becoming visible
	formulaTextView.setOnClickListener(new OnClickListener() {
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

	    public void onClick(View v) {
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	    }
	});

	
	/*
	 * Initial set up. Adds icons and click listeners for buttons
	 */
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
	backspaceButton.setText(Html.fromHtml("&larr; backspace"));

	Button bottomButton = (Button) findViewById(R.id.bottomButton);
	bottomButton.setText(BOTTOM);

	Button topButton = (Button) findViewById(R.id.topButton);
	topButton.setText("T");

	Button forAllButton = (Button) findViewById(R.id.forAllButton);
	forAllButton.setText(FORALL);

	Button thereExistsButton = (Button) findViewById(R.id.thereExistsButton);
	thereExistsButton.setText(THEREEXISTS);

	Button equalsButton = (Button) findViewById(R.id.equalsButton);
	equalsButton.setText(EQUALS);

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
		android.R.layout.simple_list_item_1, proof.predicates);
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
	for (Token t : tc.list) {
	    t.next = null;
	}
	LinkedList<Token> backup = (LinkedList<Token>) tc.list.clone();

	parser.ReInit(tc);
	try {
	    parser.Formula();
	    ParserState.saveTree(parser);
	    ParserState.findQuantifiers(ParserState.getTree());
	    Intent intent = new Intent();
	    intent.putExtra("Formula", formulaTextView.getText().toString());
	    setResult(RESULT_OK, intent);
	    finish();

	} catch (ParseException e) {
	    alert.setView(null);
	    AlertDialog.Builder alertDialog = InputSentenceActivity.alert;

	    // set title
	    alertDialog.setTitle("Error");

	    // set dialog message
	    alertDialog.setMessage("Formula is not well formed.")
		    .setPositiveButton("OK",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    // if this button is clicked, close
				    // current activity
				    dialog.dismiss();
				}
			    });

	    // show it
	    alertDialog.show();

	    tc.list = backup;

	}
    }

    public void clickAddPredicateButton(final View view) {
	dialog.putExtra("Type", ADD_NEW_PREDICATE);
	startActivityForResult(dialog, ADD_NEW_PREDICATE);
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

    public void clickBottomButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.PREDICATE;
	token.image = BOTTOM;
	insertOperator(view, token);
    }

    public void clickTopButton(View view) {
	Token token = new Token();
	token.kind = ParserConstants.PREDICATE;
	token.image = "T";
	insertOperator(view, token);
    }

    public void clickEqualsButton(View view) {
	dialog.putExtra("Type", EQUALS_ASSIGNMENT);
	startActivityForResult(dialog, EQUALS_ASSIGNMENT);
    }

    public static void clickBackspaceButton(View view) {
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

    public void clickForAllButton(View view) {
	dialog.putExtra("Type", UNIVERSAL_QUANTIFICATION);
	startActivityForResult(dialog, UNIVERSAL_QUANTIFICATION);
    }

    public void clickThereExistsButton(View view) {
	dialog.putExtra("Type", EXISTENTIAL_QUANITIFICATION);
	startActivityForResult(dialog, EXISTENTIAL_QUANITIFICATION);
    }

    public static void insertOperator(View view, Token token) {
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

    private static int getTokenIndex(int cursorIndex) {
	
	//Finds the index of a token in the token list by counting
	// tokens and their length
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	String value;
	
	
	
	if (requestCode == ADD_NEW_PREDICATE) {
	    if (resultCode == RESULT_OK) {
		value = data.getStringExtra("Predicate");
		listAdapter.add(value);
		predicateList.setAdapter(listAdapter);
		Token predicate = new Token();
		predicate.kind = ParserConstants.PREDICATE;
		predicate.image = value;
		insertOperator(null, predicate);

	    }
	} else if (requestCode == UNIVERSAL_QUANTIFICATION) {
	    if (resultCode == RESULT_OK) {
		value = data.getStringExtra("Variable");
		Token token = new Token();
		token.kind = ParserConstants.FORALL;
		token.image = FORALL + value;
		insertOperator(null, token);
	    }
	}else if (requestCode == EXISTENTIAL_QUANITIFICATION) {
	    if (resultCode == RESULT_OK) {
		value = data.getStringExtra("Variable");
		Token token = new Token();
		token.kind = ParserConstants.THEREEXISTS;
		token.image = THEREEXISTS + value;
		insertOperator(null, token);
	    }
	}
	else if (requestCode == EQUALS_ASSIGNMENT) {
	    if (resultCode == RESULT_OK) {
		value = data.getStringExtra("lhs");
		Token token = new Token();
		token.kind = ParserConstants.VARIABLE;
		token.image = value;
		insertOperator(null, token);

		Token token2 = new Token();
		token2.kind = ParserConstants.EQUALS;
		token2.image = EQUALS;
		insertOperator(null, token2);

		value = data.getStringExtra("rhs");
		Token token3 = new Token();
		token3.kind = ParserConstants.VARIABLE;
		token3.image = value;
		insertOperator(null, token3);
	    }
	}

    }
    @Override
    public void onResume() {
	super.onResume();
	StringBuilder str = new StringBuilder();
	for (int i = 0; i < tc.list.size(); i++) {
	    str.append(tc.list.get(i));
	}
	formulaTextView.setText(str);
	formulaTextView.setSelection(formulaTextView.getText().length());
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
