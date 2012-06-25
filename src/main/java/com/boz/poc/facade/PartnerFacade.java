package com.boz.poc.facade;

import java.util.Date;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.boz.poc.domain.Partner;
import com.boz.poc.dto.Partners;

/**
 * Business facade for {@link Partner} entity.
 *
 * @author jboz
 */
@Stateless
@WebService
@SOAPBinding(style = Style.RPC)
public class PartnerFacade {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Create a {@link Partner}.
	 *
	 * @param name
	 * @param birthDate
	 * @return
	 */
	public Partner createPartner(final String name, final Date birthDate) {
		final Partner partner = new Partner();
		partner.setName(name);
		partner.setBirthDate(birthDate);

		em.persist(partner);

		return partner;
	}

	/**
	 * @return all partners
	 */
	public Partners getAllPartners() {
		return new Partners(em.createQuery("from Partner order by birthDate, name", Partner.class).getResultList());
	}
}
