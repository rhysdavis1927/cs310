package logic.proof.builder.gui;

import java.util.ArrayList;
import java.util.HashMap;

import logic.proof.builder.ROI.ProofStep;
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

    private class ViewHolder {
	TextView txtFormula;
	TextView txtJustification;
	TextView txtLineNumber;
	View topDivider;
	View bottomDivider;
	View leftDivider;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	TextView txtFormula;
	TextView txtJustification;
	TextView txtLineNumber;
	View topDivider;
	View bottomDivider;

	LayoutInflater inflater = activity.getLayoutInflater();
	ProofStep step = list.get(position);

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
	    }

	    /*
	    holder = new ViewHolder();
	    holder.topDivider = (View) convertView
		    .findViewById(R.id.topDivider);

	    holder.bottomDivider = (View) convertView
		    .findViewById(R.id.bottomDivider);

	    holder.leftDivider = (View) convertView
		    .findViewById(R.id.leftDivider);

	    holder.txtFormula = (TextView) convertView
		    .findViewById(R.id.formulaView);
	    holder.txtJustification = (TextView) convertView
		    .findViewById(R.id.justificationView);
	    holder.txtLineNumber = (TextView) convertView
		    .findViewById(R.id.lineView);
	    convertView.setTag(holder);
	    */
	    topDivider = (View) convertView
		    .findViewById(R.id.topDivider);

	    bottomDivider = (View) convertView
		    .findViewById(R.id.bottomDivider);

	    txtFormula = (TextView) convertView
		    .findViewById(R.id.formulaView);
	    txtJustification = (TextView) convertView
		    .findViewById(R.id.justificationView);
	    txtLineNumber = (TextView) convertView
		    .findViewById(R.id.lineView);


	if (step.level == step.parent.level) {
	    topDivider.setVisibility(View.GONE);
	} else {
	   topDivider.setVisibility(View.VISIBLE);
	   topDivider.setTranslationX(PADDING * step.level + 56);
	}
	if (!step.endOfSubproof) {
	    bottomDivider.setVisibility(View.GONE);
	} else {
	    bottomDivider.setVisibility(View.VISIBLE);
	    bottomDivider.setTranslationX(PADDING * step.level + 56);
	}

	// holder.leftDivider.setTranslationX(PADDING*step.level+10);
	// holder.txtFormula.setTranslationX(PADDING*step.level);

	txtFormula.setText((CharSequence) step.formula);
	txtJustification.setText((CharSequence) step.justification);
	txtLineNumber.setText((CharSequence) format(step.lineNumber));

	return convertView;
    }

    private String format(Integer i) {
	if (i.toString().length() == 1) {
	    return 0 + i.toString();
	} else
	    return i.toString();
    }
}