package edu.toronto.ece1778.urbaneyes;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

import com.actionbarsherlock.app.SherlockActivity;

import edu.toronto.ece1778.urbaneyes.common.Question;
import edu.toronto.ece1778.urbaneyes.common.RadioGroupQuestion;
import edu.toronto.ece1778.urbaneyes.common.RadioGroupQuestion.Option;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RadioGroupQuestionActivity extends SherlockActivity {

	RadioGroupQuestion q;
	TextView tvQuestion;
	RadioGroup rg;
	Button submit;

	ArrayList<RadioButton> rbl = new ArrayList<RadioButton>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio_group_question);

		q = (RadioGroupQuestion) SurveyStateHolder.getCurrentQuestion();
		
		tvQuestion = (TextView) findViewById(R.id.radio_question);
		tvQuestion.setText(q.getDesc());
		
		rg = (RadioGroup) findViewById(R.id.radioGroup);
		
		int i = 0;
		for (Option o : q.getOptions()) {
			RadioButton b = new RadioButton(this);
			b.setText(o.getDesc());
			b.setId(i);
			if (i == 0) {
				b.setChecked(true);
			}
			rg.addView(b);
			rbl.add(b);
			i++;
		}
		submit = (Button) findViewById(R.id.buttonSubmit);

	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.radio_group_question, menu);
		return true;
	}
	*/

	public void onClickSubmitButton(View arg1) {
		// TODO send survey result to server

		int selectedId = rg.getCheckedRadioButtonId();
		RadioButton b = rbl.get(selectedId);
		Option so = q.getOptions().get(selectedId);

		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		
		finish();
	}
	
}
