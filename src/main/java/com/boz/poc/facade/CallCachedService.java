package com.boz.poc.facade;

import javax.annotation.Resource;
import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.cdi.Remote;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Service which cached calls.
 *
 * @author Julien Boz
 */
public class CallCachedService {

	@Inject
	private Cache<String, String> cache;

	@Inject
	@Remote("greeting-cache")
	private RemoteCache<String, String> remoteCache;

	/**
	 * Hibernate cache defined by JBoss.
	 */
	@ApplicationScoped
	@Resource(lookup = "java:jboss/infinispan/contrainer/hibernate")
	private EmbeddedCacheManager defaultCacheManager;

	public String manualGreet(final String user) {
		String cachedValue = cache.get(user);
		if (cachedValue == null) {
			cachedValue = autoGreet(user);
			cache.put(user, cachedValue);
		}
		return cachedValue;
	}

	@CacheResult(cacheName = "greeting-cache")
	public String autoGreet(final String user) {
		return "Hello" + user;
	}
}