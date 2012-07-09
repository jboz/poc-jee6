package com.boz.poc.ws;

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

import ch.mobi.posi.common.tools.ReflectionUtils;

import com.boz.poc.domain.Partner;
import com.boz.poc.facade.PartnerFacade;

/**
 * Test d'intégration.
 *
 * @author jboz
 */
@RunWith(Arquillian.class)
@UsingDataSet("datas/partners.xml")
@RunAsClient
public class PartnerServiceIT {

	@Deployment
	public static WebArchive createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(IPartnerService.class, PartnerService.class, PartnerFacade.class)
				.addPackages(true, "com.boz.poc.domain", "com.boz.poc.dto", "org.joda.time")
				.addPackages(false, "ch.mobi.posi.common.tools").addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("test-handler-chain.xml", "META-INF/handler-chain.xml")
				.setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class).exportAsString()));
	}

	@Test
	public void testGetAllPartners_ordered() throws MalformedURLException {
		final URL url = new URL("http://localhost:8080/poc-jee6/PartnerService?wsdl");
		final QName qname = new QName("http://ws.poc.boz.com/", "PartnerServiceService");
		final Service service = Service.create(url, qname);
		final IPartnerService facade = service.getPort(IPartnerService.class);

		System.out.println("----------------------" + facade.getAllPartners());

		Assert.assertNotNull(facade.getAllPartners());
		Assert.assertNotNull(facade.getAllPartners().getPartners());
		Assert.assertEquals(4, facade.getAllPartners().getPartners().size());
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
}
