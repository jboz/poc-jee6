package com.boz.poc.domain.survey;

import java.io.Serializable;

public class Response<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Question<T> question;
	private T value;

	public Question<T> getQuestion() {
		return question;
	}

	public void setQuestion(final Question<T> question) {
		this.question = question;
	}

	public T getValue() {
		return value;
	}

	public void setValue(final T value) {
		this.value = value;
	}

}
