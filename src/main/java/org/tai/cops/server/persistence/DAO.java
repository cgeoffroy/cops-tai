package org.tai.cops.server.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tai.cops.concepts.Placement;

public class DAO {
	
	final static Logger logger = LoggerFactory.getLogger(DAO.class);
	
	public static boolean save(Placement p) {
		boolean res = false;
		try {
			Session session = HibernateUtils.getSessionFactory().openSession();          
			session.beginTransaction();
			session.save(p);
			session.getTransaction().commit();
			session.close();
			res = true;
		} catch (HibernateException e) {
			logger.error("error while saving a Placement", e);
		}
		return res;
	}

	public static List<Placement> findByName(String name) {
		List<Placement> tmp = new ArrayList<>();
		List<?> cats = new ArrayList<>();
		Session session = null;
		try {
			session = HibernateUtils.getSessionFactory().openSession();         
			cats = session.createCriteria(Placement.class)
			    .add(Restrictions.eq("name", name)).list();
		} catch (HibernateException e) {
			logger.error("error while searching for a Placement by name", e);
		} finally {
			if (null != session)
				session.close();
		}
		for(Object o : cats) {
			if (o instanceof Placement)
				tmp.add((Placement) o);
		}
		return tmp;
	}
	
}
