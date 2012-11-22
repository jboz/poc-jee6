package com.boz.poc.test.dbunit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a data set should be loaded before the test run.
 * <p>
 * If a class is annotated, a test data set will be loaded before the execution of each of the test methods in the class. A data
 * set file name can explicitly be specified. If no such file name is specified, first a data set named
 * 'classname'.'testmethod'.xml will be tried, if no such file exists, 'classname'.xml will be tried. If that file also doesn't
 * exist, an exception will be thrown. Filenames that start with '/' are treated absolute. Filenames that do not start with '/',
 * are relative to the current class.
 * </p>
 * <p>
 * A test method can also be annotated with DataSet in which case you specify the dataset that needs to be loaded before this test
 * method is run. Again, a file name can explicitly be specified or if not specified, the default will be used: first
 * 'classname'.'methodname'.xml and if that file does not exist 'classname'.xml.
 * </p>
 * Examples:
 *
 * <pre>
 * <code>
 * '    @DataSet
 *      public class MyTestClass extends UnitilsJUnit3 {
 * '
 *          public void testMethod1(){
 *          }
 *
 * '        @DataSet("aCustomFileName.xml")
 *          public void testMethod2() {
 *            ...
 *          }
 *      }
 * </code>
 * </pre>
 * <p>
 * Will load a data set file named MyTestClass.xml or MyTestClass-testMethod1.xml for testMethod1 in the same directory as the
 * class. And for testMethod2 a data set file named aCustomFileName.xml in the same directory as the class is loaded. *
 * </p>
 *
 * <pre>
 * <code>
 *      public class MyTestClass extends UnitilsJUnit3 {
 *
 *          public void testMethod1(){
 *          }
 *
 * '        @DataSet
 *          public void testMethod2(){
 *            ...
 *          }
 *      }
 * </code>
 * </pre>
 *
 * Will not load any dataset for testMethod1 (there is no class level data set). Will load a data set file named MyTestClass.xml
 * or MyTestClass.testMethod2.xml for testMethod2.
 */
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@Inherited
public @interface DataSet {

  /**
   * The file name of the data set. If left empty, the default filename will be used: first 'classname'.'testMethodname'.xml will
   * be tried, if that file does not exist, 'classname'.xml is tried. If that file also does not exist, an exception is thrown.
   *
   * @return the fileName, empty for default
   */
  String[] value() default "";
}
