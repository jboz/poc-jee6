package com.boz.poc.ws;

import java.util.Date;

import javax.jws.WebService;

import com.boz.poc.domain.Partner;
import com.boz.poc.dto.Partners;

@WebService
public interface IPartnerService {

	/**
	 * Create a {@link Partner}.
	 *
	 * @param name
	 * @param birthDate
	 * @return
	 */
	public abstract Partner createPartner(final String name, final Date birthDate);

	/**
	 * @return all partners
	 */
	public abstract Partners getAllPartners();

	/**
	 * find by name
	 */
	public abstract Partner findPartnerByName(final String name);
}