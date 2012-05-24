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
import javax.persistence.Temporal;

/**
 * Contract entity.
 *
 * @author jboz
 */
@Entity
public class Contract implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@Temporal(DATE)
	private Date effectDate;

	@ManyToOne(optional = false)
	private Partner ensured;

	@Enumerated(STRING)
	@Column(nullable = false)
	private ContractState state = ContractState.PARTIAL;

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

	public Partner getEnsured() {
		return ensured;
	}

	public void setEnsured(final Partner ensured) {
		this.ensured = ensured;
	}

	public ContractState getState() {
		return state;
	}

	public void setState(final ContractState state) {
		this.state = state;
	}

}
