package com.boz.poc.presentation.tests.scopes;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "pageController")
@SessionScoped
public class PageController implements Serializable {
	private static final long serialVersionUID = 1L;

	public String navigatePage1() {
		System.out.println("Redirect to Page 1");

		return "/tests/scopes";
	}

	public String navigatePage2() {
		System.out.println("Redirect to Page 2");

		return "/tests/scopes2";
	}

	public String action() {
		System.out.println("Action Fired");

		return null;
	}

	public void actionListener(final ActionEvent event) {
		System.out.println("ActionListener Fired");
	}
}