package com.boz.poc.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.boz.poc.domain.ContractCase;
import com.boz.poc.domain.Ensured;

@Stateless
public class AffairFacade {

	@Inject
	private EntityManager em;

	public List<ContractCase> getContractCases() {
		return em.createNamedQuery("ContractCase.load", ContractCase.class).getResultList();
	}

	public void save(final Ensured ensured) {
		em.merge(ensured);
	}
}
