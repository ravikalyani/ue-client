package edu.toronto.ece1778.urbaneyes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.hardware.SensorManager;
import android.util.Log;

/**
 * Processor of the barometer data inferring the altitude.
 * 
 * @author mcupak
 * 
 */
public class AltitudeProcessor {
	private float sea_level_pressure = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;

	public static final String TAG_PRESSURE = "aws:pressure";
	public static final String TAG_LONGITUDE = "aws:longitude";
	public static final String TAG_LATITUDE = "aws:latitude";
	private static final String WEATHER_URL = "http://i.wxbug.net/REST/SP/getLiveWeatherRSS.aspx?api_key=ddm4ymenfw3qbx7u2vxgecma&lat=%f&long=%f&UnitType=1&OutputType=1";
	private float refPressure = sea_level_pressure;
	private boolean computed = false;

	public float getAltitude(float pressure) {
		return SensorManager.getAltitude(refPressure, pressure);
	}

	public void computeRefPressure(double longitude, double latitude) {
		InputStream is = null;
		computed = true;
		try {
			URL text = new URL(String.format(WEATHER_URL, latitude, longitude));

			URLConnection connection = text.openConnection();
			connection.setReadTimeout(30000);
			connection.setConnectTimeout(30000);

			is = connection.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder domParser = dbf.newDocumentBuilder();
			Document xmldoc = domParser.parse(is);
			Element root = xmldoc.getDocumentElement();

			refPressure = Float.parseFloat(getTagValue(TAG_PRESSURE, root));
		} catch (Exception e) {
			refPressure = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}

	public boolean isComputed() {
		return computed;
	}
}
