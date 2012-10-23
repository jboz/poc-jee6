package com.boz.poc.presentation.navigation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.boz.poc.domain.Step;

@ManagedBean
@SessionScoped
public class NavigationBean {

	private List<Step> steps;

	@PostConstruct
	public void init() {
		steps = new ArrayList<Step>();

		addSteps("ensured");
		addSteps("tariff");
		addSteps("roles");
		addSteps("clauses");
		addSteps("admin");
		addSteps("survey", "gen", "health", "others");
		addSteps("transfer");
	}

	private void addSteps(final String parentCode, final String... childsCode) {
		final Step parent = Step.createStep(parentCode, null);
		steps.add(parent);
		for (final String childCode : childsCode) {
			Step.createStep(childCode, parent);
		}
	}

	public List<Step> getSteps() {
		return steps;
	}
}
