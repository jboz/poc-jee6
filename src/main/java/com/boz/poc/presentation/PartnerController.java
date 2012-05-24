package com.boz.poc.presentation;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;

import com.boz.poc.domain.Partner;
import com.boz.poc.facade.PartnerFacade;

/**
 * RESTful web service.
 *
 * @author jboz
 */
@Path("/partner")
@Produces(MediaType.APPLICATION_JSON)
public class PartnerController {

	@Inject
	private PartnerFacade partnerFacade;

	@GET
	public List<Partner> getAll() {
		return partnerFacade.getAllPartners();
	}

	@POST
	// curl -d name=Bob -d birthDate=2000-01-01 http://localhost:8080/poc-jee6/partner
	public Partner create(@FormParam("name") final String name, @FormParam("birthDate") final String birthDate) {
		return partnerFacade.createPartner(name, DateTime.parse(birthDate).toDate());
	}
}
