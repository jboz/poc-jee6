package com.boz.poc.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.boz.poc.facade.Producer;
import com.sun.faces.facelets.impl.DefaultResourceResolver;

/**
 * @author Julien Boz
 */
public class DatabaseResourceResolver extends DefaultResourceResolver {

	@Override
	public URL resolveUrl(final String path) {
		final URL url = super.resolveUrl(path);
		if (url != null) {
			return url;
		}

		try {
			return new URL("db://", "entityManager", 0, path, getHandler(path));
		} catch (final MalformedURLException e) {
		}
		return null;
	}

	private static TemplateController getTemplateController() {
		return Producer.<TemplateController> lookup("TemplateController");
	}

	private static URLStreamHandler getHandler(final String path) {

		return new URLStreamHandler() {

			@Override
			protected URLConnection openConnection(final URL url) throws IOException {
				return new URLConnection(url) {

					@Override
					public long getLastModified() {
						return getTemplateController().getLastModified(path);
					}

					@Override
					public void connect() throws IOException {
					}

					@Override
					public InputStream getInputStream() throws IOException {
						return getTemplateController().getInputStream(path);
					}

				};
			}

		};

	}

}