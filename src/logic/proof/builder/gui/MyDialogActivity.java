package logic.proof.builder.gui;

	import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyDialogActivity extends Activity {
    int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	// setContentView(R.layout.activity_dialog);
	super.onCreate(savedInstanceState);

	type = getIntent().getIntExtra("Type", 0);

	// Customises the look of the dialog depending on request type
	switch (type) {
	case InputSentenceActivity.ADD_NEW_PREDICATE:
	    setTitle("Add new predicate");
	    setContentView(R.layout.predicate_dialog);
	    break;
	case InputSentenceActivity.UNIVERSAL_QUANTIFICATION:
	case InputSentenceActivity.EXISTENTIAL_QUANITIFICATION:
	    setTitle("Quantify variable");
	    setContentView(R.layout.quantification_dialog);
	    break;

	case InputSentenceActivity.EQUALS_ASSIGNMENT:
	    setTitle("Equals assignment");
	    setContentView(R.layout.equals_dialog);
	    break;
	}

    }

    public void clickOkButton(View view) {
	EditText input;
	String value;
	Intent intent = new Intent();
	/*
	 * Different validation is used depending upon the request type.
	 * when validation is passed result is returned to the calling 
	 * activity.
	 */
	switch (type) {
	case InputSentenceActivity.ADD_NEW_PREDICATE:
	    input = (EditText) findViewById(R.id.txtPredicate);
	    value = input.getText().toString();
	    value = value.replaceAll("\\s", "");
	    if (value.isEmpty()) {
		Toast.makeText(this,
			"A predicate must be at least one character long",
			Toast.LENGTH_LONG).show();
	    } else {
		value = value.substring(0, 1).toUpperCase()
			+ value.substring(1).toLowerCase();
		intent.putExtra("Predicate", value);
		setResult(RESULT_OK, intent);
		finish();
	    }
	    break;
	case InputSentenceActivity.EXISTENTIAL_QUANITIFICATION:
	case InputSentenceActivity.UNIVERSAL_QUANTIFICATION:
	    input = (EditText) findViewById(R.id.txtQuantification);
	    value = input.getText().toString();
	    value = value.replaceAll("\\s", "");
	    if (value.isEmpty()) {
		Toast.makeText(this,
			"A variable must be at least one character long",
			Toast.LENGTH_LONG).show();
	    } else {
		value = value.trim();
		value = value.toLowerCase();
		intent.putExtra("Variable", value);
		setResult(RESULT_OK, intent);
		finish();
	    }
	    break;
	case InputSentenceActivity.EQUALS_ASSIGNMENT:
	    EditText txtLHS = (EditText) findViewById(R.id.lhsEquals);
	    EditText txtRHS = (EditText) findViewById(R.id.rhsEquals);
	    String lhs = txtLHS.getText().toString();
	    String rhs = txtRHS.getText().toString();

	    rhs = rhs.replaceAll("\\s", "");
	    lhs = lhs.replaceAll("\\s", "");
	    if (lhs.isEmpty() || rhs.isEmpty()) {
		Toast.makeText(this,
			"A variable must be at least one character long",
			Toast.LENGTH_LONG).show();
	    } else {
		lhs = lhs.trim();
		lhs = lhs.toLowerCase();
		intent.putExtra("lhs", lhs);
		rhs = rhs.trim();
		rhs = rhs.toLowerCase();
		intent.putExtra("rhs", rhs);
		setResult(RESULT_OK, intent);
		finish();
	    }
	    break;
	}

    }

    public void clickCancelButton(View view) {
	setResult(RESULT_CANCELED);
	finish();
    }
}
