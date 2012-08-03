package com.boz.poc.facade;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.boz.poc.domain.Partner;
import com.boz.poc.dto.Partners;

/**
 * Business facade for {@link Partner} entity.
 *
 * @author jboz
 */
@Stateless
public class PartnerFacade {

	@Inject
	private EntityManager em;

	public Partner createPartner(final String name, final Date birthDate) {
		final Partner partner = new Partner();
		partner.setName(name);
		partner.setBirthDate(birthDate);

		em.persist(partner);

		return partner;
	}

	public Partners getAllPartners() {
		return new Partners(em.createQuery("from Partner order by birthDate, name", Partner.class).getResultList());
	}
}
