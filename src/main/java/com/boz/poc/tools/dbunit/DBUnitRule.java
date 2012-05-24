package com.boz.poc.tools.dbunit;

import static com.boz.poc.tools.dbunit.DBUnitTools.closePersistenceContext;
import static com.boz.poc.tools.dbunit.DBUnitTools.injectEntityManager;
import static com.boz.poc.tools.dbunit.DBUnitTools.insertDataSet;

import javax.persistence.EntityManager;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Permet de ne pas définir de runner ni de classe abstraite :
 *
 * <pre>
 * <code>
 * @DataSet("datas/entities")
 * public class ProductTest {
 *
 * ' @Rule
 *   public DBUnitRule dbUnitRule = DBUnitRule.init();
 *
 *   private EntityManager em;
 *
 * ' @Test
 *   public void testLoadById() {
 *     final Product entity = em.find(Product.class, 326096L);
 *
 *     assertThat(entity).isNotNull();
 *     ...
 *   }
 * }
 * </code>
 * </pre>
 *
 * @author jboz
 */
@SuppressWarnings("deprecation")
public class DBUnitRule implements MethodRule {

	private EntityManager entityManager;

	public static final DBUnitRule init() {
		return new DBUnitRule();
	}

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, final Object testObject) {
		// ne pas utiliser DBUnit si le test ne le demande pas
		final boolean dbUnitTest = isDBUnitTest(method, testObject);

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				if (dbUnitTest) {
					before(method, testObject);
				}
				try {
					base.evaluate();
				} finally {
					if (dbUnitTest) {
						after(method, testObject);
					}
				}
			}
		};
	}

	/**
	 * @return true si le test requiet DBUnit.
	 */
	private boolean isDBUnitTest(final FrameworkMethod method, final Object testObject) {
		return testObject.getClass().getAnnotation(DataSet.class) != null || method.getMethod().getAnnotation(DataSet.class) != null;
	}

	protected void before(final FrameworkMethod method, final Object testObject) {
		entityManager = injectEntityManager(testObject);
		insertDataSet(entityManager, method.getMethod(), testObject);
	}

	protected void after(final FrameworkMethod method, final Object testObject) {
		closePersistenceContext(entityManager);
	}
}
