package edu.toronto.ece1778.urbaneyes;

import com.actionbarsherlock.app.SherlockActivity;

import edu.toronto.ece1778.urbaneyes.common.Question;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NumberQuestionActivity extends SherlockActivity {

	Question q;
	TextView tvQuestion;
	EditText etNumber;
	Button submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_question);
		
		q = SurveyStateHolder.getCurrentQuestion();
		
		tvQuestion = (TextView) findViewById(R.id.number_question);
		tvQuestion.setText(q.getDesc());
		
		etNumber = (EditText) findViewById(R.id.editTextNumber);
		etNumber.setHint("0.0");

		submit = (Button) findViewById(R.id.buttonSubmit);
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.number_question, menu);
		return true;
	}
	*/
	
	public void onClickSubmitButton(View arg1) {
		// TODO send survey result to server
		double value = 0;
		Editable ed = etNumber.getText();
		if (ed != null && ed.toString() != null && !ed.toString().isEmpty()) {
			Double.parseDouble(ed.toString());
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			
			finish();
		}
	}

}
