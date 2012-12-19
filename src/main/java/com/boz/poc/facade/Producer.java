package com.boz.poc.facade;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Resources factory for services.
 *
 * @author jboz
 */
public class Producer {

	@SuppressWarnings("unused")
	@Produces
	@PersistenceContext
	private EntityManager em;
}