package ch.mobi.tips.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;

import ch.mobi.tips.model.Member;

@XmlRootElement
public class MemberHistoricDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private DefaultRevisionEntity revision;
	private Member member;
	private RevisionType type;

	public MemberHistoricDTO() {
	}

	public MemberHistoricDTO(final DefaultRevisionEntity revision, final Member member, final RevisionType type) {
		this.revision = revision;
		this.member = member;
		this.type = type;
	}

	public DefaultRevisionEntity getRevision() {
		return revision;
	}

	public void setRevision(final DefaultRevisionEntity revision) {
		this.revision = revision;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	public RevisionType getType() {
		return type;
	}

	public void setType(final RevisionType type) {
		this.type = type;
	}

}