package ch.mobi.tips.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@XmlRootElement
@Table(name = "MEMBER", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NamedQueries({
		@NamedQuery(name = "Member.all", query = "select distinct m from Member m left join fetch m.activities order by m.name"),
		@NamedQuery(name = "Member.countEmail", query = "select count(m.email) from Member m where m.email=:email and (:id is null or not m.id = :id)"),
		@NamedQuery(name = "Member.byId", query = "select m from Member m left join fetch m.activities where m.id = :id") })
// active audit feature
@Audited
public class Member implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Size(min = 1, max = 25, message = "1-25 letters and spaces")
	@Pattern(regexp = "[A-Za-z ]*", message = "Only letters and spaces")
	private String name;

	@NotNull
	@NotEmpty
	@Email(message = "Invalid format")
	private String email;

	@NotNull
	@Size(min = 10, max = 12, message = "10-12 Numbers")
	@Digits(fraction = 0, integer = 12, message = "Not valid")
	@Column(name = "phone_number")
	private String phoneNumber;

	@NotNull
	@Enumerated(STRING)
	private MemberStatus status = MemberStatus.ACTIVE;

	@ManyToMany(cascade = ALL, fetch = LAZY)
	@JoinTable
	// audit many to many
	// @AuditJoinTable
	// audit association but not target
	//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@XmlElementWrapper(name = "activities")
	@XmlElement(name = "activity")
	private Set<Activity> activities = new HashSet<Activity>();

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(final Collection<Activity> activities) {
		this.activities = activities == null ? new HashSet<Activity>() : new HashSet<Activity>(activities);
	}

	public MemberStatus getStatus() {
		return status;
	}

	public void setStatus(final MemberStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final Member other = (Member) obj;

		if (id != null) {
			// égalité sur l'identifiant technique
			return id.equals(other.id);
		} else if (id == null && other.id != null) {
			return false;
		}

		// equals sur les autres champs
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}
}