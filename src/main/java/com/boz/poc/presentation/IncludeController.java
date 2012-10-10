package com.boz.poc.presentation;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("include")
public class IncludeController {

	@GET
	@Path("{fileName}")
	public InputStream loadFile(@PathParam("fileName") final String fileName) {
		System.out.println(fileName);
		return null;
	}
}
