package edu.toronto.ece1778.urbaneyes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class FoodVendorSurveyActivity extends SherlockActivity {

	TextView tvVendorType;
	RadioGroup rgVendor;
	RadioButton rbVendor;
	TextView tvVeg;
	RadioGroup rgVegOpt;
	RadioButton rbVegOpt;
	TextView tvMinVegOpt;
	EditText etMinVegPrice;
	Button buttonSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_vendor_survey);

		tvVendorType = (TextView) findViewById(R.id.textViewLabelVendorType);
		rgVendor = (RadioGroup) findViewById(R.id.radioVendorType);
		tvVeg = (TextView) findViewById(R.id.textViewVeg);
		rgVegOpt = (RadioGroup) findViewById(R.id.radioVegOpt);
		tvMinVegOpt = (TextView) findViewById(R.id.textViewMinVegOpt);
		etMinVegPrice = (EditText) findViewById(R.id.editTextVegPrice);
		buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.food_vendor_survey, menu); return true;
	 * }
	 */

	public void onClickSubmitButton(View arg1) {

		int rbVenId = rgVendor.getCheckedRadioButtonId();
		rbVendor = (RadioButton) findViewById(rbVenId);

		int rbIdVegOpt = rgVegOpt.getCheckedRadioButtonId();
		rbVegOpt = (RadioButton) findViewById(rbIdVegOpt);

		Intent returnIntent = new Intent();
		// returnIntent.putExtra("result","");
		setResult(RESULT_OK, returnIntent);
		finish();
	}

}
