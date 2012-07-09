package com.boz.poc.facade;

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

/**
 * Test d'intégration
 *
 * @author jboz
 */
@RunWith(Arquillian.class)
@UsingDataSet("datas/partners.xml")
@RunAsClient
public class PartnerWsIT {

	@Deployment
	public static WebArchive createTestArchive() {
		final WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);

		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(PartnerFacade.class)
				.addPackages(true, "com.boz.poc.domain")
				.addPackages(false, "ch.mobi.posi.common.tools")
				.addAsManifestResource("test-persistence.xml", "persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.setWebXML(
						new StringAsset(webXml.servlet("PartnerService", PartnerFacade.class.getName(), new String[] { "/PartnerService" })
								.exportAsString()));
	}

	@Test
	public void testGetAllPartners_ordered() throws MalformedURLException {
		final QName serviceName = new QName("facade.poc.boz.com", "PartnerService");
		final URL wsdlURL = new URL("http://localhost:8080/test/PartnerService?wsdl");
		final Service service = Service.create(wsdlURL, serviceName);
		final PartnerFacade facade = service.getPort(PartnerFacade.class);

		Assert.assertNotNull(facade.getAllPartners());
		Assert.assertEquals(4, facade.getAllPartners().size());
		Assert.assertEquals(createPartner(4000), facade.getAllPartners().get(0));
		Assert.assertEquals(createPartner(2000), facade.getAllPartners().get(1));
		Assert.assertEquals(createPartner(3000), facade.getAllPartners().get(2));
		Assert.assertEquals(createPartner(1000), facade.getAllPartners().get(3));
	}

	private static Partner createPartner(final long id) {
		final Partner partner = new Partner();
		ReflectionUtils.setFieldValue(partner, "id", id);

		return partner;
	}
}
