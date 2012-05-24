package com.boz.poc.tools.dbunit;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface JpaConfig {

	/**
	 * The name of the persistence unit, as defined in the persistence config file
	 */
	String persistenceUnit() default "";

	/**
	 * The persistence xml file that has to be loaded for configuring the EntityManagerFactory. If omitted, the default
	 * META-INF/persistence.xml file is loaded.
	 */
	String configFile() default "";

}