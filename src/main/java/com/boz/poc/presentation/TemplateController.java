package com.boz.poc.presentation;

import java.io.InputStream;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("template")
@Stateless
public class TemplateController {

	@Inject
	private EntityManager em;

	@GET
	public String listAll() {
		return "templates :";
	}

	/**
	 * Insert or update a template.
	 *
	 * @param fileName
	 * @param template
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void insertOrUpdateTemplate(@FormParam("fileName") final String fileName,
			@FormParam("template") final InputStream template) {

		System.out.println("insert or update template");
	}
}
