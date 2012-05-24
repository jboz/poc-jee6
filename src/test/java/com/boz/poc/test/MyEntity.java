package com.boz.poc.test;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@Temporal(TemporalType.DATE)
	private Date birthDay;

	@Temporal(TemporalType.TIME)
	private Date timeToMarket;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;

	private Double amount;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(final Date birthDay) {
		this.birthDay = birthDay;
	}

	public Date getTimeToMarket() {
		return timeToMarket;
	}

	public void setTimeToMarket(final Date timeToMarket) {
		this.timeToMarket = timeToMarket;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(final Double amount) {
		this.amount = amount;
	}
}
