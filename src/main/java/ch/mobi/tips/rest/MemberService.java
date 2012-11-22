package ch.mobi.tips.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.mobi.tips.facade.MemberFacade;
import ch.mobi.tips.model.Member;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/members")
@RequestScoped
public class MemberService {
	
	@Inject	private Logger log;
	@Inject	private Event<Member> memberEventSrc;
	@EJB	private MemberFacade facade;

	@GET
	@Produces("text/xml")
	public List<Member> listAllMembers() {
		return facade.listAllMembers();
	}

	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Member> listAllMembersJSON() {
		return facade.listAllMembers();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public Member lookupMemberById(@PathParam("id") long id) {
		return facade.lookupMemberById(id);
	}

	@GET
	@Path("/{id:[0-9][0-9]*}/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Member lookupMemberByIdJSON(@PathParam("id") long id) {
		return facade.lookupMemberById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMember(@FormParam("name") String name, @FormParam("email") String email,
			@FormParam("phoneNumber") String phone) {
		Response.ResponseBuilder builder = null;

		try {
			final Member member = facade.createMember(name, email, phone);

			// Trigger the creation event
			memberEventSrc.fire(member);

			// Create an "ok" response
			builder = Response.ok();
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email", "Email taken");
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		}

		return builder.build();
	}

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message.
	 * This can then be used by clients to show violations.
	 * 
	 * @param violations A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());

		// Response.ResponseBuilder builder = null;
		Map<String, String> responseObj = new HashMap<String, String>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}
}
