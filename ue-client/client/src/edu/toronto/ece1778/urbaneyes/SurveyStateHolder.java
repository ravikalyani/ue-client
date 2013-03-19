package edu.toronto.ece1778.urbaneyes;

import java.util.ArrayList;
import java.util.Iterator;

import edu.toronto.ece1778.urbaneyes.common.*;

public class SurveyStateHolder {

	private static ArrayList<SurveyType> surveyTypes = new ArrayList<SurveyType>()
			// list of survey types for current user
			;
	
	private static SurveyType currentSurveyType;
	
	private static Iterator<Question> qi;        // question iterator

	private static Question currentQuestion;

	public static ArrayList<SurveyType> getSurveyTypes() {
		return surveyTypes;
	}
	
	public static void clearSurveyTypes() {
		surveyTypes.clear();
	}
	
	public static ArrayList<String> getSurveyNames() {
		ArrayList<String> al = new ArrayList<String>();
		for (SurveyType st : surveyTypes) {
			al.add(st.getName());
		}
		return al;
	}
	
	public static SurveyType getCurrentSurveyType() {
		return currentSurveyType;
	}

	public static SurveyType getSurveyType(int id) {
		for (SurveyType st : surveyTypes) {
			if (st.getId() == id) {
				return st;
			}
		}
		return null;
	}

	public static void setCurrentSurveyType(String surveyName) {
		for (SurveyType st : surveyTypes) {
			if (st.getName().equals(surveyName)) {
				currentSurveyType = st;
				qi = currentSurveyType.getQuestions().iterator();
				break;
			}
		}
	}
	
	public static Question getCurrentQuestion() {
		return currentQuestion;
	}
	
	public static boolean hasQuestion() {
		if (qi == null)
			qi = currentSurveyType.getQuestions().iterator();
		return qi.hasNext();
	}
	
	public static Question getNextQuestion() {
		if (qi == null)
			return null;
		currentQuestion = qi.next(); 
		return currentQuestion;
	}
	
	public static void addSurveyType(SurveyType st) {
		surveyTypes.add(st);
	}

	public static void reset() { 
		qi = null;
		currentQuestion = null;
	}
}
