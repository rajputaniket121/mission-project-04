package in.co.rays.proj4.test;


import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CollegeModel;

public class TestCollegeModel {
	
	public static void main(String[] args) throws ApplicationException, DuplicateRecordException {
		//testAddCollege();
		//testUpdateCollege();
		//testDeleteCollege();
		//testFindByPk();
		//testFindByName();
		testSearch();
		//testList();
	}
	
	
	public static void testAddCollege() throws  ApplicationException, DuplicateRecordException {
		CollegeModel model = new CollegeModel();
		CollegeBean bean = new CollegeBean();
		bean.setName("SDBCE");
		bean.setAddress("Indore");
		bean.setState("Indore");
		bean.setCity("Indore");
		bean.setPhoneNo("78395438743");
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addCollege(bean);
		System.out.println("College added with id "+id);
	}
	
	public static void testUpdateCollege() throws ApplicationException, DuplicateRecordException  {
		CollegeModel model = new CollegeModel();
		CollegeBean bean = new CollegeBean();
		bean.setId(1l);
		bean.setName("SDBCE");
		bean.setAddress("Indore");
		bean.setState("Indore");
		bean.setCity("Indore");
		bean.setPhoneNo("78395438743");
		bean.setCreatedBy("Anurag");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateCollege(bean);
	}
	
	public static void testDeleteCollege() throws ApplicationException  {
		CollegeModel model = new CollegeModel();
		model.deleteCollege(2l);
	}
	
	public static void testFindByPk() throws ApplicationException  {
		CollegeModel model = new CollegeModel();
		CollegeBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getAddress());
		System.out.println(bean.getState());
		System.out.println(bean.getCity());
		System.out.println(bean.getPhoneNo());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testFindByName() throws ApplicationException  {
		CollegeModel model = new CollegeModel();
		CollegeBean bean = model.findByName("SDBCT");
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getAddress());
		System.out.println(bean.getState());
		System.out.println(bean.getCity());
		System.out.println(bean.getPhoneNo());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws ApplicationException  {
		CollegeModel model = new CollegeModel();
		CollegeBean clgBean = new CollegeBean();
		clgBean.setName("SDBCT");
		List<CollegeBean> list = model.search(clgBean, 1, 5);
		Iterator<CollegeBean> it = list.iterator();
		
		while(it.hasNext()) {
			CollegeBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getAddress());
			System.out.println(bean.getState());
			System.out.println(bean.getCity());
			System.out.println(bean.getPhoneNo());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws ApplicationException{
		CollegeModel model = new CollegeModel();
		List<CollegeBean> list = model.list();
		Iterator<CollegeBean> it = list.iterator();
		
		while(it.hasNext()) {
			CollegeBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getAddress());
			System.out.println(bean.getState());
			System.out.println(bean.getCity());
			System.out.println(bean.getPhoneNo());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
