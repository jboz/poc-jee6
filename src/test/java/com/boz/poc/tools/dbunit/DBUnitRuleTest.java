package com.boz.poc.tools.dbunit;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;

import com.boz.poc.test.MyEntity;

@DataSet("entities")
public class DBUnitRuleTest {
	@Rule
	public DBUnitRule dbUnitRule = DBUnitRule.init();

	private EntityManager em;

	@Test
	public void testLoad() {
		assertThat(em).isNotNull();
		final MyEntity entity = em.find(MyEntity.class, 1l);
		assertEquals(Long.valueOf(1), entity.getId());
		assertEquals("Hervé", entity.getName());
	}

	@Test
	@DataSet("datas/others")
	public void testLoadOthers() {
		assertThat(em).isNotNull();
		final MyEntity entity = em.find(MyEntity.class, 20l);
		assertEquals(Long.valueOf(20), entity.getId());
		assertEquals("Pierre", entity.getName());
	}
}
