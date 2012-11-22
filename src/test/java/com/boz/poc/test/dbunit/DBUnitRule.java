package com.boz.poc.test.dbunit;

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

	public static final DBUnitRule init() {
		return new DBUnitRule();
	}

	@Override
	public Statement apply(final Statement base, final FrameworkMethod method, final Object testObject) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				DBUnitRunner.aroundTest(base, method, testObject);
			}
		};
	}
}
