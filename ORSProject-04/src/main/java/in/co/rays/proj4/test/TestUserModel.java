package in.co.rays.proj4.test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.model.UserModel;

public class TestUserModel {
	
	public static void main(String[] args) throws SQLException, ParseException {
//		testAddUser();
//		testUpdateUser();
//		testDeleteUser();
//		testFindByPk();
//		testFindByLogin();
//		testSearch();
//		testList();
	}
	
	
	public static void testAddUser() throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		UserModel model = new UserModel();
		UserBean bean = new UserBean();
		bean.setFirstName("Aniket");
		bean.setLastName("Rajput");
		bean.setLogin("aniket@gmail.com");
		bean.setPassword("123");
		bean.setDob(sdf.parse("12-02-2000"));
		bean.setMobileNo("7898037387");
		bean.setRoleId(1l);
		bean.setGender("Male");
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addUser(bean);
		System.out.println("User added with id "+id);
	}
	
	public static void testUpdateUser() throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		UserModel model = new UserModel();
		UserBean bean = new UserBean();
		bean.setId(1l);
		bean.setFirstName("Aniket");
		bean.setLastName("Rajput");
		bean.setLogin("aniket@gmail.com");
		bean.setPassword("123");
		bean.setDob(sdf.parse("12-02-2000"));
		bean.setMobileNo("7898037387");
		bean.setRoleId(1l);
		bean.setGender("Male");
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateUser(bean);
	}
	
	public static void testDeleteUser() throws SQLException {
		UserModel model = new UserModel();
		model.deleteUser(2l);
	}
	
	public static void testFindByPk() throws SQLException {
		UserModel model = new UserModel();
		UserBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getFirstName());
		System.out.println(bean.getLastName());
		System.out.println(bean.getLogin());
		System.out.println(bean.getPassword());
		System.out.println(bean.getDob());
		System.out.println(bean.getMobileNo());
		System.out.println(bean.getRoleId());
		System.out.println(bean.getGender());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testFindByLogin() throws SQLException {
		UserModel model = new UserModel();
		UserBean bean = model.findByLogin("aniket@gmail.com");
		System.out.println(bean.getId());
		System.out.println(bean.getFirstName());
		System.out.println(bean.getLastName());
		System.out.println(bean.getLogin());
		System.out.println(bean.getPassword());
		System.out.println(bean.getDob());
		System.out.println(bean.getMobileNo());
		System.out.println(bean.getRoleId());
		System.out.println(bean.getGender());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws SQLException {
		UserModel model = new UserModel();
		List<UserBean> list = model.search(null, 1, 5);
		Iterator<UserBean> it = list.iterator();
		
		while(it.hasNext()) {
			UserBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getFirstName());
			System.out.println(bean.getLastName());
			System.out.println(bean.getLogin());
			System.out.println(bean.getPassword());
			System.out.println(bean.getDob());
			System.out.println(bean.getMobileNo());
			System.out.println(bean.getRoleId());
			System.out.println(bean.getGender());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws SQLException {
		UserModel model = new UserModel();
		List<UserBean> list = model.list();
		Iterator<UserBean> it = list.iterator();
		
		while(it.hasNext()) {
			UserBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getFirstName());
			System.out.println(bean.getLastName());
			System.out.println(bean.getLogin());
			System.out.println(bean.getPassword());
			System.out.println(bean.getDob());
			System.out.println(bean.getMobileNo());
			System.out.println(bean.getRoleId());
			System.out.println(bean.getGender());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
