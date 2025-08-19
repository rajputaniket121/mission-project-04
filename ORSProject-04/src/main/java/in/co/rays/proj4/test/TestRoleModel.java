package in.co.rays.proj4.test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.model.RoleModel;

public class TestRoleModel {

	public static void main(String[] args) throws SQLException {
		//testAddRole();
		//testUpdateRole();
		//testDeleteRole();
		//testFindByPk();
		//testFindByName();
		//testSearch();
		//testList();
	}
	
	
	public static void testAddRole() throws SQLException {
		RoleModel model = new RoleModel();
		RoleBean bean = new RoleBean();
		bean.setName("Admin");
		bean.setDescription("Admin");
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addRole(bean);
		System.out.println("Role added with id "+id);
	}
	
	public static void testUpdateRole() throws SQLException {
		RoleModel model = new RoleModel();
		RoleBean bean = new RoleBean();
		bean.setId(1l);
		bean.setName("Admin");
		bean.setDescription("Admin");
		bean.setCreatedBy("Anurag");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateRole(bean);
	}
	
	public static void testDeleteRole() throws SQLException {
		RoleModel model = new RoleModel();
		model.deleteRole(2l);
	}
	
	public static void testFindByPk() throws SQLException {
		RoleModel model = new RoleModel();
		RoleBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getDescription());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testFindByName() throws SQLException {
		RoleModel model = new RoleModel();
		RoleBean bean = model.findByName("Admin");
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getDescription());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws SQLException {
		RoleModel model = new RoleModel();
		List<RoleBean> list = model.search(null, 1, 5);
		Iterator<RoleBean> it = list.iterator();
		
		while(it.hasNext()) {
			RoleBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws SQLException {
		RoleModel model = new RoleModel();
		List<RoleBean> list = model.list();
		Iterator<RoleBean> it = list.iterator();
		
		while(it.hasNext()) {
			RoleBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
