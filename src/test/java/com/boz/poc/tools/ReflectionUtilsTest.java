package com.boz.poc.tools;

import static org.fest.assertions.Assertions.assertThat;

import javax.ejb.AccessTimeout;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Scope;
import javax.validation.constraints.NotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.boz.poc.test.MyEntity;
import com.boz.poc.test.MyService;

/**
 * Test de la classe {@link ReflectionUtils}.
 *
 * @author jboz
 */
@SuppressWarnings("unused")
public class ReflectionUtilsTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Local
  private static class AbstractAnnotedClass {

    @NotNull
    public final String provider = "PROVIDED";

    @Named
    public void testAbstractAnnoted() {
    }
  }

  @Stateless
  private static class AnnotedClass extends AbstractAnnotedClass {

    @Inject
    public MyService service;

    @AccessTimeout(1l)
    public void testAnnoted() {
    }
  }

  @Test
  public void testGetMethodOrClassLevelAnnotation() {
    assertThat(
        ReflectionUtils.getMethodOrClassLevelAnnotation(AccessTimeout.class, AnnotedClass.class.getMethods()[0],
            AnnotedClass.class)).isInstanceOf(AccessTimeout.class);

    assertThat(
        ReflectionUtils.getMethodOrClassLevelAnnotation(Local.class, AnnotedClass.class.getMethods()[0], AnnotedClass.class))
        .isInstanceOf(Local.class);

    assertThat(
        ReflectionUtils.getMethodOrClassLevelAnnotation(Stateless.class, AnnotedClass.class.getMethods()[0], AnnotedClass.class))
        .isInstanceOf(Stateless.class);

    assertThat(
        ReflectionUtils.getMethodOrClassLevelAnnotation(Named.class, AnnotedClass.class.getMethods()[0], AnnotedClass.class))
        .isNull();

    assertThat(
        ReflectionUtils.getMethodOrClassLevelAnnotation(Inject.class, AnnotedClass.class.getMethods()[0], AnnotedClass.class))
        .isNull();

    assertThat(
        ReflectionUtils.getMethodOrClassLevelAnnotation(Scope.class, AnnotedClass.class.getMethods()[0], AnnotedClass.class))
        .isNull();
  }

  @Test
  public void testGetClassLevelAnnotation() {
    assertThat(ReflectionUtils.getClassLevelAnnotation(Stateless.class, AnnotedClass.class)).isInstanceOf(Stateless.class);
    assertThat(ReflectionUtils.getClassLevelAnnotation(Local.class, AnnotedClass.class)).isInstanceOf(Local.class);
    assertThat(ReflectionUtils.getClassLevelAnnotation(Scope.class, AnnotedClass.class)).isNull();
  }

  @Test
  public void testGetFieldAnnotatedWith() {
    assertThat(ReflectionUtils.getFieldAnnotatedWith(Object.class, Inject.class)).isNull();
    assertThat(ReflectionUtils.getFieldAnnotatedWith(AnnotedClass.class, Inject.class)).isNotNull();
    assertThat(ReflectionUtils.getFieldAnnotatedWith(AnnotedClass.class, Inject.class).getName()).isEqualTo("service");
    assertThat(ReflectionUtils.getFieldAnnotatedWith(AnnotedClass.class, NotNull.class).getName()).isEqualTo("provider");
  }

  @Test
  public void testGetFieldByType() {
    assertThat(ReflectionUtils.getFieldByType(Object.class, MyService.class)).isNull();
    assertThat(ReflectionUtils.getFieldByType(AnnotedClass.class, MyService.class)).isNotNull();
    assertThat(ReflectionUtils.getFieldByType(AnnotedClass.class, MyService.class).getName()).isEqualTo("service");
    assertThat(ReflectionUtils.getFieldByType(AnnotedClass.class, String.class).getName()).isEqualTo("provider");
  }

  @Test
  public void testGetFieldByName() {
    assertThat(ReflectionUtils.getFieldByName(Object.class, "service")).isNull();
    assertThat(ReflectionUtils.getFieldByName(AnnotedClass.class, "service").getName()).isEqualTo("service");
    assertThat(ReflectionUtils.getFieldByName(AnnotedClass.class, "provider").getName()).isEqualTo("provider");
  }

  @Test
  public void testSetFieldValueObjectClassOfTObject() {
    final AnnotedClass annotedClass = new AnnotedClass();
    assertThat(annotedClass.service).isNull();
    ReflectionUtils.setFieldValue(annotedClass, MyService.class, new MyService());
    assertThat(annotedClass.service).isNotNull();
  }

  @Test
  public void testSetFieldValueObjectStringObject() {
    final AnnotedClass annotedClass = new AnnotedClass();
    assertThat(annotedClass.service).isNull();
    ReflectionUtils.setFieldValue(annotedClass, AnnotedClass.class.getFields()[0], new MyService());
    assertThat(annotedClass.service).isNotNull();
  }

  @Test
  public void testSetFieldValueObjectFieldObject() {
    final AnnotedClass annotedClass = new AnnotedClass();
    assertThat(annotedClass.service).isNull();
    ReflectionUtils.setFieldValue(annotedClass, "service", new MyService());
    assertThat(annotedClass.service).isNotNull();
    // pas d'erreur même si le champs est final
    ReflectionUtils.setFieldValue(new AnnotedClass(), "provider", "otherValue");
  }

  @Test
  public void testGetFieldValue() {
    final AnnotedClass annotedClass = new AnnotedClass();
    assertThat(ReflectionUtils.getFieldValue(annotedClass, AnnotedClass.class.getFields()[0])).isNull();
    annotedClass.service = new MyService();
    assertThat(ReflectionUtils.getFieldValue(annotedClass, AnnotedClass.class.getFields()[0])).isNotNull();
  }

  @Test
  public void testGetFieldValue_NullPointerException() {
    thrown.expect(NullPointerException.class);

    ReflectionUtils.getFieldValue(null, AbstractAnnotedClass.class.getFields()[0]);
  }

  @Test
  public void testGetFieldValue_IllegalArgumentException() throws SecurityException, NoSuchFieldException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Error while trying to access field " + MyEntity.class.getDeclaredField("name"));

    ReflectionUtils.getFieldValue(new AnnotedClass(), MyEntity.class.getDeclaredField("name"));
  }

  @Test
  public void testSetFieldValue_NullPointerException() {
    thrown.expect(NullPointerException.class);

    ReflectionUtils.setFieldValue(null, AbstractAnnotedClass.class.getFields()[0], "otherValue");
  }

  @Test
  public void testSetFieldValue_IllegalArgumentException() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unable to assign the value to field: provider. "
        + "Ensure that this field is of the correct type. Value: 1");

    ReflectionUtils.setFieldValue(new AnnotedClass(), AbstractAnnotedClass.class.getFields()[0], Long.valueOf(1));
  }
}
