package ch.mobi.tips.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;

import ch.mobi.tips.dto.MemberHistoricDTO;
import ch.mobi.tips.envers.HistoricManager;
import ch.mobi.tips.model.Activity;
import ch.mobi.tips.model.Member;
import ch.mobi.tips.model.MemberStatus;

import com.google.common.collect.Lists;

@Stateless
public class MemberFacade {

	@Inject	private Logger log;
	@Inject	private EntityManager em;
	@Inject	private Validator validator;

	public List<Activity> getActivities() {
		return em.createNamedQuery("Activity.all", Activity.class).getResultList();
	}

	public List<Activity> getActivities(final Long... activityIds) {
		return em.createNamedQuery("Activity.byIds", Activity.class).setParameter("ids", Lists.newArrayList(activityIds))
				.getResultList();
	}

	public List<MemberHistoricDTO> listAllMembersHistorics() {
		final AuditReader reader = AuditReaderFactory.get(em);

		final List<MemberHistoricDTO> historics = new ArrayList<MemberHistoricDTO>();
		for (final Object obj : reader.createQuery().forRevisionsOfEntity(Member.class, false, true).getResultList()) {
			final Object[] result = (Object[]) obj;
			// hack to initialize activities entities
			((Member) result[0]).getActivities().size();
			historics.add(new MemberHistoricDTO((DefaultRevisionEntity) result[1], (Member) result[0], (RevisionType) result[2]));
		}
		return historics;
	}

	public List<Member> listAllMembers() {
		return em.createNamedQuery("Member.all", Member.class).getResultList();
	}

	public Member lookupMemberById(final long id) {
		return em.createNamedQuery("Member.byId", Member.class).setParameter("id", id).getSingleResult();
	}

	public void toggleHistorization() {
		HistoricManager.getInstance().toggleHistorization();
	}

	public boolean isHistorization() {
		return HistoricManager.getInstance().isHistorization();
	}

	public void quitMember(final Long id) throws Exception {
		try {
			toggleHistorization();

			final Member member = em.find(Member.class, id);
			member.setStatus(MemberStatus.INACTIVE);
			em.persist(member);
		} finally {
			toggleHistorization();
		}
	}

	public boolean delete(final long id) {
		final Member member = lookupMemberById(id);
		if (member == null) {
			throw new IllegalArgumentException("Member with id " + id + " not found !!");
		}
		em.remove(member);

		return true;
	}

	public Member createOrUpdateMember(final Long id, final String name, final String email, final String phone,
			final Long... activities) {
		// Create a new member class from fields
		final Member member = id != null ? lookupMemberById(id) : new Member();
		if (member == null) {
			throw new IllegalArgumentException("Member with id " + id + " not found !!");
		}
		member.setName(name);
		member.setEmail(email);
		member.setPhoneNumber(phone);
		member.setActivities(getActivities(activities));

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
