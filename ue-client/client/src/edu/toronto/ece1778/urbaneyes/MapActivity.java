package edu.toronto.ece1778.urbaneyes;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
// import android.view.Menu;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Menu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.toronto.ece1778.urbaneyes.common.SurveyKind;

/**
 * Activity showing the map with the current position.
 * 
 * @author mcupak
 * 
 */
public class MapActivity extends AbstractMapActivity implements
		OnNavigationListener, OnInfoWindowClickListener, OnMarkerDragListener,
		LocationSource, LocationListener {

	public static final String TAG = "URBANEYES";
	private static final String STATE_NAV = "nav";
	private static final int[] MAP_TYPE_NAMES = { R.string.normal,
			R.string.hybrid, R.string.satellite, R.string.terrain };
	private static final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_NORMAL,
			GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_SATELLITE,
			GoogleMap.MAP_TYPE_TERRAIN };
	private GoogleMap map = null;
	private static final LatLng DEFAULT_LOCATION = new LatLng(
			43.660972, -79.398483);
	private OnLocationChangedListener mapLocationListener = null;
	private LocationManager locMgr = null;
	private LatLng currentLocation = DEFAULT_LOCATION;
	private float currentAltitude = 0;
	private String currentProject = "Project 1";
	private SensorManager mSensorManager = null;

	private Criteria crit = new Criteria();
	private AltitudeProcessor altitudeProcessor = new AltitudeProcessor();
	private SensorEventListener mSensorListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// when accuracy changed, this method will be called.
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (Sensor.TYPE_PRESSURE == event.sensor.getType()) {
				currentAltitude = altitudeProcessor
						.getAltitude(event.values[0]);
			}
		}
	};

	// task downloading the reference pressure value by location
	class ComputePressureTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			altitudeProcessor.computeRefPressure(currentLocation.longitude,
					currentLocation.latitude);
			return null;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// prepare maps
		if (readyToGo()) {
			setContentView(R.layout.activity_map);

			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map);

			initListNav();

			map = mapFrag.getMap();

			if (savedInstanceState == null) {
				CameraUpdate center = CameraUpdateFactory
						.newLatLng(DEFAULT_LOCATION);
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

				map.moveCamera(center);
				map.animateCamera(zoom);
			}

			map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
			map.setOnInfoWindowClickListener(this);
			map.setOnMarkerDragListener(this);

			locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
			crit.setAccuracy(Criteria.ACCURACY_FINE);

			map.setMyLocationEnabled(true);
			map.getUiSettings().setMyLocationButtonEnabled(false);

			// load params
			// TODO: check current survey type set
			int i = getIntent().getIntExtra("selectedProject", 0);
			currentProject = SurveyStateHolder.getSurveyNames().get(i);
			SurveyStateHolder.setCurrentSurveyType(currentProject);
			/*
			case 0:
				currentProject = "Food vendor";
				break;
			case 1:
				currentProject = "Subway entrance";
				break;
			default:
				currentProject = "Uncategorized";
				break;
			}
			*/
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_NORMAL);
		locMgr.requestLocationUpdates(0L, 0.0f, crit, this, null);
		map.setLocationSource(this);
	}

	@Override
	public void onPause() {
		map.setLocationSource(null);
		locMgr.removeUpdates(this);
		mSensorManager.unregisterListener(mSensorListener);

		super.onPause();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		map.setMapType(MAP_TYPES[itemPosition]);

		return (true);
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// empty
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		// empty
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		LatLng position = marker.getPosition();
		marker.setSnippet(formatSnippet(currentLocation, currentAltitude));
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putInt(STATE_NAV, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		getSupportActionBar().setSelectedNavigationItem(
				savedInstanceState.getInt(STATE_NAV));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addPoint:
			// create a point
			openSurvey();
			return (true);
		case R.id.beginPath:
			beginPath();
			return true;
		case R.id.endPath:
			endPath();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		openSurvey();
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		this.mapLocationListener = listener;
	}

	@Override
	public void deactivate() {
		this.mapLocationListener = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (mapLocationListener != null) {
			mapLocationListener.onLocationChanged(location);

			currentLocation = new LatLng(location.getLatitude(),
					location.getLongitude());

			// compute reference altitude when location is registered for the
			// first time
			// if (!altitudeProcessor.isComputed()) {     // COMMENTED BY RAVI
				new ComputePressureTask().execute(null, null, null);
			// }       // COMMENTED BY RAVI

			// center camera on location
			CameraUpdate cu = CameraUpdateFactory.newLatLng(currentLocation);
			map.animateCamera(cu);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// unused
	}

	@Override
	public void onProviderEnabled(String provider) {
		// unused
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// unused
	}

	private void initListNav() {
		ArrayList<String> items = new ArrayList<String>();
		ArrayAdapter<String> nav = null;
		ActionBar bar = getSupportActionBar();

		for (int type : MAP_TYPE_NAMES) {
			items.add(getString(type));
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			nav = new ArrayAdapter<String>(bar.getThemedContext(),
					android.R.layout.simple_spinner_item, items);
		} else {
			nav = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, items);
		}

		nav.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setListNavigationCallbacks(nav, this);
	}

	private void openSurvey() {
		Intent i = new Intent(this, StartSurveyActivity.class);
		startActivityForResult(i, 1);
		/*
		switch (getIntent().getIntExtra("selectedProject", 0)) {
		case 0:
			i = new Intent(this, FoodVendorSurveyActivity.class);
			startActivityForResult(i, 1);
			break;
		case 1:
			i = new Intent(this, SubwayEntranceSurvey.class);
			startActivityForResult(i, 1);
			break;
		default:
			i = new Intent(this, FoodVendorSurveyActivity.class);
			startActivityForResult(i, 1);
			break;
		}
		*/
	}

	private void addPoint(GoogleMap map, LatLng latLng, String title,
			String snippet) {
		map.addMarker(new MarkerOptions().position(latLng).title(title)
				.snippet(snippet).draggable(true));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				// String result=data.getStringExtra("result");
				addPoint(map, currentLocation, currentProject,
						formatSnippet(currentLocation, currentAltitude));
			}
			if (resultCode == RESULT_CANCELED) {
				// clean up
			}
		}
	}

	private String formatSnippet(LatLng position, float altitude) {
		StringBuilder output = new StringBuilder();
		output.append("Lat: ");
		output.append(position.latitude);
		output.append("\n");
		output.append("Lon: ");
		output.append(position.longitude);
		output.append("\n");
		output.append("Alt: ");
		output.append(altitude);
		output.append("\n");

		return output.toString();
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear(); //Clear view of previous menu
        if(SurveyStateHolder.getCurrentSurveyType().getKind() == SurveyKind.POINT) {
        	getSupportMenuInflater().inflate(R.menu.map, menu);
        } else if(SurveyStateHolder.getCurrentSurveyType().getKind() == SurveyKind.PATH) {
        	getSupportMenuInflater().inflate(R.menu.map_path, menu);
        }
      
        return super.onPrepareOptionsMenu(menu);
    }	

	/*  PATH RELATED CODE */
	
	boolean beginPath = false;
	boolean endPath = false;
	
	class TrackPointsOnPathTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			SurveyPoint prev = new SurveyPoint();
			prev.latLng = new LatLng(0,0);
			prev.alt = currentAltitude;
			final double RADIUS = 6731;
			final double THRESHOLD = 50;   // meters
			
			while (!endPath) {
				double lat1 = Math.toRadians(prev.latLng.latitude);
				double lat2 = Math.toRadians(currentLocation.latitude);
				
				double lon1 = Math.toRadians(prev.latLng.longitude);
				double lon2 = Math.toRadians(currentLocation.longitude);
				
				double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + 
		                  			 Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2-lon1)) * RADIUS * 1000;
				if (d >= THRESHOLD) {
					MapActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							addPoint(map, currentLocation, currentProject,
									 formatSnippet(currentLocation, currentAltitude));
							// TODO : send data for point of current path survey
						}
					});
					prev.latLng = currentLocation;
					prev.alt = currentAltitude;
				}
				try {
					Thread.sleep(5000);  // sleep for 5 seconds
				} catch (InterruptedException e) {
					
				}
			}
			return null;
		}

	}

	class SurveyPoint {
		public LatLng latLng;
		public float alt;
	}
	
	public void beginPath() {
		beginPath = true;
		endPath = false;
		new TrackPointsOnPathTask().execute(null, null, null);
	}
	
	public void endPath() {
		beginPath = false;
		endPath = true;
	}
	
}
