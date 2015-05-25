package es.uvigo.esei.hasmment.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Persona;
import es.uvigo.esei.hasmment.entities.Usuario;

public abstract class HibernateMethods {
	//Devuelve una lista de las entidades
	public static ArrayList<DBEntity> getListEntities(String entity){
		Session session = HibernateFactory.getSession();
		Query query = session.createQuery("from " + entity);
		ArrayList<DBEntity> list = (ArrayList<DBEntity>) query.list();
		session.close();
		return list;
	}
	
	//Guarda la entidad
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
	
	//Devuelve el Usuario o Auxiliar por DNI, si no devuelve null
	public static DBEntity gestEntityByDNI(String dni, String entity){
		ArrayList<DBEntity> list = getListEntities(entity);
		Persona x = null;
		for(int i=0;i<list.size();i++) {
			if(x.getDni() == dni){
				return x;
			}
		}
		return null;
	}
	
	//Comprobar si las entidades a pedir corresponde a alguna de las creadas
	private static Boolean checkEntity(DBEntity entity){
		String c = entity.getClass().toString().replace("class es.uvigo.esei.hasmment.entities.", "").trim();
		switch (c) {
		case "Usuario":
			return true;
		case "Auxiliar":
			return true;
		case "Permiso":
			return true;
		case "Asiste":
			return true;
		default:
			return false;
		}
	}
}
