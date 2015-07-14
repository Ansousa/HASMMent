import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Permiso;
import es.uvigo.esei.hasmment.entities.Usuario;


public class HibernateMethodsTest {
	
	@Test
	public void deleteEntity() {
		Usuario usu = new Usuario("delete", "", "", "", "", 0, "");
	
		ArrayList<DBEntity> listBefore = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		
		HibernateMethods.saveEntity(usu);
		HibernateMethods.deleteEntity(usu);
		
		ArrayList<DBEntity> listAfter = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		
		Assert.assertEquals(listBefore.size(),listAfter.size());
	}
	
	@Test
	public void getAuxiliar() {
		Auxiliar aux = new Auxiliar("auxiliar", "", "", "", 0, new Date(0), new Date(0));
		
		HibernateMethods.saveEntity(aux);
		
		Auxiliar aux2 = HibernateMethods.getAuxiliar(aux.getDni());
		HibernateMethods.deleteEntity(aux);
		
		Assert.assertTrue(aux.getDni().trim().equals(aux2.getDni().trim()));
	}
	
	@Test
	public void getFirstDateAsist() {
		DateTime dateAfter = HibernateMethods.getFirstDateAsist();
		DateTime dateBefore = new DateTime(dateAfter.minusDays(1));
		
		Auxiliar a = new Auxiliar("12345678Z", "", "", "", 0, new Date(0), new Date(0));
		Usuario u = new Usuario("12345678Z", "", "", "", "", 0, "");
		Asiste as = new Asiste("12345678Z", "12345678Z", new Timestamp(dateBefore.getMillis()), new Timestamp(0), "");
		
		HibernateMethods.saveEntity(a);
		HibernateMethods.saveEntity(u);
		HibernateMethods.saveEntity(as);
		
		DateTime getDate = HibernateMethods.getFirstDateAsist();
		
		HibernateMethods.deleteEntity(as);
		HibernateMethods.deleteEntity(u);
		HibernateMethods.deleteEntity(a);
		
		Assert.assertEquals(getDate, dateBefore);
	}
	
	@Test
	public void getLastDateAsist() {
		DateTime dateBefore = HibernateMethods.getFirstDateAsist();
		DateTime dateAfter = new DateTime(dateBefore.minusDays(1));
		
		Auxiliar a = new Auxiliar("12345678Z", "", "", "", 0, new Date(0), new Date(0));
		Usuario u = new Usuario("12345678Z", "", "", "", "", 0, "");
		Asiste as = new Asiste("12345678Z", "12345678Z", new Timestamp(dateAfter.getMillis()), new Timestamp(0), "");
		
		HibernateMethods.saveEntity(a);
		HibernateMethods.saveEntity(u);
		HibernateMethods.saveEntity(as);
		
		DateTime getDate = HibernateMethods.getFirstDateAsist();
		
		HibernateMethods.deleteEntity(as);
		HibernateMethods.deleteEntity(u);
		HibernateMethods.deleteEntity(a);
		
		Assert.assertEquals(getDate, dateAfter);
	}
	
	@Test
	public void getListEntities() {
		Object o = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		Assert.assertEquals(o.getClass().toString(), "class java.util.ArrayList");
	}
	
	@Test
	public void getUsuario() {
		Usuario usu = new Usuario("usuario", "", "", "", "", 0, "");
		
		HibernateMethods.saveEntity(usu);
		
		Usuario usu2 = HibernateMethods.getUsuario(usu.getDni());
		HibernateMethods.deleteEntity(usu);
		
		Assert.assertTrue(usu.getDni().trim().equals(usu2.getDni().trim()));
	}
	
	@Test
	public void modifyEntity() {
		Usuario u = new Usuario("modify", "", "", "", "", 0, "");
		
		HibernateMethods.saveEntity(u);
		
		Usuario u2 = new Usuario("modify", "", "", "", "", 0, "");
		u.setApellido1("prueba");
		
		HibernateMethods.modifyEntity(u);
		
		Usuario u3 = HibernateMethods.getUsuario(u.getDni());
		
		HibernateMethods.deleteEntity(u);
		
		Assert.assertNotEquals(u2, u3);
	}
	
	@Test
	public void saveEntity() {
		Usuario u = new Usuario("save", "", "", "", "", 0, "");
		
		HibernateMethods.saveEntity(u);
		
		Usuario u2 = HibernateMethods.getUsuario(u.getDni());
		
		HibernateMethods.deleteEntity(u);
		
		Assert.assertEquals(u.getDni(), u2.getDni());
	}
	
	@Test
	public void searchInAsiste() {
		Auxiliar a = new Auxiliar("12345678Z", "", "", "", 0, new Date(0), new Date(0));
		Usuario u = new Usuario("12345678Z", "", "", "", "", 0, "");
		Asiste as = new Asiste("12345678Z", "12345678Z", new Timestamp(0), new Timestamp(0), "");
		
		HibernateMethods.saveEntity(a);
		HibernateMethods.saveEntity(u);
		HibernateMethods.saveEntity(as);
		
		ArrayList<DBEntity> list = HibernateMethods.searchInAsiste("12345678Z");
		
		HibernateMethods.deleteEntity(as);
		HibernateMethods.deleteEntity(u);
		HibernateMethods.deleteEntity(a);
		
		Asiste as2 = (Asiste) list.get(0);
		Assert.assertTrue(as.getDniAuxiliar().equals(as2.getDniAuxiliar()));
		
	}
	
	@Test
	public void searchInAuxiliar() {
		Auxiliar a = new Auxiliar("search", "", "", "", 0, new Date(0), new Date(0));
		
		HibernateMethods.saveEntity(a);
		ArrayList<DBEntity> list = HibernateMethods.searchInAuxiliar("search");
		
		HibernateMethods.deleteEntity(a);
		
		Auxiliar a2 = (Auxiliar)list.get(0);
		Assert.assertTrue(a.getDni().equals(a2.getDni()));
	}
	
	@Test
	public void searchInPermiso() {
		Auxiliar a = new Auxiliar("12345678Z", "", "", "", 0, new Date(0), new Date(0));
		Permiso p = new Permiso("12345678Z", new Date(0), new Date(0), "");
		
		HibernateMethods.saveEntity(a);
		HibernateMethods.saveEntity(p);
		ArrayList<DBEntity> list = HibernateMethods.searchInPermiso("12345678Z");
		
		HibernateMethods.deleteEntity(p);
		HibernateMethods.deleteEntity(a);
		
		Permiso p2 = (Permiso)list.get(0);
		Assert.assertTrue(p.getDni().equals(p2.getDni()));
	}
	
	@Test
	public void searchInUsuario() {
		Usuario u = new Usuario("search", "", "", "", "", 0, "");
		
		HibernateMethods.saveEntity(u);
		ArrayList<DBEntity> list = HibernateMethods.searchInUsuario("search");
		
		HibernateMethods.deleteEntity(u);
		
		Usuario u2 = (Usuario) list.get(0);
		
		Assert.assertTrue(u.getDni().equals(u2.getDni()));
	}
}
