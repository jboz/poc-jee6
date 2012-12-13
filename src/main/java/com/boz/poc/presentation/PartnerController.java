package com.boz.poc.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.joda.time.DateTime;

import com.boz.poc.domain.Partner;
import com.boz.poc.dto.HistoricDTO;
import com.boz.poc.facade.PartnerFacade;

/**
 * RESTful web service.
 *
 * @author jboz
 */
@Path("/partner")
@Produces(MediaType.APPLICATION_JSON)
public class PartnerController {

	@EJB
	private PartnerFacade partnerFacade;

	@Inject
	private EntityManager em;

	@GET
	public List<Partner> getAll() {
		return partnerFacade.getAllPartners();
	}

	@POST
	// curl -d name=Bob -d birthDate=2000-01-01 http://localhost:8080/poc-jee6/partner
	public Partner create(@FormParam("name") final String name, @FormParam("birthDate") final String birthDate) {
		return partnerFacade.createPartner(name, DateTime.parse(birthDate).toDate());
	}

	@GET
	@Path("/historic")
	@Produces(MediaType.APPLICATION_XML)
	public List<HistoricDTO> listAllPartnerHistorics() {
		return getHistorics(Partner.class);
	}

	public List<HistoricDTO> getHistorics(final Class<? extends Serializable> entityClass) {
		final AuditReader reader = AuditReaderFactory.get(em);

		final List<HistoricDTO> historics = new ArrayList<HistoricDTO>();
		for (final Object obj : reader.createQuery().forRevisionsOfEntity(entityClass, false, true).getResultList()) {
			final Object[] result = (Object[]) obj;
			historics.add(new HistoricDTO((Serializable) result[0], (DefaultRevisionEntity) result[1], (RevisionType) result[2]));
		}
		return historics;
	}
}
