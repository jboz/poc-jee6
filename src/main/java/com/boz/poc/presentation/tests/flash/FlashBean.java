package com.boz.poc.presentation.tests.flash;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean
@RequestScoped
public class FlashBean {

	private String firstName;
	private String lastName;

	/**
	 * set submitted values to flash scope and redirect to confirm view
	 *
	 * @return
	 */
	public String goToConfirmView() {
		final Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("firstName", firstName);
		flash.put("lastName", lastName);

		return "/tests/flash-confirm?faces-redirect=true";
	}

	/**
	 * redirect to confirm view
	 *
	 * @return
	 */
	public String goToInputFormView() {
		return "/tests/flash-input?faces-redirect=true";
	}

	/**
	 * Called on confirm.xhtml page here could be some database processing
	 *
	 * @return
	 */
	public String insertValue() {
		final Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		// preserve messages across redirect
		flash.setKeepMessages(true);

		pullValuesFromFlash(null);

		// do something with firstName, lastName
		System.out.println("First name: " + firstName);
		System.out.println("Last name: " + lastName);

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Value inserted"));
		return "/tests/flash-done?faces-redirect=true";
	}

	/**
	 * System event called before view rendering used to pull values from flash and set to bean properties
	 *
	 * @param e
	 */
	public void pullValuesFromFlash(final ComponentSystemEvent e) {
		final Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		firstName = (String) flash.get("firstName");
		lastName = (String) flash.get("lastName");
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
}