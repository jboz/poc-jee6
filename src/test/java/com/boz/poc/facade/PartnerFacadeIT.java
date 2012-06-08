package com.boz.poc.facade;

import static org.fest.assertions.Assertions.assertThat;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
public class PartnerFacadeIT {

	@Deployment
	public static JavaArchive createTestArchive() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar").addClasses(PartnerFacade.class)
				.addPackages(true, "com.boz.poc.domain").addAsManifestResource("test-persistence.xml", "persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	private PartnerFacade facade;

	@Test
	public void testGetAllPartners_ordered() {
		assertThat(facade.getAllPartners()).hasSize(4).containsExactly(createPartner(4000), createPartner(2000), createPartner(3000),
				createPartner(1000));
	}

	private static Partner createPartner(final long id) {
		final Partner partner = new Partner();
		ReflectionUtils.setFieldValue(partner, "id", id);

		return partner;
	}

}
