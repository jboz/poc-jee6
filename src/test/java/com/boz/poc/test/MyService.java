package com.boz.poc.test;

import javax.ejb.Stateless;

/**
 * Classe service pour tests.
 *
 * @author jboz
 */
@Stateless
public class MyService {

	public String greet(final String userName) {
		return "Hello, " + userName;
	}
}