package com.msaidi;

import java.io.UnsupportedEncodingException;

import org.apache.velocity.VelocityContext;

public class Main {
	public static void main(String[] args) throws UnsupportedEncodingException {
		MailSenderServiceImpl envoieMailService = MailSenderServiceImpl
				.getInstance();
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("firstName", "firstName");
		velocityContext.put("lastName", "lastName");
		envoieMailService.sendMail("mailContent.vm", velocityContext);
	}
}
