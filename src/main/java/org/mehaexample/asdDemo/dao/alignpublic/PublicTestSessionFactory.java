package org.mehaexample.asdDemo.dao.alignpublic;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PublicTestSessionFactory {
	private static SessionFactory factory;

	private PublicTestSessionFactory() {
	}

	static {
		factory = new Configuration()
				.configure("/hibernate_public_test.cfg.xml").buildSessionFactory();
	}

	public static SessionFactory getFactory() {
		return factory;
	}
}
