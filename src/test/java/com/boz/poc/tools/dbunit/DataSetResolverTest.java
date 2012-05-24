package com.boz.poc.tools.dbunit;

import static org.fest.assertions.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test de la classe {@link DataSetResolver}.
 *
 * @author jboz
 */
public class DataSetResolverTest {
	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void testGetDataSetFileName() {
		assertThat(DataSetResolver.getDataSetFileName(getClass(), null))
				.isEqualTo("com/boz/poc/tools/dbunit/DataSetResolverTest.xml");

		assertThat(DataSetResolver.getDataSetFileName(getClass(), "test")).isEqualTo("test.xml");
		assertThat(DataSetResolver.getDataSetFileName(getClass(), "/test")).isEqualTo("test.xml");
		assertThat(DataSetResolver.getDataSetFileName(getClass(), "/test.xml")).isEqualTo("test.xml");
	}

	@Test
	public void testToFile() throws MalformedURLException {
		assertThat(DataSetResolver.toFile(null)).isNull();
		assertThat(DataSetResolver.toFile(getClass().getResource("entities.xml"))).isNull();
		assertThat(DataSetResolver.toFile(new URL("http", "localhost", "test"))).isNull();
		assertThat(DataSetResolver.toFile(getClass().getResource("/entities.xml"))).isNotNull();
	}

	@Test
	public void testResolve() {
		assertThat(DataSetResolver.resolve(getClass(), "/entities.xml")).isNotNull();
	}

	@Test
	public void testResolve_IllegalArgumentException() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("DataSet file with name test.xml cannot be found");

		DataSetResolver.resolve(getClass(), "test.xml");
	}
}
