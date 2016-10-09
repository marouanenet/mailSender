package com.msaidi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.Security;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class MailSenderServiceImpl implements MailSenderService {

	private static final Logger logger = Logger
			.getLogger(MailSenderServiceImpl.class);

	public final static String MAIL_CONTENT = "mailContent.vm";
	public final static String MAIL_USERNAME = "mail.username";
	public final static String MAIL_PASSWORD = "mail.password";
	public final static String MAIL_FROM = "mail.from";
	public final static String MAIL_SUBJECT = "mail.subject";
	public final static String TEXT_HTML = "text/html";

	private static MailSenderServiceImpl instance;

	static Properties props = new Properties();

	static {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	}

	private MailSenderServiceImpl() {
		try {
			logger.debug("chargement des fichier properties");
			props.load(new FileInputStream("emailConfig.properties"));
		} catch (IOException ex) {
			logger.debug("file emailConfig.properties not found");
		}
	}

	public void sendMail(String templateVM, VelocityContext velocityContext) {
		Session session = Session.getDefaultInstance(props,
				new AuthenticatorImpl());
		Config config = Config.getInstance();
		try {
			Message message = new MimeMessage(session);
			Address from = new InternetAddress(config.getConfParam(MAIL_FROM));
			InternetAddress[] to = InternetAddress
					.parse("marouanenet@gmail.com");
			String subject = config.getConfParam(MAIL_FROM);
			Object content = getEmailContent(templateVM, velocityContext);
			String typeContent = TEXT_HTML;
			message.setFrom(from);
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject(subject);
			message.setContent(content, typeContent);
			Transport.send(message);
			logger.info("Email Sent to customer");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static MailSenderServiceImpl getInstance() {
		if (null == instance) {
			instance = new MailSenderServiceImpl();
		}
		return instance;
	}

	public static String getEmailContent(String templateVM,
			VelocityContext velocityContext) {
		StringWriter writer = new StringWriter();
		try {
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			ve.setProperty("classpath.resource.loader.class",
					ClasspathResourceLoader.class.getName());
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.NullLogChute" );
			ve.init();
			Template t = ve.getTemplate(templateVM, "UTF-8");
			t.merge(velocityContext, writer);
		} catch (Exception e) {
			logger.error(e);
		}
		return writer.toString();
	}

}
