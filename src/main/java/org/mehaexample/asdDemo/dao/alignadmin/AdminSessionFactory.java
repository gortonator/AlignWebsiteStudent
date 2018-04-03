package org.mehaexample.asdDemo.dao.alignadmin;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AdminSessionFactory {
	private static SessionFactory factory;

	private AdminSessionFactory() {
	}

	static {
		factory = new Configuration()
				.configure("/hibernate_Admin.cfg.xml").buildSessionFactory();
	}

	public static SessionFactory getFactory() {
		return factory;
	}
}
