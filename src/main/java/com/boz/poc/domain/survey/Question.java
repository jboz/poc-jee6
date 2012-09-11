package com.boz.poc.domain.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private boolean mandatory = false;
	private boolean multiResponse = true;
	private List<Question<?>> subQuestions = new ArrayList<>();
	private Question<?> parent;
	private FormType formType;

	private Class<T> responseType;
	private List<Response<T>> responses = new ArrayList<>();

	public String getCode() {
		return code;
	}

	public String getFinalCode() {
		return (parent != null ? parent.getFinalCode() + "." : "") + code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public int getNbSubResponses() {
		int nb = 0;
		for (final Question<?> question : subQuestions) {
			nb = Math.max(nb, question.getResponses().size());
		}
		// create one if empty
		return (nb < 1 ? 1 : nb) - 1;
	}

	public List<Response<T>> getResponses() {
		return responses;
	}

	public Response<T> getFirstResponse() {
		return getResponse(0);
	}

	public Response<T> getResponse(final int index) {
		while (responses.size() <= index) {
			addResponse();
		}
		return responses.get(responses.size() - 1);
	}

	private Response<T> addResponse() {
		final Response<T> response = new Response<>();
		response.setQuestion(this);
		responses.add(response);

		return response;
	}

	public FormType getFormType() {
		return formType;
	}

	public void setFormType(final FormType formType) {
		this.formType = formType;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(final boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isMultiResponse() {
		return multiResponse;
	}

	public void setMultiResponse(final boolean multiResponse) {
		this.multiResponse = multiResponse;
	}

	public List<Question<?>> getSubQuestions() {
		return subQuestions;
	}

	public void addSubQuestions(final Question<?> question) {
		subQuestions.add(question);
		question.parent = this;
	}

	public Question<?> getParent() {
		return parent;
	}

	public Class<?> getResponseType() {
		return responseType;
	}

	public void setResponseType(final Class<T> responseType) {
		this.responseType = responseType;
	}
}
