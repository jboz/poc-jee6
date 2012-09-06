package com.boz.poc.presentation.tests.scopes;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Timestamp created;

	public SessionBean() {
		created = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(final Timestamp created) {
		this.created = created;
	}
}