package com.boz.poc.domain.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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

	public void setResponsesValues(final List<T> values) {
		responses.clear();
		for (final T value : values) {
			addResponse().setValue(value);
		}
	}

	public List<T> getResponsesValues() {
		final List<T> values = new ArrayList<>();
		for (final Response<T> response : responses) {
			values.add(response.getValue());
		}
		return values;
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
		return responses.get(index);
	}

	public Response<?> getResponse(final String subQuestionCode, final int responseIndex) {
		return getSubQuestion(subQuestionCode).getResponse(responseIndex);
	}

	public Response<T> getResponseByValue(final T value) {
		if (value != null) {
			for (final Response<T> response : responses) {
				if (value.equals(response.getValue())) {
					return response;
				}
			}
		}
		return null;
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

	public Question<?> getSubQuestion(final String subQuestionCode) {
		for (final Question<?> subQuestion : subQuestions) {
			if (subQuestion.getCode().equals(subQuestionCode)) {
				return subQuestion;
			}
		}
		return null;
	}

	public void addSubQuestions(final Question<?> question) {
		subQuestions.add(question);
		question.parent = this;
	}

	public Question<?> getParent() {
		return parent;
	}

	public Class<?> getResponseType() {
		if (responseType != null) {
			return responseType;
		}
		// default type
		switch (formType) {
			case COLLAPSIBLE:
				return Boolean.class;
			case CHECK:
				return Boolean.class;
			case DATE:
				return Date.class;
			default:
				return String.class;
		}
	}

	public void setResponseType(final Class<T> responseType) {
		this.responseType = responseType;
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("code", code).append("subQuestion", subQuestions)
				.toString();
	}
}
