package com.boz.poc.presentation.tests.scopes;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "windowBean")
@CustomScoped(value = "#{window}")
public class WindowBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Timestamp created;

	public WindowBean() {
		created = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(final Timestamp created) {
		this.created = created;
	}
}