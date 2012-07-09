package com.boz.poc.ws;

import java.util.Date;

import javax.inject.Inject;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.boz.poc.domain.Partner;
import com.boz.poc.dto.Partners;
import com.boz.poc.facade.PartnerFacade;

/**
 * Business facade for {@link Partner} entity.
 *
 * @author jboz
 */
@WebService
@SOAPBinding(style = Style.DOCUMENT)
//@HandlerChain(file = "/META-INF/handler-chain.xml")
public class PartnerService implements IPartnerService {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private PartnerFacade facade;

	@Override
	public Partner createPartner(final String name, final Date birthDate) {
		return facade.createPartner(name, birthDate);
	}

	@Override
	public Partners getAllPartners() {
		return facade.getAllPartners();
	}

	@Override
	// curl -d @ws-findPartnerByName.xml http://localhost:8080/poc-jee6/PartnerService
	public Partner findPartnerByName(final String name) {
		return em.createQuery("from Partner p where p.name = :name", Partner.class).setParameter("name", name).getSingleResult();
	}
}
