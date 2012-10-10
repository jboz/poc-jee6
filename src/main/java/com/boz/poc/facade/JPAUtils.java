package com.boz.poc.facade;

import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;

public final class JPAUtils {

	private JPAUtils() {
	}

	/**
	 * Call {@link TypedQuery#getSingleResult()} and return the first result.<br>
	 *
	 * @return null if no result founded
	 * @throws NonUniqueResultException if no unique result is returned
	 */
	public static <X> X getSingleResult(final TypedQuery<X> query) throws NonUniqueResultException {
		final List<X> results = query.getResultList();
		if (CollectionUtils.isEmpty(results)) {
			return null;
		} else if (results.size() == 1) {
			return results.get(0);
		}
		throw new NonUniqueResultException();
	}
}
