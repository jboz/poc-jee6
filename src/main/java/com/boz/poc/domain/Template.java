package com.boz.poc.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Cacheable
@NamedQuery(name = "Template.findByFileName", query = "select t from Template t where t.fileName = :fileName")
public class Template implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String fileName;

	@NotNull
	@Temporal(TIMESTAMP)
	private Date lastModified;

	@Lob
	@NotNull
	protected byte[] template;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(final Date lastModified) {
		this.lastModified = lastModified;
	}

	public byte[] getTemplate() {
		return template;
	}

	public void setTemplate(final byte[] template) {
		this.template = template;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final Template other = (Template) obj;

		if (id != null) {
			// égalité sur l'identifiant technique
			return id.equals(other.id);
		} else if (id == null && other.id != null) {
			return false;
		}

		// equals sur les autres champs
		return EqualsBuilder.reflectionEquals(this, obj, new String[] { "template" });
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
