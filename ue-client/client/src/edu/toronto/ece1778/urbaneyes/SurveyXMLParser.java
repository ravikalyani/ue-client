package edu.toronto.ece1778.urbaneyes;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import edu.toronto.ece1778.urbaneyes.common.*;

public class SurveyXMLParser {

	/*
	 * <survey>
	 * 		<id>6</id>
	 * 		<name>Test Survey</name>
	 * 		<priv>false</priv>
	 * 		<owner>
	 * 			<email>foo@example.com</email>
	 * 			<id>1</id>
	 * 			<name>Sean Smith</name>
	 * 			<password>hello</password>
	 * 		</owner>
	 * 		<contributors>
	 * 			<email>foo@example.com</email>
	 * 			<id>1</id>
	 * 			<name>Sean Smith</name>
	 * 			<password>hello</password>
	 * 		</contributors>
	 * 		<description>This is a sample survey.</description>
	 * 		<questions>
	 * 			<answerType>TEXT</answerType>
	 * 			<id>4</id>
	 * 			<name>What is this place?</name>
	 * 		</questions>
	 * </survey>
	 *  
	 */

	private static final String ns = null;
	
	SurveyType st;
	
	public SurveyXMLParser(SurveyType st) {
		this.st = st;
	}
	
	public void parse(InputStream in)
			throws Exception {
		
		try {
			XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readSurvey(parser);
		} finally {
			in.close();
		}
	}
	
	private void readSurvey(XmlPullParser parser) throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, ns, "survey");
		while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("questions")) {
	            st.addQuestion(readQuestion(parser));
	        } else {
	            skip(parser);
	        }
	    }
	}

	private Question readQuestion(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		Question q = new Question();
		parser.require(XmlPullParser.START_TAG, ns, "questions");
		while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("answerType")) {
	            q.setAnsType(readAnswerType(parser));
	        } else if (name.equals("id")) {
	            q.setId(readId(parser));
	        } else if (name.equals("name")) {
	            q.setDesc(readName(parser));
	        } else {
	            skip(parser);
	        }
	    }
		return q;
	}

	private AnswerType readAnswerType(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "answerType");
	    String at = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "answerType");
	    if (at.equals("NUMBER")) {
	    	return AnswerType.NUMBER;
	    } else if (at.equals("TEXT")) {
	    	return AnswerType.TEXT;
	    } else if (at.equals("RADIOGROUP")) {
	    	return AnswerType.RADIOGROUP;
	    } else if (at.equals("LONGITUDE")) {
	    	return AnswerType.LONGITUDE;
	    } else if (at.equals("LATITUDE")) {
	    	return AnswerType.LATITUDE;
	    } else if (at.equals("ALTITUDE")) {
	    	return AnswerType.ALTITUDE;
	    }
	    return AnswerType.TEXT;
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
