package edu.toronto.ece1778.urbaneyes;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.view.Menu;
import com.actionbarsherlock.app.SherlockActivity;

import edu.toronto.ece1778.urbaneyes.common.Question;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TextQuestionActivity extends SherlockActivity {

	Question q;
	TextView tvQuestion;
	EditText etText;
	Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_question);

		q = SurveyStateHolder.getCurrentQuestion();
		
		tvQuestion = (TextView) findViewById(R.id.text_question);
		tvQuestion.setText(q.getDesc());
		
		etText = (EditText) findViewById(R.id.editTextText);
		etText.setHint("");
		
		submit = (Button) findViewById(R.id.buttonSubmit);
		
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_question, menu);
		return true;
	}
	 */

	public void onClickSubmitButton(View arg1) {
		// TODO send survey result to server
		Editable ed = etText.getText();
		if (ed != null && !ed.toString().isEmpty()) {
			String value = etText.getText().toString();
			
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			
			finish();
		}
	}
	
}
