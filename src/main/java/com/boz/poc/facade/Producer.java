package com.boz.poc.facade;

import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Resources factory for services.
 *
 * @author jboz
 */
public class Producer {

	@Produces
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public static <T> T lookup(final String serviceName) {
		try {
			return (T) new InitialContext().lookup("java:module/" + serviceName);
		} catch (final Exception e) {
			return null;
		}
	}
}