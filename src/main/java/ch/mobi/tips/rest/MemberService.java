package ch.mobi.tips.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import ch.mobi.tips.model.Member;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/members")
@RequestScoped
@Stateful
public class MemberService {

	// @formatter:off
	@Inject	private Event<Member> memberEventSrc;
	@Inject	private Logger log;
	@Inject	private EntityManager em;
	@Inject	private Validator validator;
	// @formatter:on

	@GET
	@Produces("text/xml")
	public List<Member> listAllMembers() {
		return em.createNamedQuery("Member.all", Member.class).getResultList();
	}

	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Member> listAllMembersJSON() {
		return listAllMembers();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("text/xml")
	public Member lookupMemberById(@PathParam("id") final long id) {
		return em.find(Member.class, id);
	}

	@GET
	@Path("/{id:[0-9][0-9]*}/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Member lookupMemberByIdJSON(@PathParam("id") final long id) {
		return lookupMemberById(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMember(@FormParam("id") final String id, @FormParam("name") final String name,
			@FormParam("email") final String email, @FormParam("phoneNumber") final String phone) {
		Response.ResponseBuilder builder = null;

		try {
			final Member member = createOrUpdateMember(StringUtils.isNotBlank(id) ? Long.valueOf(id) : null, name, email, phone);

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

	public Member createOrUpdateMember(final Long id, final String name, final String email, final String phone) {
		// Create a new member class from fields
		final Member member = id != null ? lookupMemberById(id) : new Member();
		if (member == null) {
			throw new IllegalArgumentException("Member with id " + id + " not found !!");
		}
		member.setName(name);
		member.setEmail(email);
		member.setPhoneNumber(phone);

		// Validates member using bean validation
		validateMember(member);

		// Register the member
		log.info("Registering " + member.getName());
		em.persist(member);

		return member;
	}

	/**
	 * <p>
	 * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
	 * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
	 * </p>
	 * <p>
	 * If the error is caused because an existing member with the same email is registered it throws a regular validation
	 * exception so that it can be interpreted separately.
	 * </p>
	 * 
	 * @param member Member to be validated
	 * @throws ConstraintViolationException If Bean Validation errors exist
	 * @throws ValidationException If member with the same email already exists
	 */
	private void validateMember(final Member member) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		final Set<ConstraintViolation<Member>> violations = validator.validate(member);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		// Check the uniqueness of the email address
		if (emailAlreadyExists(member.getEmail(), member.getId())) {
			throw new ValidationException("Unique Email Violation");
		}
	}

	/**
	 * Checks if a member with the same email address is already registered. This is the only way to easily capture the
	 * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
	 * 
	 * @param email The email to check
	 * @return True if the email already exists, and false otherwise
	 */
	public boolean emailAlreadyExists(final String email, final Long id) {
		final long matchCounter = em.createNamedQuery("Member.countEmail", Long.class).setParameter("email", email)
				.setParameter("id", id).getSingleResult();
		if (matchCounter > 0) {
			return true;
		}
		return false;
	}
}
