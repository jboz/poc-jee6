package ch.mobi.tips.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import ch.mobi.tips.model.Member;

@Stateless
public class MemberFacade {

	@Inject	private Logger log;
	@Inject	private EntityManager em;
	@Inject	private Validator validator;

	public List<Member> listAllMembers() {
		// Use @SupressWarnings to force IDE to ignore warnings about "genericizing" the results of
		// this query
		@SuppressWarnings("unchecked")
		// We recommend centralizing inline queries such as this one into @NamedQuery annotations on
		// the @Entity class
		// as described in the named query blueprint:
		// https://blueprints.dev.java.net/bpcatalog/ee5/persistence/namedquery.html
		final List<Member> results = em.createQuery("select m from Member m order by m.name").getResultList();
		return results;
	}

	public Member lookupMemberById(long id) {
		return em.find(Member.class, id);
	}

	public Member createMember(String name, String email, String phone) {
		// Create a new member class from fields
		Member member = new Member();
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
	 * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is
	 * standard bean validation errors then it will throw a ConstraintValidationException with the set of the constraints
	 * violated.
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
	private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Member>> violations = validator.validate(member);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		// Check the uniqueness of the email address
		if (emailAlreadyExists(member.getEmail())) {
			throw new ValidationException("Unique Email Violation");
		}
	}

	/**
	 * Checks if a member with the same email address is already registered. This is the only way to
	 * easily capture the "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
	 * 
	 * @param email The email to check
	 * @return True if the email already exists, and false otherwise
	 */
	public boolean emailAlreadyExists(String email) {
		Query checkEmailExists = em.createQuery(" SELECT COUNT(b.email) FROM Member b WHERE b.email=:emailparam");
		checkEmailExists.setParameter("emailparam", email);
		long matchCounter = 0;
		matchCounter = (Long) checkEmailExists.getSingleResult();
		if (matchCounter > 0) {
			return true;
		}
		return false;
	}
}
