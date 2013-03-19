package edu.toronto.ece1778.urbaneyes;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Activity showing the legal terms.
 * 
 * @author mcupak
 *
 */
public class LicenseActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_license);

		TextView license = (TextView) findViewById(R.id.txtLicense);

		license.setText(GooglePlayServicesUtil
				.getOpenSourceSoftwareLicenseInfo(this));
	}
}
