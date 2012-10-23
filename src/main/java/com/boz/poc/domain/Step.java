package com.boz.poc.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Step implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private Step parent;

	private Set<Step> childs;

	public String getCode() {
		return (parent != null ? parent.getCode() + "." : "") + code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Step getParent() {
		return parent;
	}

	public void setParent(final Step parent) {
		this.parent = parent;
	}

	public Set<Step> getChilds() {
		return childs;
	}

	public void add(final Step child) {
		if (childs == null) {
			childs = new HashSet<>();
		}
		childs.add(child);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public static Step createStep(final String code, final Step parent) {
		final Step step = new Step();
		step.code = code;
		step.parent = parent;
		if (parent != null) {
			parent.add(step);
		}

		return step;
	}
}
