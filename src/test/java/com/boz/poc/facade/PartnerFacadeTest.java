package com.boz.poc.facade;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.mobi.posi.common.tools.DateUtils;
import ch.mobi.posi.common.tools.ReflectionUtils;
import ch.mobi.posi.common.tools.dbunit.DBUnitRule;
import ch.mobi.posi.common.tools.dbunit.DataSet;
import ch.mobi.posi.common.tools.mockito.MockingRule;

import com.boz.poc.domain.Partner;
import com.boz.poc.ws.IPartnerService;

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

	@Inject
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
		assertThat(facade.getAllPartners().getPartners()).hasSize(1).containsOnly(partnerA);
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
		assertThat(facade.getAllPartners().getPartners()).hasSize(4).containsExactly(createPartner(4000), createPartner(2000),
				createPartner(3000), createPartner(1000));
	}

	@Test
	// TODO curl -d name=Bob -d birthDate=2000-01-01 http://localhost:8080/poc-jee6/rest/partner
	public void testConsumeGetAllPartners() throws MalformedURLException {
		final URL url = new URL("http://localhost:8080/poc-jee6/PartnerService?wsdl");

		// 1st argument service URI, refer to wsdl document above
		// 2nd argument is service name, refer to wsdl document above
		final QName qname = new QName("http://ws.poc.boz.com/", "PartnerServiceService");

		final Service service = Service.create(url, qname);

		final IPartnerService facade = service.getPort(IPartnerService.class);

		final Partner expected = new Partner();
		expected.setBirthDate(DateUtils.parse("01.01.2010"));
		expected.setName("Joe");
		final Partner partnerCreated = facade.createPartner("Joe", DateUtils.parse("01.01.2010"));

		assertThat(partnerCreated).isNotNull();
		assertThat(partnerCreated.getId()).isNotNull();
		partnerCreated.setId(null);
		assertThat(partnerCreated).isEqualTo(expected);

		assertThat(facade.getAllPartners()).isNotNull();
		assertThat(facade.getAllPartners().getPartners().size()).isGreaterThanOrEqualTo(1);
		System.out.println(facade.getAllPartners());
	}
}
