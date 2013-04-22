package logic.proof.builder.gui;

import java.util.ArrayList;

import logic.proof.builder.proof.ProofStep;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
    static final String FORMULA = "Formula";
    static final String JUSTIFICATION = "Justification";
    static final String LINE_NUMBER = "Line Number";
    static final int PADDING = 45;
    public ArrayList<ProofStep> list;
    Activity activity;

    public ListViewAdapter(Activity activity, ArrayList<ProofStep> list) {
	super();
	this.activity = activity;
	this.list = list;
    }

    public int getCount() {
	// TODO Auto-generated method stub
	return list.size();
    }

    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return list.get(position);
    }

    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	TextView txtFormula;
	TextView txtJustification;
	TextView txtLineNumber;
	View topDivider;
	View bottomDivider;
	TextView txtVariable;

	LayoutInflater inflater = activity.getLayoutInflater();
	ProofStep step = list.get(position);

	/*
	 * The presentation of each line of the list view depends upon
	 * the level of the that line e.g level 1 is a subproof, level 2 is
	 * a subproof nested in subproof.
	 */
	switch (step.level) {
	case 0:
	    convertView = inflater.inflate(R.layout.row0, null);
	    break;
	case 1:
	    convertView = inflater.inflate(R.layout.row1, null);

	    break;
	case 2:
	    convertView = inflater.inflate(R.layout.row2, null);
	    break;
	case 3:
	    convertView = inflater.inflate(R.layout.row3, null);
	    break;
	case 4:
	    convertView = inflater.inflate(R.layout.row4, null);
	    break;
	}

	topDivider = (View) convertView.findViewById(R.id.topDivider);

	bottomDivider = (View) convertView.findViewById(R.id.bottomDivider);

	txtFormula = (TextView) convertView.findViewById(R.id.formulaView);
	txtJustification = (TextView) convertView
		.findViewById(R.id.justificationView);
	txtLineNumber = (TextView) convertView.findViewById(R.id.lineView);

	txtVariable = (TextView) convertView.findViewById(R.id.variableView);

	txtFormula.setText((CharSequence) step.formula);
	txtJustification.setText((CharSequence) step.justification);
	txtLineNumber.setText((CharSequence) format(step.lineNumber));

	if (step.level == step.parent.level) {
	    // If not a different level to parent, then no divider 
	    //at top needed
	    topDivider.setVisibility(View.GONE);
	} else {
	    //otherwise divider is need and indentation
	    // depends on level
	    topDivider.setVisibility(View.VISIBLE);
	    topDivider.setTranslationX(PADDING * step.level + 56);
	}
	if (!step.endOfSubproof) {
	    // If not last line of subproof then no divider 
	    //at bottom needed
	    bottomDivider.setVisibility(View.INVISIBLE);
	} else {
	  //otherwise divider is need and indentation
	    // depends on level
	    bottomDivider.setVisibility(View.VISIBLE);
	    bottomDivider.setTranslationX(PADDING * step.level + 56);
	}

	if (step.introducedVariable != null) {
	    txtVariable.setText(step.introducedVariable);
	    txtVariable.setVisibility(View.VISIBLE);

	} else {
	    txtVariable.setVisibility(View.GONE);
	}

	if (step.parent.introducedVariable != null) {

	} else {

	}


	return convertView;
    }

    private String format(Integer i) {
	if (i.toString().length() == 1) {
	    return 0 + i.toString();
	} else
	    return i.toString();
    }
}