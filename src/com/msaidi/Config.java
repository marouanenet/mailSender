package com.msaidi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class Config {

	/**
	 * singleton de la classe.
	 */
	private static Config instance;

	private final Properties properties;
	private static final Logger logger = Logger.getLogger(Config.class);

	private Config() {
		properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream("emailConfig.properties");
			properties.load(fileInputStream);
		} catch (final FileNotFoundException e) {
			logger.debug("file emailConfig.properties not found");
		} catch (final IOException e) {
		} finally {
			IOUtils.closeQuietly(fileInputStream);
		}
	}

	/**
	 * Constructeur avec le nom du fichier properties.
	 * 
	 * @param fileName
	 *            nom du fichier properties
	 */
	private Config(final String fileName) throws IOException {
		properties = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			properties.load(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * permet de r�cup�rer un singleton de classe.
	 * 
	 * @return singleton de la classe
	 * @throws IOException
	 *             si un probl�me survient lors du chargemnt du fichier
	 *             properties
	 */
	public static synchronized Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	public String getConfParam(final String key) {
		return properties.getProperty(key);
	}

}
