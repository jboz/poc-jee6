package com.boz.poc.ws;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler implements SOAPHandler<SOAPMessageContext> {
	private static final Logger LOG = LoggerFactory.getLogger(MessageHandler.class);

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public boolean handleFault(final SOAPMessageContext context) {
		logToSystemOut(context);
		return true;
	}

	@Override
	public boolean handleMessage(final SOAPMessageContext context) {
		logToSystemOut(context);
		return true;
	}

	private void logToSystemOut(final SOAPMessageContext smc) {
		final Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty.booleanValue()) {
			LOG.debug("Outgoing message:");
		} else {
			LOG.debug("Incoming message:");
		}

		final SOAPMessage message = smc.getMessage();
		try (final ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
			message.writeTo(bout);

			LOG.debug(bout.toString());

		} catch (final Exception e) {
			LOG.error("Exception in handler: ", e);
		}
	}

	@Override
	public void close(final MessageContext context) {
	}
}
