package com.boz.poc.presentation;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.infinispan.manager.EmbeddedCacheManager;

import com.boz.poc.facade.CallCachedService;

/**
 * RESTful web service.
 *
 * @author Julien Boz
 */
@Path("/cache")
@Produces(MediaType.APPLICATION_JSON)
public class CallCachedController {

	@EJB
	private CallCachedService service;

	@Resource(lookup = "java:jboss/infinispan/contrainer/hibernate")
	private EmbeddedCacheManager defaultCacheManager;

	@GET
	@Path("/manual/{userName}")
	public String manualHello(@PathParam("userName") final String userName) {
		return service.manualGreet(userName);
	}

	@GET
	@Path("/auto/{userName}")
	public String autoHello(@PathParam("userName") final String userName) {
		return service.autoGreet(userName);
	}
}
