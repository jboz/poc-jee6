package com.boz.poc.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

/**
 * Entity implementation class for Entity: ContractCase
 */
@Entity
@NamedQuery(name = "ContractCase.load", query = "select c from ContractCase c")
public class ContractCase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@Temporal(DATE)
	private Date effectDate;

	@ManyToOne(optional = false)
	private Ensured ensuredA;

	@ManyToOne(optional = true)
	private Ensured ensuredB;

	@Enumerated(STRING)
	@Column(nullable = false)
	private ContractState state = ContractState.OFFER;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(final Date effectDate) {
		this.effectDate = effectDate;
	}

	public Ensured getEnsuredA() {
		return ensuredA;
	}

	public void setEnsuredA(final Ensured ensuredA) {
		this.ensuredA = ensuredA;
	}

	public Ensured getEnsuredB() {
		return ensuredB;
	}

	public void setEnsuredB(final Ensured ensuredB) {
		this.ensuredB = ensuredB;
	}

	public ContractState getState() {
		return state;
	}

	public void setState(final ContractState state) {
		this.state = state;
	}

}
