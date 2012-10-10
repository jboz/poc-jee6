package com.boz.poc.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/upload")
public class UploadController {

	@Inject
	private EntityManager em;

	private final List<String> uploads = new ArrayList<>();

	@GET
	public String listUploaded() {
		return uploads.toString();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(final MultipartFormDataInput datas) throws IOException {
		final String fileName = getFileName("file", datas);
		final InputStream stream = datas.getFormDataPart("file", InputStream.class, null);

		return Response.status(200).entity("file uploaded " + fileName + ", stream : " + stream).build();
	}

	/**
	 * @return file name of the first part of the message.
	 */
	private static String getFileName(final String formParam, final MultipartFormDataInput stream) {
		return getFileName(stream.getFormDataMap().get(formParam).get(0));
	}

	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data; name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename, is there a easy way in RESTEasy?
	private static String getFileName(final InputPart inputPart) {
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
