package com.boz.poc.domain.survey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private boolean selected = false;
	private Map<String, String> responses = new HashMap<>();

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Map<String, String> getResponses() {
		return responses;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

}
