package com.msaidi;

import org.apache.velocity.VelocityContext;

public interface MailSenderService {
	void sendMail(String templateVM, VelocityContext velocityContext);
}
