package com.boz.poc.presentation.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.boz.poc.domain.survey.FormType;
import com.boz.poc.domain.survey.Question;

@ManagedBean
@SessionScoped
public class SurveyBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public List<Question<?>> questions = new ArrayList<>();

	public List<Question<?>> getQuestions() {
		return questions;
	}

	@PostConstruct
	public void init() {
		questions.add(createTaillePoids());
		questions.add(createFumezVous());
		questions.add(createMedecinTraitant());
	}

	private Question<String> createMedecinTraitant() {
		final Question<String> question =  createQuestion("medecinTraitant", false, FormType.COLLAPSIBLE, true);
		question.addSubQuestions(createQuestion("nom", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("prenom", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("rue", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("npa", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("ville", FormType.TEXT_FIELD));

		return question;
	}

	private Question<String> createFumezVous() {
		final Question<String> question = createQuestion("fumezVous", false, FormType.COLLAPSIBLE, true);
		question.addSubQuestions(createQuestion("quoi", FormType.COMBO, FumeQuoi.class));
		question.addSubQuestions(createQuestion("frequence", FormType.COMBO, FumeFrequence.class));

		find(0, question).getResponse(0).setValue(FumeQuoi.CIGARETTE);
		find(1, question).getResponse(0).setValue(FumeFrequence.MAX20);
		find(0, question).getResponse(1).setValue(FumeQuoi.JOINT);
		find(1, question).getResponse(1).setValue(FumeFrequence.MAX5);

		return question;
	}

	@SuppressWarnings("unchecked")
	private static <T> Question<T> find(final int index, final Question<?> question) {
		return (Question<T>) question.getSubQuestions().get(index);
	}

	private Question<String> createTaillePoids() {
		final Question<String> question = createQuestion("taillePoids", FormType.COLLAPSIBLE);
		question.addSubQuestions(createQuestion("taille", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("poids", FormType.TEXT_FIELD));

		return question;
	}

	private Question<String> createQuestion(final String code, final FormType type) {
		return createQuestion(code, true, type, false);
	}

	private <T> Question<T> createQuestion(final String code, final FormType type, final Class<T> reponseType) {
		return createQuestion(code, true, type, false, reponseType);
	}

	private Question<String> createQuestion(final String code, final boolean mandatory, final FormType type,
			final boolean multiResponse) {
		return createQuestion(code, mandatory, type, multiResponse, String.class);
	}

	private <T> Question<T> createQuestion(final String code, final boolean mandatory, final FormType type,
			final boolean multiResponse, final Class<T> reponseType) {
		final Question<T> question = new Question<>();
		question.setCode(code);
		question.setFormType(type);
		question.setMultiResponse(multiResponse);
		question.setResponseType(reponseType);
		return question;
	}

	public static enum FumeQuoi {
		CIGARETTE, JOINT, AUTRE;
	}

	public static enum FumeFrequence {
		MAX5, MAX10, MAX20, NO_LIMIT;
	}

	public <T> Object[] values(final Class<T> type) {
		return type.getEnumConstants();
	}
}
