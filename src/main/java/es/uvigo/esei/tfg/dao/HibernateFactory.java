package es.uvigo.esei.tfg.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public abstract class HibernateFactory {
	private static SessionFactory factory;
	
	//Crea una sesion para hacer operaciones en la base de datos
	public static Session getSession()
	{
		if (factory == null) {
			Configuration configuration = new Configuration().configure();
	    	StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
	    	factory = configuration.buildSessionFactory(builder.build());
		}
		
		return factory.openSession();
	}
	
	//Cierra la fabrica de sesiones
	public static void closeFactory()
	{
		if(factory != null)
		{
			factory.close();
			factory = null;
		}
	}
}
