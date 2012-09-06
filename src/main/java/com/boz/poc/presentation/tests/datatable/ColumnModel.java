package com.boz.poc.presentation.tests.datatable;

public class ColumnModel {
	String value; // represents sortBy / filterBy as one field
	String headerText;

	public ColumnModel(final String value, final String headerText) {
		this.value = value;
		this.headerText = headerText;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(final String headerText) {
		this.headerText = headerText;
	}
}