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
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import ch.mobi.tips.dto.MemberHistoricDTO;
import ch.mobi.tips.facade.MemberFacade;
import ch.mobi.tips.model.Activity;
import ch.mobi.tips.model.Member;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/members")
@RequestScoped
public class MemberService {

	// @formatter:off
	@EJB	private MemberFacade facade;
	@Inject	private Event<Member> memberEventSrc;
	@Inject	private Logger log;
	// @formatter:on

	@GET
	@Path("/activities")
	public List<Activity> getActivities() {
		return facade.getActivities();
	}

	@POST
	@Path("/historization")
	public void toggleHistorization() {
		facade.toggleHistorization();
	}

	@GET
	@Path("/historization")
	public boolean isHistorization() {
		return facade.isHistorization();
	}

	@GET
	@Path("/historics")
	@Produces(MediaType.APPLICATION_XML)
	public List<MemberHistoricDTO> listAllMembersHistorics() {
		return facade.listAllMembersHistorics();
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Member> listAllMembers() {
		return facade.listAllMembers();
	}

	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Member> listAllMembersJSON() {
		return listAllMembers();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_XML)
	public Member lookupMemberById(@PathParam("id") final long id) {
		return facade.lookupMemberById(id);
	}

	@GET
	@Path("/{id:[0-9][0-9]*}/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Member lookupMemberByIdJSON(@PathParam("id") final long id) {
		return lookupMemberById(id);
	}

	@POST
	@Path("/quit/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response quitMember(@PathParam("id") final Long id) throws Exception {
		facade.quitMember(id);
		return Response.ok().build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMember(@FormParam("id") final String id, @FormParam("name") final String name,
			@FormParam("email") final String email, @FormParam("phoneNumber") final String phone,
			@FormParam("activities") final Long... activityIds) {
		Response.ResponseBuilder builder = null;

		try {

			final Member member = facade.createOrUpdateMember(StringUtils.isNotBlank(id) ? Long.valueOf(id) : null, name, email,
					phone, activityIds);

			// Trigger the creation event
			memberEventSrc.fire(member);

			// Create an "ok" response
			builder = Response.ok();
		} catch (final ConstraintViolationException ce) {
			// Handle bean validation issues
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (final ValidationException e) {
			// Handle the unique constrain violation
			final Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email", "Email taken");
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		}

		return builder.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public boolean delete(@PathParam("id") final long id) {
		return facade.delete(id);
	}

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
	 * by clients to show violations.
	 *
	 * @param violations A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	private Response.ResponseBuilder createViolationResponse(final Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());

		// Response.ResponseBuilder builder = null;
		final Map<String, String> responseObj = new HashMap<String, String>();

		for (final ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}

}
