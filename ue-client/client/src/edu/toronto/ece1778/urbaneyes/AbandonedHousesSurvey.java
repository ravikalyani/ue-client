package edu.toronto.ece1778.urbaneyes;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AbandonedHousesSurvey extends SherlockActivity {

  TextView tvQ1;
	TextView tvQ2;
	TextView tvQ3;

	RadioGroup rgQ1;
	EditText etQ2;
	EditText etQ3;
	
	Button buttonSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abandoned_houses_survey);
		
		tvQ1 = (TextView) findViewById(R.id.textViewAHSQ1);
		rgQ1 = (RadioGroup) findViewById(R.id.radioAHSQ1);
		
		tvQ2 = (TextView) findViewById(R.id.textViewAHSQ2);
		etQ2 = (EditText) findViewById(R.id.editTextAHSQ2);
		
		tvQ3 = (TextView) findViewById(R.id.textViewAHSQ3);
		etQ3 = (EditText) findViewById(R.id.editTextAHSQ3);
		
		buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.abandoned_houses_survey, menu);
		return true;
	}
	*/

	public void onClickSubmitButton(View arg1) {

		finish();          // TODO comment out this later...
	}

}
