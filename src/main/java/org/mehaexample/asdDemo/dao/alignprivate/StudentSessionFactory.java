package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class StudentSessionFactory {
	private static SessionFactory factory;

	private StudentSessionFactory() {
	}

	static {
		factory = new Configuration()
				.configure("/hibernate.Private.cfg.xml").buildSessionFactory();
	}

	public static synchronized SessionFactory getFactory() {
		return factory;
	}
}
