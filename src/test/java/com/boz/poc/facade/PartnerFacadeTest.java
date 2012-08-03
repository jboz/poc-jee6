package com.boz.poc.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.mobi.posi.common.tools.ReflectionUtils;

import com.boz.commons.test.dbunit.DBUnitRule;
import com.boz.commons.test.dbunit.DataSet;
import com.boz.commons.test.dbunit.InjectEntiyManager;
import com.boz.commons.test.mockito.MockingRule;
import com.boz.poc.domain.Partner;

/**
 * Test de la facade {@link PartnerFacade}.
 *
 * @author jboz
 */
public class PartnerFacadeTest {

	@Rule
	public final MockingRule mockingRule = MockingRule.init();

	@Rule
	public final DBUnitRule dbUnitRule = DBUnitRule.init();

	@InjectEntiyManager
	private PartnerFacade facade = new PartnerFacade();

	@Before
	public void setUp() {
	}

	@Test
	public void testCreatePartner() {
		final EntityManager em = mock(EntityManager.class);
		ReflectionUtils.setFieldValue(facade, EntityManager.class, em);
		final Partner partner = facade.createPartner("Bob", DateTime.parse("1952-01-05T11:55").toDate());
		assertThat(partner).isNotNull();
		assertThat(partner.getName()).isEqualTo("Bob");
		assertThat(partner.getBirthDate()).isEqualTo(DateTime.parse("1952-01-05T11:55").toDate());

		verify(em).persist(partner);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetAllPartners() {
		final EntityManager em = mock(EntityManager.class);
		ReflectionUtils.setFieldValue(facade, EntityManager.class, em);
		final TypedQuery<Partner> typedQuery = mock(TypedQuery.class);
		when(em.createQuery("from Partner order by birthDate, name", Partner.class)).thenReturn(typedQuery);

		when(typedQuery.getResultList()).thenReturn(null);
		assertThat(facade.getAllPartners()).isNull();

		final Partner partnerA = createPartner("a", null);
		when(typedQuery.getResultList()).thenReturn(Arrays.asList(partnerA));
		assertThat(facade.getAllPartners()).hasSize(1).containsOnly(partnerA);
	}

	private static Partner createPartner(final String name, final Date birthDate) {
		final Partner partner = new Partner();
		partner.setName(name);
		partner.setBirthDate(birthDate);

		return partner;
	}

	private static Partner createPartner(final long id) {
		final Partner partner = new Partner();
		ReflectionUtils.setFieldValue(partner, "id", id);

		return partner;
	}

	@Test
	@DataSet("datas/partners")
	public void testGetAllPartners_ordered() {
		assertThat(facade.getAllPartners()).hasSize(4).containsExactly(createPartner(4000), createPartner(2000), createPartner(3000),
				createPartner(1000));
	}
}
