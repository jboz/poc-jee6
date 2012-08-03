package com.boz.poc.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.boz.poc.domain.Partner;

/**
 * Business facade for {@link Partner} entity.
 *
 * @author jboz
 */
@Stateless
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
	public List<Partner> getAllPartners() {
		return em.createQuery("from Partner order by birthDate, name", Partner.class).getResultList();
	}
}
