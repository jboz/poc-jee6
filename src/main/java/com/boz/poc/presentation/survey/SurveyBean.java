package com.boz.poc.presentation.survey;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.boz.poc.domain.survey.Question;

@ManagedBean
@SessionScoped
public class SurveyBean {

	public List<Question> questions = new ArrayList<>();

	@PostConstruct
	public void init() {
		questions.add(createQuestion("poidsTaille"));
		questions.add(createQuestion("medecinTraitant"));
	}

	private static Question createQuestion(final String code) {
		final Question question = new Question();
		question.setCode(code);

		return question;
	}

	public List<Question> getQuestions() {
		return questions;
	}
}
