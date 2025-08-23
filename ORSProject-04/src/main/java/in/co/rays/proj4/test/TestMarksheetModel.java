package in.co.rays.proj4.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.MarksheetModel;

public class TestMarksheetModel {
	
	public static void main(String[] args) throws ApplicationException, DuplicateRecordException  {
		//testAddMarksheet();
		testUpdateMarksheet();
		//testDeleteMarksheet();
		//testFindByPk();
		//testFindByRollNo();
		//testSearch();
		//testList();
	}
	
	
	public static void testAddMarksheet() throws ApplicationException, DuplicateRecordException  {
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = new MarksheetBean();
		bean.setRollNo("123");
		bean.setStudentId(1l);
		bean.setPhysics(24);
		bean.setChemistry(55);
		bean.setMaths(66);
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addMarksheet(bean);
		System.out.println("Marksheet added with id "+id);
	}
	
	public static void testUpdateMarksheet() throws ApplicationException, DuplicateRecordException  {
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = new MarksheetBean();
		bean.setId(1l);
		bean.setRollNo("123");
		bean.setStudentId(1l);
		bean.setPhysics(24);
		bean.setChemistry(55);
		bean.setMaths(86);
		bean.setCreatedBy("Anurag");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateMarksheet(bean);
	}
	
	public static void testDeleteMarksheet() throws ApplicationException  {
		MarksheetModel model = new MarksheetModel();
		model.deleteMarksheet(2l);
	}
	
	public static void testFindByPk() throws ApplicationException  {
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getRollNo());
		System.out.println(bean.getStudentId());
		System.out.println(bean.getName());
		System.out.println(bean.getPhysics());
		System.out.println(bean.getChemistry());
		System.out.println(bean.getMaths());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testFindByRollNo() throws ApplicationException  {
		MarksheetModel model = new MarksheetModel();
		MarksheetBean bean = model.findByRollNO("Admin");
		System.out.println(bean.getId());
		System.out.println(bean.getRollNo());
		System.out.println(bean.getStudentId());
		System.out.println(bean.getName());
		System.out.println(bean.getPhysics());
		System.out.println(bean.getChemistry());
		System.out.println(bean.getMaths());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws ApplicationException  {
		MarksheetModel model = new MarksheetModel();
		List<MarksheetBean> list = model.search(null, 1, 5);
		Iterator<MarksheetBean> it = list.iterator();
		
		while(it.hasNext()) {
			MarksheetBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getRollNo());
			System.out.println(bean.getStudentId());
			System.out.println(bean.getName());
			System.out.println(bean.getPhysics());
			System.out.println(bean.getChemistry());
			System.out.println(bean.getMaths());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws ApplicationException  {
		MarksheetModel model = new MarksheetModel();
		List<MarksheetBean> list = model.list();
		Iterator<MarksheetBean> it = list.iterator();
		
		while(it.hasNext()) {
			MarksheetBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getRollNo());
			System.out.println(bean.getStudentId());
			System.out.println(bean.getName());
			System.out.println(bean.getPhysics());
			System.out.println(bean.getChemistry());
			System.out.println(bean.getMaths());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
