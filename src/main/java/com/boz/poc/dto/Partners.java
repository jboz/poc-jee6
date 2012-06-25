package com.boz.poc.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.boz.poc.domain.Partner;

@XmlRootElement
public class Partners {

	private List<Partner> partners;

	public Partners() {
	}

	public Partners(final List<Partner> partners) {
		this.partners = partners;
	}

	public void setPartners(final List<Partner> partners) {
		this.partners = partners;
	}

	public List<Partner> getPartners() {
		return partners;
	}
}
