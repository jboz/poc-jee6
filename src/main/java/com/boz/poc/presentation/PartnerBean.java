package com.boz.poc.presentation;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.RowEditEvent;

import com.boz.poc.domain.Partner;
import com.boz.poc.facade.PartnerFacade;

@ManagedBean
@RequestScoped
public class PartnerBean {

	@EJB
	private PartnerFacade facade;

	private Partner partner = new Partner();

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(final Partner partner) {
		this.partner = partner;
	}

	public List<Partner> getPartners() {
		return facade.getAllPartners();
	}

	public void savePartner(final ActionEvent event) {
		facade.saveOrUpdate(partner);
		partner = new Partner();
	}

	public void onEdit(final RowEditEvent event) {
		final FacesMessage msg = new FacesMessage("Partner edited", ((Partner) event.getObject()).getName());
		FacesContext.getCurrentInstance().addMessage(null, msg);

		facade.saveOrUpdate((Partner) event.getObject());
	}

	public void onCancel(final RowEditEvent event) {
		final FacesMessage msg = new FacesMessage("Partner edition cancelled", ((Partner) event.getObject()).getName());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
