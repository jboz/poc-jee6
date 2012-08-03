package com.boz.poc.ws;

import static org.fest.assertions.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.protocol.servlet.arq514hack.descriptors.api.web.WebAppDescriptor;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.mobi.posi.common.tools.DateUtils;
import ch.mobi.posi.common.tools.ReflectionUtils;

import com.boz.poc.domain.Partner;
import com.boz.poc.dto.Partners;
import com.boz.poc.facade.PartnerFacade;

/**
 * Test d'intégration.
 *
 * @author jboz
 */
@RunWith(Arquillian.class)
@RunAsClient
public class PartnerServiceIT {

	@Deployment
	public static WebArchive createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(IPartnerService.class, PartnerService.class, PartnerFacade.class, MessageHandler.class)
				.addPackages(true, "com.boz.poc.domain", "com.boz.poc.dto", "org.joda.time")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("test-handler-chain.xml", "META-INF/handler-chain.xml")
				.setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class).exportAsString()));
	}

	@Test
	@UsingDataSet("datas/partners.xml")
	public void testGetAllPartners_ordered() throws MalformedURLException {
		final URL url = new URL("http://localhost:8280/test/PartnerService?wsdl");
		final QName qname = new QName("http://ws.poc.boz.com/", "PartnerServiceService");
		final Service service = Service.create(url, qname);
		final IPartnerService facade = service.getPort(IPartnerService.class);

		final Partners partners = facade.getAllPartners();
		Assert.assertNotNull(partners);
		Assert.assertNotNull(partners.getPartners());
		assertThat(partners.getPartners()).hasSize(4).containsExactly(createPartner(4000), createPartner(2000), createPartner(3000),
				createPartner(1000));
		Assert.assertEquals(createPartner(4000), facade.getAllPartners().getPartners().get(0));
		Assert.assertEquals(createPartner(2000), facade.getAllPartners().getPartners().get(1));
		Assert.assertEquals(createPartner(3000), facade.getAllPartners().getPartners().get(2));
		Assert.assertEquals(createPartner(1000), facade.getAllPartners().getPartners().get(3));
	}

	private static Partner createPartner(final long id) {
		final Partner partner = new Partner();
		ReflectionUtils.setFieldValue(partner, "id", id);

		return partner;
	}

	// @Test
	// TODO curl -d name=Bob -d birthDate=2000-01-01 http://localhost:8080/poc-jee6/rest/partner
	public void testConsumeGetAllPartners() throws MalformedURLException {
		final URL url = new URL("http://localhost:8280/test/PartnerService?wsdl");

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

		final Partner joe = facade.findPartnerByName("Joe");

		assertThat(joe).isNotNull();
		assertThat(joe.getName()).isEqualTo("Joe");
		assertThat(joe.getBirthDate()).isEqualTo(DateUtils.parse("01.01.2010"));
	}
}
