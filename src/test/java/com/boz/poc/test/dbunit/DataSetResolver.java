package com.boz.poc.test.dbunit;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * Data set file resolver.
 *
 * @author jboz
 */
public final class DataSetResolver {

	private DataSetResolver() {
	}

	/**
	 * Resolves the location for a data set with a certain name. An exception is raised if the file could not be found.
	 *
	 * @param testClass The test class, not null
	 * @param dataSetName The name of the data set, not null
	 * @return The data set file, not null
	 */
	public static File resolve(final Class<?> testClass, final String dataSetName) {
		// construct file name
		final String dataSetFileName = getDataSetFileName(testClass, dataSetName);

		// find file in classpath
		final URL dataSetUrl = testClass.getResource('/' + dataSetFileName);
		if (dataSetUrl == null) {
			throw new IllegalArgumentException("DataSet file with name " + dataSetFileName + " cannot be found");
		}
		return toFile(dataSetUrl);
	}

	/**
	 * Get the file name for the data set.
	 *
	 * @param testClass The test class, not null
	 * @param dataSetName The data set name, not null
	 * @return The file name, not null
	 */
	protected static String getDataSetFileName(final Class<?> testClass, final String dataSetName) {
		if (StringUtils.isBlank(dataSetName)) {
			// empty means, use default file name, which is the name of the class + extension
			return getDefaultDataSetFileName(testClass);
		}
		String cleanedPath = dataSetName;
		// remove first char if it's a /
		if (dataSetName.startsWith("/")) {
			cleanedPath = dataSetName.substring(1);
		}
		return cleanedPath.endsWith(".xml") ? cleanedPath : cleanedPath + ".xml";
	}

	/**
	 * Convert from a <code>URL</code> to a <code>File</code>.
	 * <p>
	 * From version 1.1 this method will decode the URL. Syntax such as <code>file:///my%20docs/file.txt</code> will be correctly
	 * decoded to <code>/my docs/file.txt</code>.
	 *
	 * @param url the file URL to convert, null returns null
	 * @return the equivalent <code>File</code> object, or <code>null</code> if the URL's protocol is not <code>file</code>
	 * @throws IllegalArgumentException if the file is incorrectly encoded
	 */
	public static File toFile(final URL url) {
		if (url == null || !url.getProtocol().equals("file")) {
			return null;
		} else {
			return new File(url.getFile().replace('/', File.separatorChar));
		}
	}

	/**
	 * Gets the name of the default testdata file at class level The default name is constructed as follows: 'classname without
	 * packagename'.xml
	 *
	 * @param testClass The test class, not null
	 * @param extension The configured extension of dataset files
	 * @return The default filename, not null
	 */
	private static String getDefaultDataSetFileName(final Class<?> testClass) {
		return testClass.getName().replaceAll("\\.", "/") + ".xml";
	}
}
