package com.boz.poc.presentation.tests.datatable;

public class Task {
	int id;
	String topic;
	String action;
	String notes;

	public Task(final int id, final String topic, final String action, final String notes) {
		this.id = id;
		this.topic = topic;
		this.action = action;
		this.notes = notes;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public String getAction() {
		return action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(final String notes) {
		this.notes = notes;
	}
}