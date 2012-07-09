package com.boz.poc.facade;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.boz.poc.domain.Partner;

import ch.mobi.posi.common.tools.ReflectionUtils;

/**
 * Test d'intégration
 *
 * @author jboz
 */
@RunWith(Arquillian.class)
@UsingDataSet("datas/partners.xml")
public class PartnerFacadeIT {

	@Deployment
	public static JavaArchive createTestArchive() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar").addClasses(PartnerFacade.class)
				.addPackages(true, "com.boz.poc.domain", "org.joda.time").addPackages(false, "ch.mobi.posi.common.tools")
				.addAsManifestResource("test-persistence.xml", "persistence.xml").addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	private PartnerFacade facade;

	@Test
	public void testGetAllPartners_ordered() {
		Assert.assertNotNull(facade.getAllPartners());

		System.out.println("----------------------" + facade.getAllPartners());

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
