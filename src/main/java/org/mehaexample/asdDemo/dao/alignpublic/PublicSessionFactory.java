package org.mehaexample.asdDemo.dao.alignpublic;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PublicSessionFactory {
	private static SessionFactory factory;

	private PublicSessionFactory() {
	}

	static {
		factory = new Configuration()
				.configure("/hibernate_Public.cfg.xml").buildSessionFactory();
	}

	public static SessionFactory getFactory() {
		return factory;
	}
}
