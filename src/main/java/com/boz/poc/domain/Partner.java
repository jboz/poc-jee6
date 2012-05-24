package com.boz.poc.domain;

import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Partner entity.
 *
 * @author jboz
 */
@Entity
public class Partner implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Column(nullable = false)
	@Temporal(DATE)
	private Date birthDate;

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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final Partner other = (Partner) obj;

		if (id != null) {
			// égalité sur l'identifiant technique
			return id.equals(other.id);
		} else if (id == null && other.id != null) {
			return false;
		}

		// equals sur les autres champs
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
