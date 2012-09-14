package com.boz.poc.presentation.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.boz.poc.domain.survey.BrevetLicence;
import com.boz.poc.domain.survey.FormType;
import com.boz.poc.domain.survey.FumeFrequence;
import com.boz.poc.domain.survey.FumeQuoi;
import com.boz.poc.domain.survey.Localisation;
import com.boz.poc.domain.survey.Niveau;
import com.boz.poc.domain.survey.Question;
import com.boz.poc.domain.survey.Sports;

@ManagedBean
@SessionScoped
public class SurveyBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public List<Question<?>> questions = new ArrayList<>();

	public List<Question<?>> getQuestions() {
		return questions;
	}

	/**
	 * Check if a static form exist for this question.
	 *
	 * @param questionCode
	 * @return
	 */
	public boolean staticFormExists(final String questionCode) {
		return ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
				.getResourceAsStream("/survey-static/" + questionCode + ".xhtml") != null;
	}

	@PostConstruct
	public void init() {
		questions.add(createTaillePoids());
		questions.add(createFumezVous());
		questions.add(createMedecinTraitant());
		questions.add(createSports());
	}

	private Question<?> createSports() {
		final Question<String> question = createQuestion("sports", FormType.COLLAPSIBLE, false, true);
		question.addSubQuestions(createQuestion("activite", FormType.COMBO, Sports.class)); // quoi

		// fréquence suivant activité
		question.addSubQuestions(createQuestion("nbPlongeParAn", FormType.TEXT_FIELD, Integer.class));
		question.addSubQuestions(createQuestion("nbHeureParAn", FormType.TEXT_FIELD, Integer.class));
		question.addSubQuestions(createQuestion("nbTourParAn", FormType.TEXT_FIELD, Integer.class));

		question.addSubQuestions(createQuestion("profondeurMax", FormType.TEXT_FIELD, Integer.class));
		question.addSubQuestions(createQuestion("niveau", FormType.RADIO, Niveau.class));
		question.addSubQuestions(createQuestion("localisation", FormType.RADIO, Localisation.class));
		question.addSubQuestions(createQuestion("club", FormType.CHECK, Boolean.class));
		question.addSubQuestions(createQuestion("brevet", FormType.CHECK_MULTI, BrevetLicence.class));
		// question.addSubQuestions(createQuestion("questions", FormType.COMBO, FumeFrequence.class)); // autres questions

		return question;
	}

	private Question<String> createMedecinTraitant() {
		final Question<String> question = createQuestion("medecinTraitant", FormType.COLLAPSIBLE, false, true);
		question.addSubQuestions(createQuestion("nom", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("prenom", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("rue", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("npa", FormType.TEXT_FIELD));
		question.addSubQuestions(createQuestion("ville", FormType.TEXT_FIELD));

		return question;
	}

	private Question<String> createFumezVous() {
		final Question<String> question = createQuestion("fumezVous", FormType.COLLAPSIBLE, false, true);
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
		return createQuestion(code, type, true, false);
	}

	private <T> Question<T> createQuestion(final String code, final FormType type, final Class<T> reponseType) {
		return createQuestion(code, type, reponseType, true, false);
	}

	private Question<String> createQuestion(final String code, final FormType type, final boolean mandatory,
			final boolean multiResponse) {
		return createQuestion(code, type, null, mandatory, multiResponse);
	}

	private <T> Question<T> createQuestion(final String code, final FormType type, final Class<T> reponseType,
			final boolean mandatory, final boolean multiResponse) {
		final Question<T> question = new Question<>();
		question.setCode(code);
		question.setFormType(type);
		question.setMultiResponse(multiResponse);
		question.setResponseType(reponseType);
		return question;
	}

	public <T> Object[] valuesFromClassName(final String className) {
		try {
			return values(Class.forName(className));
		} catch (final ClassNotFoundException e) {
			return null;
		}
	}

	public <T> Object[] values(final Class<T> type) {
		return type.getEnumConstants();
	}
}
