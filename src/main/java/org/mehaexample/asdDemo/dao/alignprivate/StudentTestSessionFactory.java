package org.mehaexample.asdDemo.dao.alignprivate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class StudentTestSessionFactory {
	private static SessionFactory factory;

	private StudentTestSessionFactory() {
	}

	static {
		factory = new Configuration()
				.configure("/hibernate_private_test.cfg.xml").buildSessionFactory();
	}

	public synchronized static SessionFactory getFactory() {
		return factory;
	}
}
