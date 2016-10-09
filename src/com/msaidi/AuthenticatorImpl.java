package com.msaidi;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class AuthenticatorImpl extends Authenticator {

	public final static String MAIL_USERNAME = "mail.username";
	public final static String MAIL_PASSWORD = "mail.password";

	protected PasswordAuthentication getPasswordAuthentication() {
		Config config = Config.getInstance();
		return new PasswordAuthentication(config.getConfParam(MAIL_USERNAME),
				config.getConfParam(MAIL_PASSWORD));
	}
}
