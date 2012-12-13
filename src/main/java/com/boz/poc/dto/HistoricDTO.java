package com.boz.poc.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;

import com.boz.poc.domain.Partner;

@XmlRootElement
@XmlSeeAlso(Partner.class)
public class HistoricDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Object entity;
	private DefaultRevisionEntity revisionEntity;
	private RevisionType revisionType;

	public HistoricDTO() {
	}

	public HistoricDTO(final Object entity, final DefaultRevisionEntity revisionEntity, final RevisionType revisionType) {
		this.entity = entity;
		this.revisionEntity = revisionEntity;
		this.revisionType = revisionType;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(final Object entity) {
		this.entity = entity;
	}

	public DefaultRevisionEntity getRevisionEntity() {
		return revisionEntity;
	}

	public void setRevisionEntity(final DefaultRevisionEntity revisionEntity) {
		this.revisionEntity = revisionEntity;
	}

	public RevisionType getRevisionType() {
		return revisionType;
	}

	public void setRevisionType(final RevisionType revisionType) {
		this.revisionType = revisionType;
	}

}
