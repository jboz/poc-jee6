package com.boz.poc.presentation;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public final class RestEasyUtils {

	private RestEasyUtils() {
	}

	/**
	 * @return file name of the first part of the message.
	 */
	public static String getFileName(final String formParam, final MultipartFormDataInput stream) {
		return getFileName(stream.getFormDataMap().get(formParam).get(0));
	}

	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data; name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename, is there a easy way in RESTEasy?
	public static String getFileName(final InputPart inputPart) {
		final MultivaluedMap<String, String> header = inputPart.getHeaders();
		final String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (final String filename : contentDisposition) {
			if (filename.trim().startsWith("filename")) {
				final String[] name = filename.split("=");

				return name[1].trim().replaceAll("\"", "");
			}
		}
		return "unknown";
	}
}
