package com.boz.poc.domain;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: Ensured
 */
@Entity
public class Ensured implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@ManyToOne(optional = false)
	private Partner partner;

	@Enumerated(STRING)
	@Column(nullable = false)
	private AcceptationState acceptation = AcceptationState.IN_PROGRESS;

	public Long getId() {
		return id;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(final Partner partner) {
		this.partner = partner;
	}

	public AcceptationState getAcceptation() {
		return acceptation;
	}

	public void setAcceptation(final AcceptationState acceptation) {
		this.acceptation = acceptation;
	}

}
