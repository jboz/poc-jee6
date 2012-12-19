package com.boz.poc.presentation;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import com.boz.poc.domain.AcceptationState;
import com.boz.poc.domain.ContractCase;
import com.boz.poc.domain.Ensured;
import com.boz.poc.facade.AffairFacade;

/**
 * Gestionnaire d'une affaire.
 */
@ManagedBean
@SessionScoped
public class AffairBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private AffairFacade affairFacade;

	@Produces
	@Named
	@RequestScoped
	public List<ContractCase> getCases() {
		return affairFacade.getContractCases();
	}

	private Ensured affair;

	public Ensured getAffair() {
		return affair;
	}

	public void setAffair(final Ensured affair) {
		this.affair = affair;
	}

	public void accept(final ActionEvent event) {
		affair.setAcceptation(AcceptationState.ACCEPTED);
		affairFacade.save(affair);
	}

	public void declin(final ActionEvent event) {
		affair.setAcceptation(AcceptationState.REFUSED);
		affairFacade.save(affair);
	}
}
