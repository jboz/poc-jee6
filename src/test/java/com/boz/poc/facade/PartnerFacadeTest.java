package com.boz.poc.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import ch.mobi.posi.common.tools.DateUtils;
import ch.mobi.posi.common.tools.ReflectionUtils;
import ch.mobi.posi.common.tools.dbunit.DBUnitRule;
import ch.mobi.posi.common.tools.dbunit.DataSet;
import ch.mobi.posi.common.tools.mockito.MockingRule;

import com.boz.poc.domain.Partner;

/**
 * Test de la facade {@link PartnerFacade}.
 *
 * @author jboz
 */
@PrepareForTest(DateUtils.class)
// @RunWith(PowerMockRunner.class)
// @PowerMockIgnore({"ch.qos.logback.*", "org.*", "javax.*"})
public class PartnerFacadeTest {

	@Rule
	public final MockingRule mockingRule = MockingRule.init();

	@Rule
	public final DBUnitRule dbUnitRule = DBUnitRule.init();

	@Rule
	public PowerMockRule powerMockRule = new PowerMockRule();

	@Inject
	private PartnerFacade facade = new PartnerFacade();

	@Before
	public void setUp() {
	}

	@Test
	public void testCreatePartner() {
		final EntityManager em = mock(EntityManager.class);
		facade.em = em;
		final Partner partner = facade.createPartner("Bob", DateTime.parse("1952-01-05T11:55").toDate());
		assertThat(partner).isNotNull();
		assertThat(partner.getName()).isEqualTo("Bob");
		assertThat(partner.getBirthDate()).isEqualTo(DateTime.parse("1952-01-05T11:55").toDate());

		verify(em).persist(partner);
	}

	@Test
	public void testCreatePartner_noBirth() {
		mockStatic(DateUtils.class);

		PowerMockito.when(DateUtils.getToday()).thenReturn(LocalDate.parse("1952-01-05").toDate());

		final EntityManager em = mock(EntityManager.class);
		facade.em = em;
		final Partner partner = facade.createPartner("Bob", null); // no birth date
		assertThat(partner).isNotNull();
		assertThat(partner.getName()).isEqualTo("Bob");
		assertThat(partner.getBirthDate()).isEqualTo(DateTime.parse("1952-01-05T00:00").toDate());

		verify(em).persist(partner);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetAllPartners() {
		final EntityManager em = mock(EntityManager.class);
		facade.em = em;
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
