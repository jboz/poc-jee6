package com.boz.poc.presentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.icefaces.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.boz.poc.domain.Template;
import com.boz.poc.facade.JPAUtils;

@Path("/template")
@Stateless
public class TemplateController {

	@Inject
	private EntityManager em;

	private final List<String> uploads = new ArrayList<>();

	@GET
	public String listUploaded() {
		return uploads.toString();
	}

	public Template findTemplate(final String fileName) {
		return JPAUtils.getSingleResult(em.createNamedQuery("Template.findByFileName", Template.class).setParameter("fileName",
				fileName));
	}

	/**
	 * Upload a template and persit to database.<br>
	 * Do en update if the template is already in data base.
	 *
	 * @param datas
	 * @return
	 * @throws IOException
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(final MultipartFormDataInput datas) throws IOException {
		final String fileName = RestEasyUtils.getFileName("file", datas);
		final InputStream stream = datas.getFormDataPart("file", InputStream.class, null);

		Template template = findTemplate(fileName);

		final boolean newOne = template == null;
		if (template == null) {
			template = new Template();
			template.setFileName("/survey-static-inDB/" + fileName);
		}
		template.setTemplate(IOUtils.toByteArray(stream)); // set new stream
		template.setLastModified(new Date()); // update last modified date

		em.persist(template);

		return Response.status(200).entity("template " + (newOne ? "inserted" : "updated") + " : " + template).build();
	}

	public long getLastModified(final String fileName) {
		final Template template = findTemplate(fileName);

		return template == null ? System.currentTimeMillis() : template.getLastModified().getTime();
	}

	public InputStream getInputStream(final String fileName) {
		final Template template = findTemplate(fileName);

		return template == null ? null : new ByteArrayInputStream(template.getTemplate());
	}
}
