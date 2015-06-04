package es.uvigo.esei.hasmment.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Permiso;
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
	
	public static ArrayList<DBEntity> searchInUsuario(String pattern){
		ArrayList<DBEntity> toRet = new ArrayList<DBEntity>();
		ArrayList<DBEntity> list = getListEntities(HibernateEntities.USUARIO);
		Boolean isIn;
		for (DBEntity dbEntity : list) {
			Usuario u = (Usuario) dbEntity;
			isIn = true;
			if(!u.getDni().toLowerCase().contains(pattern.toLowerCase())) {
				if(!u.getNombre().toLowerCase().contains(pattern.toLowerCase())){
					if(!u.getApellido1().toLowerCase().contains(pattern.toLowerCase())){
						if(!u.getApellido2().toLowerCase().contains(pattern.toLowerCase())) {
							if(!u.getDireccion().toLowerCase().contains(pattern.toLowerCase())) {
								if(!(new Integer(u.getHoras()).toString().toLowerCase().contains(pattern.toLowerCase()))){
									if(!u.getModalidad().toLowerCase().contains(pattern.toLowerCase())){
										isIn = false;
									}
								}
							}
						}
					}
				}
			}
			if(isIn)
				toRet.add(dbEntity);	
		}
		
		return toRet;
	}
	
	public static ArrayList<DBEntity> searchInAuxiliar(String pattern){
		ArrayList<DBEntity> toRet = new ArrayList<DBEntity>();
		ArrayList<DBEntity> list = getListEntities(HibernateEntities.AUXILIAR);
		Boolean isIn;
		for (DBEntity dbEntity : list) {
			Auxiliar a = (Auxiliar) dbEntity;
			isIn = true;
			if(!a.getDni().toLowerCase().contains(pattern.toLowerCase())) {
				if(!a.getNombre().toLowerCase().contains(pattern.toLowerCase())){
					if(!a.getApellido1().toLowerCase().contains(pattern.toLowerCase())){
						if(!a.getApellido2().toLowerCase().contains(pattern.toLowerCase())) {
							if(!(new Integer(a.getHoras()).toString().toLowerCase().contains(pattern.toLowerCase()))){
								if(!a.getFechaInicioContrato().toString().toLowerCase().contains(pattern.toLowerCase())){
									if(!a.getFechaFinContrato().toString().toLowerCase().contains(pattern.toLowerCase()))
										isIn = false;
								}
							}
						}
					}
				}
			}
			if(isIn)
				toRet.add(dbEntity);	
		}
		
		return toRet;
	}
	
	public static ArrayList<DBEntity> searchInPermiso(String pattern){
		ArrayList<DBEntity> toRet = new ArrayList<DBEntity>();
		ArrayList<DBEntity> list = getListEntities(HibernateEntities.PERMISO);
		Boolean isIn;
		for (DBEntity dbEntity : list) {
			Permiso p = (Permiso) dbEntity;
			isIn = true;
			if(!p.getDni().toLowerCase().contains(pattern.toLowerCase())) {
				if(!p.getFechaInicioPermiso().toString().toLowerCase().contains(pattern.toLowerCase())){
					if(!p.getFechaFinPermiso().toString().toLowerCase().contains(pattern.toLowerCase())){
						if(!p.getTipo().toLowerCase().contains(pattern.toLowerCase())) {
							isIn = false;
						}
					}
				}
			}
			if(isIn)
				toRet.add(dbEntity);	
		}
		
		return toRet;
	}
	
	public static ArrayList<DBEntity> searchInAsiste(String pattern){
		ArrayList<DBEntity> toRet = new ArrayList<DBEntity>();
		ArrayList<DBEntity> list = getListEntities(HibernateEntities.ASISTE);
		Boolean isIn;
		for (DBEntity dbEntity : list) {
			Asiste a = (Asiste) dbEntity;
			isIn = true;
			if(!a.getDniUsuario().toLowerCase().contains(pattern.toLowerCase())) {
				if(!a.getDniAuxiliar().toLowerCase().contains(pattern.toLowerCase())) {
					if(!a.getFechaHoraInicioAsistencia().toString().toLowerCase().contains(pattern.toLowerCase())){
						if(!a.getFechaHoraFinAsistencia().toString().toLowerCase().contains(pattern.toLowerCase())){
							if(!a.getActividad().toLowerCase().contains(pattern.toLowerCase())) {
								isIn = false;
							}
						}
					}
				}
			}
			if(isIn)
				toRet.add(dbEntity);	
		}
		
		return toRet;
	}
	
	public static DateTime getLastDateAsist() {
		DateTime last = new DateTime(1);
		DateTime toCheck;
		ArrayList<DBEntity> asists = getListEntities(HibernateEntities.ASISTE);
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			toCheck = new DateTime(a.getFechaHoraFinAsistencia());
			if(toCheck.isAfter(last))
				last = toCheck;
		}
		
		return last;
	}
	
	public static DateTime getFirstDateAsist() {
		DateTime last = getLastDateAsist();
		DateTime toCheck;
		ArrayList<DBEntity> asists = getListEntities(HibernateEntities.ASISTE);
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			toCheck = new DateTime(a.getFechaHoraInicioAsistencia());
			if(toCheck.isBefore(last))
				last = toCheck;
		}
		
		return last;
	}
}
