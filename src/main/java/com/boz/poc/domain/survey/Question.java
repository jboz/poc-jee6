package com.boz.poc.domain.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Question<?> parent;
	private String code;
	private FormType formType;
	private boolean multiResponse = false;
	private Class<T> responseType;

	private List<Question<?>> subQuestions = new ArrayList<>();
	private List<Response<T>> responses = new ArrayList<>();

	public void addSubQuestions(final Question<?> subQuestion) {
		subQuestions.add(subQuestion);
		subQuestion.setParent(this);
	}

	public int getNbSubResponses() {
		int cpt = 0;
		for (final Question<?> question : subQuestions) {
			cpt += question.getResponses().size();
		}
		return cpt;
	}

	public Response<T> getFirstResponse() {
		return getResponse(0);
	}

	public List<T> getResponsesValues() {
		List<T> values = new ArrayList<>();
		for (Response<T> response : responses) {
			values.add(response.getValue());
		}

		return values;
	}

	public Response<T> getResponse(final int index) {
		return index < 0 ? null : index >= responses.size() ? addNewResponse() : responses.get(index);
	}

	public List<Response<T>> getResponses() {
		return responses;
	}

	public Response<T> addNewResponse() {
		final Response<T> response = new Response<>();
		response.setQuestion(this);
		responses.add(response);

		return response;
	}

	public List<Question<?>> getSubQuestions() {
		return subQuestions;
	}

	public Question<?> getSubQuestion(final String code) {
		for (final Question<?> subQuestion : subQuestions) {
			if (code.equals(subQuestion.getCode())) {
				return subQuestion;
			}
		}
		return null;
	}

	public Question<?> getParent() {
		return parent;
	}

	public void setParent(final Question<?> parent) {
		this.parent = parent;
	}

	public String getFinalCode() {
		return (parent != null ? parent.getFinalCode() + "." : "") + getCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public FormType getFormType() {
		return formType;
	}

	public void setFormType(final FormType formType) {
		this.formType = formType;
	}

	public boolean isMultiResponse() {
		return multiResponse;
	}

	public void setMultiResponse(final boolean multiResponse) {
		this.multiResponse = multiResponse;
	}

	public Class<T> getResponseType() {
		return responseType;
	}

	public void setResponseType(final Class<T> responseType) {
		this.responseType = responseType;
	}

}
