package com.boz.poc.tools.mockito;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;

/**
 * <p>
 * A simple {@link MockingRule} that delegates to {@link MockitoAnnotations#initMocks(Object)} to initialize fields (in the unit
 * test class) annotated with {@link org.mockito.Mock}. Let's you write, for example:
 * </p>
 *
 * <pre>
 * public class MyTest {
 *
 *     &#064;Rule
 *     public MockingRule mockitoRule = MockingRule.init();
 *
 *     &#064;Mock
 *     private Foo mock;
 *
 *     &#064;Test
 *     public void testSomething() {
 *         assertNotNull(mock);
 *         Something something = new Something(mock);
 *         // ...
 *     }
 * </pre>
 */
@SuppressWarnings("deprecation")
public class MockingRule implements MethodRule {

	public static final MockingRule init() {
		return new MockingRule();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement, org.junit.runners.model.FrameworkMethod,
	 *      java.lang.Object)
	 */
	@Override
	public final Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				MockitoAnnotations.initMocks(target);
				base.evaluate();
			}
		};
	}
}