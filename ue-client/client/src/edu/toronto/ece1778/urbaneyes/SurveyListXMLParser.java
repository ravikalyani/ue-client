package edu.toronto.ece1778.urbaneyes;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import edu.toronto.ece1778.urbaneyes.common.*;

public class SurveyListXMLParser {

	/*
	 * <collection>
	 * 		<survey>
	 * 			<description>This is another survey.</description>
	 * 			<id>7</id>
	 * 			<name>Another Survey</name>
	 * 			<priv>false</priv>
	 * 		</survey>
	 * 		<survey>
	 * 			<description>This is a sample survey.</description>
	 * 			<id>6</id>
	 * 			<name>Test Survey</name>
	 * 			<priv>false</priv>
	 * 		</survey>
	 * </collection>
	 * 
	 */

	private static final String ns = null;
	
	public void parse(InputStream in)
			throws Exception {
		
		try {
			XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readCollection(parser);
		} finally {
			in.close();
		}
	}
	
	private void readCollection(XmlPullParser parser) throws Exception {
		parser.require(XmlPullParser.START_TAG, ns, "collection");
		
		 while (parser.next() != XmlPullParser.END_TAG) {
		        if (parser.getEventType() != XmlPullParser.START_TAG) {
		            continue;
		        }
		        String name = parser.getName();
		        if (name.equals("survey")) {
		            readSurvey(parser);
		        } else {
		            skip(parser);
		        }
		 }
	}
	
	private void readSurvey(XmlPullParser parser) throws XmlPullParserException, IOException {
		SurveyType st = new SurveyType();

		parser.require(XmlPullParser.START_TAG, ns, "survey");
		while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("id")) {
	            st.setId(readId(parser));
	        } else if (name.equals("name")) {
	            st.setName(readName(parser));
	        } else {
	            skip(parser);
	        }
	    }
		
		SurveyStateHolder.addSurveyType(st);
	}
	
	private int readId(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "id");
	    String id = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "id");
	    return Integer.parseInt(id);
	}

	private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "name");
	    String name = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "name");
	    return name;
	}

	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
