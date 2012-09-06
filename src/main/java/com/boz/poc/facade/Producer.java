package com.boz.poc.facade;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.infinispan.cdi.ConfigureCache;
import org.infinispan.configuration.cache.Configuration;

import com.boz.poc.cache.GreetingCache;

/**
 * Resources factory for services.
 *
 * @author jboz
 */
public class Producer {

	@Produces
	@PersistenceContext
	private EntityManager em;

	// // This is the cache name.
	// @ConfigureCache("greeting-cache")
	// // This is the cache qualifier.
	// @GreetingCache
	// @Produces
	// public Configuration greetingCacheConfiguration() {
	// return new ConfigurationBuilder().eviction().strategy(EvictionStrategy.LRU).maxEntries(1000).build();
	// }

	// The same example without providing a custom configuration.
	// In this case the default cache configuration will be used.
	@ConfigureCache("greeting-cache")
	@GreetingCache
	@Produces
	public Configuration greetingCacheConfiguration;

	// @GreetingCache
	// @Produces
	// @ApplicationScoped
	// public EmbeddedCacheManager specificEmbeddedCacheManager() {
	// return new DefaultCacheManager(new ConfigurationBuilder().expiration().lifespan(60000l).build());
	// }
}