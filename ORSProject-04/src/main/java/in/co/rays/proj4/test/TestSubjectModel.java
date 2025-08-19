package in.co.rays.proj4.test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.model.SubjectModel;

public class TestSubjectModel {
	
	public static void main(String[] args) throws SQLException {
		//testAddSubject();
		//testUpdateSubject();
		//testDeleteSubject();
		//testFindByPk();
		//testFindByName();
		//testSearch();
		testList();
	}
	
	
	public static void testAddSubject() throws SQLException {
		SubjectModel model = new SubjectModel();
		SubjectBean bean = new SubjectBean();
		bean.setName("Programing");
		bean.setCourseId(1l);
		bean.setCourseName("Temp");
		bean.setDescription("Java course");
		bean.setCreatedBy("Aniket");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		Long id =  model.addSubject(bean);
		System.out.println("Subject added with id "+id);
	}
	
	public static void testUpdateSubject() throws SQLException {
		SubjectModel model = new SubjectModel();
		SubjectBean bean = new SubjectBean();
		bean.setId(1l);
		bean.setName("Python");
		bean.setCourseId(1l);
		bean.setCourseName("Temp");
		bean.setDescription("Core python");
		bean.setCreatedBy("Anurag");
		bean.setModifiedBy("Aniket");
		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
		model.updateSubject(bean);
	}
	
	public static void testDeleteSubject() throws SQLException {
		SubjectModel model = new SubjectModel();
		model.deleteSubject(2l);
	}
	
	public static void testFindByPk() throws SQLException {
		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByPk(1l);
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getCourseId());
		System.out.println(bean.getCourseName());
		System.out.println(bean.getDescription());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testFindByName() throws SQLException {
		SubjectModel model = new SubjectModel();
		SubjectBean bean = model.findByName("Admin");
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getCourseId());
		System.out.println(bean.getCourseName());
		System.out.println(bean.getDescription());
		System.out.println(bean.getCreatedBy());
		System.out.println(bean.getModifiedBy());
		System.out.println(bean.getCreatedDateTime());
		System.out.println(bean.getModifiedDateTime());
		
	}
	
	public static void testSearch() throws SQLException {
		SubjectModel model = new SubjectModel();
		List<SubjectBean> list = model.search(null, 1, 5);
		Iterator<SubjectBean> it = list.iterator();
		
		while(it.hasNext()) {
			SubjectBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}
	
	public static void testList() throws SQLException {
		SubjectModel model = new SubjectModel();
		List<SubjectBean> list = model.list();
		Iterator<SubjectBean> it = list.iterator();
		
		while(it.hasNext()) {
			SubjectBean bean =  it.next();
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getCourseId());
			System.out.println(bean.getCourseName());
			System.out.println(bean.getDescription());
			System.out.println(bean.getCreatedBy());
			System.out.println(bean.getModifiedBy());
			System.out.println(bean.getCreatedDateTime());
			System.out.println(bean.getModifiedDateTime());
		}
	}

}
