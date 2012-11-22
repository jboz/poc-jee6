package ch.mobi.tips.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.mobi.tips.model.Member;
import ch.mobi.tips.rest.MemberService;
import ch.mobi.tips.util.Resources;

/**
 * Uses Arquilian to test the JAX-RS processing class for member registration.
 * 
 * @author balunasj
 */
@RunWith(Arquillian.class)
public class MemberRegistrationTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(Member.class, MemberService.class, Resources.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	MemberService memberRegistration;

	@Inject
	Logger log;

	@Test
	public void testRegister() throws Exception {
		final Response response = memberRegistration.createMember(null, "Jane Doe", "jane@mailinator.com", "2125551234");

		assertEquals("Unexpected response status", 200, response.getStatus());
		log.info(" New member was persisted and returned status " + response.getStatus());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testInvalidRegister() throws Exception {
		final Response response = memberRegistration.createMember(null, "", "", "");

		assertEquals("Unexpected response status", 400, response.getStatus());
		assertNotNull("response.getEntity() should not null", response.getEntity());
		assertEquals("Unexpected response.getEntity(). It contains " + response.getEntity(), 3,
				((Map<String, String>) response.getEntity()).size());
		log.info("Invalid member register attempt failed with return code " + response.getStatus());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDuplicateEmail() throws Exception {
		// Register an initial user
		memberRegistration.createMember(null, "Jane Doe", "jane@mailinator.com", "2125551234");

		// Register a different user with the same email
		final Response response = memberRegistration.createMember(null, "John Doe", "jane@mailinator.com", "2133551234");

		assertEquals("Unexpected response status", 409, response.getStatus());
		assertNotNull("response.getEntity() should not null", response.getEntity());
		assertEquals("Unexpected response.getEntity(). It contains" + response.getEntity(), 1,
				((Map<String, String>) response.getEntity()).size());
		log.info("Duplicate member register attempt failed with return code " + response.getStatus());
	}
}
