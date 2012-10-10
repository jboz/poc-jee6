package com.boz.poc.presentation;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaboz.commons.test.DateUtils;

@ManagedBean(name = "msgs")
@RequestScoped
public class MessageProvider extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProvider.class);

	private static final String CURRENCY = "CURRENCY";
	private static final String NOW = "NOW";

	private ResourceBundle bundle;

	public ResourceBundle getBundle() {
		if (bundle == null) {
			final String bundleName = "labels";
			final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			try {
				bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentLoader(bundleName));
			} catch (final MissingResourceException e) {
				// bundle with this name not found;
			}
		}
		return bundle;
	}

	public static ClassLoader getCurrentLoader(final Object fallbackClass) {
		return Thread.currentThread().getContextClassLoader() == null ? fallbackClass.getClass().getClassLoader() : Thread
				.currentThread().getContextClassLoader();
	}

	@Override
	public String get(final Object key) {
		try {
			final String value = getBundle().getString(String.valueOf(key));
			if (value != null) {
				return replaceConstants(value);
			}
		} catch (final Exception e) {
		}
		System.out.println(key + " has no bundle value !");
		return "";
	}

	private static String replaceConstants(final String value) {
		return value.replaceAll(around(CURRENCY), "CHF").replaceAll(around(NOW), DateUtils.format(DateUtils.getToday()));
	}

	private static String around(final String value) {
		return "\\[" + value + "\\]";
	}

	public static Object resolveExpression(final String expression) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final Application app = facesContext.getApplication();
		final ExpressionFactory elFactory = app.getExpressionFactory();
		final ELContext elContext = facesContext.getELContext();
		final ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
		return valueExp.getValue(elContext);
	}
}
