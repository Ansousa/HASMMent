package es.uvigo.esei.hasmment.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;

@SuppressWarnings("unchecked")
public abstract class HibernateMethods {
	//Devuelve una lista de las entidades
	public static ArrayList<DBEntity> getListEntities(String entity){
		Session session = HibernateFactory.getSession();
		Query query = session.createQuery("from " + entity);
		ArrayList<DBEntity> list = (ArrayList<DBEntity>) query.list();
		session.close();
		return list;
	}
	
	//Guarda una entidad
	public static void saveEntity(DBEntity entity){
		if(checkEntity(entity))
		{
			Session session = HibernateFactory.getSession();
			session.beginTransaction();
			session.save(entity);
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
	}
	
	//Modifica una entidad
	public static void modifyEntity(DBEntity entity){
		if(checkEntity(entity))
		{
			Session session = HibernateFactory.getSession();
			session.beginTransaction();
			session.update(entity);
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
	}
	
	//Elimina una entidad dada
	public static void deleteEntity(DBEntity entity) {
		if(checkEntity(entity)) {
			Session session = HibernateFactory.getSession();
			session.beginTransaction();
			session.delete(entity);
			session.flush();
			session.getTransaction().commit();
			session.close();
		}
	}
	
	//devuelve un usuario por su dni
	public static Usuario getUsuario(String DNI) {
		Session session = HibernateFactory.getSession();
	 	Usuario u = (Usuario)session.createQuery("from Usuario u where u.dni = '" + DNI +"'").uniqueResult();
	 	session.close();
	 	return u;
	}
	
	//devuelve un auxiliar por su dni
	public static Auxiliar getAuxiliar(String DNI) {
		Session session = HibernateFactory.getSession();
		Auxiliar a = (Auxiliar)session.createQuery("from Auxiliar u where u.dni = '" + DNI +"'").uniqueResult();
		session.close();
		return a;
	}
	
	//Comprobar si las entidades a pedir corresponde a alguna de las creadas, si todo correcto devuelve true
	private static Boolean checkEntity(DBEntity entity){
		String c = entity.getClass().toString().replace("class es.uvigo.esei.hasmment.entities.", "").trim();
		switch (c) {
		case HibernateEntities.USUARIO:
			return true;
		case HibernateEntities.AUXILIAR:
			return true;
		case HibernateEntities.PERMISO:
			return true;
		case HibernateEntities.ASISTE:
			return true;
		default:
			return false;
		}
	}
}
