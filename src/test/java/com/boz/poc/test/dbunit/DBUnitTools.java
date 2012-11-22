package com.boz.poc.test.dbunit;

import static com.boz.poc.test.ReflectionUtils.getFieldByType;
import static com.boz.poc.test.ReflectionUtils.getFieldValue;
import static com.boz.poc.test.ReflectionUtils.getMethodOrClassLevelAnnotation;
import static com.boz.poc.test.ReflectionUtils.setFieldValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Séparation des méthodes utilitaires.
 *
 * @author jboz
 */
public final class DBUnitTools {
	private static final Logger LOG = LoggerFactory.getLogger(DBUnitTools.class);

	private DBUnitTools() {
	}

	/**
	 * Injection de {@link EntityManager}.
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static EntityManager injectEntityManager(final Object testObject) {

		final EntityManager entityManager = createFactory(testObject).createEntityManager();

		// on va setter l'entity manager
		final Field field = getNext(testObject.getClass());

		if (EntityManager.class.equals(field.getType())) {
			// soit directement comme attribut
			setFieldValue(testObject, field, entityManager);

		} else if (field.getAnnotation(Inject.class) != null) {
			// soit dans une dépendance annotée @Inject

			final Field fieldEntityManager = getFieldByType(field.getType(), EntityManager.class);
			if (fieldEntityManager != null) {
				// cette dépendance contient bien un champ entity manager
				Object dependance = getFieldValue(testObject, field);
				if (dependance == null) {
					try {
						// on instancie la dépendance si null
						dependance = field.getType().newInstance();
						setFieldValue(testObject, field, dependance);
					} catch (final Exception e) {
						new IllegalArgumentException(e);
					}
				}
				// on attribue un EntityManager à cette dépendance
				setFieldValue(dependance, fieldEntityManager, entityManager);
			}
		}
		return entityManager;
	}

	private static Field getNext(final Class<? extends Object> clazz) {
		for (final Field field : clazz.getDeclaredFields()) {
			if (EntityManager.class.equals(field.getType()) || field.getAnnotation(Inject.class) != null) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Création du context JPA.
	 */
	private static EntityManagerFactory createFactory(final Object testObject) {
		final JpaConfig jpaConfig = testObject.getClass().getAnnotation(JpaConfig.class);

		return Persistence.createEntityManagerFactory(jpaConfig == null ? "pu-test" : jpaConfig.persistenceUnit());
	}

	public static boolean needClean(final Object testObject) {
		final JpaConfig jpaConfig = testObject.getClass().getAnnotation(JpaConfig.class);

		return jpaConfig == null ? true : jpaConfig.clean();
	}

	/**
	 * Création du data set.
	 */
	public static IDataSet getDataSet(final Method testMethod, final Object testObject) throws MalformedURLException,
			DataSetException {
		final Class<?> testClass = testObject.getClass();
		final DataSet dataSetAnnotation = getMethodOrClassLevelAnnotation(DataSet.class, testMethod, testClass);
		if (dataSetAnnotation == null) {
			// No @DataSet annotation found
			return null;
		}
		final List<IDataSet> dataSets = new ArrayList<IDataSet>();

		final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder().setDtdMetadata(false).setColumnSensing(true);
		for (final String dataSetName : dataSetAnnotation.value()) {
			dataSets.add(builder.build(DataSetResolver.resolve(testClass, dataSetName)));
		}

		final ReplacementDataSet replacementDataSet = new ReplacementDataSet(new CompositeDataSet(
				dataSets.toArray(new IDataSet[dataSets.size()])));

		replacementDataSet.addReplacementObject("[NULL]", null);
		replacementDataSet.addReplacementObject("[NOW]", Calendar.getInstance().getTime());

		replacementDataSet.setStrictReplacement(true);

		return replacementDataSet;
	}

	/**
	 * Création d'une datasource basé sur les propriétés JPA.
	 */
	private static DataSource createDataSource(final EntityManagerFactory factory) {
		final String driverClassName = (String) factory.getProperties().get("hibernate.connection.driver_class");
		final String databaseUrl = (String) factory.getProperties().get("hibernate.connection.url");
		final String userName = (String) factory.getProperties().get("hibernate.connection.username");
		final String password = (String) factory.getProperties().get("hibernate.connection.password");

		LOG.info("Creating data source. Driver: " + driverClassName + ", url: " + databaseUrl + ", user: " + userName
				+ ", password: <not shown>");
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		dataSource.setUrl(databaseUrl);

		return dataSource;
	}

	public static IDatabaseConnection getConnection(final EntityManager entityManager) {
		final String schema = (String) entityManager.getEntityManagerFactory().getProperties().get("hibernate.default_schema");

		final DataSource dataSource = createDataSource(entityManager.getEntityManagerFactory());

		IDatabaseConnection connection;
		try {
			connection = new DatabaseConnection(dataSource.getConnection(), schema, false);
		} catch (DatabaseUnitException | SQLException e) {
			throw new IllegalStateException(e);
		}
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new DefaultDataTypeFactory() {
			@Override
			public Collection<String> getValidDbProducts() {
				return Arrays.asList(new String[] { "hsql", "oracle", "db2", "h2" });
			}
		});
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_TABLE_TYPE, new String[] { "TABLE", "VIEW" });

		return connection;
	}

	/**
	 * Ferme l'accès aux données.
	 */
	public static void closePersistenceContext(final EntityManager entityManager) {
		if (entityManager != null) {
			// ferme via la factory
			entityManager.getEntityManagerFactory().close();
		}
	}
}
